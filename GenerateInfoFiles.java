import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateInfoFiles {

    public static void main(String[] args) {

        int cantidadProductos = 10;
        int cantidadVendedores = 5;
        int cantidadVentas = 5;

        createProductsFile(cantidadProductos);
        createSalesManInfoFile(cantidadVendedores);

        long[] documentos = {
            58347291,
            29481736,
            74192836,
            38174625,
            62918374
        };

        for (int i = 0; i < cantidadVendedores; i++) {

            String nombreArchivo = "salesman" + (i + 1) + ".txt";

            createSalesMenFile(
                cantidadVentas,
                nombreArchivo,
                documentos[i]
            );
        }

        System.out.println("Proceso terminado correctamente.");

    }

    public static void createProductsFile(int productsCount) {

        String[] productNames = {
            "Arroz",
            "Leche",
            "Cafe",
            "Pan",
            "Azucar",
            "Sal",
            "Aceite",
            "Harina",
            "Galletas",
            "Chocolate"
        };

        Random random = new Random();

        try {

            FileWriter archivo = new FileWriter("products.txt");

            for (int i = 1; i <= productsCount; i++) {

                String nombre = productNames[i - 1];

                int precio = 2000 + random.nextInt(9000);

                archivo.write(
                    i + ";" + nombre + ";" + precio + "\n"
                );
            }

            archivo.close();

            System.out.println(
                "Archivo de productos creado correctamente."
            );

        } catch (IOException e) {

            System.out.println(
                "Error al crear el archivo de productos."
            );
        }
    }

    public static void createSalesManInfoFile(int salesmanCount) {

        String[] nombres = {
            "Juan",
            "Maria",
            "Carlos",
            "Ana",
            "Pedro"
        };

        String[] apellidos = {
            "Rivero",
            "Gomez",
            "Perez",
            "Rodriguez",
            "Martinez"
        };

        long[] documentos = {
            58347291,
            29481736,
            74192836,
            38174625,
            62918374
        };

        try {

            FileWriter archivo =
                new FileWriter("salesmanInfo.txt");

            for (int i = 0; i < salesmanCount; i++) {

                archivo.write(
                    "CC;" +
                    documentos[i] + ";" +
                    nombres[i] + ";" +
                    apellidos[i] + "\n"
                );
            }

            archivo.close();

            System.out.println(
                "Archivo de vendedores creado correctamente."
            );

        } catch (IOException e) {

            System.out.println(
                "Error al crear el archivo de vendedores."
            );
        }
    }

    public static void createSalesMenFile(
            int randomSalesCount,
            String name,
            long id) {

        Random random = new Random();

        try {

            FileWriter archivo =
                new FileWriter(name);

            archivo.write("CC;" + id + "\n");

            for (int i = 0; i < randomSalesCount; i++) {

                int producto =
                    1 + random.nextInt(10);

                int cantidad =
                    1 + random.nextInt(10);

                archivo.write(
                    producto + ";" + cantidad + ";\n"
                );
            }

            archivo.close();

            System.out.println(
                "Archivo de ventas creado: " + name
            );

        } catch (IOException e) {

            System.out.println(
                "Error al crear el archivo de ventas."
            );
        }
    }
}