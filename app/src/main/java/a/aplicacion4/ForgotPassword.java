package a.aplicacion4;

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

public class ForgotPassword extends AppCompatActivity {
    private EditText etEmail, etNewPassword;
    private Button btnResetPassword;
    private FirebaseHelper firebaseHelper;
    private static final String TAG = "ForgotPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        // Inicializar Firebase
        firebaseHelper = FirebaseHelper.getInstance();
        
        // Inicializar vistas
        etEmail = findViewById(R.id.editEmail);
        etNewPassword = findViewById(R.id.editNewPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        btnResetPassword.setOnClickListener(v -> cambiarContraseña());
    }

    private void cambiarContraseña() {
        String email = etEmail.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();

        if (email.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference usuariosRef = firebaseHelper.getReference("usuarios");
        
        usuariosRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Actualizar la contraseña
                        snapshot.getRef().child("password").setValue(newPassword)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(ForgotPassword.this, 
                                    "Contraseña actualizada exitosamente", Toast.LENGTH_SHORT).show();
                                finish(); // Volver a InicioSesion
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error al actualizar contraseña: " + e.getMessage());
                                Toast.makeText(ForgotPassword.this, 
                                    "Error al actualizar contraseña", Toast.LENGTH_SHORT).show();
                            });
                        return;
                    }
                } else {
                    Toast.makeText(ForgotPassword.this, 
                        "No existe una cuenta con este email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error al buscar usuario: " + databaseError.getMessage());
                Toast.makeText(ForgotPassword.this, 
                    "Error al buscar usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}