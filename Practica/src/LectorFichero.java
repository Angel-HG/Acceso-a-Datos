import java.io.*;
import java.util.*;

public class LectorFichero {
    static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        File archivo;
        boolean sobreEscribir;
        String ruta;

        do {
            System.out.println("Ingrese el nombre del fichero: ");
            ruta = entrada.nextLine();

            //Comprueba que el fichero exista
             archivo = new File("Practica/Ficheros/" + ruta);
             sobreEscribir = true;

             if (archivo.length() > 10000) System.out.println("Fichero inválido, tamaño superior a 10000 bytes");


        } while (archivo.length() > 10000);

        if (!archivo.exists()) {
            System.out.println("El archivo no existe. ¿Desea crearlo? s/n");
            String opcion = entrada.nextLine().toLowerCase();
            sobreEscribir = false;

            if (!opcion.equals("s")) {
                System.out.println("Fin");
                return;
            }
        }

        else {
            if (!validarFichero(ruta)) {
                System.out.println("Formato de archivo inválido");
                return;
            }
        }

        //Inicia el menú
        iniciarMenu(entrada, ruta, sobreEscribir);
    }

    private static void iniciarMenu(Scanner entrada, String ruta, boolean sobreEscribir) {
        int opcion;

        do {
            System.out.println("\n========== MENÚ PRINCIPAL ========= \n");
            System.out.println("1. Añadir usuario\n2. Mostrar usuarios introducidos\n3. Generar fichero de concordancias\n4. Salir\n");
            System.out.println("=================================== \n");
            System.out.println("Seleccione una opción: ");

            try {
                opcion = entrada.nextInt();
                entrada.nextLine();

                switch (opcion) {
                    case 1 -> annadirUsuario(new BufferedReader(new FileReader("Practica/Ficheros/" + ruta)), entrada, ruta, sobreEscribir);
                    case 2 -> mostrarUsuarios(new BufferedReader(new FileReader("Practica/Ficheros/" + ruta)));
                    case 3 -> generarFicheroCooncordancia(new BufferedReader(new FileReader("Practica/Ficheros/" + ruta)), entrada);
                    case 4 -> System.out.println("FIN");
                    default -> System.out.println("Opción inválida");
                }

            } catch (Exception e) {
                System.out.println("Debe introducir un número.");
                entrada.nextLine();
                opcion = 0;
            }

        } while (opcion != 4);
    }

    //Metodo para añadir a un usuario
    private static void annadirUsuario(BufferedReader br, Scanner entrada, String ruta, boolean sobreEscribir) throws IOException {
        //Obtiene el último número del usuario
        int siguienteUsuario = siguienteUsuario(br);

        br.close();

        String aficiones;

        do {
            System.out.println("Escriba sus aficiones, separadas por espacios en blanco: ");
            aficiones = entrada.nextLine().toUpperCase();

            if (aficiones.isEmpty()) System.out.println("Cantidad de aficiones inválida. Intentelo de nuevo");
        } while (aficiones.isEmpty());

        //Escribe el usuario con las aficiones
        try (FileWriter fw = new FileWriter("Practica/Ficheros/" + ruta, sobreEscribir)) {
            fw.write("U" + siguienteUsuario + " ");
            fw.write(aficiones + "\n");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Metodo para obtener un codigo que no este diponible del primer número disponible
    private static int siguienteUsuario(BufferedReader br) {
        String linea;
        ArrayList<Integer> codigos = new ArrayList<>();

        try {
            //Recorre todas las líneas extrayendo los números
            while ((linea = br.readLine()) != null) {
                String[] usuario = linea.split(" ");

                //Extrae el numero lo convierte a int
                int codigoUsuario = Integer.parseInt(usuario[0].substring(1));
                codigos.add(codigoUsuario);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (codigos.isEmpty()) return 0;
        //ordena los numeros en caso que estén desordenados
        Collections.sort(codigos);

        //Bucle que va comparando todos los números que hay, en caso que falte uno devuelve el primer número que falte, en caso contrario devuelve el último número mas 1
        for (int i = 0; i < codigos.size() - 1; i++) {
            int siguienteCodigo = codigos.get(i) + 1;

            if (siguienteCodigo != codigos.get(i + 1)) return siguienteCodigo;
        }
        return codigos.getLast() + 1;
    }

    //Metodo para listar usuarios
    private static void mostrarUsuarios(BufferedReader br) {
        String linea;

        try {
            while ((linea = br.readLine()) != null) {

                //Divide toda la linea en un array por espacios, muestra el primer elemento que es el usuario y luego las aficiones.
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

    //Metodo para generar el fichero de concordancia
    private static void generarFicheroCooncordancia(BufferedReader br, Scanner entrada) {
        String linea;
        ArrayList<String> lineas = new ArrayList<>();

        //Pide el minimo de coincidencias
        System.out.println("\nIngrese la cantidad deseada de coincidencias. Minimo una: ");
        int minCoincidendias = entrada.nextInt();

        while (minCoincidendias <= 0) {
            System.out.println("\nNúmero de coincidencias inválido. Inténtelo de nuevo: ");
            minCoincidendias = entrada.nextInt();
        }

        //Extrae las lineas del fichero al array
        try {
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ArrayList<String> listaCoincidencias = new ArrayList<>();

        //Primer bucle que ira recorriendo todas las lineas extrayendo el usuario para tomarlo de referencia
        int numParejas = 0;
        for (int lineaUsuario = 0; lineaUsuario < lineas.size(); lineaUsuario++) {
            String[] primerUsuario = lineas.get(lineaUsuario).split(" ");

            //Esto es para ordenar las aficiones de forma alfabetica ignorando al usuario
            Arrays.sort(primerUsuario, 1, primerUsuario.length);

            //Seegundo bucle recorrera toda la lista a partir del usuario de referencia extratendo un usuario para comparar aficiones
            for (int lineaUsuarioComparado = lineaUsuario + 1; lineaUsuarioComparado < lineas.size(); lineaUsuarioComparado++) {
                String[] segundoUsuario = lineas.get(lineaUsuarioComparado).split(" ");

                //En caso que el segundo usuario no tenga la cantidad necesaria de aficiones se lo salta
                if (segundoUsuario.length - 1 < minCoincidendias) continue;

                //Lo mismo que el anterior, para ordenar las aficiones del usuario
                Arrays.sort(segundoUsuario, 1, segundoUsuario.length);

                StringBuilder coincidencia = new StringBuilder();

                //Prepara la parte de la línea donde se ven los usuarios.
                coincidencia.append(primerUsuario[0]).append(" ").append(segundoUsuario[0]).append(" ");
                int cantidadCoincidencias = 0;

                //Bucle para comprobar que alguna aficion coincide con todas las aficiones del usuario.
                for (int i = 1; i < primerUsuario.length; i++) {
                    for (int y = 1; y < segundoUsuario.length; y++) {

                        if (primerUsuario[i].equals(segundoUsuario[y])) {
                            coincidencia.append(primerUsuario[i]).append(" ");
                            cantidadCoincidencias++;
                        }
                    }
                }
                if (cantidadCoincidencias >= minCoincidendias) {
                    listaCoincidencias.add(coincidencia.toString());
                    numParejas++;
                }
            }
            //Muestra la cantidad de parejas de cada usuario
        }
        System.out.printf("   El fichero contiene %d parejas\n", numParejas);

        //Una vez que ya esta el array con las cooincidencias se ordena
        if (listaCoincidencias.isEmpty()) System.out.println("No existen parejas");
        else ordenarAficiones(listaCoincidencias);


    }

    //Metodo para ordenar y escribir el fichero
    private static void ordenarAficiones(ArrayList<String> coincidencias) {

        //Matriz de ArrayLists para poder almacenar todas las lineas en arrays y ordenarlos
        ArrayList<ArrayList<String>> lineasOrdenada = new ArrayList<>();

        //Bucle para extraer todas las lineas y meterla a una matriz
        for (String linea : coincidencias) {
            String[] aficiones = linea.split(" ");

            lineasOrdenada.add(new ArrayList<>(Arrays.asList(aficiones)));
        }

        //Ordenar la matriz según la longitud de cada ArrayList, crear un comparador y luego le indica reverse para que sea descendente, por último ordena
        Comparator<ArrayList<String>> comparador = Comparator.comparingInt(ArrayList::size);
        comparador = comparador.reversed();

        lineasOrdenada.sort(comparador);

        //Con la matriz ordenada crea el fichero recorriendo la matriz.
        try (FileWriter fw = new FileWriter("Practica/Ficheros/concordancias.txt")) {
            for (ArrayList<String> strings : lineasOrdenada) {
                for (String string : strings) {
                    fw.write(string);
                    fw.write(" ");
                }
                fw.write("\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Metodo que válida el formato del fichero
    private static boolean validarFichero(String ruta) {
        ArrayList<String> usuarios = new ArrayList<>();

        String linea;
        try (BufferedReader br = new BufferedReader(new FileReader("Practica/Ficheros/" + ruta))){

            //Recorre todas las líneas hasta llegar hasta la última comprobando que cumple con el formato
            while ((linea = br.readLine()) != null) {
                String[] usuario = linea.split(" ");


                //Comprueba que al menos el usuario tenga una afición
                if (usuario.length < 2) return false;

                //Coprueba que el codigo de usuario la menos tenga la U y un número
                if (usuario[0].length() < 2) return false;
                if (usuario[0].charAt(0) != 'U') return false;

                //Válida que sea un número correcto
                for (int i = 1; i < usuario[0].length(); i++) {
                    char caracter = usuario[0].charAt(i);

                    if (caracter < '0' || caracter > '9') return false;
                }

                //Comprueba que no haya usuario repetido
                if (usuarios.contains(usuario[0])) return false;
                else usuarios.add(usuario[0]);

                //Crea una copia y ordena las aficiones del usuario
                String[] aficionesOrdenadas = linea.split(" ");
                Arrays.sort(aficionesOrdenadas, 1, aficionesOrdenadas.length);

                for (int aficion = 1; aficion < usuario.length; aficion++) {
                    for (int posicion = 0; posicion < usuario[aficion].length(); posicion++) {
                        char caracter = usuario[aficion].charAt(posicion);

                        //Válida que las aficiones estén en mayúsculas
                        if (caracter < 'A' || caracter > 'Z') return false;
                    }

                    //Valida que las aficiones están ordenadas alfabéticamente
                    if (!(usuario[aficion].equals(aficionesOrdenadas[aficion]))) return false;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
}
