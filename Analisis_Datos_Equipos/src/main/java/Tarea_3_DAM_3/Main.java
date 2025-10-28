package Tarea_3_DAM_3;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        ConexionMySQL conexion = new ConexionMySQL("root", "", "equipos");
        GestionEquipos gestor = new GestionEquipos(conexion);

        try {
            conexion.conectar();
            int opcion;

            do {
                System.out.println("MENÚ PRINCIPAL");
                System.out.println("1 - Recorrer directorio y crear tablas");
                System.out.println("2 - Rellenar datos en tabla");
                System.out.println("3 - Mostrar equipo");
                System.out.println("4 - Eliminar todas las tablas");
                System.out.println("5 - Salir");
                System.out.print("Selecciona una opción: ");
                opcion = teclado.nextInt();
                teclado.nextLine(); // limpiar buffer

                switch (opcion) {
                    case 1:
                        System.out.print("Ruta del directorio con archivos XML: ");
                        String rutaDir = teclado.nextLine();
                        gestor.crearTablas(rutaDir);
                        break;

                    case 2:
                        System.out.print("Nombre del equipo (archivo XML sin .xml): ");
                        String nombreEquipo = teclado.nextLine();
                        gestor.insertarDatosDesdeXML("ruta/del/directorio/" + nombreEquipo + ".xml");
                        break;

                    case 3:
                        System.out.print("Nombre del equipo a mostrar: ");
                        String equipoMostrar = teclado.nextLine();
                        ResultSet rs = conexion.mostrarEquipo(equipoMostrar);
                        System.out.println("Jugadores del equipo " + equipoMostrar + ":");
                        while (rs.next()) {
                            System.out.println("- " + rs.getString("nombre") + " | Dorsal: " + rs.getInt("dorsal")
                                    + " | Posición: " + rs.getString("demarcacion")
                                    + " | Nacimiento: " + rs.getString("nacimiento"));
                        }
                        break;

                    case 4:
                        System.out.println("Eliminando todas las tablas...");
                        conexion.eliminarTodasTablas();
                        break;

                    case 5:
                        System.out.println("Saliendo del programa...");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }

            } while (opcion != 5);

            conexion.desconectar();

        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }

        teclado.close();
    }
}
