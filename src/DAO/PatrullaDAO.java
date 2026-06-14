package DAO;

import Entidad.Patrulla;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos para la entidad {@link Patrulla}.
 * Proporciona operaciones CRUD sobre la tabla {@code Patrulla} y metodos
 * auxiliares para asignar recursos, vehiculos, rutas y equipos de comunicacion.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class PatrullaDAO {

    /** Mapea una fila del ResultSet a un objeto Patrulla. */
    private Patrulla mapearFila(ResultSet rs) throws SQLException {
        Patrulla p = new Patrulla();
        p.setId(rs.getInt("id"));
        p.setCodigo(rs.getString("codigo"));
        p.setEstado(Patrulla.Estado.fromString(rs.getString("estado")));
        p.setVehiculoId(rs.getInt("vehiculo_id"));
        p.setRutaId(rs.getInt("ruta_id"));
        p.setEquipoComunicacionId(rs.getInt("equipoComunicacion_id"));
        return p;
    }

    /**
     * Inserta una nueva patrulla en la base de datos y actualiza su ID generado.
     *
     * @param con conexion activa a la base de datos.
     * @param p   objeto {@code Patrulla} con los datos a insertar.
     * @return la misma patrulla con el ID asignado por la base de datos.
     * @throws Exception si se produce un error durante la insercion.
     */
    public Patrulla insertar(Connection con, Patrulla p) throws Exception {
        PreparedStatement stmt = null;
        ResultSet keys = null;
        try {
            stmt = con.prepareStatement(
                "INSERT INTO Patrulla (codigo, estado, vehiculo_id, ruta_id, equipoComunicacion_id) VALUES (?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, p.getCodigo());
            stmt.setString(2, p.getEstado().getValor());
            if (p.getVehiculoId() > 0) { stmt.setInt(3, p.getVehiculoId()); } else { stmt.setNull(3, Types.INTEGER); }
            if (p.getRutaId() > 0) { stmt.setInt(4, p.getRutaId()); } else { stmt.setNull(4, Types.INTEGER); }
            if (p.getEquipoComunicacionId() > 0) { stmt.setInt(5, p.getEquipoComunicacionId()); } else { stmt.setNull(5, Types.INTEGER); }
            stmt.executeUpdate();
            keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                p.setId(keys.getInt(1));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al insertar patrulla: " + ex.getMessage());
        } finally {
            if (keys != null) keys.close();
            if (stmt != null) stmt.close();
        }
        return p;
    }

    /**
     * Busca una patrulla por su identificador unico.
     *
     * @param con conexion activa a la base de datos.
     * @param id  identificador de la patrulla a buscar.
     * @return la patrulla encontrada o {@code null} si no existe.
     * @throws Exception si se produce un error durante la consulta.
     */
    public Patrulla findById(Connection con, int id) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Patrulla patrulla = null;
        try {
            stmt = con.prepareStatement("SELECT * FROM Patrulla WHERE id=?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                patrulla = mapearFila(rs);
            }
        } catch (SQLException ex) {
            throw new Exception("Error al buscar patrulla: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return patrulla;
    }

    /**
     * Busca una patrulla por su codigo alfanumerico.
     *
     * @param con    conexion activa a la base de datos.
     * @param codigo codigo de la patrulla a buscar.
     * @return la patrulla encontrada o {@code null} si no existe.
     * @throws Exception si se produce un error durante la consulta.
     */
    public Patrulla findByCodigo(Connection con, String codigo) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Patrulla patrulla = null;
        try {
            stmt = con.prepareStatement("SELECT * FROM Patrulla WHERE codigo=?");
            stmt.setString(1, codigo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                patrulla = mapearFila(rs);
            }
        } catch (SQLException ex) {
            throw new Exception("Error al buscar patrulla por codigo: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return patrulla;
    }

    /**
     * Recupera todas las patrullas registradas en la base de datos.
     *
     * @param con conexion activa a la base de datos.
     * @return lista con todas las patrullas; vacia si no hay ninguna.
     * @throws Exception si se produce un error durante la consulta.
     */
    public List<Patrulla> findAll(Connection con) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Patrulla> lista = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM Patrulla");
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al listar patrullas: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return lista;
    }

    /**
     * Actualiza todos los campos de una patrulla existente en la base de datos.
     *
     * @param con conexion activa a la base de datos.
     * @param p   objeto {@code Patrulla} con los nuevos datos (debe tener ID valido).
     * @return {@code true} si al menos una fila fue modificada; {@code false} en caso contrario.
     * @throws Exception si se produce un error durante la actualizacion.
     */
    public boolean actualizar(Connection con, Patrulla p) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement(
                "UPDATE Patrulla SET codigo=?, estado=?, vehiculo_id=?, ruta_id=?, equipoComunicacion_id=? WHERE id=?");
            stmt.setString(1, p.getCodigo());
            stmt.setString(2, p.getEstado().getValor());
            if (p.getVehiculoId() > 0) { stmt.setInt(3, p.getVehiculoId()); } else { stmt.setNull(3, Types.INTEGER); }
            if (p.getRutaId() > 0) { stmt.setInt(4, p.getRutaId()); } else { stmt.setNull(4, Types.INTEGER); }
            if (p.getEquipoComunicacionId() > 0) { stmt.setInt(5, p.getEquipoComunicacionId()); } else { stmt.setNull(5, Types.INTEGER); }
            stmt.setInt(6, p.getId());
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al actualizar patrulla: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Elimina una patrulla de la base de datos por su identificador.
     *
     * @param con conexion activa a la base de datos.
     * @param id  identificador de la patrulla a eliminar.
     * @return {@code true} si la patrulla fue eliminada; {@code false} si no existia.
     * @throws Exception si se produce un error durante la eliminacion.
     */
    public boolean eliminar(Connection con, int id) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("DELETE FROM Patrulla WHERE id=?");
            stmt.setInt(1, id);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al eliminar patrulla: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Asigna un vehiculo a una patrulla actualizando la columna {@code vehiculo_id}.
     *
     * @param con        conexion activa a la base de datos.
     * @param patrullaId identificador de la patrulla destino.
     * @param vehiculoId identificador del vehiculo a asignar.
     * @return {@code true} si la asignacion fue realizada con exito.
     * @throws Exception si se produce un error durante la operacion.
     */
    public boolean asignarVehiculo(Connection con, int patrullaId, int vehiculoId) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("UPDATE Patrulla SET vehiculo_id=? WHERE id=?");
            stmt.setInt(1, vehiculoId);
            stmt.setInt(2, patrullaId);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al asignar vehiculo a patrulla: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Asigna una ruta a una patrulla actualizando la columna {@code ruta_id}.
     *
     * @param con        conexion activa a la base de datos.
     * @param patrullaId identificador de la patrulla destino.
     * @param rutaId     identificador de la ruta a asignar.
     * @return {@code true} si la asignacion fue realizada con exito.
     * @throws Exception si se produce un error durante la operacion.
     */
    public boolean asignarRuta(Connection con, int patrullaId, int rutaId) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("UPDATE Patrulla SET ruta_id=? WHERE id=?");
            stmt.setInt(1, rutaId);
            stmt.setInt(2, patrullaId);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al asignar ruta a patrulla: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Asigna un equipo de comunicacion a una patrulla actualizando la columna {@code equipoComunicacion_id}.
     *
     * @param con        conexion activa a la base de datos.
     * @param patrullaId identificador de la patrulla destino.
     * @param equipoId   identificador del equipo de comunicacion a asignar.
     * @return {@code true} si la asignacion fue realizada con exito.
     * @throws Exception si se produce un error durante la operacion.
     */
    public boolean asignarEquipoComunicacion(Connection con, int patrullaId, int equipoId) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("UPDATE Patrulla SET equipoComunicacion_id=? WHERE id=?");
            stmt.setInt(1, equipoId);
            stmt.setInt(2, patrullaId);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al asignar equipo de comunicacion a patrulla: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Anade un recurso a la tabla auxiliar {@code PatrullaRecurso}.
     *
     * @param con        conexion activa a la base de datos.
     * @param patrullaId identificador de la patrulla a la que se anade el recurso.
     * @param recurso    descripcion del recurso a anadir.
     * @return {@code true} si el recurso fue insertado con exito.
     * @throws Exception si se produce un error durante la insercion.
     */
    public boolean addRecurso(Connection con, int patrullaId, String recurso) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("INSERT INTO PatrullaRecurso (patrulla_id, recurso) VALUES (?,?)");
            stmt.setInt(1, patrullaId);
            stmt.setString(2, recurso);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al añadir recurso a patrulla: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Obtiene la lista de recursos asignados a una patrulla.
     *
     * @param con        conexion activa a la base de datos.
     * @param patrullaId identificador de la patrulla cuyos recursos se quieren obtener.
     * @return lista de cadenas con los recursos; vacia si no hay ninguno.
     * @throws Exception si se produce un error durante la consulta.
     */
    public List<String> getRecursos(Connection con, int patrullaId) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String> lista = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT recurso FROM PatrullaRecurso WHERE patrulla_id=?");
            stmt.setInt(1, patrullaId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("recurso"));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al obtener recursos de patrulla: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return lista;
    }

    /**
     * Elimina un recurso concreto de la tabla auxiliar {@code PatrullaRecurso}.
     *
     * @param con        conexion activa a la base de datos.
     * @param patrullaId identificador de la patrulla a la que pertenece el recurso.
     * @param recurso    descripcion exacta del recurso a eliminar.
     * @return {@code true} si el recurso fue eliminado; {@code false} si no existia.
     * @throws Exception si se produce un error durante la eliminacion.
     */
    public boolean eliminarRecurso(Connection con, int patrullaId, String recurso) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("DELETE FROM PatrullaRecurso WHERE patrulla_id=? AND recurso=?");
            stmt.setInt(1, patrullaId);
            stmt.setString(2, recurso);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al eliminar recurso de patrulla: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }
}
