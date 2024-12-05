package shinohana.aplicacion_shinohana3;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AddProducto extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_producto);
        
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Agregar Producto");
    }
}