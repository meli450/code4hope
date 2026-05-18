package DAO;

import Entidad.Ruta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos para la entidad {@link Ruta}.
 * Proporciona operaciones CRUD sobre la tabla {@code Ruta}.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class RutaDAO {

    /** Mapea una fila del ResultSet a un objeto Ruta. */
    private Ruta mapearFila(ResultSet rs) throws SQLException {
        Ruta r = new Ruta();
        r.setId(rs.getInt("id"));
        r.setNombre(rs.getString("nombre"));
        r.setEstado(Ruta.EstadoRuta.fromString(rs.getString("estado")));
        r.setFechaMision(rs.getString("fechaMision"));
        r.setHoraInicio(rs.getString("horaInicio"));
        r.setHoraFin(rs.getString("horaFin"));
        r.setIndicePuntoActual(rs.getInt("indicePuntoActual"));
        r.setGradoPeligrosidad(rs.getString("gradoPeligrosidad"));
        r.setNumKm(rs.getFloat("numKm"));
        return r;
    }

    /**
     * Inserta una nueva ruta en la base de datos y actualiza su ID generado.
     *
     * @param con conexion activa a la base de datos.
     * @param r   objeto {@code Ruta} con los datos a insertar.
     * @return la misma ruta con el ID asignado por la base de datos.
     * @throws Exception si se produce un error durante la insercion.
     */
    public Ruta insertar(Connection con, Ruta r) throws Exception {
        PreparedStatement stmt = null;
        ResultSet keys = null;
        try {
            stmt = con.prepareStatement(
                "INSERT INTO Ruta (nombre, estado, fechaMision, horaInicio, horaFin, indicePuntoActual, gradoPeligrosidad, numKm) VALUES (?,?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, r.getNombre());
            stmt.setString(2, r.getEstado().getValor());
            stmt.setString(3, r.getFechaMision());
            stmt.setString(4, r.getHoraInicio());
            stmt.setString(5, r.getHoraFin());
            stmt.setInt(6, r.getIndicePuntoActual());
            stmt.setString(7, r.getGradoPeligrosidad());
            stmt.setFloat(8, r.getNumKm());
            stmt.executeUpdate();
            keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                r.setId(keys.getInt(1));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al insertar ruta: " + ex.getMessage());
        } finally {
            if (keys != null) keys.close();
            if (stmt != null) stmt.close();
        }
        return r;
    }

    /**
     * Busca una ruta por su identificador unico.
     *
     * @param con conexion activa a la base de datos.
     * @param id  identificador de la ruta a buscar.
     * @return la ruta encontrada o {@code null} si no existe.
     * @throws Exception si se produce un error durante la consulta.
     */
    public Ruta findById(Connection con, int id) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Ruta ruta = null;
        try {
            stmt = con.prepareStatement("SELECT * FROM Ruta WHERE id=?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ruta = mapearFila(rs);
            }
        } catch (SQLException ex) {
            throw new Exception("Error al buscar ruta: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return ruta;
    }

    /**
     * Recupera todas las rutas registradas en la base de datos.
     *
     * @param con conexion activa a la base de datos.
     * @return lista con todas las rutas; vacia si no hay ninguna.
     * @throws Exception si se produce un error durante la consulta.
     */
    public List<Ruta> findAll(Connection con) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Ruta> lista = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM Ruta");
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al listar rutas: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return lista;
    }

    /**
     * Actualiza todos los campos de una ruta existente en la base de datos.
     *
     * @param con conexion activa a la base de datos.
     * @param r   objeto {@code Ruta} con los nuevos datos (debe tener ID valido).
     * @return {@code true} si al menos una fila fue modificada; {@code false} en caso contrario.
     * @throws Exception si se produce un error durante la actualizacion.
     */
    public boolean actualizar(Connection con, Ruta r) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement(
                "UPDATE Ruta SET nombre=?, estado=?, fechaMision=?, horaInicio=?, horaFin=?, indicePuntoActual=?, gradoPeligrosidad=?, numKm=? WHERE id=?");
            stmt.setString(1, r.getNombre());
            stmt.setString(2, r.getEstado().getValor());
            stmt.setString(3, r.getFechaMision());
            stmt.setString(4, r.getHoraInicio());
            stmt.setString(5, r.getHoraFin());
            stmt.setInt(6, r.getIndicePuntoActual());
            stmt.setString(7, r.getGradoPeligrosidad());
            stmt.setFloat(8, r.getNumKm());
            stmt.setInt(9, r.getId());
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al actualizar ruta: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Elimina una ruta de la base de datos por su identificador.
     *
     * @param con conexion activa a la base de datos.
     * @param id  identificador de la ruta a eliminar.
     * @return {@code true} si la ruta fue eliminada; {@code false} si no existia.
     * @throws Exception si se produce un error durante la eliminacion.
     */
    public boolean eliminar(Connection con, int id) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("DELETE FROM Ruta WHERE id=?");
            stmt.setInt(1, id);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al eliminar ruta: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }
}
