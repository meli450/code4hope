package DAO;

import Entidad.Vehiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos para la entidad {@link Vehiculo}.
 * Proporciona operaciones CRUD sobre la tabla {@code Vehiculo} y metodos
 * auxiliares para filtrar por disponibilidad y actualizar el estado del vehiculo.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class VehiculoDAO {

    /** Mapea una fila del ResultSet a un objeto Vehiculo. */
    private Vehiculo mapearFila(ResultSet rs) throws SQLException {
        Vehiculo v = new Vehiculo();
        v.setId(rs.getInt("id"));
        v.setCodigo(rs.getString("codigo"));
        v.setTipo(Vehiculo.TipoVehiculo.fromString(rs.getString("tipo")));
        v.setRefrigerado(rs.getBoolean("refrigerado"));
        v.setMatricula(rs.getString("matricula"));
        v.setDisponible(rs.getBoolean("disponible"));
        return v;
    }

    /**
     * Inserta un nuevo vehiculo en la base de datos y actualiza su ID generado.
     *
     * @param con conexion activa a la base de datos.
     * @param v   objeto {@code Vehiculo} con los datos a insertar.
     * @return el mismo vehiculo con el ID asignado por la base de datos.
     * @throws Exception si se produce un error durante la insercion.
     */
    public Vehiculo insertar(Connection con, Vehiculo v) throws Exception {
        PreparedStatement stmt = null;
        ResultSet keys = null;
        try {
            stmt = con.prepareStatement(
                "INSERT INTO Vehiculo (codigo, tipo, refrigerado, matricula, disponible) VALUES (?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, v.getCodigo());
            stmt.setString(2, v.getTipo().getValor());
            stmt.setBoolean(3, v.isRefrigerado());
            stmt.setString(4, v.getMatricula());
            stmt.setBoolean(5, v.isDisponible());
            stmt.executeUpdate();
            keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                v.setId(keys.getInt(1));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al insertar vehiculo: " + ex.getMessage());
        } finally {
            if (keys != null) keys.close();
            if (stmt != null) stmt.close();
        }
        return v;
    }

    /**
     * Busca un vehiculo por su identificador unico.
     *
     * @param con conexion activa a la base de datos.
     * @param id  identificador del vehiculo a buscar.
     * @return el vehiculo encontrado o {@code null} si no existe.
     * @throws Exception si se produce un error durante la consulta.
     */
    public Vehiculo findById(Connection con, int id) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vehiculo vehiculo = null;
        try {
            stmt = con.prepareStatement("SELECT * FROM Vehiculo WHERE id=?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                vehiculo = mapearFila(rs);
            }
        } catch (SQLException ex) {
            throw new Exception("Error al buscar vehiculo: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return vehiculo;
    }

    /**
     * Recupera todos los vehiculos registrados en la base de datos.
     *
     * @param con conexion activa a la base de datos.
     * @return lista con todos los vehiculos; vacia si no hay ninguno.
     * @throws Exception si se produce un error durante la consulta.
     */
    public List<Vehiculo> findAll(Connection con) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Vehiculo> lista = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM Vehiculo");
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al listar vehiculos: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return lista;
    }

    /**
     * Actualiza todos los campos de un vehiculo existente en la base de datos.
     *
     * @param con conexion activa a la base de datos.
     * @param v   objeto {@code Vehiculo} con los nuevos datos (debe tener ID valido).
     * @return {@code true} si al menos una fila fue modificada; {@code false} en caso contrario.
     * @throws Exception si se produce un error durante la actualizacion.
     */
    public boolean actualizar(Connection con, Vehiculo v) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement(
                "UPDATE Vehiculo SET codigo=?, tipo=?, refrigerado=?, matricula=?, disponible=? WHERE id=?");
            stmt.setString(1, v.getCodigo());
            stmt.setString(2, v.getTipo().getValor());
            stmt.setBoolean(3, v.isRefrigerado());
            stmt.setString(4, v.getMatricula());
            stmt.setBoolean(5, v.isDisponible());
            stmt.setInt(6, v.getId());
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al actualizar vehiculo: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Elimina un vehiculo de la base de datos por su identificador.
     *
     * @param con conexion activa a la base de datos.
     * @param id  identificador del vehiculo a eliminar.
     * @return {@code true} si el vehiculo fue eliminado; {@code false} si no existia.
     * @throws Exception si se produce un error durante la eliminacion.
     */
    public boolean eliminar(Connection con, int id) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("DELETE FROM Vehiculo WHERE id=?");
            stmt.setInt(1, id);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al eliminar vehiculo: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }

    /**
     * Recupera todos los vehiculos que se encuentran disponibles para ser asignados.
     *
     * @param con conexion activa a la base de datos.
     * @return lista de vehiculos disponibles; vacia si no hay ninguno.
     * @throws Exception si se produce un error durante la consulta.
     */
    public List<Vehiculo> findDisponibles(Connection con) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Vehiculo> lista = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM Vehiculo WHERE disponible=1");
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        } catch (SQLException ex) {
            throw new Exception("Error al buscar vehiculos disponibles: " + ex.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return lista;
    }

    /**
     * Actualiza unicamente la disponibilidad de un vehiculo en la base de datos.
     *
     * @param con        conexion activa a la base de datos.
     * @param id         identificador del vehiculo a modificar.
     * @param disponible nuevo valor de disponibilidad ({@code true} para disponible, {@code false} para en uso).
     * @return {@code true} si la actualizacion se realizo con exito.
     * @throws Exception si se produce un error durante la operacion.
     */
    public boolean setDisponible(Connection con, int id, boolean disponible) throws Exception {
        PreparedStatement stmt = null;
        boolean resultado = false;
        try {
            stmt = con.prepareStatement("UPDATE Vehiculo SET disponible=? WHERE id=?");
            stmt.setBoolean(1, disponible);
            stmt.setInt(2, id);
            resultado = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new Exception("Error al actualizar disponibilidad de vehiculo: " + ex.getMessage());
        } finally {
            if (stmt != null) stmt.close();
        }
        return resultado;
    }
}
