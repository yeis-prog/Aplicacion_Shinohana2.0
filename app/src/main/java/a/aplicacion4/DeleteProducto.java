package a.aplicacion4;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aja.example.aplicacion_shinohana20.helpers.FirebaseHelper;
import aja.example.aplicacion_shinohana20.models.Producto;
import aja.example.aplicacion_shinohana20.adapters.ProductoAdapter;

public class DeleteProducto extends AppCompatActivity {
    private EditText etBuscar;
    private RecyclerView rvProductos;
    private FirebaseHelper firebaseHelper;
    private ProductoAdapter productoAdapter;
    private List<Producto> productos;
    private static final String TAG = "DeleteProducto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_producto);
        
        // Inicializar Firebase
        firebaseHelper = FirebaseHelper.getInstance();
        
        // Inicializar vistas
        etBuscar = findViewById(R.id.etBuscar);
        rvProductos = findViewById(R.id.rvProductos);

        // Configurar RecyclerView
        productos = new ArrayList<>();
        productoAdapter = new ProductoAdapter(productos, this::eliminarProducto);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        rvProductos.setAdapter(productoAdapter);

        // Cargar productos
        cargarProductos();

        // Configurar búsqueda
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarProductos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void cargarProductos() {
        DatabaseReference productosRef = firebaseHelper.getReference("productos");
        productosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Producto producto = snapshot.getValue(Producto.class);
                    if (producto != null) {
                        producto.setId(snapshot.getKey());
                        productos.add(producto);
                    }
                }
                productoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Error al cargar productos: " + error.getMessage());
            }
        });
    }

    private void filtrarProductos(String texto) {
        List<Producto> productosFiltrados = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                productosFiltrados.add(producto);
            }
        }
        productoAdapter.actualizarLista(productosFiltrados);
    }

    private void eliminarProducto(Producto producto) {
        DatabaseReference productoRef = firebaseHelper.getReference("productos")
            .child(producto.getId());

        productoRef.removeValue()
            .addOnSuccessListener(aVoid -> {
                Toast.makeText(DeleteProducto.this, 
                    "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error al eliminar producto: " + e.getMessage());
                Toast.makeText(DeleteProducto.this, 
                    "Error al eliminar producto", Toast.LENGTH_SHORT).show();
            });
    }
} 