package DAO;

import Entidad.EquipoComunicacion;
import Entidad.EquipoLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos para la entidad {@link EquipoComunicacion}.
 * Proporciona operaciones CRUD sobre la tabla {@code EquipoComunicacion} y metodos
 * auxiliares para gestionar el registro de actividad ({@code EquipoLog}) del equipo.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class EquipoComunicacionDAO {

    /** Mapea una fila del ResultSet a un objeto EquipoComunicacion. */
    private EquipoComunicacion mapearFila(ResultSet rs) throws SQLException {
        EquipoComunicacion e = new EquipoComunicacion();
        e.setId(rs.getInt("id"));
        e.setNombre(rs.getString("nombre"));
        e.setEstadoEquipo(EquipoComunicacion.EstadoEquipo.fromString(rs.getString("estadoEquipo")));
        return e;
    }

    /**
     * Inserta un nuevo equipo de comunicacion en la base de datos y actualiza su ID generado.
     *
     * @param con conexion activa a la base de datos.
     * @param e   objeto {@code EquipoComunicacion} con los datos a insertar.
     * @return el mismo equipo con el ID asignado por la base de datos.
     * @throws Exception si se produce un error durante la insercion.
     */
    public EquipoComunicacion insertar(Connection con, EquipoComunicacion e) throws Exception {
        PreparedStatement stmt = null;
        ResultSet keys = null;
        try {
            stmt = con.prepareStatement(
                "INSERT INTO EquipoComunicacion (nombre, estadoEquipo) VALUES (?,?)",
                Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, e.getNombre());
            stmt.setString(2, e.getEstadoEquipo().getValor());
            stmt.executeUpdate();
            keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                e.setId(keys.getInt(1));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al insertar equipo de comunicacion: " + ex.getMessage());
        } finally {
            if (keys != null) keys.close();
            if (stmt != null) stmt.close();
        }
        return e;
    }

    /**
     * Busca un equipo de comunicacion por su identificador unico.
     *
     * @param con conexion activa a la base de datos.
     * @param id  identificador del equipo a buscar.
     * @return el equipo encontrado o {@code null} si no existe.
     * @throws Exception si se produce un error durante la consulta.
     */
    public EquipoComunicacion findById(Connection con, int id) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        EquipoComunicacion equipo = null;
        try {
            stmt = con.prepareStatement("SELECT * FROM EquipoComunicacion WHERE id=?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                equipo = mapearFila(rs);
            }
        } catch (SQLException ex) {
            throw new Exception("Error al buscar equipo de comunicacion: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return equipo;
    }

    /**
     * Recupera todos los equipos de comunicacion registrados en la base de datos.
     *
     * @param con conexion activa a la base de datos.
     * @return lista con todos los equipos; vacia si no hay ninguno.
     * @throws Exception si se produce un error durante la consulta.
     */
    public List<EquipoComunicacion> findAll(Connection con) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EquipoComunicacion> lista = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM EquipoComunicacion");
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al listar equipos de comunicacion: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return lista;
    }

    /**
     * Actualiza el nombre y el estado de un equipo de comunicacion existente.
     *
     * @param con conexion activa a la base de datos.
     * @param e   objeto {@code EquipoComunicacion} con los nuevos datos (debe tener ID valido).
     * @return {@code true} si al menos una fila fue modificada; {@code false} en caso contrario.
     * @throws Exception si se produce un error durante la actualizacion.
     */
    public boolean actualizar(Connection con, EquipoComunicacion e) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("UPDATE EquipoComunicacion SET nombre=?, estadoEquipo=? WHERE id=?");
            stmt.setString(1, e.getNombre());
            stmt.setString(2, e.getEstadoEquipo().getValor());
            stmt.setInt(3, e.getId());
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al actualizar equipo de comunicacion: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Elimina un equipo de comunicacion de la base de datos por su identificador.
     *
     * @param con conexion activa a la base de datos.
     * @param id  identificador del equipo a eliminar.
     * @return {@code true} si el equipo fue eliminado; {@code false} si no existia.
     * @throws Exception si se produce un error durante la eliminacion.
     */
    public boolean eliminar(Connection con, int id) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("DELETE FROM EquipoComunicacion WHERE id=?");
            stmt.setInt(1, id);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al eliminar equipo de comunicacion: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Inserta una nueva entrada en el log de actividad de un equipo.
     *
     * @param con conexion activa a la base de datos.
     * @param log objeto {@code EquipoLog} con la entrada a registrar.
     * @return {@code true} si la entrada fue insertada con exito.
     * @throws Exception si se produce un error durante la insercion.
     */
    public boolean insertarLog(Connection con, EquipoLog log) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("INSERT INTO EquipoLog (equipo_id, entrada) VALUES (?,?)");
            stmt.setInt(1, log.getEquipoId());
            stmt.setString(2, log.getEntrada());
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al insertar entrada de log: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Recupera el historial de actividad de un equipo ordenado cronologicamente.
     *
     * @param con      conexion activa a la base de datos.
     * @param equipoId identificador del equipo cuyo log se quiere obtener.
     * @return lista de entradas de log ordenadas por fecha y hora; vacia si no hay ninguna.
     * @throws Exception si se produce un error durante la consulta.
     */
    public List<EquipoLog> getLog(Connection con, int equipoId) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EquipoLog> lista = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM EquipoLog WHERE equipo_id=? ORDER BY fechaHora");
            stmt.setInt(1, equipoId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                EquipoLog log = new EquipoLog();
                log.setId(rs.getInt("id"));
                log.setEquipoId(rs.getInt("equipo_id"));
                log.setEntrada(rs.getString("entrada"));
                log.setFechaHora(rs.getString("fechaHora"));
                lista.add(log);
            }
        } catch (SQLException ex) {
            throw new Exception("Error al obtener log de equipo: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return lista;
    }
}
