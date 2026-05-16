package DAO;

import Entidad.CamaraRefrigeracion;
import java.sql.*;

/**
 * Clase DAO para la gestion de camaras de refrigeracion del sistema Code4Hope.
 * Implementa las operaciones necesarias sobre la tabla CAMARA_REFRIGERACION.
 *
 * La conexion se recibe como parametro en cada metodo; su gestion
 * es responsabilidad de la clase llamante (Conexion_DB).
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class CamaraRefrigeracionDAO {

    /**
     * Imprime los detalles de una excepcion SQL.
     */
    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                System.err.println("--- SQLException ----------------------------");
                System.err.println("  Mensaje    : " + e.getMessage());
                System.err.println("  SQLState   : " + ((SQLException) e).getSQLState());
                System.err.println("  Codigo err : " + ((SQLException) e).getErrorCode());
                Throwable t = ex.getCause();
                if (t != null) {
                    System.err.println("  Causa      : " + t);
                }
                System.err.println("---------------------------------------------");
            }
        }
    }

    /**
     * Inserta una nueva camara de refrigeracion en la tabla CAMARA_REFRIGERACION.
     * Genera un codigo UUID nuevo y lo asigna como clave primaria.
     *
     * @param con    Conexion activa con la base de datos
     * @param camara Objeto CamaraRefrigeracion con los datos a insertar
     * @return El codigo UUID asignado a la camara, o null si fallo la insercion
     */
    public String insertarCamara(Connection con, CamaraRefrigeracion camara) {
        PreparedStatement ps = null;
        String codigoGenerado = null;

        try {
            codigoGenerado = camara.getCodigo();

            ps = con.prepareStatement(
                    "INSERT INTO CAMARA_REFRIGERACION " +
                            "(codigo, capacidad, temperatura_minima, temperatura_maxima, temperatura_actual, activo) " +
                            "VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, codigoGenerado);
            ps.setDouble(2, camara.getCapacidad());
            ps.setDouble(3, camara.getTemperaturaMinima());
            ps.setDouble(4, camara.getTemperaturaMaxima());
            ps.setDouble(5, camara.getTemperaturaActual());
            ps.setBoolean(6, camara.estaActivo());
            ps.executeUpdate();

            System.out.println("  Camara de refrigeracion insertada -> " + codigoGenerado.substring(0, 8) + "...");

        } catch (SQLException ex) {
            printSQLException(ex);
            codigoGenerado = null;
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return codigoGenerado;
    }

    /**
     * Recupera una camara de refrigeracion por su codigo UUID.
     *
     * @param con    Conexion activa con la base de datos
     * @param codigo UUID de la camara a recuperar
     * @return Objeto CamaraRefrigeracion con los datos de la BD, o null si no
     *         existe
     */
    public CamaraRefrigeracion obtenerCamara(Connection con, String codigo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        CamaraRefrigeracion camara = null;

        try {
            ps = con.prepareStatement(
                    "SELECT codigo, capacidad, temperatura_minima, temperatura_maxima, " +
                            "       temperatura_actual, activo " +
                            "FROM CAMARA_REFRIGERACION " +
                            "WHERE codigo = ?");
            ps.setString(1, codigo);
            rs = ps.executeQuery();

            if (rs.next()) {
                camara = new CamaraRefrigeracion(
                        rs.getString("codigo"),
                        rs.getDouble("capacidad"),
                        rs.getDouble("temperatura_minima"),
                        rs.getDouble("temperatura_maxima"),
                        rs.getDouble("temperatura_actual"),
                        rs.getBoolean("activo"));
            }

        } catch (SQLException ex) {
            printSQLException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return camara;
    }
}
