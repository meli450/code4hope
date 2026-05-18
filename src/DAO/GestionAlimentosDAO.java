package DAO;

import Entidad.Alimento;
import Entidad.LoteAlimentos;
import Entidad.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para el modulo de Gestion de Alimentos del sistema Code4Hope.
 * Implementa todas las operaciones CRUD sobre las tablas:
 * PRODUCTOS, ALIMENTOS, LOTE, LOTE_ALIMENTOS.
 *
 * La conexion con la BD se recibe como parametro en cada metodo.
 * La gestion (apertura y cierre) de la conexion es responsabilidad
 * de la clase que llama, usando Conexion_DB.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class GestionAlimentosDAO {

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
     * Inserta un nuevo alimento en las tablas PRODUCTOS y ALIMENTOS.
     * Usa transaccion para garantizar la integridad referencial.
     *
     * @param con      Conexion activa con la base de datos
     * @param alimento Objeto Alimento con los datos a insertar
     * @return ID generado por la BD (AUTO_INCREMENT) o -1 si fallo
     */
    public int insertarAlimento(Connection con, Alimento alimento) {
        PreparedStatement psprod = null;
        PreparedStatement psali = null;
        ResultSet rs = null;
        int idGenerado = -1;

        try {
            con.setAutoCommit(false);

            psprod = con.prepareStatement(
                    "INSERT INTO PRODUCTOS (nombre, descripcion, unidad_medida, precio, categoria, tipo) " +
                            "VALUES (?, ?, ?, ?, ?, 'ALIMENTO')",
                    Statement.RETURN_GENERATED_KEYS);
            psprod.setString(1, alimento.getNombre());
            psprod.setString(2, alimento.getDescripcion());
            psprod.setString(3, alimento.getUnidadMedida());
            psprod.setDouble(4, alimento.getPrecio());
            psprod.setString(5, alimento.getCategoria());
            psprod.executeUpdate();

            rs = psprod.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                alimento.setIdProducto(idGenerado);
            }

            psali = con.prepareStatement(
                    "INSERT INTO ALIMENTOS " +
                            "(id_producto, calorias, tipo_dieta, necesita_refrigeracion, temperatura_min, temperatura_max) "
                            +
                            "VALUES (?, ?, ?, ?, ?, ?)");
            psali.setInt(1, idGenerado);
            psali.setInt(2, alimento.getCalorias());
            psali.setString(3, alimento.getTipoDieta());
            psali.setBoolean(4, alimento.isNecesitaRefrigeracion());
            psali.setDouble(5, alimento.getTemperaturaMin());
            psali.setDouble(6, alimento.getTemperaturaMax());
            psali.executeUpdate();

            con.commit();
            System.out.println("  Alimento insertado -> ID: " + idGenerado + " - " + alimento.getNombre());

        } catch (SQLException ex) {
            try {
                if (con != null) {
                    con.rollback();
                    System.err.println("  Rollback ejecutado. No se inserto el alimento.");
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
                if (psali != null)
                    psali.close();
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
     * Comprueba si existe un alimento con el ID indicado en la base de datos.
     *
     * @param con        Conexion activa con la base de datos
     * @param idProducto Identificador del producto a comprobar
     * @return true si existe; false si no existe o hay error
     */
    public boolean existeAlimento(Connection con, int idProducto) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean existe = false;

        try {
            ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM ALIMENTOS WHERE id_producto = ?");
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
     * Recupera un alimento completo (PRODUCTOS + ALIMENTOS) por su ID.
     *
     * @param con        Conexion activa con la base de datos
     * @param idProducto Identificador del alimento a recuperar
     * @return Objeto Alimento con todos sus datos, o null si no existe
     */
    public Alimento obtenerAlimento(Connection con, int idProducto) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Alimento alimento = null;

        try {
            ps = con.prepareStatement(
                    "SELECT p.id_producto, p.nombre, p.descripcion, p.unidad_medida, " +
                            "       p.precio, p.categoria, " +
                            "       a.calorias, a.tipo_dieta, a.necesita_refrigeracion, " +
                            "       a.temperatura_min, a.temperatura_max " +
                            "FROM PRODUCTOS p " +
                            "INNER JOIN ALIMENTOS a ON p.id_producto = a.id_producto " +
                            "WHERE p.id_producto = ?");
            ps.setInt(1, idProducto);
            rs = ps.executeQuery();

            if (rs.next()) {
                alimento = new Alimento(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("precio"),
                        rs.getString("categoria"),
                        rs.getInt("calorias"),
                        rs.getString("tipo_dieta"),
                        rs.getBoolean("necesita_refrigeracion"),
                        rs.getDouble("temperatura_min"),
                        rs.getDouble("temperatura_max"));
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

        return alimento;
    }

    /**
     * Recupera todos los alimentos existentes en la base de datos.
     *
     * @param con Conexion activa con la base de datos
     * @return Lista de objetos Alimento; lista vacia si no hay datos o hay error
     */
    public List<Alimento> obtenerTodosAlimentos(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Alimento> alimentos = new ArrayList<>();
        Alimento a;

        try {
            ps = con.prepareStatement(
                    "SELECT p.id_producto, p.nombre, p.descripcion, p.unidad_medida, " +
                            "       p.precio, p.categoria, " +
                            "       a.calorias, a.tipo_dieta, a.necesita_refrigeracion, " +
                            "       a.temperatura_min, a.temperatura_max " +
                            "FROM PRODUCTOS p " +
                            "INNER JOIN ALIMENTOS a ON p.id_producto = a.id_producto " +
                            "ORDER BY p.nombre ASC");
            rs = ps.executeQuery();

            while (rs.next()) {
                a = new Alimento(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("precio"),
                        rs.getString("categoria"),
                        rs.getInt("calorias"),
                        rs.getString("tipo_dieta"),
                        rs.getBoolean("necesita_refrigeracion"),
                        rs.getDouble("temperatura_min"),
                        rs.getDouble("temperatura_max"));
                alimentos.add(a);
            }
            System.out.println("  Alimentos recuperados: " + alimentos.size());

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

        return alimentos;
    }

    /**
     * Actualiza los datos de un alimento existente en PRODUCTOS y ALIMENTOS.
     *
     * @param con      Conexion activa con la base de datos
     * @param alimento Objeto Alimento con los datos actualizados (debe tener ID
     *                 valido)
     * @return true si la actualizacion fue exitosa; false si fallo
     */
    public boolean actualizarAlimento(Connection con, Alimento alimento) {
        PreparedStatement psprod = null;
        PreparedStatement psali = null;
        boolean exitoso = false;
        int filasProductos;
        int filasAlimento;

        try {
            con.setAutoCommit(false);

            psprod = con.prepareStatement(
                    "UPDATE PRODUCTOS " +
                            "SET nombre = ?, descripcion = ?, unidad_medida = ?, precio = ?, categoria = ? " +
                            "WHERE id_producto = ? AND tipo = 'ALIMENTO'");
            psprod.setString(1, alimento.getNombre());
            psprod.setString(2, alimento.getDescripcion());
            psprod.setString(3, alimento.getUnidadMedida());
            psprod.setDouble(4, alimento.getPrecio());
            psprod.setString(5, alimento.getCategoria());
            psprod.setInt(6, alimento.getIdProducto());
            filasProductos = psprod.executeUpdate();

            psali = con.prepareStatement(
                    "UPDATE ALIMENTOS " +
                            "SET calorias = ?, tipo_dieta = ?, necesita_refrigeracion = ?, " +
                            "    temperatura_min = ?, temperatura_max = ? " +
                            "WHERE id_producto = ?");
            psali.setInt(1, alimento.getCalorias());
            psali.setString(2, alimento.getTipoDieta());
            psali.setBoolean(3, alimento.isNecesitaRefrigeracion());
            psali.setDouble(4, alimento.getTemperaturaMin());
            psali.setDouble(5, alimento.getTemperaturaMax());
            psali.setInt(6, alimento.getIdProducto());
            filasAlimento = psali.executeUpdate();

            if (filasProductos > 0 && filasAlimento > 0) {
                con.commit();
                exitoso = true;
                System.out.println("  Alimento actualizado -> ID: " + alimento.getIdProducto());
            } else {
                con.rollback();
                System.err.println("  Alimento ID " + alimento.getIdProducto() + " no encontrado.");
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
                if (psali != null)
                    psali.close();
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
     * Elimina un alimento de la base de datos (CASCADE elimina tambien en
     * ALIMENTOS).
     *
     * @param con        Conexion activa con la base de datos
     * @param idProducto Identificador del alimento a eliminar
     * @return true si se elimino correctamente; false si no se encontro o hubo
     *         error
     */
    public boolean eliminarAlimento(Connection con, int idProducto) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasEliminadas;

        try {
            ps = con.prepareStatement(
                    "DELETE FROM PRODUCTOS WHERE id_producto = ? AND tipo = 'ALIMENTO'");
            ps.setInt(1, idProducto);
            filasEliminadas = ps.executeUpdate();

            if (filasEliminadas > 0) {
                exitoso = true;
                System.out.println("  Alimento eliminado -> ID: " + idProducto);
            } else {
                System.err.println("  Alimento ID " + idProducto + " no encontrado.");
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
     * Inserta un nuevo lote de alimentos en las tablas LOTE y LOTE_ALIMENTOS.
     *
     * @param con  Conexion activa con la base de datos
     * @param lote Objeto LoteAlimentos con los datos del lote a insertar
     * @return ID del lote generado, o -1 si fallo
     */
    public int insertarLoteAlimentos(Connection con, LoteAlimentos lote) {
        PreparedStatement pslote = null;
        PreparedStatement psloteali = null;
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

            psloteali = con.prepareStatement(
                    "INSERT INTO LOTE_ALIMENTOS (id_lote, temperatura_control, humedad_control, codigo_almacen) " +
                            "VALUES (?, ?, ?, ?)");
            psloteali.setInt(1, idGenerado);
            psloteali.setDouble(2, lote.getTemperaturaControl());
            psloteali.setDouble(3, lote.getHumedadControl());
            psloteali.setString(4, lote.getCodigoAlmacen());
            psloteali.executeUpdate();

            con.commit();
            System.out.println("  Lote alimento insertado -> ID: " + idGenerado);

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
                if (psloteali != null)
                    psloteali.close();
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
     * Recupera todos los lotes de alimentos con estado ACTIVO.
     * Se usa para mostrar al usuario las opciones disponibles
     * antes de elegir cual asignar a una patrulla.
     *
     * @param con Conexion activa con la base de datos
     * @return Lista de LoteAlimentos activos, vacia si no hay ninguno o si fallo
     */
    public List<LoteAlimentos> listarLotesAlimentosActivos(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<LoteAlimentos> lotes = new ArrayList<>();
        Date fechaCad;
        LoteAlimentos lote;

        try {
            ps = con.prepareStatement(
                    "SELECT l.id_lote, l.id_producto, l.cantidad, l.fecha_entrada, " +
                            "       l.fecha_caducidad, l.estado, " +
                            "       la.temperatura_control, la.humedad_control, la.codigo_almacen " +
                            "FROM LOTE l " +
                            "INNER JOIN LOTE_ALIMENTOS la ON l.id_lote = la.id_lote " +
                            "WHERE l.estado = 'ACTIVO' " +
                            "ORDER BY l.id_lote ASC");
            rs = ps.executeQuery();

            while (rs.next()) {
                fechaCad = rs.getDate("fecha_caducidad");
                lote = new LoteAlimentos(
                        rs.getInt("id_lote"),
                        rs.getInt("id_producto"),
                        rs.getInt("cantidad"),
                        rs.getDate("fecha_entrada").toLocalDate(),
                        fechaCad != null ? fechaCad.toLocalDate() : null,
                        rs.getString("estado"),
                        rs.getDouble("temperatura_control"),
                        rs.getDouble("humedad_control"),
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
        System.out.println("  " + producto.generarAlertaStock(cantidad, 50));
        System.out.println("  ------------------------------------------------");
    }

}
