
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GenerateInfoFiles {

    private static final Random RANDOM = new Random();

    private static final String[] NOMBRES = {
        "Juan", "Maria", "Carlos", "Ana", "Pedro",
        "Laura", "Andres", "Sofia", "Daniel", "Camila"
    };

    private static final String[] APELLIDOS = {
        "Rivero", "Gomez", "Perez", "Rodriguez",
        "Martinez", "Lopez", "Garcia", "Torres",
        "Ramirez", "Moreno"
    };

    private static final String[] PRODUCTOS = {
        "Arroz", "Leche", "Cafe", "Pan", "Azucar",
        "Sal", "Aceite", "Harina", "Galletas", "Chocolate"
    };

    public static void main(String[] args) {

        int cantidadProductos = 10;
        int cantidadVendedores = 5;
        int cantidadVentas = 5;

        try {
            createProductsFile(cantidadProductos);
            createSalesManInfoFile(cantidadVendedores);

            Set<Long> documentos =
                obtenerDocumentosGenerados(cantidadVendedores);

            int contador = 1;

            for (Long documento : documentos) {
                String nombreArchivo = "salesman" + contador + ".txt";

                createSalesMenFile(
                    cantidadVentas,
                    nombreArchivo,
                    documento
                );

                contador++;
            }

            System.out.println("Proceso terminado correctamente.");

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createProductsFile(
            int productsCount) throws IOException {

        if (productsCount <= 0) {
            throw new IllegalArgumentException(
                "La cantidad de productos debe ser mayor que cero."
            );
        }

        try (FileWriter archivo = new FileWriter("productos.txt")) {

            for (int i = 1; i <= productsCount; i++) {
                String nombre = PRODUCTOS[(i - 1) % PRODUCTOS.length];
                int precio = 2000 + RANDOM.nextInt(9000);

                archivo.write(
                    i + ";" + nombre + ";" + precio
                    + System.lineSeparator()
                );
            }
        }

        System.out.println("Archivo de productos creado correctamente.");
    }

    public static void createSalesManInfoFile(
            int salesmanCount) throws IOException {

        if (salesmanCount <= 0) {
            throw new IllegalArgumentException(
                "La cantidad de vendedores debe ser mayor que cero."
            );
        }

        Set<Long> documentos = new HashSet<Long>();

        try (FileWriter archivo = new FileWriter("salesmanInfo.txt")) {

            for (int i = 0; i < salesmanCount; i++) {
                long documento;

                do {
                    documento = 10000000L
                        + RANDOM.nextInt(90000000);
                } while (documentos.contains(documento));

                documentos.add(documento);

                String nombre = NOMBRES[i % NOMBRES.length];
                String apellido = APELLIDOS[i % APELLIDOS.length];

                archivo.write(
                    (i + 1) + ";CC;" + documento + ";"
                    + nombre + ";" + apellido
                    + System.lineSeparator()
                );
            }
        }

        System.out.println("Archivo de vendedores creado correctamente.");
    }

    public static void createSalesMenFile(
            int randomSalesCount,
            String name,
            long documentoVendedor) throws IOException {

        if (randomSalesCount <= 0) {
            throw new IllegalArgumentException(
                "La cantidad de ventas debe ser mayor que cero."
            );
        }

        int cantidadProductos = contarProductos();

        if (cantidadProductos <= 0) {
            throw new IOException(
                "No existen productos para generar las ventas."
            );
        }

        try (FileWriter archivo = new FileWriter(name)) {

 
            archivo.write("CC;" + documentoVendedor
                + System.lineSeparator());

         
            for (int i = 0; i < randomSalesCount; i++) {
                int producto = 1 + RANDOM.nextInt(cantidadProductos);
                int cantidad = 1 + RANDOM.nextInt(10);

                archivo.write(
                    producto + ";" + cantidad
                    + System.lineSeparator()
                );
            }
        }

        System.out.println("Archivo de ventas creado: " + name);
    }

    private static Set<Long> obtenerDocumentosGenerados(
            int cantidad) throws IOException {

        Set<Long> documentos = new HashSet<Long>();

        try (BufferedReader lector = new BufferedReader(
                new FileReader("salesmanInfo.txt"))) {

            String linea;

            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(";");

                
                if (datos.length == 5) {
                    documentos.add(Long.parseLong(datos[2].trim()));
                }
            }
        }

        if (documentos.size() != cantidad) {
            throw new IOException(
                "No se pudieron obtener todos los vendedores generados."
            );
        }

        return documentos;
    }

    private static int contarProductos() throws IOException {

        File archivo = new File("productos.txt");

        if (!archivo.exists()) {
            return 0;
        }

        int cantidad = 0;

        try (BufferedReader lector = new BufferedReader(
                new FileReader(archivo))) {

            while (lector.readLine() != null) {
                cantidad++;
            }
        }

        return cantidad;
    }
}
