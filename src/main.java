import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class main {

   
    private static Map<Integer, Producto> productos = new HashMap<Integer, Producto>();

   
    private static Map<Long, Vendedor> vendedores = new HashMap<Long, Vendedor>();

  
    private static Map<Integer, Integer> cantidadesVendidas =
            new HashMap<Integer, Integer>();

  
    public static void main(String[] args) {

        try {

            leerProductos();
            leerVendedores();
            leerArchivosVentas();

            generarReporteVendedores();
            generarReporteProductos();

            System.out.println(
                "Proceso terminado correctamente."
            );

        } catch (Exception e) {

            System.out.println(
                "ERROR: " + e.getMessage()
            );
        }
    }

   
    private static void leerProductos() throws Exception {

        File archivo = new File("products.txt");

        if (!archivo.exists()) {
            throw new Exception(
                "No se encontró el archivo products.txt."
            );
        }

        BufferedReader lector =
                new BufferedReader(new FileReader(archivo));

        String linea;
        int numeroLinea = 0;

        try {

            while ((linea = lector.readLine()) != null) {

                numeroLinea++;

                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";");

                if (datos.length != 3) {
                    throw new Exception(
                        "Formato incorrecto en products.txt, línea "
                        + numeroLinea
                    );
                }

                int id = Integer.parseInt(datos[0].trim());
                String nombre = datos[1].trim();
                double precio = Double.parseDouble(datos[2].trim());

                if (id <= 0) {
                    throw new Exception(
                        "El ID del producto debe ser positivo. Línea "
                        + numeroLinea
                    );
                }

                if (precio <= 0) {
                    throw new Exception(
                        "El precio del producto debe ser positivo. Línea "
                        + numeroLinea
                    );
                }

                if (productos.containsKey(id)) {
                    throw new Exception(
                        "El producto con ID " + id
                        + " está repetido."
                    );
                }

                productos.put(
                    id,
                    new Producto(id, nombre, precio)
                );

                cantidadesVendidas.put(id, 0);
            }

        } finally {

            lector.close();
        }
    }

   
    private static void leerVendedores() throws Exception {

        File archivo = new File("salesmanInfo.txt");

        if (!archivo.exists()) {
            throw new Exception(
                "No se encontró el archivo salesmanInfo.txt."
            );
        }

        BufferedReader lector =
                new BufferedReader(new FileReader(archivo));

        String linea;
        int numeroLinea = 0;

        try {

            while ((linea = lector.readLine()) != null) {

                numeroLinea++;

                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";");

                if (datos.length != 4) {
                    throw new Exception(
                        "Formato incorrecto en salesmanInfo.txt, línea "
                        + numeroLinea
                    );
                }

                String tipoDocumento = datos[0].trim();
                long numeroDocumento =
                        Long.parseLong(datos[1].trim());
                String nombres = datos[2].trim();
                String apellidos = datos[3].trim();

                if (numeroDocumento <= 0) {
                    throw new Exception(
                        "Número de documento inválido en salesmanInfo.txt."
                    );
                }

                if (vendedores.containsKey(numeroDocumento)) {
                    throw new Exception(
                        "El vendedor con documento "
                        + numeroDocumento
                        + " está repetido."
                    );
                }

                vendedores.put(
                    numeroDocumento,
                    new Vendedor(
                        tipoDocumento,
                        numeroDocumento,
                        nombres,
                        apellidos
                    )
                );
            }

        } finally {

            lector.close();
        }
    }

   
    private static void leerArchivosVentas() throws Exception {

        File carpeta = new File(".");

        File[] archivos = carpeta.listFiles();

        if (archivos == null) {
            throw new Exception(
                "No se pudo leer la carpeta del proyecto."
            );
        }

        boolean encontroArchivoVentas = false;

        for (File archivo : archivos) {

            String nombre = archivo.getName();

            if (!archivo.isFile()) {
                continue;
            }

            if (!nombre.startsWith("salesman")
                    || !nombre.endsWith(".txt")
                    || nombre.equals("salesmanInfo.txt")) {

                continue;
            }

            encontroArchivoVentas = true;

            procesarArchivoVentas(archivo);
        }

        if (!encontroArchivoVentas) {
            throw new Exception(
                "No se encontraron archivos de ventas."
            );
        }
    }


    private static void procesarArchivoVentas(File archivo)
            throws Exception {

        BufferedReader lector =
                new BufferedReader(new FileReader(archivo));

        String linea;
        int numeroLinea = 0;
        long documentoVendedor = 0;

        try {

            linea = lector.readLine();
            numeroLinea++;

            if (linea == null) {
                throw new Exception(
                    "El archivo " + archivo.getName()
                    + " está vacío."
                );
            }

            String[] encabezado = linea.split(";");

            if (encabezado.length != 2) {
                throw new Exception(
                    "Encabezado incorrecto en "
                    + archivo.getName()
                );
            }

            String tipoDocumento =
                    encabezado[0].trim();

            documentoVendedor =
                    Long.parseLong(encabezado[1].trim());

            if (!vendedores.containsKey(documentoVendedor)) {
                throw new Exception(
                    "El vendedor con documento "
                    + documentoVendedor
                    + " del archivo "
                    + archivo.getName()
                    + " no existe en salesmanInfo.txt."
                );
            }

            Vendedor vendedor =
                    vendedores.get(documentoVendedor);

            while ((linea = lector.readLine()) != null) {

                numeroLinea++;

                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";");

                if (datos.length < 2) {
                    throw new Exception(
                        "Venta incorrecta en "
                        + archivo.getName()
                        + ", línea "
                        + numeroLinea
                    );
                }

                int idProducto =
                        Integer.parseInt(datos[0].trim());

                int cantidad =
                        Integer.parseInt(datos[1].trim());

                if (cantidad <= 0) {
                    throw new Exception(
                        "La cantidad vendida debe ser positiva en "
                        + archivo.getName()
                        + ", línea "
                        + numeroLinea
                    );
                }

                if (!productos.containsKey(idProducto)) {
                    throw new Exception(
                        "El producto con ID "
                        + idProducto
                        + " no existe en products.txt. "
                        + "Archivo: "
                        + archivo.getName()
                    );
                }

                Producto producto =
                        productos.get(idProducto);

                double valorVenta =
                        producto.getPrecio() * cantidad;

                vendedor.agregarRecaudo(valorVenta);

                int cantidadActual =
                        cantidadesVendidas.get(idProducto);

                cantidadesVendidas.put(
                    idProducto,
                    cantidadActual + cantidad
                );
            }

        } catch (NumberFormatException e) {

            throw new Exception(
                "Hay un número inválido en "
                + archivo.getName()
                + ", línea "
                + numeroLinea
            );

        } finally {

            lector.close();
        }
    }

   
    private static void generarReporteVendedores()
            throws IOException {

        List<Vendedor> lista =
                new ArrayList<Vendedor>(
                    vendedores.values()
                );

        Collections.sort(
            lista,
            new Comparator<Vendedor>() {

                @Override
                public int compare(
                        Vendedor vendedor1,
                        Vendedor vendedor2) {

                    return Double.compare(
                        vendedor2.getDineroRecaudado(),
                        vendedor1.getDineroRecaudado()
                    );
                }
            }
        );

        BufferedWriter escritor =
                new BufferedWriter(
                    new FileWriter("reporte_vendedores.csv")
                );

        try {

            for (Vendedor vendedor : lista) {

                escritor.write(
                    String.format(
                        "%.2f;%s",
                        vendedor.getDineroRecaudado(),
                        vendedor.getNombreCompleto()
                    )
                );

                escritor.newLine();
            }

        } finally {

            escritor.close();
        }

        System.out.println(
            "Reporte de vendedores creado correctamente."
        );
    }

  
    private static void generarReporteProductos()
            throws IOException {

        List<Producto> lista =
                new ArrayList<Producto>(
                    productos.values()
                );

        Collections.sort(
            lista,
            new Comparator<Producto>() {

                @Override
                public int compare(
                        Producto producto1,
                        Producto producto2) {

                    int cantidad1 =
                            cantidadesVendidas.get(
                                producto1.getId()
                            );

                    int cantidad2 =
                            cantidadesVendidas.get(
                                producto2.getId()
                            );

                    return Integer.compare(
                        cantidad2,
                        cantidad1
                    );
                }
            }
        );

        BufferedWriter escritor =
                new BufferedWriter(
                    new FileWriter("reporte_productos.csv")
                );

        try {

            for (Producto producto : lista) {

                escritor.write(
                    producto.getNombre()
                    + ";"
                    + producto.getPrecio()
                );

                escritor.newLine();
            }

        } finally {

            escritor.close();
        }

        System.out.println(
            "Reporte de productos creado correctamente."
        );
    }
}
