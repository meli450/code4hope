package DAO;

import java.sql.*;

/**
 * Clase de utilidad para gestionar la conexion con la base de datos MySQL.
 * Proporciona metodos para abrir y cerrar la conexion de forma centralizada.
 *
 * Base de datos : code4hope
 * Host : localhost:3306
 * Usuario : root
 * Contrasena : root
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class Conexion_DB {

    private static final String URL = "jdbc:mysql://localhost:3306/code4hope";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Abre y devuelve una conexion activa con la base de datos.
     *
     * @return Objeto Connection listo para usar
     * @throws Exception Si el driver no se encuentra o la conexion falla
     */
    public Connection abrirConexion() throws Exception {
        Connection conexion = null;
        try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexion establecida con la BD");
        } catch (ClassNotFoundException error) {
            throw new Exception("Driver no encontrado: " + error.getMessage());
        } catch (SQLException error) {
            error.printStackTrace();
            throw new Exception("No se pudo conectar: " + error.getMessage());
        }
        return conexion;
    }

    /**
     * Cierra la conexion con la base de datos si esta abierta.
     *
     * @param conexion Objeto Connection a cerrar (puede ser null)
     * @throws Exception Si ocurre un error al cerrar la conexion
     */
    public void cerrarConexion(Connection conexion) throws Exception {
        try {
            if (conexion != null) {
                conexion.close();
                System.out.println("Conexion cerrada con la BD");
            }
        } catch (SQLException error) {
            throw new Exception("Error al cerrar: " + error.getMessage());
        }
    }
}
