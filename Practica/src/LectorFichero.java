import java.io.*;
import java.util.Scanner;

public class LectorFichero {
    static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Ingrese el nombre del fichero: ");
        String ruta = entrada.nextLine();
        File archivo = new File("Practica/Ficheros/" + ruta);
        BufferedReader br;
        boolean sobreEscribir = true;

        if (!archivo.exists()) {
            System.out.println("El archivo no existe. ¿Desea crearlo? s/n");
            String opcion = entrada.nextLine().toLowerCase();
            sobreEscribir = false;


            if (!opcion.equals("s")) {
                System.out.println("Fin");
                return;
            }
        }

        try (FileWriter fw = new FileWriter("Practica/Ficheros/" + ruta, sobreEscribir)) {
            br = new BufferedReader(new FileReader("Practica/Ficheros/" + ruta));

            int opcion;

            do {
                System.out.println("\n========== MENÚ PRINCIPAL ========= \n");
                System.out.println("1. Añadir usuario\n2. Mostrar usuarios introducidos\n3. Generar fichero de concordancias\n4. Salir\n");
                System.out.println("=================================== \n");
                System.out.println("Seleccione una opción: ");

                opcion = entrada.nextInt();
                entrada.nextLine();

                switch (opcion) {
                    case 1 -> {
                        fw.write("U100 ");
                        System.out.println("Escriba sus aficiones: ");
                        String aficiones = entrada.nextLine().toUpperCase();
                        fw.write(aficiones + "\n");
                    }
                }

            } while (opcion != 4);

        } catch (IOException e) {
            System.out.println(e);
        }



    }



}
