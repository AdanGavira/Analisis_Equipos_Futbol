package Tarea_3_DAM_3;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.sql.SQLException;

public class GestionEquipos {

    private ConexionMySQL conexion;

    public GestionEquipos(ConexionMySQL conexion) {
        this.conexion = conexion;
    }

    // Recorrer directorio y crear tablas

    public void crearTablas (String rutaDirectorio) {
        File carpeta = new File(rutaDirectorio);
        File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));

        if (archivos != null) {
            for (File archivo : archivos) {

                String nombreTabla = archivo.getName().replace(".xml", "");

                try {
                    conexion.crearTablaEquipo(nombreTabla);
                    System.out.println("Tabla creada: " + nombreTabla);

                } catch (SQLException e) {

                    System.err.println("Error creando tabla " + nombreTabla + ": " + e.getMessage());
                }
            }
        }
    }

    // Insertar datos en tabla desde archivo XML

    public void insertarDatosDesdeXML(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        String nombreTabla = archivo.getName().replace(".xml", "");

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(archivo);
            doc.getDocumentElement().normalize();

            NodeList jugadores = doc.getElementsByTagName("jugador");

            for (int i = 0; i < jugadores.getLength(); i++) {
                Node nodo = jugadores.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodo;

                    String nombre = elemento.getElementsByTagName("nombre").item(0).getTextContent();
                    int dorsal = Integer.parseInt(elemento.getElementsByTagName("dorsal").item(0).getTextContent());
                    String demarcacion = elemento.getElementsByTagName("demarcacion").item(0).getTextContent();
                    String nacimiento = elemento.getElementsByTagName("nacimiento").item(0).getTextContent();

                    conexion.insertarJugador(nombreTabla, nombre, dorsal, demarcacion, nacimiento);
                }
            }

            System.out.println("Datos insertados en tabla: " + nombreTabla);

        } catch (Exception e) {

            System.err.println("Error procesando archivo " + rutaArchivo + ": " + e.getMessage());
        }
    }
}

