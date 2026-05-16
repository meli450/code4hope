/*Subsistema Talleres de formación
 *Realizado por: Melisa
 * MonitorDAO */
package DAO;

import Entidad.Monitor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para las operaciones CRUD de la entidad Monitor sobre la bd.
 */
public class MonitorDAO {

    /**
     * Inserta un nuevo monitor en la bd.
     *
     * @param con     conexion a la bd
     * @param monitor objeto Monitor con los datos a insertar
     * @return true si la insercion fue exitosa
     */
    public boolean insertarMonitor(Connection con, Monitor monitor) {
        PreparedStatement ps = null;
        boolean exitoso = false;

        try {
            ps = con.prepareStatement(
                    "INSERT INTO monitor_a (nif, nombre, apellido, telefono, direccion, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, monitor.getNif());
            ps.setString(2, monitor.getNombre());
            ps.setString(3, monitor.getApellido());
            ps.setString(4, monitor.getTelefono());
            ps.setString(5, monitor.getDireccion());
            ps.setBoolean(6, monitor.isActivo());
            ps.executeUpdate();
            exitoso = true;
            System.out.println("  Monitor/a insertado con NIF: " + monitor.getNif());

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Obtiene un/a monitor a partir de su NIF.
     *
     * @param con conexion a la bd
     * @param nif NIF del o la monitor/a a buscar
     * @return objeto Monitor o null si no existe
     */
    public Monitor obtenerMonitor(Connection con, String nif) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Monitor monitor = null;

        try {
            ps = con.prepareStatement(
                    "SELECT nif, nombre, apellido, telefono, direccion, activo " +
                    "FROM monitor_a WHERE nif = ?");
            ps.setString(1, nif);
            rs = ps.executeQuery();

            if (rs.next()) {
                monitor = new Monitor(
                        rs.getString("nif"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("direccion"));
                monitor.setActivo(rs.getBoolean("activo"));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return monitor;
    }

    /**
     * Obtiene la lista de todos/as los o las monitores/as activos ordenados por apellido y nombre
     *
     * @param con conexion a la bd
     * @return lista de monitores activos/as
     */
    public List<Monitor> obtenerTodosMonitores(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Monitor> monitores = new ArrayList<>();

        try {
            ps = con.prepareStatement(
                    "SELECT nif, nombre, apellido, telefono, direccion, activo " +
                    "FROM monitor_a WHERE activo = true ORDER BY apellido, nombre");
            rs = ps.executeQuery();

            while (rs.next()) {
                Monitor m = new Monitor(
                        rs.getString("nif"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("direccion"));
                m.setActivo(rs.getBoolean("activo"));
                monitores.add(m);
            }
            System.out.println("  Monitores recuperados: " + monitores.size());

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return monitores;
    }

    /**
     * Actualiza los datos de un/a monitor/a existente en la bd
     *
     * @param con     conexion a la bd
     * @param monitor objeto Monitor con los datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizarMonitor(Connection con, Monitor monitor) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasActualizadas;

        try {
            ps = con.prepareStatement(
                    "UPDATE monitor_a SET nombre = ?, apellido = ?, telefono = ?, direccion = ? " +
                    "WHERE nif = ?");
            ps.setString(1, monitor.getNombre());
            ps.setString(2, monitor.getApellido());
            ps.setString(3, monitor.getTelefono());
            ps.setString(4, monitor.getDireccion());
            ps.setString(5, monitor.getNif());
            filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                exitoso = true;
                System.out.println("  Monitor/a actualizado con NIF: " + monitor.getNif());
            } else {
                System.out.println("  Monitor/a con NIF " + monitor.getNif() + " no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Realiza la baja logica de un/a monitor/a estableciendo activo a false
     *
     * @param con conexion a la bd
     * @param nif NIF del o la monitor/a a dar de baja
     * @return true si la operacion fue exitosa
     */
    public boolean darDeBaja(Connection con, String nif) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasActualizadas;

        try {
            ps = con.prepareStatement(
                    "UPDATE monitor_a SET activo = false WHERE nif = ?");
            ps.setString(1, nif);
            filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                exitoso = true;
                System.out.println("  Monitor/a dado de baja con NIF: " + nif);
            } else {
                System.out.println("  Monitor/a con NIF " + nif + " no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }
}
