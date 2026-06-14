package DAO;

import Entidad.Almacen;
import Entidad.AlmacenAlimentos;
import Entidad.AlmacenMedicamentos;
import Entidad.CamaraRefrigeracion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para la gestion de almacenes del sistema Code4Hope.
 * Implementa las operaciones CRUD sobre las tablas:
 * ALMACEN, ALMACEN_ALIMENTOS, ALMACEN_MEDICAMENTOS y CAMARA_REFRIGERACION.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class AlmacenDAO {

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
     * Construye un objeto CamaraRefrigeracion a partir de la fila actual del
     * ResultSet.
     * Devuelve null si la columna cam_codigo es NULL (almacen sin camara).
     *
     * @param rs ResultSet posicionado en la fila a leer
     * @return CamaraRefrigeracion leida de la BD, o null si no hay camara
     * @throws SQLException si falla la lectura de alguna columna
     */
    private CamaraRefrigeracion leerCamara(ResultSet rs) throws SQLException {
        CamaraRefrigeracion camara = null;

        String camCodigo = rs.getString("cam_codigo");
        if (camCodigo != null) {
            camara = new CamaraRefrigeracion(
                    camCodigo,
                    rs.getDouble("cam_capacidad"),
                    rs.getDouble("cam_tmin"),
                    rs.getDouble("cam_tmax"),
                    rs.getDouble("cam_tactual"),
                    rs.getBoolean("cam_activo"));
        }

        return camara;
    }

    /**
     * Inserta un nuevo AlmacenAlimentos en las tablas ALMACEN y ALMACEN_ALIMENTOS.
     * Si el almacen tiene camara de refrigeracion asociada, la inserta primero en
     * CAMARA_REFRIGERACION dentro de la misma transaccion.
     * Usa transaccion para garantizar la integridad referencial.
     *
     * @param con     Conexion activa con la base de datos
     * @param almacen Objeto AlmacenAlimentos a persistir
     * @return true si la insercion fue correcta; false si fallo
     */
    public boolean insertarAlmacenAlimentos(Connection con, AlmacenAlimentos almacen) {
        PreparedStatement psAlmacen = null;
        PreparedStatement psTipo = null;
        ResultSet rsKeys = null;
        boolean exitoso = false;
        CamaraRefrigeracionDAO daoCamera;
        String codigoCamara;
        daoCamera = new CamaraRefrigeracionDAO();

        try {
            con.setAutoCommit(false);

            if (almacen.getCamara() != null) {
                codigoCamara = daoCamera.insertarCamara(con, almacen.getCamara());
                if (codigoCamara == null) {
                    throw new SQLException("No se pudo insertar la camara de refrigeracion.");
                }
            }

            psAlmacen = con.prepareStatement(
                    "INSERT INTO ALMACEN (codigo, ubicacion, stock_minimo, stock_maximo, codigo_camara) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            psAlmacen.setString(1, almacen.getCodigo());
            psAlmacen.setString(2, almacen.getUbicacion());
            psAlmacen.setInt(3, almacen.getStockMinimo());
            psAlmacen.setInt(4, almacen.getStockMaximo());
            if (almacen.getCamara() != null) {
                psAlmacen.setString(5, almacen.getCamara().getCodigo());
            } else {
                psAlmacen.setNull(5, Types.VARCHAR);
            }
            psAlmacen.executeUpdate();

            rsKeys = psAlmacen.getGeneratedKeys();
            if (rsKeys.next()) {
                almacen.setIdAlmacen(rsKeys.getInt(1));
            }

            psTipo = con.prepareStatement(
                    "INSERT INTO ALMACEN_ALIMENTOS (codigo) VALUES (?)");
            psTipo.setString(1, almacen.getCodigo());
            psTipo.executeUpdate();

            con.commit();
            exitoso = true;
            System.out.println("  AlmacenAlimentos insertado -> ID: " + almacen.getIdAlmacen());

        } catch (SQLException ex) {
            try {
                if (con != null) {
                    con.rollback();
                    System.err.println("  Rollback: no se inserto el almacen de alimentos.");
                }
            } catch (SQLException eRollback) {
                printSQLException(eRollback);
            }
            printSQLException(ex);
        } finally {
            try {
                if (rsKeys != null)
                    rsKeys.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (psTipo != null)
                    psTipo.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (psAlmacen != null)
                    psAlmacen.close();
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
     * Inserta un nuevo AlmacenMedicamentos en las tablas ALMACEN y
     * ALMACEN_MEDICAMENTOS.
     * Si el almacen tiene camara de refrigeracion asociada, la inserta primero en
     * CAMARA_REFRIGERACION dentro de la misma transaccion.
     * Usa transaccion para garantizar la integridad referencial.
     *
     * @param con     Conexion activa con la base de datos
     * @param almacen Objeto AlmacenMedicamentos a persistir
     * @return true si la insercion fue correcta; false si fallo
     */
    public boolean insertarAlmacenMedicamentos(Connection con, AlmacenMedicamentos almacen) {
        PreparedStatement psAlmacen = null;
        PreparedStatement psTipo = null;
        ResultSet rsKeys = null;
        boolean exitoso = false;
        CamaraRefrigeracionDAO daoCamera;
        String codigoCamara;
        daoCamera = new CamaraRefrigeracionDAO();

        try {
            con.setAutoCommit(false);

            if (almacen.getCamara() != null) {
                codigoCamara = daoCamera.insertarCamara(con, almacen.getCamara());
                if (codigoCamara == null) {
                    throw new SQLException("No se pudo insertar la camara de refrigeracion.");
                }
            }

            psAlmacen = con.prepareStatement(
                    "INSERT INTO ALMACEN (codigo, ubicacion, stock_minimo, stock_maximo, codigo_camara) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            psAlmacen.setString(1, almacen.getCodigo());
            psAlmacen.setString(2, almacen.getUbicacion());
            psAlmacen.setInt(3, almacen.getStockMinimo());
            psAlmacen.setInt(4, almacen.getStockMaximo());
            if (almacen.getCamara() != null) {
                psAlmacen.setString(5, almacen.getCamara().getCodigo());
            } else {
                psAlmacen.setNull(5, Types.VARCHAR);
            }
            psAlmacen.executeUpdate();

            rsKeys = psAlmacen.getGeneratedKeys();
            if (rsKeys.next()) {
                almacen.setIdAlmacen(rsKeys.getInt(1));
            }

            psTipo = con.prepareStatement(
                    "INSERT INTO ALMACEN_MEDICAMENTOS (codigo) VALUES (?)");
            psTipo.setString(1, almacen.getCodigo());
            psTipo.executeUpdate();

            con.commit();
            exitoso = true;
            System.out.println("  AlmacenMedicamentos insertado -> ID: " + almacen.getIdAlmacen());

        } catch (SQLException ex) {
            try {
                if (con != null) {
                    con.rollback();
                    System.err.println("  Rollback: no se inserto el almacen de medicamentos.");
                }
            } catch (SQLException eRollback) {
                printSQLException(eRollback);
            }
            printSQLException(ex);
        } finally {
            try {
                if (rsKeys != null)
                    rsKeys.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (psTipo != null)
                    psTipo.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
            try {
                if (psAlmacen != null)
                    psAlmacen.close();
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
     * Recupera un AlmacenAlimentos por su codigo UUID.
     * Hace JOIN de ALMACEN con ALMACEN_ALIMENTOS y LEFT JOIN con
     * CAMARA_REFRIGERACION.
     * Si el almacen tiene camara registrada, la carga y la asigna al objeto.
     *
     * @param con    Conexion activa con la base de datos
     * @param codigo UUID del almacen a recuperar
     * @return AlmacenAlimentos con los datos de la BD, o null si no existe
     */
    public AlmacenAlimentos obtenerAlmacenAlimentos(Connection con, String codigo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        AlmacenAlimentos almacen = null;
        CamaraRefrigeracion camara;

        try {
            ps = con.prepareStatement(
                    "SELECT a.id_almacen, a.codigo, a.ubicacion, a.stock_minimo, a.stock_maximo, " +
                            "       cr.codigo AS cam_codigo, cr.capacidad AS cam_capacidad, " +
                            "       cr.temperatura_minima AS cam_tmin, cr.temperatura_maxima AS cam_tmax, " +
                            "       cr.temperatura_actual AS cam_tactual, cr.activo AS cam_activo " +
                            "FROM ALMACEN a " +
                            "INNER JOIN ALMACEN_ALIMENTOS aa ON a.codigo = aa.codigo " +
                            "LEFT JOIN CAMARA_REFRIGERACION cr ON a.codigo_camara = cr.codigo " +
                            "WHERE a.codigo = ?");
            ps.setString(1, codigo);
            rs = ps.executeQuery();

            if (rs.next()) {
                almacen = new AlmacenAlimentos(
                        rs.getString("ubicacion"),
                        rs.getInt("stock_minimo"),
                        rs.getInt("stock_maximo"));
                almacen.setCodigo(rs.getString("codigo"));
                almacen.setIdAlmacen(rs.getInt("id_almacen"));
                camara = leerCamara(rs);
                if (camara != null) {
                    almacen.almacenarCamara(camara);
                }
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

        return almacen;
    }

    /**
     * Recupera un AlmacenMedicamentos por su codigo UUID.
     * Hace JOIN de ALMACEN con ALMACEN_MEDICAMENTOS y LEFT JOIN con
     * CAMARA_REFRIGERACION.
     * Si el almacen tiene camara registrada, la carga y la asigna al objeto.
     *
     * @param con    Conexion activa con la base de datos
     * @param codigo UUID del almacen a recuperar
     * @return AlmacenMedicamentos con los datos de la BD, o null si no existe
     */
    public AlmacenMedicamentos obtenerAlmacenMedicamentos(Connection con, String codigo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        AlmacenMedicamentos almacen = null;
        CamaraRefrigeracion camara;

        try {
            ps = con.prepareStatement(
                    "SELECT a.id_almacen, a.codigo, a.ubicacion, a.stock_minimo, a.stock_maximo, " +
                            "       cr.codigo AS cam_codigo, cr.capacidad AS cam_capacidad, " +
                            "       cr.temperatura_minima AS cam_tmin, cr.temperatura_maxima AS cam_tmax, " +
                            "       cr.temperatura_actual AS cam_tactual, cr.activo AS cam_activo " +
                            "FROM ALMACEN a " +
                            "INNER JOIN ALMACEN_MEDICAMENTOS am ON a.codigo = am.codigo " +
                            "LEFT JOIN CAMARA_REFRIGERACION cr ON a.codigo_camara = cr.codigo " +
                            "WHERE a.codigo = ?");
            ps.setString(1, codigo);
            rs = ps.executeQuery();

            if (rs.next()) {
                almacen = new AlmacenMedicamentos(
                        rs.getString("ubicacion"),
                        rs.getInt("stock_minimo"),
                        rs.getInt("stock_maximo"));
                almacen.setCodigo(rs.getString("codigo"));
                almacen.setIdAlmacen(rs.getInt("id_almacen"));
                camara = leerCamara(rs);
                if (camara != null) {
                    almacen.almacenarCamara(camara);
                }
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

        return almacen;
    }

    /**
     * Recupera todos los almacenes de alimentos existentes en la base de datos.
     * Incluye la camara de refrigeracion de cada almacen si la tiene.
     * Util para presentar al usuario las opciones disponibles al insertar un lote
     * de alimentos.
     *
     * @param con Conexion activa con la base de datos
     * @return Lista de AlmacenAlimentos; vacia si no hay ninguno registrado
     */
    public List<AlmacenAlimentos> obtenerTodosAlmacenesAlimentos(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AlmacenAlimentos> almacenes = new ArrayList<>();
        AlmacenAlimentos aa;
        CamaraRefrigeracion camara;

        try {
            ps = con.prepareStatement(
                    "SELECT a.id_almacen, a.codigo, a.ubicacion, a.stock_minimo, a.stock_maximo, " +
                            "       cr.codigo AS cam_codigo, cr.capacidad AS cam_capacidad, " +
                            "       cr.temperatura_minima AS cam_tmin, cr.temperatura_maxima AS cam_tmax, " +
                            "       cr.temperatura_actual AS cam_tactual, cr.activo AS cam_activo " +
                            "FROM ALMACEN a " +
                            "INNER JOIN ALMACEN_ALIMENTOS aa ON a.codigo = aa.codigo " +
                            "LEFT JOIN CAMARA_REFRIGERACION cr ON a.codigo_camara = cr.codigo " +
                            "ORDER BY a.id_almacen");
            rs = ps.executeQuery();

            while (rs.next()) {
                aa = new AlmacenAlimentos(
                        rs.getString("ubicacion"),
                        rs.getInt("stock_minimo"),
                        rs.getInt("stock_maximo"));
                aa.setCodigo(rs.getString("codigo"));
                aa.setIdAlmacen(rs.getInt("id_almacen"));
                camara = leerCamara(rs);
                if (camara != null) {
                    aa.almacenarCamara(camara);
                }
                almacenes.add(aa);
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

        return almacenes;
    }

    /**
     * Recupera todos los almacenes de medicamentos existentes en la base de datos.
     * Incluye la camara de refrigeracion de cada almacen si la tiene.
     * Util para presentar al usuario las opciones disponibles al insertar un lote
     * de medicamentos.
     *
     * @param con Conexion activa con la base de datos
     * @return Lista de AlmacenMedicamentos; vacia si no hay ninguno registrado
     */
    public List<AlmacenMedicamentos> obtenerTodosAlmacenesMedicamentos(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AlmacenMedicamentos> almacenes = new ArrayList<>();
        AlmacenMedicamentos am;
        CamaraRefrigeracion camara;

        try {
            ps = con.prepareStatement(
                    "SELECT a.id_almacen, a.codigo, a.ubicacion, a.stock_minimo, a.stock_maximo, " +
                            "       cr.codigo AS cam_codigo, cr.capacidad AS cam_capacidad, " +
                            "       cr.temperatura_minima AS cam_tmin, cr.temperatura_maxima AS cam_tmax, " +
                            "       cr.temperatura_actual AS cam_tactual, cr.activo AS cam_activo " +
                            "FROM ALMACEN a " +
                            "INNER JOIN ALMACEN_MEDICAMENTOS am ON a.codigo = am.codigo " +
                            "LEFT JOIN CAMARA_REFRIGERACION cr ON a.codigo_camara = cr.codigo " +
                            "ORDER BY a.id_almacen");
            rs = ps.executeQuery();

            while (rs.next()) {
                am = new AlmacenMedicamentos(
                        rs.getString("ubicacion"),
                        rs.getInt("stock_minimo"),
                        rs.getInt("stock_maximo"));
                am.setCodigo(rs.getString("codigo"));
                am.setIdAlmacen(rs.getInt("id_almacen"));
                camara = leerCamara(rs);
                if (camara != null) {
                    am.almacenarCamara(camara);
                }
                almacenes.add(am);
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

        return almacenes;
    }

    /**
     * Recupera todos los almacenes de la base de datos, independientemente de su
     * tipo.
     * Determina si cada almacen es de alimentos o de medicamentos mediante LEFT
     * JOIN
     * con ambas tablas de subtipo. Incluye la camara de refrigeracion si la hay.
     *
     * @param con Conexion activa con la base de datos
     * @return Lista de Almacen (AlmacenAlimentos o AlmacenMedicamentos); vacia si
     *         no hay datos
     */
    public List<Almacen> obtenerTodosAlmacenes(Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Almacen> almacenes = new ArrayList<>();
        String tipo;
        Almacen almacen;
        AlmacenAlimentos aa;
        CamaraRefrigeracion camaraAA;
        AlmacenMedicamentos am;
        CamaraRefrigeracion camaraAM;

        try {
            ps = con.prepareStatement(
                    "SELECT a.id_almacen, a.codigo, a.ubicacion, a.stock_minimo, a.stock_maximo, " +
                            "       CASE " +
                            "         WHEN aa.codigo IS NOT NULL THEN 'ALIMENTOS' " +
                            "         WHEN am.codigo IS NOT NULL THEN 'MEDICAMENTOS' " +
                            "         ELSE 'DESCONOCIDO' " +
                            "       END AS tipo, " +
                            "       cr.codigo AS cam_codigo, cr.capacidad AS cam_capacidad, " +
                            "       cr.temperatura_minima AS cam_tmin, cr.temperatura_maxima AS cam_tmax, " +
                            "       cr.temperatura_actual AS cam_tactual, cr.activo AS cam_activo " +
                            "FROM ALMACEN a " +
                            "LEFT JOIN ALMACEN_ALIMENTOS    aa ON a.codigo = aa.codigo " +
                            "LEFT JOIN ALMACEN_MEDICAMENTOS am ON a.codigo = am.codigo " +
                            "LEFT JOIN CAMARA_REFRIGERACION cr ON a.codigo_camara = cr.codigo " +
                            "ORDER BY a.id_almacen");
            rs = ps.executeQuery();

            while (rs.next()) {
                tipo = rs.getString("tipo");

                if ("ALIMENTOS".equals(tipo)) {
                    aa = new AlmacenAlimentos(
                            rs.getString("ubicacion"),
                            rs.getInt("stock_minimo"),
                            rs.getInt("stock_maximo"));
                    aa.setCodigo(rs.getString("codigo"));
                    aa.setIdAlmacen(rs.getInt("id_almacen"));
                    camaraAA = leerCamara(rs);
                    if (camaraAA != null) {
                        aa.almacenarCamara(camaraAA);
                    }
                    almacen = aa;
                } else if ("MEDICAMENTOS".equals(tipo)) {
                    am = new AlmacenMedicamentos(
                            rs.getString("ubicacion"),
                            rs.getInt("stock_minimo"),
                            rs.getInt("stock_maximo"));
                    am.setCodigo(rs.getString("codigo"));
                    am.setIdAlmacen(rs.getInt("id_almacen"));
                    camaraAM = leerCamara(rs);
                    if (camaraAM != null) {
                        am.almacenarCamara(camaraAM);
                    }
                    almacen = am;
                } else {
                    almacen = null;
                }

                if (almacen != null) {
                    almacenes.add(almacen);
                }
            }
            System.out.println("  Almacenes recuperados: " + almacenes.size());

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

        return almacenes;
    }

    /**
     * Elimina un almacen de la base de datos por su ID numerico.
     * El ON DELETE CASCADE de ALMACEN_ALIMENTOS / ALMACEN_MEDICAMENTOS
     * se encarga de borrar el registro del subtipo automaticamente.
     *
     * @param con       Conexion activa con la base de datos
     * @param idAlmacen ID numerico del almacen a eliminar (AUTO_INCREMENT)
     * @return true si se elimino correctamente; false si no se encontro o hubo
     *         error
     */
    public boolean eliminarAlmacen(Connection con, int idAlmacen) {
        PreparedStatement ps = null;
        boolean exitoso = false;
        int filasEliminadas;

        try {
            ps = con.prepareStatement(
                    "DELETE FROM ALMACEN WHERE id_almacen = ?");
            ps.setInt(1, idAlmacen);
            filasEliminadas = ps.executeUpdate();

            if (filasEliminadas > 0) {
                exitoso = true;
                System.out.println("  Almacen eliminado -> ID: " + idAlmacen);
            } else {
                System.err.println("  Almacen con ID " + idAlmacen + " no encontrado.");
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
}
