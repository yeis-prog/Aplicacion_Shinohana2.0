package a.aplicacion4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import aja.example.aplicacion_shinohana20.helpers.FirebaseHelper;
import aja.example.aplicacion_shinohana20.models.Usuario;
import shinohana.aplicacion_shinohana3.ForgotPassword;
import shinohana.aplicacion_shinohana3.RegistroNewUsuario;
import shinohana.aplicacion_shinohana3.menuproductos;

public class InicioSesion extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegistro, btnForgotPassword;
    private FirebaseHelper firebaseHelper;
    private static final String TAG = "InicioSesion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_sesion);

        // Inicializar Firebase
        firebaseHelper = FirebaseHelper.getInstance();
        
        // Inicializar vistas con los IDs correctos
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistro = findViewById(R.id.btnRegistro);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        // Configurar botón de registro
        btnRegistro.setOnClickListener(v -> {
            startActivity(new Intent(InicioSesion.this, RegistroNewUsuario.class));
        });

        // Configurar botón de forgot password
        btnForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(InicioSesion.this, ForgotPassword.class));
        });

        // Configurar botón de login
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            verificarCredenciales(email, password);
        });
    }

    private void verificarCredenciales(String email, String password) {
        DatabaseReference usuariosRef = firebaseHelper.getReference("usuarios");
        
        usuariosRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Usuario usuario = snapshot.getValue(Usuario.class);
                        if (usuario != null && usuario.getPassword().equals(password)) {
                            // Credenciales correctas
                            Toast.makeText(InicioSesion.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(InicioSesion.this, menuproductos.class);
                            intent.putExtra("userEmail", email);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                }
                // Credenciales incorrectas
                Toast.makeText(InicioSesion.this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error al verificar credenciales: " + databaseError.getMessage());
                Toast.makeText(InicioSesion.this, "Error al verificar credenciales", Toast.LENGTH_SHORT).show();
            }
        });
    }
}