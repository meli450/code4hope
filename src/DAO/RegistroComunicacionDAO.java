package DAO;

import Entidad.RegistroComunicacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos para la entidad {@link RegistroComunicacion}.
 * Proporciona operaciones de insercion y consulta sobre la tabla
 * {@code RegistroComunicacion}, permitiendo gestionar el historial de mensajes
 * de los equipos de comunicacion.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class RegistroComunicacionDAO {

    /** Mapea una fila del ResultSet a un objeto RegistroComunicacion. */
    private RegistroComunicacion mapearFila(ResultSet rs) throws SQLException {
        RegistroComunicacion r = new RegistroComunicacion();
        r.setId(rs.getInt("id"));
        r.setEquipoId(rs.getInt("equipo_id"));
        r.setHora(rs.getString("hora"));
        r.setTipo(RegistroComunicacion.TipoMensaje.fromString(rs.getString("tipo")));
        r.setMensaje(rs.getString("mensaje"));
        r.setEmisor(rs.getString("emisor"));
        return r;
    }

    /**
     * Inserta un nuevo registro de comunicacion en la base de datos y actualiza su ID generado.
     *
     * @param con conexion activa a la base de datos.
     * @param r   objeto {@code RegistroComunicacion} con los datos del mensaje a insertar.
     * @return el mismo registro con el ID asignado por la base de datos.
     * @throws Exception si se produce un error durante la insercion.
     */
    public RegistroComunicacion insertar(Connection con, RegistroComunicacion r) throws Exception {
        PreparedStatement stmt = null;
        ResultSet keys = null;
        try {
            stmt = con.prepareStatement(
                "INSERT INTO RegistroComunicacion (equipo_id, hora, tipo, mensaje, emisor) VALUES (?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, r.getEquipoId());
            stmt.setString(2, r.getHora());
            stmt.setString(3, r.getTipo().getValor());
            stmt.setString(4, r.getMensaje());
            stmt.setString(5, r.getEmisor());
            stmt.executeUpdate();
            keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                r.setId(keys.getInt(1));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al insertar registro de comunicacion: " + ex.getMessage());
        } finally {
            if (keys != null) keys.close();
            if (stmt != null) stmt.close();
        }
        return r;
    }

    /**
     * Recupera todos los registros de comunicacion de un equipo, ordenados cronologicamente.
     *
     * @param con      conexion activa a la base de datos.
     * @param equipoId identificador del equipo cuyos registros se quieren obtener.
     * @return lista de registros ordenada por hora; vacia si no hay ninguno.
     * @throws Exception si se produce un error durante la consulta.
     */
    public List<RegistroComunicacion> findByEquipoId(Connection con, int equipoId) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RegistroComunicacion> lista = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM RegistroComunicacion WHERE equipo_id=? ORDER BY hora");
            stmt.setInt(1, equipoId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al listar registros de comunicacion: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return lista;
    }

    /**
     * Obtiene el registro de comunicacion mas reciente de un equipo.
     *
     * @param con      conexion activa a la base de datos.
     * @param equipoId identificador del equipo del que se quiere la ultima comunicacion.
     * @return el registro mas reciente o {@code null} si el equipo no tiene registros.
     * @throws Exception si se produce un error durante la consulta.
     */
    public RegistroComunicacion getUltimaComunicacion(Connection con, int equipoId) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        RegistroComunicacion ultima = null;
        try {
            stmt = con.prepareStatement(
                "SELECT * FROM RegistroComunicacion WHERE equipo_id=? ORDER BY hora DESC LIMIT 1");
            stmt.setInt(1, equipoId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ultima = mapearFila(rs);
            }
        } catch (SQLException ex) {
            throw new Exception("Error al obtener ultima comunicacion: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return ultima;
    }
}
