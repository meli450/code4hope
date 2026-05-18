/*Subsistema Talleres de formación
 *Realizado por: Melisa
 * TallerDAO */
package DAO;

import Entidad.Participante;
import Entidad.PerfilEnum;
import Entidad.Recurso;
import Entidad.EstadoRecursoEnum;
import Entidad.EstadoTallerEnum;
import Entidad.Taller;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para las operaciones CRUD de la entidad Taller sobre la bd
 * Gestiona tambien las relaciones con participantes y recursos
 */
public class TallerDAO {

    /**
     * Inserta un nuevo taller en la bd.
     *
     * @param con    conexion a la bd
     * @param taller objeto Taller con los datos a insertar
     * @return identificador generado o -1 si fallo la insercion
     */
    public int insertarTaller(Connection con, Taller taller) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idGenerado = -1;

        try {
            ps = con.prepareStatement(
                    "INSERT INTO taller (titulo, descripcion, perfil_dest, etiqueta, espacio, " +
                    "aforo_maximo, fecha_inicio, fecha_fin, fecha_cancelacion, incidencia, estado, nif) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, taller.getTitulo());
            ps.setString(2, taller.getDescripcion());
            ps.setString(3, taller.getPerfilRequerido().name());
            ps.setString(4, taller.getEtiqueta());
            ps.setString(5, taller.getEspacio());
            ps.setInt(6, taller.getAforoMaximo());
            ps.setString(7, taller.getFechaInicio());
            ps.setString(8, taller.getFechaFin());
            ps.setString(9, taller.getFechaCancelacion());
            ps.setString(10, taller.getIncidencia());
            ps.setString(11, taller.getEstado().name());
            if (taller.getNif() != null && !taller.getNif().isEmpty()) {
                ps.setString(12, taller.getNif());
            } else {
                ps.setNull(12, Types.VARCHAR);
            }
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                taller.setCod(idGenerado);
            }
            System.out.println("  Taller insertado -> ID: " + idGenerado);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return idGenerado;
    }

    /**
     * Obtiene un taller a partir de su codigo.
     *
     * @param con conexion a la bd
     * @param cod codigo identificador del taller
     * @return objeto Taller o null si no existe
     */
    public Taller obtenerTaller(Connection con, int cod) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Taller taller = null;

