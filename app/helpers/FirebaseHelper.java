package shinohana.aplicacion_shinohana3.helpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;
import android.content.Context;
import android.util.Log;

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static final String TAG = "FirebaseHelper";
    
    private static final String DATABASE_URL = "https://new-aplicacionshino-default-rtdb.firebaseio.com/";

    private FirebaseHelper() {
        try {
            // Inicializar la base de datos con la URL específica
            database = FirebaseDatabase.getInstance(DATABASE_URL);
            
            // Habilitar la persistencia de datos offline (opcional)
            database.setPersistenceEnabled(true);
            
            // Obtener la referencia raíz
            reference = database.getReference();
            
            // Configurar el modo de sincronización para mantener los datos actualizados
            reference.keepSynced(true);
            
            Log.d(TAG, "Firebase inicializado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static synchronized FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public DatabaseReference getReference(String path) {
        return reference.child(path);
    }

    // Método para obtener una referencia específica con ordenamiento
    public DatabaseReference getOrderedReference(String path, String orderBy) {
        return reference.child(path).orderByChild(orderBy);
    }

    // Método para verificar la conexión
    public void checkConnection(DatabaseReference.CompletionListener listener) {
        reference.child(".info/connected").setValue(true, listener);
    }

    // Método para limpiar la instancia (útil al cerrar la aplicación)
    public static void clearInstance() {
        instance = null;
    }
} 