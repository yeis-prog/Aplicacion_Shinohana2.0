package a.aplicacion4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private TextView forgotPasswordText;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Firebase Auth y Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://new-aplicacionshino-default-rtdb.firebaseio.com/")
                .getReference();

        // Inicializar vistas
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);

        // Configurar click listeners
        loginButton.setOnClickListener(v -> loginUser());
        registerButton.setOnClickListener(v -> {
            // Implementar navegación a la pantalla de registro
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
        forgotPasswordText.setOnClickListener(v -> handleForgotPassword());
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Verificar si el usuario existe en la base de datos
                            checkUserInDatabase(user);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Autenticación fallida: " + 
                                     task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserInDatabase(FirebaseUser firebaseUser) {
        mDatabase.child("users").child(firebaseUser.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            // Usuario existe en la base de datos
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Crear nuevo usuario en la base de datos
                            User newUser = new User(
                                firebaseUser.getUid(),
                                firebaseUser.getEmail(),
                                firebaseUser.getDisplayName() != null ? 
                                    firebaseUser.getDisplayName() : "Usuario"
                            );
                            mDatabase.child("users").child(firebaseUser.getUid())
                                    .setValue(newUser)
                                    .addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, 
                                                "Error al crear usuario en la base de datos", 
                                                Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, 
                            "Error al verificar usuario en la base de datos", 
                            Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleForgotPassword() {
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Ingrese su email para recuperar la contraseña", 
                Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, 
                            "Se ha enviado un correo para restablecer su contraseña", 
                            Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, 
                            "Error al enviar el correo de recuperación: " + 
                            task.getException().getMessage(), 
                            Toast.LENGTH_LONG).show();
                    }
                });
    }
} 