package a.aplicacion4;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aja.example.aplicacion_shinohana20.helpers.FirebaseHelper;
import aja.example.aplicacion_shinohana20.models.Producto;

public class UpdateProducto extends AppCompatActivity {
    private Spinner spinnerProductos;
    private EditText etNombre, etPrecio, etStock, etDescripcion;
    private Button btnSelectImage, btnActualizar;
    private FirebaseHelper firebaseHelper;
    private List<Producto> productos;
    private List<String> nombreProductos;
    private static final String TAG = "UpdateProducto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_producto);
        
        // Inicializar Firebase
        firebaseHelper = FirebaseHelper.getInstance();
        
        // Inicializar vistas
        spinnerProductos = findViewById(R.id.spinnerProductos);
        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        etStock = findViewById(R.id.etStock);
        etDescripcion = findViewById(R.id.etDescripcion);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnActualizar = findViewById(R.id.btnActualizar);

        productos = new ArrayList<>();
        nombreProductos = new ArrayList<>();

        // Cargar productos en el spinner
        cargarProductos();

        // Configurar listener del spinner
        spinnerProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarDatosProducto(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Configurar botón de actualizar
        btnActualizar.setOnClickListener(v -> actualizarProducto());
    }

    private void cargarProductos() {
        DatabaseReference productosRef = firebaseHelper.getReference("productos");
        productosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productos.clear();
                nombreProductos.clear();
                
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Producto producto = snapshot.getValue(Producto.class);
                    if (producto != null) {
                        productos.add(producto);
                        nombreProductos.add(producto.getNombre());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    UpdateProducto.this,
                    android.R.layout.simple_spinner_item,
                    nombreProductos
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductos.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Error al cargar productos: " + error.getMessage());
            }
        });
    }

    private void cargarDatosProducto(int position) {
        if (position >= 0 && position < productos.size()) {
            Producto producto = productos.get(position);
            etNombre.setText(producto.getNombre());
            etPrecio.setText(String.valueOf(producto.getPrecio()));
            etStock.setText(String.valueOf(producto.getStock()));
            etDescripcion.setText(producto.getDescripcion());
        }
    }

    private void actualizarProducto() {
        int position = spinnerProductos.getSelectedItemPosition();
        if (position < 0 || position >= productos.size()) {
            Toast.makeText(this, "Por favor seleccione un producto", Toast.LENGTH_SHORT).show();
            return;
        }

        Producto productoSeleccionado = productos.get(position);
        String nombre = etNombre.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);

            DatabaseReference productoRef = firebaseHelper.getReference("productos")
                .child(productoSeleccionado.getId());

            productoRef.child("nombre").setValue(nombre);
            productoRef.child("precio").setValue(precio);
            productoRef.child("stock").setValue(stock);
            productoRef.child("descripcion").setValue(descripcion)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateProducto.this, 
                        "Producto actualizado exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al actualizar producto: " + e.getMessage());
                    Toast.makeText(UpdateProducto.this, 
                        "Error al actualizar producto", Toast.LENGTH_SHORT).show();
                });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show();
        }
    }
} 