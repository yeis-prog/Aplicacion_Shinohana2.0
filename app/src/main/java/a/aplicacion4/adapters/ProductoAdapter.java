package a.aplicacion4.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import aja.example.aplicacion_shinohana20.R;
import aja.example.aplicacion_shinohana20.models.Producto;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private List<Producto> productos;
    private OnProductoListener listener;

    public interface OnProductoListener {
        void onEliminarClick(Producto producto);
    }

    public ProductoAdapter(List<Producto> productos, OnProductoListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    @Override
    public ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void actualizarLista(List<Producto> nuevaLista) {
        productos = nuevaLista;
        notifyDataSetChanged();
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvPrecio, tvStock;
        private Button btnEliminar;

        ProductoViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvStock = itemView.findViewById(R.id.tvStock);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        void bind(Producto producto) {
            tvNombre.setText(producto.getNombre());
            tvPrecio.setText(String.format("$%.2f", producto.getPrecio()));
            tvStock.setText(String.format("Stock: %d", producto.getStock()));
            btnEliminar.setOnClickListener(v -> listener.onEliminarClick(producto));
        }
    }
} 