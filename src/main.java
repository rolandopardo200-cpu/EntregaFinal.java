
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class main {

    public static void main(String[] args) {
        try {
            generarReporteVentasPorVendedor(
                "vendedores.txt",
                "productos.txt",
                "ventas.txt",
                "reporte_vendedores.csv"
            );

            generarReporteProductosMasVendidos(
                "productos.txt",
                "ventas.txt",
                "reporte_productos.csv"
            );

            System.out.println(
                "Proceso finalizado con exito: "
                + "los dos reportes fueron generados correctamente."
            );

        } catch (Exception e) {
            System.err.println(
                "Error durante la generacion de reportes: "
                + e.getMessage()
            );
        }
    }

    private static void generarReporteVentasPorVendedor(
            String rutaVendedores,
            String rutaProductos,
            String rutaVentas,
            String rutaSalida) throws IOException {

        Map<Integer, String> vendedores = new TreeMap<>();
        Map<Integer, Double> precios = cargarPrecios(rutaProductos);
        Map<Integer, Double> totales = new HashMap<>();

        try (BufferedReader reader =
                new BufferedReader(new FileReader(rutaVendedores))) {

            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] d = linea.split(";", -1);

                if (d.length < 4) {
                    throw new IOException(
                        "Registro de vendedor invalido: " + linea
                    );
                }

                int id = Integer.parseInt(d[0].trim());
                String nombre = d[2].trim() + " " + d[3].trim();

                vendedores.put(id, nombre);
                totales.put(id, 0.0);
            }
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(rutaVentas))) {

            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] d = linea.split(";", -1);

                if (d.length < 3) {
                    throw new IOException(
                        "Registro de venta invalido: " + linea
                    );
                }

                int idVendedor = Integer.parseInt(d[0].trim());
                int idProducto = Integer.parseInt(d[1].trim());
                int cantidad = Integer.parseInt(d[2].trim());

                if (!vendedores.containsKey(idVendedor)) {
                    throw new IOException(
                        "Vendedor inexistente: " + idVendedor
                    );
                }

                if (!precios.containsKey(idProducto)) {
                    throw new IOException(
                        "Producto inexistente: " + idProducto
                    );
                }

                if (cantidad <= 0) {
                    throw new IOException(
                        "Cantidad de venta invalida: " + linea
                    );
                }

                double importe = precios.get(idProducto) * cantidad;

                totales.put(
                    idVendedor,
                    totales.get(idVendedor) + importe
                );
            }
        }

        List<Integer> ids = new ArrayList<>(vendedores.keySet());

        ids.sort(
            Comparator.comparingDouble(
                (Integer id) -> totales.get(id)
            ).reversed()
        );

        try (PrintWriter writer =
                new PrintWriter(new FileWriter(rutaSalida))) {

            writer.println("ID_VENDEDOR;VENDEDOR;TOTAL_VENTAS");

            for (int id : ids) {
                writer.printf(
                    java.util.Locale.US,
                    "%d;%s;%.2f%n",
                    id,
                    vendedores.get(id),
                    totales.get(id)
                );
            }

            verificarEscritura(writer);
        }
    }

    private static void generarReporteProductosMasVendidos(
            String rutaProductos,
            String rutaVentas,
            String rutaSalida) throws IOException {

        Map<Integer, String> nombres = new TreeMap<>();
        Map<Integer, Integer> cantidades = new HashMap<>();

        try (BufferedReader reader =
                new BufferedReader(new FileReader(rutaProductos))) {

            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] d = linea.split(";", -1);

                if (d.length < 3) {
                    throw new IOException(
                        "Registro de producto invalido: " + linea
                    );
                }

                int id = Integer.parseInt(d[0].trim());
                nombres.put(id, d[1].trim());
                cantidades.put(id, 0);
            }
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(rutaVentas))) {

            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] d = linea.split(";", -1);

                if (d.length < 3) {
                    throw new IOException(
                        "Registro de venta invalido: " + linea
                    );
                }

                int idProducto = Integer.parseInt(d[1].trim());
                int cantidad = Integer.parseInt(d[2].trim());

                if (!nombres.containsKey(idProducto)) {
                    throw new IOException(
                        "Producto inexistente: " + idProducto
                    );
                }

                if (cantidad <= 0) {
                    throw new IOException(
                        "Cantidad de venta invalida: " + linea
                    );
                }

                cantidades.put(
                    idProducto,
                    cantidades.get(idProducto) + cantidad
                );
            }
        }

        List<Integer> ids = new ArrayList<>(nombres.keySet());

        ids.sort(
            Comparator.comparingInt(
                (Integer id) -> cantidades.get(id)
            ).reversed()
        );

        try (PrintWriter writer =
                new PrintWriter(new FileWriter(rutaSalida))) {

            writer.println("ID_PRODUCTO;PRODUCTO;CANTIDAD_TOTAL");

            for (int id : ids) {
                writer.printf(
                    "%d;%s;%d%n",
                    id,
                    nombres.get(id),
                    cantidades.get(id)
                );
            }

            verificarEscritura(writer);
        }
    }

    private static Map<Integer, Double> cargarPrecios(
            String rutaProductos) throws IOException {

        Map<Integer, Double> precios = new HashMap<>();

        try (BufferedReader reader =
                new BufferedReader(new FileReader(rutaProductos))) {

            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] d = linea.split(";", -1);

                if (d.length < 3) {
                    throw new IOException(
                        "Registro de producto invalido: " + linea
                    );
                }

                int id = Integer.parseInt(d[0].trim());
                double precio = Double.parseDouble(d[2].trim());

                if (precio < 0) {
                    throw new IOException(
                        "Precio invalido para producto: " + id
                    );
                }

                precios.put(id, precio);
            }
        }

        return precios;
    }

    private static void verificarEscritura(PrintWriter writer)
            throws IOException {

        writer.flush();

        if (writer.checkError()) {
            throw new IOException(
                "No se pudo escribir correctamente el reporte."
            );
        }
    }
}
