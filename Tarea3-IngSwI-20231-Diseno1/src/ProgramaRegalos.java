import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProgramaRegalos {

    public static void main(String[] args) {
        // Pedimos al usuario la edad y el precio máximo
        int edad = pedirEdad();
        double precioMaximo = pedirPrecioMaximo();

        // Cargamos los datos de los proveedores y los productos desde los archivos JSON
        List<Proveedor> proveedores = cargarProveedoresDesdeArchivo("proveedores.json");
        List<Producto> productos = cargarProductosDesdeArchivo("productos.json", proveedores);

        // Buscamos los regalos que cumplan con los criterios del usuario
        List<Regalo> regalos = buscarRegalos(edad, precioMaximo, productos);

        // Mostramos la lista de regalos encontrados
        mostrarRegalos(regalos);
    }
    private static List<Regalo> buscarRegalos(int edad, double precioMaximo, List<Producto> productos) {
        List<Regalo> regalos = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getEdadRecomendada() <= edad && producto.getPrecioTotal() <= precioMaximo) {
                regalos.add(new Regalo(producto));
            }
        }
        return regalos;
    }

    private static void mostrarRegalos(List<Regalo> regalos) {
        System.out.println("Regalos encontrados:");
        if (regalos.isEmpty()) {
            System.out.println("No se encontraron regalos que cumplan con los criterios.");
        } else {
            for (Regalo regalo : regalos) {
                System.out.println("- " + regalo.getProducto().getNombre() + " (precio: $" + regalo.getProducto().getPrecioTotal() + ")");
            }
        }
    }


    private static int pedirEdad() {
        Scanner scanner = new Scanner(System.in);
        int edad = 0;
        do {
            System.out.print("Ingrese la edad de la persona a la que desea hacerle el regalo: ");
            try {
                edad = Integer.parseInt(scanner.nextLine());
                if (edad < 0) {
                    System.out.println("La edad no puede ser negativa. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("La edad debe ser un número entero. Intente de nuevo.");
            }
        } while (edad < 0);
        return edad;
    }

    private static double pedirPrecioMaximo() {
        Scanner scanner = new Scanner(System.in);
        double precioMaximo = 0.0;
        do {
            System.out.print("Ingrese el precio máximo que está dispuesto a pagar por el regalo: ");
            try {
                precioMaximo = Double.parseDouble(scanner.nextLine());
                if (precioMaximo < 0) {
                    System.out.println("El precio máximo no puede ser negativo. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("El precio máximo debe ser un número decimal. Intente de nuevo.");
            }
        } while (precioMaximo < 0);
        return precioMaximo;
    }

    private static List<Proveedor> cargarProveedoresDesdeArchivo(String nombreArchivo) {
        List<Proveedor> proveedores = new ArrayList<>();
        try {
            String contenido = Files.readString(Paths.get(nombreArchivo));
            JSONArray jsonArray = new JSONArray(contenido);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre");
                double precioEnvio = jsonObject.getDouble("precioEnvio");
                Proveedor proveedor = new Proveedor(nombre, precioEnvio);
                proveedores.add(proveedor);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los proveedores desde el archivo " + nombreArchivo + ": " + e.getMessage());
        } catch (JSONException e) {
            System.out.println("Error al parsear los datos de los proveedores desde el archivo " + nombreArchivo + ": " + e.getMessage());
        }
        return proveedores;
    }

    private static List<Producto> cargarProductosDesdeArchivo(String nombreArchivo, List<Proveedor> proveedores) {
        List<Producto> productos = new ArrayList<>();
        try {
            String contenido = Files.readString(Paths.get(nombreArchivo));
            JSONArray jsonArray = new JSONArray(contenido);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre");
                int edadRecomendada = jsonObject.getInt("edadRecomendada");
                String nombreProveedor = jsonObject.getString("proveedor");
                double precioBase = jsonObject.getDouble("precioBase");
                Proveedor proveedor = buscarProveedorPorNombre(nombreProveedor, proveedores);
                Producto producto = new Producto(nombre, edadRecomendada, precioBase, proveedor);
                productos.add(producto);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los productos desde el archivo " + nombreArchivo + ": " + e.getMessage());
        } catch (JSONException e) {
            System.out.println("Error al parsear los datos de los productos desde el archivo " + nombreArchivo + ": " + e.getMessage());
        }
        return productos;
    }

    public static Proveedor buscarProveedorPorNombre(String nombreProveedor, List<Proveedor> proveedores) {
        for (Proveedor proveedor : proveedores) {
            if (proveedor.getNombre().equals(nombreProveedor)) {
                return proveedor;
            }
        }
        return null;
    }

}
