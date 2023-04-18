public class Producto {
    private String nombre;
    private int edadRecomendada;
    private double precioBase;
    private Proveedor proveedor;

    public Producto(String nombre, int edadRecomendada, double precioBase, Proveedor proveedor) {
        this.nombre = nombre;
        this.edadRecomendada = edadRecomendada;
        this.precioBase = precioBase;
        this.proveedor = proveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdadRecomendada() {
        return edadRecomendada;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public double getPrecioTotal() {
        return getPrecioTotal();
    }
}
