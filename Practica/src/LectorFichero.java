import java.io.*;
import java.util.ArrayList;
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

        int opcion;

        do {
            System.out.println("\n========== MENÚ PRINCIPAL ========= \n");
            System.out.println("1. Añadir usuario\n2. Mostrar usuarios introducidos\n3. Generar fichero de concordancias\n4. Salir\n");
            System.out.println("=================================== \n");
            System.out.println("Seleccione una opción: ");

            opcion = entrada.nextInt();
            entrada.nextLine();

            try {

                switch (opcion) {
                    case 1 -> {
                        br = new BufferedReader(new FileReader("Practica/Ficheros/" + ruta));

                        int siguienteUsuario = ultimoUsuario(br);

                        br.close();

                        System.out.println("Escriba sus aficiones: ");
                        String aficiones = entrada.nextLine().toUpperCase();

                        try (FileWriter fw = new FileWriter("Practica/Ficheros/" + ruta, sobreEscribir)) {
                            fw.write("U" + siguienteUsuario + " ");
                            fw.write(aficiones + "\n");
                        }
                    }

                    case 2 -> mostrarUsuarios(new BufferedReader(new FileReader("Practica/Ficheros/" + ruta)));

                    case 3 -> generarFicheroCooncordancia(new BufferedReader(new FileReader("Practica/Ficheros/" + ruta)), entrada);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } while (opcion != 4);
    }

    private static int ultimoUsuario(BufferedReader br) {
        String linea;
        String ultimaLinea = "";

        try {

            while ((linea = br.readLine()) != null) {
                ultimaLinea = linea;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        StringBuilder codigo = new StringBuilder();

        for (int i = 1; i < ultimaLinea.length(); i++) {
            char caracter = ultimaLinea.charAt(i);

            if (caracter >= '0' && caracter <= '9') codigo.append(caracter);
            if (caracter == ' ') {
                int ultimoNumero = Integer.parseInt(codigo.toString());
                return ultimoNumero + 1;
            }
        }
        return 0;
    }

    private static void mostrarUsuarios(BufferedReader br) {
        String linea;

        try {
            while ((linea = br.readLine()) != null) {

                String[] parametros = linea.split(" ");

                System.out.print("Usuario: " + parametros[0]);
                System.out.print(" | Aficiones: ");

                for (int i = 1; i < parametros.length; i++) {
                    System.out.print(parametros[i] + " ");
                }

                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void generarFicheroCooncordancia(BufferedReader br, Scanner entrada) {
        String linea;
        ArrayList<String> lineas = new ArrayList<>();

        System.out.println("\nIngrese la cantidad deseada de coincidencias. Minimo una: ");
        int minCoincidendias = entrada.nextInt();

        while (minCoincidendias <= 0) {
            System.out.println("\nNúmero de coincidencias inválido. Inténtelo de nuevo: ");
            minCoincidendias = entrada.nextInt();
        }

        try {
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ArrayList<String> listaCoincidencias = new ArrayList<>();

        for (int lineaUsuario = 0; lineaUsuario < lineas.size(); lineaUsuario++) {
            String[] primerUsuario = lineas.get(lineaUsuario).split(" ");

            for (int lineaUsuarioComparado = lineaUsuario + 1; lineaUsuarioComparado < lineas.size(); lineaUsuarioComparado++) {
                String[] segundoUsuario = lineas.get(lineaUsuarioComparado).split(" ");

                StringBuilder coincidencia = new StringBuilder();
                coincidencia.append(primerUsuario[0]).append(" ").append(segundoUsuario[0]).append(" ");
                int cantidadCoincidencias = 0;

                for (int i = 1; i < primerUsuario.length; i++) {
                    for (int y = 1; y < segundoUsuario.length; y++) {

                        if (primerUsuario[i].equals(segundoUsuario[y])) {
                            coincidencia.append(primerUsuario[i]).append(" ");
                            cantidadCoincidencias++;
                        }
                    }
                }
                if (cantidadCoincidencias >= minCoincidendias) listaCoincidencias.add(coincidencia.toString());
            }
        }

        try (FileWriter fw = new FileWriter("Practica/Ficheros/Coincidencias.txt")) {
            for (String usuario : listaCoincidencias) {
                fw.write(usuario);
                fw.write("\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
