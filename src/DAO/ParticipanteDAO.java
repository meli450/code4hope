/*Subsistema Talleres de formación
 *Realizado por: Melisa
 * ParticipanteDAO */
package DAO;

import Entidad.Participante;
import Entidad.PerfilEnum;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para las operaciones CRUD de la entidad Participante sobre la bd
 */
public class ParticipanteDAO {

    /**
     * Inserta un nuevo participante en la bd.
     *
     * @param con          conexion a la bd
     * @param participante objeto Participante con los datos a insertar
     * @return identificador generado o -1 si fallo la insercion
     */
    public int insertarParticipante(Connection con, Participante participante) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idGenerado = -1;

        try {
            ps = con.prepareStatement(
                    "INSERT INTO participante (nombre, apellido, genero, edad, perfil, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, participante.getNombre());
            ps.setString(2, participante.getApellido());
            ps.setString(3, participante.getGenero());
            ps.setInt(4, participante.getEdad());
            ps.setString(5, participante.getPerfil().name());
            ps.setBoolean(6, participante.isActivo());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                participante.setId(idGenerado);
            }
            System.out.println("  Participante insertado con ID: " + idGenerado);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return idGenerado;
    }

    /**
     * Obtiene un/a participante a partir de su identificador
     *
     * @param con conexion a la bd
     * @param id  identificador del o la participante
     * @return objeto Participante / null si no existe
     */
    public Participante obtenerParticipante(Connection con, int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Participante participante = null;
        try {
            ps = con.prepareStatement(
                    "SELECT id, nombre, apellido, genero, edad, perfil, activo " +
                    "FROM participante WHERE id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                participante = new Participante(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("genero"),
                        rs.getInt("edad"),
                        PerfilEnum.valueOf(rs.getString("perfil")));
                participante.setId(rs.getInt("id"));
                participante.setActivo(rs.getBoolean("activo"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return participante;
    }

    /**
     * Obtiene la lista de todos los o las participantes activos ordenados por apellido y nombre
     *
     * @param con conexion a la bd
     * @return lista de participantes activos/as
     */
    public List<Participante> obtenerTodosParticipantes(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Participante> participantes = new ArrayList<>();

        try {
            ps = con.prepareStatement(
                    "SELECT id, nombre, apellido, genero, edad, perfil, activo " +
                    "FROM participante WHERE activo = true ORDER BY apellido, nombre");
            rs = ps.executeQuery();

            while (rs.next()) {
                Participante p = new Participante(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("genero"),
                        rs.getInt("edad"),
                        PerfilEnum.valueOf(rs.getString("perfil")));
                p.setId(rs.getInt("id"));
                p.setActivo(rs.getBoolean("activo"));
                participantes.add(p);
            }
            System.out.println("  Participantes recuperados: " + participantes.size());

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return participantes;
    }

    /**
     * Actualiza los datos de un/a participante existente en la bd
     *
     * @param con          conexion a la bd
     * @param participante objeto Participante con los datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizarParticipante(Connection con, Participante participante) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasActualizadas;

        try {
            ps = con.prepareStatement(
                    "UPDATE participante SET nombre = ?, apellido = ?, genero = ?, edad = ?, perfil = ? " +
                    "WHERE id = ?");
            ps.setString(1, participante.getNombre());
            ps.setString(2, participante.getApellido());
            ps.setString(3, participante.getGenero());
            ps.setInt(4, participante.getEdad());
            ps.setString(5, participante.getPerfil().name());
            ps.setInt(6, participante.getId());
            filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                exitoso = true;
                System.out.println("  Participante actualizado con ID: " + participante.getId());
            } else {
                System.out.println("  Participante con ID " + participante.getId() + " no encontrado.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Realiza la baja de un/a participante estableciendo activo a false
     *
     * @param con conexion a la bd
     * @param id  identificador del o la participante a dar de baja
     * @return true si la operacion fue exitosa
     */
    public boolean darDeBaja(Connection con, int id) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasActualizadas;

        try {
            ps = con.prepareStatement(
                    "UPDATE participante SET activo = false WHERE id = ?");
            ps.setInt(1, id);
            filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                exitoso = true;
                System.out.println("  Participante dado de baja con ID: " + id);
            } else {
                System.out.println("  Participante con ID " + id + " no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return exitoso;
    }
}
