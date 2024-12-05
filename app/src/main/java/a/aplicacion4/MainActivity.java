package a.aplicacion4;

import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import androidx.annotation.NonNull;

import aja.example.aplicacion_shinohana20.helpers.FirebaseHelper;
import aja.example.aplicacion_shinohana20.models.Usuario;

public class MainActivity extends AppCompatActivity {

    private FirebaseHelper firebaseHelper;
    private DatabaseReference usuariosRef;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        try {
            // Inicializar Firebase Helper
            firebaseHelper = FirebaseHelper.getInstance();
            Log.d(TAG, "FirebaseHelper inicializado correctamente");
            
            // Obtener referencia a la colección "usuarios"
            usuariosRef = firebaseHelper.getReference("usuarios");
            Log.d(TAG, "Referencia a usuarios obtenida correctamente");
            
            // Ejemplo de cómo guardar un usuario
            Usuario nuevoUsuario = new Usuario();
            usuariosRef.child(nuevoUsuario.getId()).setValue(nuevoUsuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Usuario guardado exitosamente");
                        Toast.makeText(MainActivity.this, 
                            "Usuario guardado exitosamente", 
                            Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error al guardar usuario: " + e.getMessage());
                        Toast.makeText(MainActivity.this, 
                            "Error al guardar usuario: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                });

        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar Firebase: " + e.getMessage());
            e.printStackTrace();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}