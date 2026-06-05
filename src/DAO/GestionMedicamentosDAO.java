package DAO;

import Entidad.LoteMedicamentos;
import Entidad.Medicamento;
import Entidad.Paciente;
import Entidad.Prescripcion;
import Entidad.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para el modulo de Gestion de Medicamentos del sistema Code4Hope.
 * Implementa todas las operaciones CRUD sobre las tablas:
 * PRODUCTOS, MEDICAMENTOS, LOTE, LOTE_MEDICAMENTOS, PACIENTE, PRESCRIPCION.
 *
 * La conexion con la BD se recibe como parametro en cada metodo.
 * La gestion (apertura y cierre) de la conexion es responsabilidad
 * de la clase que llama, usando Conexion_DB.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class GestionMedicamentosDAO {

    /**
     * Imprime los detalles de una excepcion SQL.
     */
    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                System.err.println("--- SQLException ----------------------------");
                System.err.println("  Mensaje    : " + e.getMessage());
                System.err.println("  SQLState   : " + ((SQLException) e).getSQLState());
                System.err.println("  Codigo err : " + ((SQLException) e).getErrorCode());
                Throwable t = ex.getCause();
                if (t != null) {
                    System.err.println("  Causa      : " + t);
                }
                System.err.println("---------------------------------------------");
            }
        }
    }

    /**
     * Inserta un nuevo medicamento en las tablas PRODUCTOS y MEDICAMENTOS.
     *
     * @param con         Conexion activa con la base de datos
     * @param medicamento Objeto Medicamento con los datos a insertar
     * @return ID generado por la BD (AUTO_INCREMENT) o -1 si fallo
     */
    public int insertarMedicamento(Connection con, Medicamento medicamento) {
        PreparedStatement psprod = null;
        PreparedStatement psmed = null;
        ResultSet rs = null;
        int idGenerado = -1;

        try {
            con.setAutoCommit(false);

            psprod = con.prepareStatement(
                    "INSERT INTO PRODUCTOS (nombre, descripcion, unidad_medida, precio, categoria, tipo) " +
                            "VALUES (?, ?, ?, ?, ?, 'MEDICAMENTO')",
                    Statement.RETURN_GENERATED_KEYS);
            psprod.setString(1, medicamento.getNombre());
            psprod.setString(2, medicamento.getDescripcion());
            psprod.setString(3, medicamento.getUnidadMedida());
            psprod.setDouble(4, medicamento.getPrecio());
            psprod.setString(5, medicamento.getCategoria());
            psprod.executeUpdate();

            rs = psprod.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                medicamento.setIdProducto(idGenerado);
            }

            psmed = con.prepareStatement(
                    "INSERT INTO MEDICAMENTOS " +
                            "(id_producto, principio_activo, dosis, via_administracion, " +
                            " necesita_receta, temperatura_almacenamiento) " +
                            "VALUES (?, ?, ?, ?, ?, ?)");
            psmed.setInt(1, idGenerado);
            psmed.setString(2, medicamento.getPrincipioActivo());
            psmed.setString(3, medicamento.getDosis());
            psmed.setString(4, medicamento.getViaAdministracion());
            psmed.setBoolean(5, medicamento.isNecesitaReceta());
            psmed.setDouble(6, medicamento.getTemperaturaAlmacenamiento());
            psmed.executeUpdate();

            con.commit();
            System.out.println("  Medicamento insertado -> ID: " + idGenerado + " - " + medicamento.getNombre());

        } catch (SQLException ex) {
            try {
                if (con != null) {
                    con.rollback();
                    System.err.println("  Rollback ejecutado. No se inserto el medicamento.");
                }
            } catch (SQLException eRollback) {
                printSQLException(eRollback);
            }
            printSQLException(ex);
            idGenerado = -1;
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (psmed != null)
                    psmed.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (psprod != null)
                    psprod.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return idGenerado;
    }

    /**
     * Comprueba si existe un medicamento con el ID indicado en la base de datos.
     */
    public boolean existeMedicamento(Connection con, int idProducto) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean existe = false;

        try {
            ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM MEDICAMENTOS WHERE id_producto = ?");
            ps.setInt(1, idProducto);
            rs = ps.executeQuery();

            if (rs.next()) {
                existe = rs.getInt(1) > 0;
            }

        } catch (SQLException ex) {
            printSQLException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return existe;
    }

    /**
     * Recupera un medicamento completo (PRODUCTOS + MEDICAMENTOS) por su ID.
     */
    public Medicamento obtenerMedicamento(Connection con, int idProducto) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Medicamento medicamento = null;

        try {
            ps = con.prepareStatement(
                    "SELECT p.id_producto, p.nombre, p.descripcion, p.unidad_medida, " +
                            "       p.precio, p.categoria, " +
                            "       m.principio_activo, m.dosis, m.via_administracion, " +
                            "       m.necesita_receta, m.temperatura_almacenamiento " +
                            "FROM PRODUCTOS p " +
                            "INNER JOIN MEDICAMENTOS m ON p.id_producto = m.id_producto " +
                            "WHERE p.id_producto = ?");
            ps.setInt(1, idProducto);
            rs = ps.executeQuery();

            if (rs.next()) {
                medicamento = new Medicamento(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("precio"),
                        rs.getString("categoria"),
                        rs.getString("principio_activo"),
                        rs.getString("dosis"),
                        rs.getString("via_administracion"),
                        rs.getBoolean("necesita_receta"),
                        rs.getDouble("temperatura_almacenamiento"));
            }

        } catch (SQLException ex) {
            printSQLException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return medicamento;
    }

    /**
     * Recupera todos los medicamentos existentes en la base de datos.
     */
    public List<Medicamento> obtenerTodosMedicamentos(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Medicamento> medicamentos = new ArrayList<>();
        Medicamento m;

        try {
            ps = con.prepareStatement(
                    "SELECT p.id_producto, p.nombre, p.descripcion, p.unidad_medida, " +
                            "       p.precio, p.categoria, " +
                            "       m.principio_activo, m.dosis, m.via_administracion, " +
                            "       m.necesita_receta, m.temperatura_almacenamiento " +
                            "FROM PRODUCTOS p " +
                            "INNER JOIN MEDICAMENTOS m ON p.id_producto = m.id_producto " +
                            "ORDER BY p.nombre ASC");
            rs = ps.executeQuery();

            while (rs.next()) {
                m = new Medicamento(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("precio"),
                        rs.getString("categoria"),
                        rs.getString("principio_activo"),
                        rs.getString("dosis"),
                        rs.getString("via_administracion"),
                        rs.getBoolean("necesita_receta"),
                        rs.getDouble("temperatura_almacenamiento"));
                medicamentos.add(m);
            }
            System.out.println("  Medicamentos recuperados: " + medicamentos.size());

        } catch (SQLException ex) {
            printSQLException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return medicamentos;
    }

    /**
     * Actualiza los datos de un medicamento en PRODUCTOS y MEDICAMENTOS.
     */
    public boolean actualizarMedicamento(Connection con, Medicamento medicamento) {
        PreparedStatement psprod = null;
        PreparedStatement psmed = null;
        boolean exitoso = false;
        int filasProd;
        int filasMed;

        try {
            con.setAutoCommit(false);

            psprod = con.prepareStatement(
                    "UPDATE PRODUCTOS " +
                            "SET nombre = ?, descripcion = ?, unidad_medida = ?, precio = ?, categoria = ? " +
                            "WHERE id_producto = ? AND tipo = 'MEDICAMENTO'");
            psprod.setString(1, medicamento.getNombre());
            psprod.setString(2, medicamento.getDescripcion());
            psprod.setString(3, medicamento.getUnidadMedida());
            psprod.setDouble(4, medicamento.getPrecio());
            psprod.setString(5, medicamento.getCategoria());
            psprod.setInt(6, medicamento.getIdProducto());
            filasProd = psprod.executeUpdate();

            psmed = con.prepareStatement(
                    "UPDATE MEDICAMENTOS " +
                            "SET principio_activo = ?, dosis = ?, via_administracion = ?, " +
                            "    necesita_receta = ?, temperatura_almacenamiento = ? " +
                            "WHERE id_producto = ?");
            psmed.setString(1, medicamento.getPrincipioActivo());
            psmed.setString(2, medicamento.getDosis());
            psmed.setString(3, medicamento.getViaAdministracion());
            psmed.setBoolean(4, medicamento.isNecesitaReceta());
            psmed.setDouble(5, medicamento.getTemperaturaAlmacenamiento());
            psmed.setInt(6, medicamento.getIdProducto());
            filasMed = psmed.executeUpdate();

            if (filasProd > 0 && filasMed > 0) {
                con.commit();
                exitoso = true;
                System.out.println("  Medicamento actualizado -> ID: " + medicamento.getIdProducto());
            } else {
                con.rollback();
                System.err.println("  Medicamento ID " + medicamento.getIdProducto() + " no encontrado.");
            }

        } catch (SQLException ex) {
            try {
                if (con != null)
                    con.rollback();
            } catch (SQLException eRollback) {
                printSQLException(eRollback);
            }
            printSQLException(ex);
        } finally {
            try {
                if (psmed != null)
                    psmed.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (psprod != null)
                    psprod.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return exitoso;
    }

    /**
     * Elimina un medicamento de la base de datos (CASCADE borra tabla hija).
     */
    public boolean eliminarMedicamento(Connection con, int idProducto) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasEliminadas;

        try {
            ps = con.prepareStatement(
                    "DELETE FROM PRODUCTOS WHERE id_producto = ? AND tipo = 'MEDICAMENTO'");
            ps.setInt(1, idProducto);
            filasEliminadas = ps.executeUpdate();

            if (filasEliminadas > 0) {
                exitoso = true;
                System.out.println("  Medicamento eliminado -> ID: " + idProducto);
            } else {
                System.err.println("  Medicamento ID " + idProducto + " no encontrado.");
            }

        } catch (SQLException ex) {
            printSQLException(ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return exitoso;
    }

    /**
     * Inserta un nuevo lote de medicamentos en LOTE y LOTE_MEDICAMENTOS.
     */
    public int insertarLoteMedicamentos(Connection con, LoteMedicamentos lote) {
        PreparedStatement pslote = null;
        PreparedStatement pslotemed = null;
        ResultSet rs = null;
        int idGenerado = -1;

        try {
            con.setAutoCommit(false);

            pslote = con.prepareStatement(
                    "INSERT INTO LOTE (id_producto, cantidad, fecha_entrada, fecha_caducidad, estado) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pslote.setInt(1, lote.getIdProducto());
            pslote.setInt(2, lote.getCantidad());
            pslote.setDate(3, Date.valueOf(lote.getFechaEntrada()));
            pslote.setDate(4, lote.getFechaCaducidad() != null
                    ? Date.valueOf(lote.getFechaCaducidad())
                    : null);
            pslote.setString(5, lote.getEstado());
            pslote.executeUpdate();

            rs = pslote.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                lote.setIdLote(idGenerado);
            }

            pslotemed = con.prepareStatement(
                    "INSERT INTO LOTE_MEDICAMENTOS " +
                            "(id_lote, numero_lote_fabricante, condiciones_almacenamiento, codigo_almacen) " +
                            "VALUES (?, ?, ?, ?)");
            pslotemed.setInt(1, idGenerado);
            pslotemed.setString(2, lote.getNumeroLoteFabricante());
            pslotemed.setString(3, lote.getCondicionesAlmacenamiento());
            pslotemed.setString(4, lote.getCodigoAlmacen());
            pslotemed.executeUpdate();

            con.commit();
            System.out.println("  Lote medicamento insertado -> ID: " + idGenerado);

        } catch (SQLException ex) {
            try {
                if (con != null) {
                    con.rollback();
                    System.err.println("  Rollback: no se inserto el lote.");
                }
            } catch (SQLException eRollback) {
                printSQLException(eRollback);
            }
            printSQLException(ex);
            idGenerado = -1;
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (pslotemed != null)
                    pslotemed.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (pslote != null)
                    pslote.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return idGenerado;
    }

    /**
     * Registra un nuevo paciente en la tabla PACIENTE.
     */
    public int insertarPaciente(Connection con, Paciente paciente) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idGenerado = -1;

        try {
            ps = con.prepareStatement(
                    "INSERT INTO PACIENTE (nombre, apellidos, fecha_nacimiento, alergias, historial_medico) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setDate(3, paciente.getFechaNacimiento() != null
                    ? Date.valueOf(paciente.getFechaNacimiento())
                    : null);
            ps.setString(4, paciente.getAlergias());
            ps.setString(5, paciente.getHistorialMedico());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                paciente.setIdPaciente(idGenerado);
            }
            System.out.println("  Paciente registrado -> ID: " + idGenerado + " - " + paciente.getNombreCompleto());

        } catch (SQLException ex) {
            printSQLException(ex);
            idGenerado = -1;
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return idGenerado;
    }

    /**
     * Recupera un paciente por su ID de la tabla PACIENTE.
     * Devuelve null si no existe.
     */
    public Paciente obtenerPaciente(Connection con, int idPaciente) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Paciente paciente = null;
        Date fechaNac;

        try {
            ps = con.prepareStatement(
                    "SELECT id_paciente, nombre, apellidos, fecha_nacimiento, " +
                            "alergias, historial_medico " +
                            "FROM PACIENTE WHERE id_paciente = ?");
            ps.setInt(1, idPaciente);
            rs = ps.executeQuery();

            if (rs.next()) {
                fechaNac = rs.getDate("fecha_nacimiento");
                paciente = new Paciente(
                        rs.getInt("id_paciente"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        fechaNac != null ? fechaNac.toLocalDate() : null,
                        rs.getString("alergias"),
                        rs.getString("historial_medico"));
            }

        } catch (SQLException ex) {
            printSQLException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return paciente;
    }

    /**
     * Registra una nueva prescripcion medica en la tabla PRESCRIPCION.
     */
    public int insertarPrescripcion(Connection con, Prescripcion prescripcion) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idGenerado = -1;

        try {
            ps = con.prepareStatement(
                    "INSERT INTO PRESCRIPCION " +
                            "(id_paciente, id_producto, dosis, frecuencia, duracion, fecha_inicio, fecha_fin, estado) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, prescripcion.getIdPaciente());
            ps.setInt(2, prescripcion.getIdProducto());
            ps.setString(3, prescripcion.getDosis());
            ps.setString(4, prescripcion.getFrecuencia());
            ps.setInt(5, prescripcion.getDuracion());
            ps.setDate(6, prescripcion.getFechaInicio() != null
                    ? Date.valueOf(prescripcion.getFechaInicio())
                    : null);
            ps.setDate(7, prescripcion.getFechaFin() != null
                    ? Date.valueOf(prescripcion.getFechaFin())
                    : null);
            ps.setString(8, prescripcion.getEstado());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                prescripcion.setIdPrescripcion(idGenerado);
            }
            System.out.println("  Prescripcion insertada -> ID: " + idGenerado);

        } catch (SQLException ex) {
            printSQLException(ex);
            idGenerado = -1;
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return idGenerado;
    }

    /**
     * Recupera todas las prescripciones activas de un paciente.
     */
    public List<Prescripcion> obtenerPrescripcionesActivas(Connection con, int idPaciente) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Prescripcion> prescripciones = new ArrayList<>();
        Date fechaIni;
        Date fechaFin;
        Prescripcion p;

        try {
            ps = con.prepareStatement(
                    "SELECT id_prescripcion, id_paciente, id_producto, dosis, frecuencia, " +
                            "       duracion, fecha_inicio, fecha_fin, estado " +
                            "FROM PRESCRIPCION " +
                            "WHERE id_paciente = ? AND estado = 'ACTIVA' " +
                            "ORDER BY fecha_inicio DESC");
            ps.setInt(1, idPaciente);
            rs = ps.executeQuery();

            while (rs.next()) {
                fechaIni = rs.getDate("fecha_inicio");
                fechaFin = rs.getDate("fecha_fin");
                p = new Prescripcion(
                        rs.getInt("id_prescripcion"),
                        rs.getInt("id_paciente"),
                        rs.getInt("id_producto"),
                        rs.getString("dosis"),
                        rs.getString("frecuencia"),
                        rs.getInt("duracion"),
                        fechaIni != null ? fechaIni.toLocalDate() : null,
                        fechaFin != null ? fechaFin.toLocalDate() : null,
                        rs.getString("estado"));
                prescripciones.add(p);
            }
            System.out.println("  Prescripciones activas del paciente " + idPaciente + ": " + prescripciones.size());

        } catch (SQLException ex) {
            printSQLException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return prescripciones;
    }

    /**
     * Recupera todos los medicamentos que requieren cadena de frio (temp <= 8
     * grados C).
     */
    public List<Medicamento> obtenerMedicamentosConRefrigeracion(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Medicamento> refrigerados = new ArrayList<>();
        Medicamento m;

        try {
            ps = con.prepareStatement(
                    "SELECT p.id_producto, p.nombre, p.descripcion, p.unidad_medida, " +
                            "       p.precio, p.categoria, " +
                            "       m.principio_activo, m.dosis, m.via_administracion, " +
                            "       m.necesita_receta, m.temperatura_almacenamiento " +
                            "FROM PRODUCTOS p " +
                            "INNER JOIN MEDICAMENTOS m ON p.id_producto = m.id_producto " +
                            "WHERE m.temperatura_almacenamiento <= 8.0 " +
                            "ORDER BY m.temperatura_almacenamiento ASC");
            rs = ps.executeQuery();

            while (rs.next()) {
                m = new Medicamento(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("precio"),
                        rs.getString("categoria"),
                        rs.getString("principio_activo"),
                        rs.getString("dosis"),
                        rs.getString("via_administracion"),
                        rs.getBoolean("necesita_receta"),
                        rs.getDouble("temperatura_almacenamiento"));
                refrigerados.add(m);
            }
            System.out.println("  Medicamentos con refrigeracion: " + refrigerados.size());

        } catch (SQLException ex) {
            printSQLException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return refrigerados;
    }

    /**
     * Recupera todos los lotes de medicamentos con estado ACTIVO.
     * Se usa para mostrar al usuario las opciones disponibles
     * antes de elegir cual asignar a una patrulla.
     *
     * @param con Conexion activa con la base de datos
     * @return Lista de LoteMedicamentos activos, vacia si no hay ninguno o si fallo
     */
    public List<LoteMedicamentos> listarLotesMedicamentosActivos(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<LoteMedicamentos> lotes = new ArrayList<>();
        Date fechaCad;
        LoteMedicamentos lote;

        try {
            ps = con.prepareStatement(
                    "SELECT l.id_lote, l.id_producto, l.cantidad, l.fecha_entrada, " +
                            "       l.fecha_caducidad, l.estado, " +
                            "       lm.numero_lote_fabricante, lm.condiciones_almacenamiento, lm.codigo_almacen " +
                            "FROM LOTE l " +
                            "INNER JOIN LOTE_MEDICAMENTOS lm ON l.id_lote = lm.id_lote " +
                            "WHERE l.estado = 'ACTIVO' " +
                            "ORDER BY l.id_lote ASC");
            rs = ps.executeQuery();

            while (rs.next()) {
                fechaCad = rs.getDate("fecha_caducidad");
                lote = new LoteMedicamentos(
                        rs.getInt("id_lote"),
                        rs.getInt("id_producto"),
                        rs.getInt("cantidad"),
                        rs.getDate("fecha_entrada").toLocalDate(),
                        fechaCad != null ? fechaCad.toLocalDate() : null,
                        rs.getString("estado"),
                        rs.getString("numero_lote_fabricante"),
                        rs.getString("condiciones_almacenamiento"),
                        rs.getString("codigo_almacen"));
                lotes.add(lote);
            }

        } catch (SQLException ex) {
            printSQLException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }

        return lotes;
    }

    /**
     * Muestra los detalles de un producto usando polimorfismo.
     */
    public void mostrarDetallesProducto(Producto producto, int cantidad) {
        System.out.println("\n  -- Datos del producto --------------------------");
        System.out.println("  " + producto.toString());
        System.out.println("  " + producto.getDetallesEspecificos());
        System.out.println("  Valor inventario (" + cantidad + " u.): " +
                String.format("%.2f", producto.calcularValorTotal(cantidad)) + " EUR");
        System.out.println("  " + producto.generarAlertaStock(cantidad, 20));
        System.out.println("  ------------------------------------------------");
    }

}
