package DAO;

import Entidad.Tripulante;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos para la entidad {@link Tripulante}.
 * Proporciona operaciones CRUD sobre la tabla {@code Tripulante} y metodos
 * auxiliares para asignar o liberar tripulantes de una patrulla.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class TripulanteDAO {

    /** Mapea una fila del ResultSet a un objeto Tripulante. */
    private Tripulante mapearFila(ResultSet rs) throws SQLException {
        Tripulante t = new Tripulante();
        t.setId(rs.getInt("id"));
        t.setNif(rs.getString("nif"));
        t.setNombre(rs.getString("nombre"));
        t.setApellido(rs.getString("apellido"));
        t.setTelefonoContacto(rs.getString("telefonoContacto"));
        t.setRol(Tripulante.Rol.fromString(rs.getString("rol")));
        t.setEstadoOperativo(Tripulante.EstadoOperativo.fromString(rs.getString("estadoOperativo")));
        t.setPatrullaId(rs.getInt("patrulla_id"));
        return t;
    }

    /**
     * Inserta un nuevo tripulante en la base de datos y actualiza su ID generado.
     *
     * @param con conexion activa a la base de datos.
     * @param t   objeto {@code Tripulante} con los datos a insertar.
     * @return el mismo tripulante con el ID asignado por la base de datos.
     * @throws Exception si se produce un error durante la insercion.
     */
    public Tripulante insertar(Connection con, Tripulante t) throws Exception {
        PreparedStatement stmt = null;
        ResultSet keys = null;
        try {
            stmt = con.prepareStatement(
                "INSERT INTO Tripulante (nif, nombre, apellido, telefonoContacto, rol, estadoOperativo, patrulla_id) VALUES (?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, t.getNif());
            stmt.setString(2, t.getNombre());
            stmt.setString(3, t.getApellido());
            stmt.setString(4, t.getTelefonoContacto());
            stmt.setString(5, t.getRol().getValor());
            stmt.setString(6, t.getEstadoOperativo().getValor());
            if (t.getPatrullaId() > 0) {
                stmt.setInt(7, t.getPatrullaId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.executeUpdate();
            keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                t.setId(keys.getInt(1));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al insertar tripulante: " + ex.getMessage());
        } finally {
            if (keys != null) keys.close();
            if (stmt != null) stmt.close();
        }
        return t;
    }

    /**
     * Busca un tripulante por su identificador unico.
     *
     * @param con conexion activa a la base de datos.
     * @param id  identificador del tripulante a buscar.
     * @return el tripulante encontrado o {@code null} si no existe.
     * @throws Exception si se produce un error durante la consulta.
     */
    public Tripulante findById(Connection con, int id) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Tripulante tripulante = null;
        try {
            stmt = con.prepareStatement("SELECT * FROM Tripulante WHERE id=?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                tripulante = mapearFila(rs);
            }
        } catch (SQLException ex) {
            throw new Exception("Error al buscar tripulante: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return tripulante;
    }

    /**
     * Recupera todos los tripulantes registrados en la base de datos.
     *
     * @param con conexion activa a la base de datos.
     * @return lista con todos los tripulantes; vacia si no hay ninguno.
     * @throws Exception si se produce un error durante la consulta.
     */
    public List<Tripulante> findAll(Connection con) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Tripulante> lista = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM Tripulante");
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al listar tripulantes: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return lista;
    }

    /**
     * Recupera todos los tripulantes asignados a una patrulla concreta.
     *
     * @param con        conexion activa a la base de datos.
     * @param patrullaId identificador de la patrulla cuyos tripulantes se quieren obtener.
     * @return lista de tripulantes pertenecientes a la patrulla; vacia si no hay ninguno.
     * @throws Exception si se produce un error durante la consulta.
     */
    public List<Tripulante> findByPatrullaId(Connection con, int patrullaId) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Tripulante> lista = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM Tripulante WHERE patrulla_id=?");
            stmt.setInt(1, patrullaId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al listar tripulantes de patrulla: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return lista;
    }

    /**
     * Actualiza todos los campos de un tripulante existente en la base de datos.
     *
     * @param con conexion activa a la base de datos.
     * @param t   objeto {@code Tripulante} con los nuevos datos (debe tener ID valido).
     * @return {@code true} si al menos una fila fue modificada; {@code false} en caso contrario.
     * @throws Exception si se produce un error durante la actualizacion.
     */
    public boolean actualizar(Connection con, Tripulante t) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement(
                "UPDATE Tripulante SET nif=?, nombre=?, apellido=?, telefonoContacto=?, rol=?, estadoOperativo=?, patrulla_id=? WHERE id=?");
            stmt.setString(1, t.getNif());
            stmt.setString(2, t.getNombre());
            stmt.setString(3, t.getApellido());
            stmt.setString(4, t.getTelefonoContacto());
            stmt.setString(5, t.getRol().getValor());
            stmt.setString(6, t.getEstadoOperativo().getValor());
            if (t.getPatrullaId() > 0) {
                stmt.setInt(7, t.getPatrullaId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.setInt(8, t.getId());
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al actualizar tripulante: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Elimina un tripulante de la base de datos por su identificador.
     *
     * @param con conexion activa a la base de datos.
     * @param id  identificador del tripulante a eliminar.
     * @return {@code true} si el tripulante fue eliminado; {@code false} si no existia.
     * @throws Exception si se produce un error durante la eliminacion.
     */
    public boolean eliminar(Connection con, int id) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("DELETE FROM Tripulante WHERE id=?");
            stmt.setInt(1, id);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al eliminar tripulante: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Asigna un tripulante a una patrulla y actualiza su estado operativo a {@code ASIGNADO}.
     *
     * @param con          conexion activa a la base de datos.
     * @param tripulanteId identificador del tripulante a asignar.
     * @param patrullaId   identificador de la patrulla destino.
     * @return {@code true} si la asignacion fue realizada con exito.
     * @throws Exception si se produce un error durante la operacion.
     */
    public boolean asignarAPatrulla(Connection con, int tripulanteId, int patrullaId) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement(
                "UPDATE Tripulante SET patrulla_id=?, estadoOperativo=? WHERE id=?");
            stmt.setInt(1, patrullaId);
            stmt.setString(2, Tripulante.EstadoOperativo.ASIGNADO.getValor());
            stmt.setInt(3, tripulanteId);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al asignar tripulante a patrulla: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Libera a un tripulante de su patrulla actual, poniendo su estado a {@code DISPONIBLE}.
     *
     * @param con          conexion activa a la base de datos.
     * @param tripulanteId identificador del tripulante a liberar.
     * @return {@code true} si la operacion se realizo con exito.
     * @throws Exception si se produce un error durante la operacion.
     */
    public boolean liberar(Connection con, int tripulanteId) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement(
                "UPDATE Tripulante SET patrulla_id=NULL, estadoOperativo=? WHERE id=?");
            stmt.setString(1, Tripulante.EstadoOperativo.DISPONIBLE.getValor());
            stmt.setInt(2, tripulanteId);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al liberar tripulante: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }
}
