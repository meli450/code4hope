package DAO;

import Entidad.PuntoRuta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos para la entidad {@link PuntoRuta}.
 * Proporciona operaciones CRUD sobre la tabla {@code PuntoRuta} y un metodo
 * para recuperar los puntos ordenados por posicion dentro de una ruta.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class PuntoRutaDAO {

    /** Mapea una fila del ResultSet a un objeto PuntoRuta. */
    private PuntoRuta mapearFila(ResultSet rs) throws SQLException {
        PuntoRuta p = new PuntoRuta();
        p.setId(rs.getInt("id"));
        p.setRutaId(rs.getInt("ruta_id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setTipo(PuntoRuta.TipoPunto.fromString(rs.getString("tipo")));
        p.setLatitud(rs.getDouble("latitud"));
        p.setLongitud(rs.getDouble("longitud"));
        p.setEstado(PuntoRuta.EstadoPunto.fromString(rs.getString("estado")));
        p.setHoraEstimada(rs.getString("horaEstimada"));
        p.setHoraRealLlegada(rs.getString("horaRealLlegada"));
        p.setNotasIncidencia(rs.getString("notasIncidencia"));
        p.setEsGasolinera(rs.getBoolean("esGasolinera"));
        p.setPosicion(rs.getInt("posicion"));
        return p;
    }

    /**
     * Inserta un nuevo punto de ruta en la base de datos y actualiza su ID generado.
     *
     * @param con conexion activa a la base de datos.
     * @param p   objeto {@code PuntoRuta} con los datos a insertar.
     * @return el mismo punto con el ID asignado por la base de datos.
     * @throws Exception si se produce un error durante la insercion.
     */
    public PuntoRuta insertar(Connection con, PuntoRuta p) throws Exception {
        PreparedStatement stmt = null;
        ResultSet keys = null;
        try {
            stmt = con.prepareStatement(
                "INSERT INTO PuntoRuta (ruta_id, nombre, descripcion, tipo, latitud, longitud, estado, horaEstimada, horaRealLlegada, notasIncidencia, esGasolinera, posicion) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, p.getRutaId());
            stmt.setString(2, p.getNombre());
            stmt.setString(3, p.getDescripcion());
            stmt.setString(4, p.getTipo().getValor());
            stmt.setDouble(5, p.getLatitud());
            stmt.setDouble(6, p.getLongitud());
            stmt.setString(7, p.getEstado().getValor());
            stmt.setString(8, p.getHoraEstimada());
            stmt.setString(9, p.getHoraRealLlegada());
            stmt.setString(10, p.getNotasIncidencia());
            stmt.setBoolean(11, p.isEsGasolinera());
            stmt.setInt(12, p.getPosicion());
            stmt.executeUpdate();
            keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                p.setId(keys.getInt(1));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al insertar punto de ruta: " + ex.getMessage());
        } finally {
            if (keys != null) keys.close();
            if (stmt != null) stmt.close();
        }
        return p;
    }

    /**
     * Busca un punto de ruta por su identificador unico.
     *
     * @param con conexion activa a la base de datos.
     * @param id  identificador del punto a buscar.
     * @return el punto encontrado o {@code null} si no existe.
     * @throws Exception si se produce un error durante la consulta.
     */
    public PuntoRuta findById(Connection con, int id) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PuntoRuta punto = null;
        try {
            stmt = con.prepareStatement("SELECT * FROM PuntoRuta WHERE id=?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                punto = mapearFila(rs);
            }
        } catch (SQLException ex) {
            throw new Exception("Error al buscar punto de ruta: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return punto;
    }

    /**
     * Recupera todos los puntos pertenecientes a una ruta, ordenados por posicion.
     *
     * @param con    conexion activa a la base de datos.
     * @param rutaId identificador de la ruta cuyos puntos se quieren obtener.
     * @return lista de puntos de la ruta ordenada por el campo {@code posicion}; vacia si no hay ninguno.
     * @throws Exception si se produce un error durante la consulta.
     */
    public List<PuntoRuta> findByRutaId(Connection con, int rutaId) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<PuntoRuta> lista = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM PuntoRuta WHERE ruta_id=? ORDER BY posicion");
            stmt.setInt(1, rutaId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al listar puntos de ruta: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return lista;
    }

    /**
     * Actualiza todos los campos de un punto de ruta existente en la base de datos.
     *
     * @param con conexion activa a la base de datos.
     * @param p   objeto {@code PuntoRuta} con los nuevos datos (debe tener ID valido).
     * @return {@code true} si al menos una fila fue modificada; {@code false} en caso contrario.
     * @throws Exception si se produce un error durante la actualizacion.
     */
    public boolean actualizar(Connection con, PuntoRuta p) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement(
                "UPDATE PuntoRuta SET nombre=?, descripcion=?, tipo=?, latitud=?, longitud=?, estado=?, horaEstimada=?, horaRealLlegada=?, notasIncidencia=?, esGasolinera=?, posicion=? WHERE id=?");
            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getDescripcion());
            stmt.setString(3, p.getTipo().getValor());
            stmt.setDouble(4, p.getLatitud());
            stmt.setDouble(5, p.getLongitud());
            stmt.setString(6, p.getEstado().getValor());
            stmt.setString(7, p.getHoraEstimada());
            stmt.setString(8, p.getHoraRealLlegada());
            stmt.setString(9, p.getNotasIncidencia());
            stmt.setBoolean(10, p.isEsGasolinera());
            stmt.setInt(11, p.getPosicion());
            stmt.setInt(12, p.getId());
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al actualizar punto de ruta: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Elimina un punto de ruta de la base de datos por su identificador.
     *
     * @param con conexion activa a la base de datos.
     * @param id  identificador del punto a eliminar.
     * @return {@code true} si el punto fue eliminado; {@code false} si no existia.
     * @throws Exception si se produce un error durante la eliminacion.
     */
    public boolean eliminar(Connection con, int id) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("DELETE FROM PuntoRuta WHERE id=?");
            stmt.setInt(1, id);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al eliminar punto de ruta: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }
}
