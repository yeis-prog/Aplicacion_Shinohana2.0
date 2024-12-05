package shinohana.aplicacion_shinohana3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class menuproductos extends AppCompatActivity {
    private String userEmail;
    private Menu adminMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuproductos);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Ocultar título por defecto

        // Obtener el email del usuario que inició sesión
        userEmail = getIntent().getStringExtra("userEmail");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú según el tipo de usuario
        if ("yeis@gmail.com".equals(userEmail)) {
            getMenuInflater().inflate(R.menu.menu_admin, menu);
            adminMenu = menu;
        } else {
            getMenuInflater().inflate(R.menu.menu_user, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        // Opción disponible para todos los usuarios
        if (id == R.id.action_mqtt) {
            startActivity(new Intent(this, PruebasMQTT.class));
            return true;
        }
        
        // Opciones solo para administrador
        if (id == R.id.action_add) {
            startActivity(new Intent(this, AddProducto.class));
            return true;
        } else if (id == R.id.action_delete) {
            startActivity(new Intent(this, DeleteProducto.class));
            return true;
        } else if (id == R.id.action_update) {
            startActivity(new Intent(this, UpdateProducto.class));
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}