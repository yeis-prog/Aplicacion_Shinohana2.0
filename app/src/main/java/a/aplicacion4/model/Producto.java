package a.aplicacion4.model;

public class Producto {
    private String id;
    private String nombre;
    private double precio;
    private int stock;
    private String descripcion;
    private String imagenUrl;

    // Constructor vacío requerido para Firebase
    public Producto() {}

    public Producto(String id, String nombre, double precio, int stock, String descripcion, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
} 