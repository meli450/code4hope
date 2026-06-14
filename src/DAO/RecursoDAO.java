/*Subsistema Talleres de formación
 *Realizado por: Melisa
 * RecursoDAO */
package DAO;

import Entidad.Recurso;
import Entidad.EstadoRecursoEnum;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para las operaciones CRUD de la entidad Recurso sobre la bd
 */
public class RecursoDAO {

    /**
     * Inserta un nuevo recurso en la bd
     *
     * @param con     conexion a la bd
     * @param recurso objeto Recurso con los datos a insertar
     * @return identificador generado o -1 si fallo la insercion
     */
    public int insertarRecurso(Connection con, Recurso recurso) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idGenerado = -1;

        try {
            ps = con.prepareStatement(
                    "INSERT INTO recurso (tipo, disponibilidad, cantidad, es_fungible, idp) " +
                    "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, recurso.getTipo());
            ps.setString(2, recurso.getEstado().name());
            ps.setInt(3, recurso.getCantidad());
            ps.setBoolean(4, recurso.isEsFungible());
            if (recurso.getIdPatrulla() > 0) {
                ps.setInt(5, recurso.getIdPatrulla());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                recurso.setId(idGenerado);
            }
            System.out.println("  Recurso insertado con ID: " + idGenerado);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return idGenerado;
    }

    /**
     * Obtiene un recurso a partir de su identificador.
     *
     * @param con conexion a la bd
     * @param id  identificador del recurso
     * @return objeto Recurso o null si no existe
     */
    public Recurso obtenerRecurso(Connection con, int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Recurso recurso = null;

        try {
            ps = con.prepareStatement(
                    "SELECT id, tipo, disponibilidad, cantidad, es_fungible, idp " +
                    "FROM recurso WHERE id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                recurso = new Recurso(
                        rs.getString("tipo"),
                        rs.getInt("cantidad"),
                        rs.getBoolean("es_fungible"));
                recurso.setId(rs.getInt("id"));
                recurso.setEstado(EstadoRecursoEnum.valueOf(rs.getString("disponibilidad")));
                int idp = rs.getInt("idp");
                if (!rs.wasNull()) {
                    recurso.setIdPatrulla(idp);
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return recurso;
    }

    /**
     * Obtiene la lista de todos los recursos ordenados por tipo
     *
     * @param con conexion a la bd
     * @return lista de recursos
     */
    public List<Recurso> obtenerTodosRecursos(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Recurso> recursos = new ArrayList<>();

        try {
            ps = con.prepareStatement(
                    "SELECT id, tipo, disponibilidad, cantidad, es_fungible, idp " +
                    "FROM recurso ORDER BY tipo");
            rs = ps.executeQuery();

            while (rs.next()) {
                Recurso r = new Recurso(
                        rs.getString("tipo"),
                        rs.getInt("cantidad"),
                        rs.getBoolean("es_fungible"));
                r.setId(rs.getInt("id"));
                r.setEstado(EstadoRecursoEnum.valueOf(rs.getString("disponibilidad")));
                int idp = rs.getInt("idp");
                if (!rs.wasNull()) {
                    r.setIdPatrulla(idp);
                }
                recursos.add(r);
            }
            System.out.println("  Recursos recuperados: " + recursos.size());

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return recursos;
    }

    /**
     * Actualiza los datos basicos de un recurso en la bd
     *
     * @param con     conexion a la bd
     * @param recurso objeto Recurso con los datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizarRecurso(Connection con, Recurso recurso) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasActualizadas;

        try {
            ps = con.prepareStatement(
                    "UPDATE recurso SET tipo = ?, cantidad = ?, es_fungible = ? WHERE id = ?");
            ps.setString(1, recurso.getTipo());
            ps.setInt(2, recurso.getCantidad());
            ps.setBoolean(3, recurso.isEsFungible());
            ps.setInt(4, recurso.getId());
            filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                exitoso = true;
                System.out.println("  Recurso actualizado con ID: " + recurso.getId());
            } else {
                System.out.println("  Recurso con ID " + recurso.getId() + " no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Elimina un recurso de la bd
     *
     * @param con conexion a la bd
     * @param id  identificador del recurso a eliminar
     * @return true si la eliminacion fue exitosa
     */
    public boolean eliminarRecurso(Connection con, int id) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasEliminadas;

        try {
            ps = con.prepareStatement("DELETE FROM recurso WHERE id = ?");
            ps.setInt(1, id);
            filasEliminadas = ps.executeUpdate();

            if (filasEliminadas > 0) {
                exitoso = true;
                System.out.println("  Recurso eliminado con ID: " + id);
            } else {
                System.out.println("  Recurso con ID " + id + " no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Asigna un recurso a una patrulla y actualiza su estado a EN_USO
     *
     * @param con        conexion a la bd
     * @param idRecurso  identificador del recurso
     * @param idPatrulla identificador de la patrulla
     * @return true si la asignacion fue exitosa
     */
    public boolean asignarAPatrulla(Connection con, int idRecurso, int idPatrulla) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasActualizadas;

        try {
            ps = con.prepareStatement(
                    "UPDATE recurso SET idp = ?, disponibilidad = ? WHERE id = ?");
            ps.setInt(1, idPatrulla);
            ps.setString(2, EstadoRecursoEnum.EN_USO.name());
            ps.setInt(3, idRecurso);
            filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                exitoso = true;
                System.out.println("  Recurso " + idRecurso + " asignado a patrulla " + idPatrulla);
            } else {
                System.out.println("  Recurso con ID " + idRecurso + " no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Libera un recurso de la patrulla asignada y lo marca como DISPONIBLE
     *
     * @param con       conexion a la bd
     * @param idRecurso identificador del recurso a liberar
     * @return true si la operacion fue exitosa
     */
    public boolean liberarDePatrulla(Connection con, int idRecurso) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasActualizadas;

        try {
            ps = con.prepareStatement(
                    "UPDATE recurso SET idp = NULL, disponibilidad = ? WHERE id = ?");
            ps.setString(1, EstadoRecursoEnum.DISPONIBLE.name());
            ps.setInt(2, idRecurso);
            filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                exitoso = true;
                System.out.println("  Recurso " + idRecurso + " liberado de la patrulla.");
            } else {
                System.out.println("  Recurso con ID " + idRecurso + " no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Cambia el estado de disponibilidad de un recurso
     *
     * @param con       conexion a la bd
     * @param idRecurso identificador del recurso
     * @param estado    nuevo estado de disponibilidad
     * @return true si la actualizacion fue exitosa
     */
    public boolean modificarEstado(Connection con, int idRecurso, EstadoRecursoEnum estado) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasActualizadas;

        try {
            ps = con.prepareStatement(
                    "UPDATE recurso SET disponibilidad = ? WHERE id = ?");
            ps.setString(1, estado.name());
            ps.setInt(2, idRecurso);
            filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                exitoso = true;
                System.out.println("  Estado de recurso " + idRecurso + " actualizado a: " + estado.name());
            } else {
                System.out.println("  Recurso con ID " + idRecurso + " no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }
}
