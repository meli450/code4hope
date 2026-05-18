/*Subsistema Talleres de formación
 *Realizado por: Melisa
 * EncuestaDAO */
package DAO;

import Entidad.Encuesta;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para las operaciones CRUD de la entidad Encuesta sobre la bd.
 * Gestiona tambien la importacion de respuestas desde CSV.
 */
public class EncuestaDAO {

    /**
     * Inserta una nueva encuesta en la bd.
     *
     * @param con      conexion a la bd
     * @param encuesta objeto Encuesta con los datos a insertar
     * @return identificador generado o -1 si fallo la insercion
     */
    public int insertarEncuesta(Connection con, Encuesta encuesta) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idGenerado = -1;

        try {
            ps = con.prepareStatement(
                    "INSERT INTO encuesta (titulo, enlace, informe, codt) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, encuesta.getTitulo());
            ps.setString(2, encuesta.getEnlace());
            ps.setString(3, encuesta.getInforme());
            ps.setInt(4, encuesta.getCodTaller());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                encuesta.setCod(idGenerado);
            }
            System.out.println("  Encuesta registrada con ID: " + idGenerado);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return idGenerado;
    }

    /**
     * Obtiene una encuesta a partir de su codigo.
     *
     * @param con conexion a la bd
     * @param cod codigo identificador de la encuesta
     * @return objeto Encuesta o null si no existe
     */
    public Encuesta obtenerEncuesta(Connection con, int cod) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Encuesta encuesta = null;

        try {
            ps = con.prepareStatement(
                    "SELECT cod, titulo, enlace, informe, codt FROM encuesta WHERE cod = ?");
            ps.setInt(1, cod);
            rs = ps.executeQuery();

            if (rs.next()) {
                encuesta = new Encuesta(
                        rs.getString("titulo"),
                        rs.getString("enlace"),
                        rs.getInt("codt"));
                encuesta.setCod(rs.getInt("cod"));
                encuesta.setInforme(rs.getString("informe"));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return encuesta;
    }

    /**
     * Obtiene todas las encuestas asociadas a un taller.
     *
     * @param con       conexion a la bd
     * @param codTaller codigo del taller
     * @return lista de encuestas del taller
     */
    public List<Encuesta> obtenerEncuestasDeTaller(Connection con, int codTaller) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Encuesta> encuestas = new ArrayList<>();

        try {
            ps = con.prepareStatement(
                    "SELECT cod, titulo, enlace, informe, codt "
                    + "FROM encuesta WHERE codt = ? ORDER BY cod");
            ps.setInt(1, codTaller);
            rs = ps.executeQuery();

            while (rs.next()) {
                Encuesta e = new Encuesta(
                        rs.getString("titulo"),
                        rs.getString("enlace"),
                        rs.getInt("codt"));
                e.setCod(rs.getInt("cod"));
                e.setInforme(rs.getString("informe"));
                encuestas.add(e);
            }
            System.out.println(" Encuestas del taller " + codTaller + ": " + encuestas.size());

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return encuestas;
    }

    /**
     * Guarda el informe estadistico en la encuesta indicada.
     *
     * @param con         conexion a la bd
     * @param codEncuesta codigo de la encuesta
     * @param informe     contenido del informe estadistico
     * @return true si el informe se guardo correctamente
     */
    public boolean guardarInforme(Connection con, int codEncuesta, String informe) {
        PreparedStatement ps = null;
        boolean exitoso = false;

        try {
            ps = con.prepareStatement("UPDATE encuesta SET informe = ? WHERE cod = ?");
            ps.setString(1, informe);
            ps.setInt(2, codEncuesta);

            if (ps.executeUpdate()> 0) {
                exitoso = true;
                System.out.println("  Informe guardado con la ID de Encuesta: " + codEncuesta);
            } else {
                System.out.println("  Encuesta con ID " + codEncuesta + " no encontrada.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return exitoso;
    }

    /**
     * Lee un fichero csv y devuelve todas las filas como lista de arrays de cadenas
     *
     * @param rutaArchivo ruta al fichero csv exportado de Google Forms
     * @return lista de filas donde la primera es la cabecera
     */
    public List<String[]> leerCSV(String rutaArchivo) {
        List<String[]> filas = new ArrayList<>();
        BufferedReader br = null;
        String linea;

        try {
            br = new BufferedReader(new FileReader(rutaArchivo));
            linea = br.readLine();
            while (linea != null) {
                filas.add(linea.split(","));
                linea = br.readLine();
            }
            System.out.println("  Respuestas leidas del CSV: " + (filas.size() - 1));

        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar el fichero: " + e.getMessage());
                }
            }
        }
        return filas;
    }
}
