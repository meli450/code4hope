package DAO;

import Entidad.AsignacionLote;
import Entidad.Patrulla;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para la gestion de asignaciones de lotes a patrullas.
 * Implementa las operaciones sobre la tabla ASIGNACION_LOTE y permite
 * consultar las patrullas registradas en la tabla Patrulla.
 *
 * La conexion con la BD se recibe como parametro en cada metodo.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class AsignacionLoteDAO {

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
     * Inserta una nueva asignacion de lote a patrulla en ASIGNACION_LOTE.
     *
     * @param con        Conexion activa con la base de datos
     * @param asignacion Objeto AsignacionLote con los datos a insertar
     * @return ID generado por la BD (AUTO_INCREMENT) o -1 si fallo
     */
    public int insertar(Connection con, AsignacionLote asignacion) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idGenerado = -1;

        try {
            ps = con.prepareStatement(
                    "INSERT INTO ASIGNACION_LOTE " +
                            "(id_lote, id_patrulla, cantidad_asignada, fecha_asignacion) " +
                            "VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, asignacion.getIdLote());
            ps.setInt(2, asignacion.getIdPatrulla());
            ps.setInt(3, asignacion.getCantidadAsignada());
            ps.setDate(4, Date.valueOf(asignacion.getFechaAsignacion()));
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                asignacion.setIdAsignacion(idGenerado);
            }
            System.out.println("  Asignacion registrada -> ID: " + idGenerado);

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

        return idGenerado;
    }

    /**
     * Recupera todas las patrullas registradas en la BD.
     * Se usa para mostrar al usuario las opciones disponibles
     * antes de elegir a cual asignar un lote.
     *
     * @param con Conexion activa con la base de datos
     * @return Lista de Patrulla, vacia si no hay ninguna o si fallo la consulta
     */
    public List<Patrulla> listarPatrullas(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Patrulla> patrullas = new ArrayList<>();
        Patrulla p;

        try {
            ps = con.prepareStatement(
                    "SELECT id, codigo, estado, vehiculo_id, ruta_id, equipoComunicacion_id " +
                            "FROM Patrulla " +
                            "ORDER BY id ASC");
            rs = ps.executeQuery();

            while (rs.next()) {
                p = new Patrulla();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setEstado(Patrulla.Estado.fromString(rs.getString("estado")));
                p.setVehiculoId(rs.getInt("vehiculo_id"));
                p.setRutaId(rs.getInt("ruta_id"));
                p.setEquipoComunicacionId(rs.getInt("equipoComunicacion_id"));
                patrullas.add(p);
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

        return patrullas;
    }

    /**
     * Recupera todas las asignaciones asociadas a un lote concreto.
     *
     * @param con    Conexion activa con la base de datos
     * @param idLote ID del lote del que se quieren ver las asignaciones
     * @return Lista de AsignacionLote, vacia si no hay ninguna o si fallo la
     *         consulta
     */
    public List<AsignacionLote> obtenerPorLote(Connection con, int idLote) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AsignacionLote> asignaciones = new ArrayList<>();
        AsignacionLote a;

        try {
            ps = con.prepareStatement(
                    "SELECT id_asignacion, id_lote, id_patrulla, " +
                            "       cantidad_asignada, fecha_asignacion " +
                            "FROM ASIGNACION_LOTE " +
                            "WHERE id_lote = ? " +
                            "ORDER BY fecha_asignacion ASC");
            ps.setInt(1, idLote);
            rs = ps.executeQuery();

            while (rs.next()) {
                a = new AsignacionLote(
                        rs.getInt("id_asignacion"),
                        rs.getInt("id_lote"),
                        rs.getInt("id_patrulla"),
                        rs.getInt("cantidad_asignada"),
                        rs.getDate("fecha_asignacion").toLocalDate());
                asignaciones.add(a);
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

        return asignaciones;
    }
}
