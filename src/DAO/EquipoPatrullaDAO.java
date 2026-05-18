package DAO;

import Entidad.EquipoComunicacion;
import Entidad.Patrulla;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos para la tabla de asociacion {@code Equipo_Patrulla}.
 * Gestiona el vinculo muchos-a-muchos entre {@link EquipoComunicacion} y {@link Patrulla},
 * permitiendo vincular, desvincular y consultar las relaciones entre ambas entidades.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class EquipoPatrullaDAO {

    /**
     * Crea un vinculo entre un equipo de comunicacion y una patrulla en la tabla {@code Equipo_Patrulla}.
     *
     * @param con        conexion activa a la base de datos.
     * @param equipoId   identificador del equipo de comunicacion a vincular.
     * @param patrullaId identificador de la patrulla a vincular.
     * @return {@code true} si el vinculo fue creado con exito.
     * @throws Exception si se produce un error durante la insercion.
     */
    public boolean vincular(Connection con, int equipoId, int patrullaId) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement(
                "INSERT INTO Equipo_Patrulla (equipo_id, patrulla_id) VALUES (?,?)");
            stmt.setInt(1, equipoId);
            stmt.setInt(2, patrullaId);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al vincular equipo con patrulla: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Elimina el vinculo entre un equipo de comunicacion y una patrulla.
     *
     * @param con        conexion activa a la base de datos.
     * @param equipoId   identificador del equipo de comunicacion a desvincular.
     * @param patrullaId identificador de la patrulla de la que se desvincula el equipo.
     * @return {@code true} si el vinculo fue eliminado; {@code false} si no existia.
     * @throws Exception si se produce un error durante la eliminacion.
     */
    public boolean desvincular(Connection con, int equipoId, int patrullaId) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement(
                "DELETE FROM Equipo_Patrulla WHERE equipo_id=? AND patrulla_id=?");
            stmt.setInt(1, equipoId);
            stmt.setInt(2, patrullaId);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al desvincular equipo de patrulla: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Recupera todas las patrullas vinculadas a un equipo de comunicacion concreto.
     *
     * @param con      conexion activa a la base de datos.
     * @param equipoId identificador del equipo cuyas patrullas se quieren obtener.
     * @return lista de patrullas vinculadas al equipo; vacia si no hay ninguna.
     * @throws Exception si se produce un error durante la consulta.
     */
    public List<Patrulla> getPatrullasPorEquipo(Connection con, int equipoId) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Patrulla> lista = new ArrayList<>();
        try {
            stmt = con.prepareStatement(
                "SELECT p.* FROM Patrulla p " +
                "INNER JOIN Equipo_Patrulla ep ON p.id = ep.patrulla_id " +
                "WHERE ep.equipo_id=?");
            stmt.setInt(1, equipoId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Patrulla p = new Patrulla();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setEstado(Patrulla.Estado.fromString(rs.getString("estado")));
                p.setVehiculoId(rs.getInt("vehiculo_id"));
                p.setRutaId(rs.getInt("ruta_id"));
                p.setEquipoComunicacionId(rs.getInt("equipoComunicacion_id"));
                lista.add(p);
            }
        } catch (SQLException ex) {
            throw new Exception("Error al obtener patrullas del equipo: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return lista;
    }

    /**
     * Recupera todos los equipos de comunicacion vinculados a una patrulla concreta.
     *
     * @param con        conexion activa a la base de datos.
     * @param patrullaId identificador de la patrulla cuyos equipos se quieren obtener.
     * @return lista de equipos vinculados a la patrulla; vacia si no hay ninguno.
     * @throws Exception si se produce un error durante la consulta.
     */
    public List<EquipoComunicacion> getEquiposPorPatrulla(Connection con, int patrullaId) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EquipoComunicacion> lista = new ArrayList<>();
        try {
            stmt = con.prepareStatement(
                "SELECT e.* FROM EquipoComunicacion e " +
                "INNER JOIN Equipo_Patrulla ep ON e.id = ep.equipo_id " +
                "WHERE ep.patrulla_id=?");
            stmt.setInt(1, patrullaId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                EquipoComunicacion e = new EquipoComunicacion();
                e.setId(rs.getInt("id"));
                e.setNombre(rs.getString("nombre"));
                e.setEstadoEquipo(EquipoComunicacion.EstadoEquipo.fromString(rs.getString("estadoEquipo")));
                lista.add(e);
            }
        } catch (SQLException ex) {
            throw new Exception("Error al obtener equipos de la patrulla: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return lista;
    }

    /**
     * Comprueba si existe ya un vinculo entre un equipo de comunicacion y una patrulla.
     *
     * @param con        conexion activa a la base de datos.
     * @param equipoId   identificador del equipo de comunicacion.
     * @param patrullaId identificador de la patrulla.
     * @return {@code true} si el vinculo existe; {@code false} en caso contrario.
     * @throws Exception si se produce un error durante la consulta.
     */
    public boolean existeVinculo(Connection con, int equipoId, int patrullaId) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement(
                "SELECT 1 FROM Equipo_Patrulla WHERE equipo_id=? AND patrulla_id=?");
            stmt.setInt(1, equipoId);
            stmt.setInt(2, patrullaId);
            rs = stmt.executeQuery();
            resultado = rs.next();
        } catch (SQLException ex) {
            throw new Exception("Error al verificar vinculo: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return resultado;
    }
}
