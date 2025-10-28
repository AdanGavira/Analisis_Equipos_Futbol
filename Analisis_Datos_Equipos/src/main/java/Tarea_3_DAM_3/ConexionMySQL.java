package Tarea_3_DAM_3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Clase para la conexión con una base de datos MySQL
 *
 * @author Francisco Jesús Delgado Almirón
 */
public class ConexionMySQL {

    // Base de datos a la que nos conectamos
    private String BD;
    // Usuario de la base de datos
    private String USUARIO;
    // Contraseña del usuario de la base de datos
    private String PASS;
    // Objeto donde se almacenará nuestra conexión
    private Connection connection;
    // Indica que está en localhost
    private String HOST;
    // Zona horaria
    private TimeZone zonahoraria;

    /**
     * Constructor de la clase
     *
     * @param usuario Usuario de la base de datos
     * @param pass Contraseña del usuario
     * @param bd Base de datos a la que nos conectamos
     */
    public ConexionMySQL(String usuario, String pass, String bd) {
        HOST = "localhost";
        USUARIO = usuario;
        PASS = pass;
        BD = bd;
        connection = null;
    }

    /**
     * Comprueba que el driver de MySQL esté correctamente integrado
     *
     * @throws SQLException Se lanzará cuando haya un fallo con la base de datos
     */
    private void registrarDriver() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al conectar con MySQL: " + e.getMessage());
        }
    }

    /**
     * Conecta con la base de datos
     *
     * @throws SQLException Se lanzará cuando haya un fallo con la base de datos
     */
    public void conectar() throws SQLException {
        if (connection == null || connection.isClosed()) {
            registrarDriver();
            // Obtengo la zona horaria
            Calendar now = Calendar.getInstance();
            zonahoraria = now.getTimeZone();
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://" + HOST + "/" + BD + "?user="
                    + USUARIO + "&password=" + PASS + "&useLegacyDatetimeCode=false&serverTimezone="
                    + zonahoraria.getID());
        }
    }

    /**
     * Cierra la conexión con la base de datos
     *
     * @throws SQLException Se lanzará cuando haya un fallo con la base de datos
     */
    public void desconectar() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


    /**
     * Ejecuta una consulta SELECT
     *
     * @param consulta Consulta SELECT a ejecutar
     * @return Resultado de la consulta
     * @throws SQLException Se lanzará cuando haya un fallo con la base de datos
     */
    public ResultSet ejecutarSelect(String consulta) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rset = stmt.executeQuery(consulta);

        return rset;
    }

    /**
     * Ejecuta una consulta INSERT, DELETE o UPDATE
     *
     * @param consulta Consulta INSERT, DELETE o UPDATE a ejecutar
     * @return Cantidad de filas afectadas
     * @throws SQLException Se lanzará cuando haya un fallo con la base de datos
     */
    public int ejecutarInsertDeleteUpdate(String consulta) throws SQLException {
        Statement stmt = connection.createStatement();
        int fila = stmt.executeUpdate(consulta);

        return fila;
    }

    public void crearTablaEquipo (String nombreTabla) throws SQLException{
        String consulta = "CREATE TABLE IF NOT EXISTS " +  nombreTabla + " ("
                + "nombre VARCHAR(100), "
                + "dorsal INT, "
                + "demarcacion VARCHAR(50), "
                + "nacimiento VARCHAR(20), "
                + ")";
        ejecutarInsertDeleteUpdate(consulta);
    }

    public void eliminarTabla(String nombreTabla) throws SQLException{
        String consulta = "DROP TABLE IF EXISTS " + nombreTabla + "";
        ejecutarInsertDeleteUpdate(consulta);
    }

    public void eliminarTodasTablas() throws SQLException{
        ResultSet rs = ejecutarSelect("SHOW TABLES");
        while (rs.next()) {
            String tabla = rs.getString(1);
            ejecutarInsertDeleteUpdate(tabla);
        }
    }

    public void insertarJugador(String nombreTabla, String Nombre, int dorsal, String demarcacion, String nacimiento) throws SQLException{
        String consulta = "INSERT INTO " + nombreTabla + " (nombre, dorsal, demarcacion, nacimiento) VALUES (" + Nombre + ", " + dorsal + ", " + demarcacion + ", " + nacimiento + ")";
        ejecutarInsertDeleteUpdate(consulta);
    }

    public ResultSet mostrarEquipo(String nombreTabla) throws SQLException{
        String consulta = "SELECT * FROM " + nombreTabla + "";
        return ejecutarSelect(consulta);
    }
}
