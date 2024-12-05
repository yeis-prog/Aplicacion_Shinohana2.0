package shinohana.aplicacion_shinohana3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import aja.example.aplicacion_shinohana20.helpers.FirebaseHelper;
import aja.example.aplicacion_shinohana20.models.Usuario;

public class RegistroNewUsuario extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword;
    private Button btnRegistrar;
    private FirebaseHelper firebaseHelper;
    private static final String TAG = "RegistroNewUsuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_new_usuario);

        // Inicializar Firebase
        firebaseHelper = FirebaseHelper.getInstance();
        
        // Inicializar vistas con los IDs correctos
        etUsername = findViewById(R.id.editUsername);
        etEmail = findViewById(R.id.editEmail);
        etPassword = findViewById(R.id.editPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String nombre = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference usuariosRef = firebaseHelper.getReference("usuarios");
        String id = usuariosRef.push().getKey();
        
        Usuario nuevoUsuario = new Usuario(id, nombre, email, password);
        
        usuariosRef.child(id).setValue(nuevoUsuario)
            .addOnSuccessListener(aVoid -> {
                Toast.makeText(RegistroNewUsuario.this, 
                    "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                finish(); // Volver a InicioSesion
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error al registrar usuario: " + e.getMessage());
                Toast.makeText(RegistroNewUsuario.this, 
                    "Error al registrar usuario: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            });
    }
}