        try {
            ps = con.prepareStatement(
                    "SELECT cod, titulo, descripcion, perfil_dest, etiqueta, espacio, " +
                    "aforo_maximo, fecha_inicio, fecha_fin, fecha_cancelacion, incidencia, estado, nif " +
                    "FROM taller WHERE cod = ?");
            ps.setInt(1, cod);
            rs = ps.executeQuery();

            if (rs.next()) {
                taller = new Taller(
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        PerfilEnum.valueOf(rs.getString("perfil_dest")),
                        rs.getString("etiqueta"),
                        rs.getString("espacio"),
                        rs.getInt("aforo_maximo"),
                        rs.getString("fecha_inicio"),
                        rs.getString("nif"));
                taller.setCod(rs.getInt("cod"));
                taller.setFechaFin(rs.getString("fecha_fin"));
                taller.setFechaCancelacion(rs.getString("fecha_cancelacion"));
                taller.setIncidencia(rs.getString("incidencia"));
                taller.setEstado(EstadoTallerEnum.valueOf(rs.getString("estado")));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return taller;
    }

    /**
     * Obtiene la lista de todos los talleres ordenados por codigo.
     *
     * @param con conexion a la bd
     * @return lista de talleres
     */
    public List<Taller> obtenerTodosTalleres(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Taller> talleres = new ArrayList<>();

        try {
            ps = con.prepareStatement(
                    "SELECT cod, titulo, descripcion, perfil_dest, etiqueta, espacio, " +
                    "aforo_maximo, fecha_inicio, fecha_fin, fecha_cancelacion, incidencia, estado, nif " +
                    "FROM taller ORDER BY cod");
            rs = ps.executeQuery();

            while (rs.next()) {
                Taller t = new Taller(
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        PerfilEnum.valueOf(rs.getString("perfil_dest")),
                        rs.getString("etiqueta"),
                        rs.getString("espacio"),
                        rs.getInt("aforo_maximo"),
                        rs.getString("fecha_inicio"),
                        rs.getString("nif"));
                t.setCod(rs.getInt("cod"));
                t.setFechaFin(rs.getString("fecha_fin"));
                t.setFechaCancelacion(rs.getString("fecha_cancelacion"));
                t.setIncidencia(rs.getString("incidencia"));
                t.setEstado(EstadoTallerEnum.valueOf(rs.getString("estado")));
                talleres.add(t);
            }
            System.out.println("  Talleres recuperados: " + talleres.size());

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return talleres;
    }

    /**
     * Obtiene la lista de talleres filtrados por estado.
     *
     * @param con    conexion a la bd
     * @param estado estado por el que filtrar
     * @return lista de talleres con el estado indicado
     */
    public List<Taller> obtenerTalleresPorEstado(Connection con, EstadoTallerEnum estado) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Taller> talleres = new ArrayList<>();

        try {
            ps = con.prepareStatement(
                    "SELECT cod, titulo, descripcion, perfil_dest, etiqueta, espacio, " +
                    "aforo_maximo, fecha_inicio, fecha_fin, fecha_cancelacion, incidencia, estado, nif " +
                    "FROM taller WHERE estado = ? ORDER BY cod");
            ps.setString(1, estado.name());
            rs = ps.executeQuery();

            while (rs.next()) {
                Taller t = new Taller(
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        PerfilEnum.valueOf(rs.getString("perfil_dest")),
                        rs.getString("etiqueta"),
                        rs.getString("espacio"),
                        rs.getInt("aforo_maximo"),
                        rs.getString("fecha_inicio"),
                        rs.getString("nif"));
                t.setCod(rs.getInt("cod"));
                t.setFechaFin(rs.getString("fecha_fin"));
                t.setFechaCancelacion(rs.getString("fecha_cancelacion"));
                t.setIncidencia(rs.getString("incidencia"));
                t.setEstado(EstadoTallerEnum.valueOf(rs.getString("estado")));
                talleres.add(t);
            }
            System.out.println("  Talleres recuperados: " + talleres.size());

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return talleres;
    }

    /**
     * Actualiza los datos de un taller existente en la bd.
     *
     * @param con    conexion a la bd
     * @param taller objeto Taller con los datos actualizados
     * @return true si la actualizacion fue exitosa
     */
    public boolean actualizarTaller(Connection con, Taller taller) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasActualizadas;

        try {
            ps = con.prepareStatement(
                    "UPDATE taller SET titulo = ?, descripcion = ?, perfil_dest = ?, etiqueta = ?, " +
                    "espacio = ?, aforo_maximo = ?, fecha_inicio = ?, fecha_fin = ?, " +
                    "fecha_cancelacion = ?, incidencia = ?, estado = ?, nif = ? WHERE cod = ?");
            ps.setString(1, taller.getTitulo());
            ps.setString(2, taller.getDescripcion());
            ps.setString(3, taller.getPerfilRequerido().name());
            ps.setString(4, taller.getEtiqueta());
            ps.setString(5, taller.getEspacio());
            ps.setInt(6, taller.getAforoMaximo());
            ps.setString(7, taller.getFechaInicio());
            ps.setString(8, taller.getFechaFin());
            ps.setString(9, taller.getFechaCancelacion());
            ps.setString(10, taller.getIncidencia());
            ps.setString(11, taller.getEstado().name());
            if (taller.getNif() != null && !taller.getNif().isEmpty()) {
                ps.setString(12, taller.getNif());
            } else {
                ps.setNull(12, Types.VARCHAR);
            }
            ps.setInt(13, taller.getCod());
            filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                exitoso = true;
                System.out.println("  Taller actualizado -> ID: " + taller.getCod());
            } else {
                System.out.println("  Taller con ID " + taller.getCod() + " no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Elimina un taller y en cascada sus participantes, recursos y encuestas.
     *
     * @param con conexion a la bd
     * @param cod codigo del taller a eliminar
     * @return true si la eliminacion fue exitosa
     */
    public boolean eliminarTaller(Connection con, int cod) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasEliminadas;

        try {
            ps = con.prepareStatement("DELETE FROM taller WHERE cod = ?");
            ps.setInt(1, cod);
            filasEliminadas = ps.executeUpdate();

            if (filasEliminadas > 0) {
                exitoso = true;
                System.out.println("  Taller eliminado -> ID: " + cod);
            } else {
                System.out.println("  Taller con ID " + cod + " no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Cancela un taller guardando la incidencia y la fecha actual del sistema.
     *
     * @param con        conexion a la bd
     * @param cod        codigo del taller a cancelar
     * @param incidencia motivo de la cancelacion
     * @return true si la operacion fue exitosa
     */
    public boolean cancelarTaller(Connection con, int cod, String incidencia) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasActualizadas;
        String fechaHoy = java.time.LocalDate.now().toString();

        try {
            ps = con.prepareStatement(
                    "UPDATE taller SET estado = ?, fecha_cancelacion = ?, incidencia = ? WHERE cod = ?");
            ps.setString(1, EstadoTallerEnum.CANCELADO.name());
            ps.setString(2, fechaHoy);
            ps.setString(3, incidencia);
            ps.setInt(4, cod);
            filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) {
                exitoso = true;
                System.out.println("  Taller " + cod + " cancelado.");
            } else {
                System.out.println("  Taller con ID " + cod + " no encontrado.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Cuenta cuantos participantes hay inscritos en un taller.
     *
     * @param con       conexion a la bd
     * @param codTaller codigo del taller
     * @return numero de participantes inscritos
     */
    public int contarParticipantes(Connection con, int codTaller) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int total = 0;

        try {
            ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM participa WHERE codt = ?");
            ps.setInt(1, codTaller);
            rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return total;
    }

    /**
     * Inscribe a un participante en un taller registrando las fechas de participacion.
     *
     * @param con           conexion a la bd
     * @param codTaller     codigo del taller
     * @param idParticipante identificador del participante
     * @param fechaInicio   fecha de inicio de la participacion (yyyy-MM-dd)
     * @param fechaFin      fecha de fin de la participacion (yyyy-MM-dd, puede ser null)
     * @return true si la inscripcion fue exitosa
     */
    public boolean inscribirParticipante(Connection con, int codTaller,
            int idParticipante, String fechaInicio, String fechaFin) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filas;

        try {
            ps = con.prepareStatement(
                    "INSERT INTO participa (codt, iduser, fecha_ini, fecha_fin) VALUES (?, ?, ?, ?)");
            ps.setInt(1, codTaller);
            ps.setInt(2, idParticipante);
            ps.setString(3, fechaInicio);
            if (fechaFin != null && !fechaFin.isEmpty()) {
                ps.setString(4, fechaFin);
            } else {
                ps.setNull(4, Types.DATE);
            }
            filas = ps.executeUpdate();

            if (filas > 0) {
                exitoso = true;
                System.out.println("  Participante " + idParticipante + " inscrito en taller " + codTaller);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Da de baja a un participante de un taller eliminando su registro de la tabla participa.
     *
     * @param con            conexion a la bd
     * @param codTaller      codigo del taller
     * @param idParticipante identificador del participante
     * @return true si la baja fue exitosa
     */
    public boolean darDeBajaParticipanteEnTaller(Connection con, int codTaller, int idParticipante) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filas;

        try {
            ps = con.prepareStatement(
                    "DELETE FROM participa WHERE codt = ? AND iduser = ?");
            ps.setInt(1, codTaller);
            ps.setInt(2, idParticipante);
            filas = ps.executeUpdate();

            if (filas > 0) {
                exitoso = true;
                System.out.println("  Participante " + idParticipante + " dado de baja del taller " + codTaller);
            } else {
                System.out.println("  No se encontro la participacion indicada.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Obtiene los participantes inscritos en un taller mediante join con la tabla participa.
     *
     * @param con       conexion a la bd
     * @param codTaller codigo del taller
     * @return lista de participantes inscritos
     */
    public List<Participante> obtenerParticipantesDeTaller(Connection con, int codTaller) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Participante> participantes = new ArrayList<>();

        try {
            ps = con.prepareStatement(
                    "SELECT p.id, p.nombre, p.apellido, p.genero, p.edad, p.perfil, p.activo " +
                    "FROM participante p " +
                    "INNER JOIN participa pt ON p.id = pt.iduser " +
                    "WHERE pt.codt = ? ORDER BY p.apellido, p.nombre");
            ps.setInt(1, codTaller);
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
            System.out.println("  Participantes del taller " + codTaller + ": " + participantes.size());

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return participantes;
    }

    /**
     * Asigna un recurso a un taller registrando las fechas de uso.
     *
     * @param con        conexion a la bd
     * @param codTaller  codigo del taller
     * @param idRecurso  identificador del recurso
     * @param fechaInicio fecha de inicio del uso (yyyy-MM-dd)
     * @param fechaFin   fecha de fin del uso (yyyy-MM-dd, puede ser null)
     * @return true si la asignacion fue exitosa
     */
    public boolean asignarRecurso(Connection con, int codTaller, int idRecurso,
            String fechaInicio, String fechaFin) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filas;

        try {
            ps = con.prepareStatement(
                    "INSERT INTO tiene (codt, idr, fecha_ini, fecha_fin) VALUES (?, ?, ?, ?)");
            ps.setInt(1, codTaller);
            ps.setInt(2, idRecurso);
            ps.setString(3, fechaInicio);
            if (fechaFin != null && !fechaFin.isEmpty()) {
                ps.setString(4, fechaFin);
            } else {
                ps.setNull(4, Types.DATE);
            }
            filas = ps.executeUpdate();

            if (filas > 0) {
                exitoso = true;
                System.out.println("  Recurso " + idRecurso + " asignado al taller " + codTaller);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Elimina la asignacion de un recurso de un taller.
     *
     * @param con       conexion a la bd
     * @param codTaller codigo del taller
     * @param idRecurso identificador del recurso
     * @return true si la eliminacion fue exitosa
     */
    public boolean eliminarRecursoDeTaller(Connection con, int codTaller, int idRecurso) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filas;

        try {
            ps = con.prepareStatement(
                    "DELETE FROM tiene WHERE codt = ? AND idr = ?");
            ps.setInt(1, codTaller);
            ps.setInt(2, idRecurso);
            filas = ps.executeUpdate();

            if (filas > 0) {
                exitoso = true;
                System.out.println("  Recurso " + idRecurso + " eliminado del taller " + codTaller);
            } else {
                System.out.println("  No se encontro la asignacion indicada.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return exitoso;
    }

    /**
     * Obtiene los recursos asignados a un taller mediante join con la tabla tiene.
     *
     * @param con       conexion a la bd
     * @param codTaller codigo del taller
     * @return lista de recursos asignados al taller
     */
    public List<Recurso> obtenerRecursosDeTaller(Connection con, int codTaller) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Recurso> recursos = new ArrayList<>();

        try {
            ps = con.prepareStatement(
                    "SELECT r.id, r.tipo, r.disponibilidad, r.cantidad, r.es_fungible, r.idp " +
                    "FROM recurso r " +
                    "INNER JOIN tiene t ON r.id = t.idr " +
                    "WHERE t.codt = ? ORDER BY r.tipo");
            ps.setInt(1, codTaller);
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
            System.out.println("  Recursos del taller " + codTaller + ": " + recursos.size());

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return recursos;
    }
}
