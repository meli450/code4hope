package Entidad;

import DAO.*;

import java.io.*;
import java.sql.Connection;
import java.util.*;

public class GestorFicherosPatrullas {

    private static final String SEP = ";";

    // =========================================================================
    // 1. INFORME DE MISION EN HTML
    // =========================================================================

    // Le pides el ID de una patrulla y genera un fichero HTML con toda su
    // informacion
    // estado, vehiculo, ruta, tripulantes, puntos y comunicaciones.
    // Puedes escribir el nombre que quieras para el fichero, o pulsar Enter
    // para que use el nombre por defecto: informe_patrulla_(id).html
    public static void generarInformeMisionHTML(Scanner sc, Connection con,
            PatrullaDAO daoP, VehiculoDAO daoV, RutaDAO daoR,
            PuntoRutaDAO daoPR, TripulanteDAO daoT,
            EquipoComunicacionDAO daoE, RegistroComunicacionDAO daoRC) {
        System.out.println("\nGenerar informe de mision (HTML) ");
        System.out.print("ID de la patrulla: ");
        int id = -1;
        try {
            id = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID no valido.");
        }
        if (id <= 0) {
            return;
        }
        try {
            Patrulla p = daoP.findById(con, id);
            if (p == null) {
                System.out.println("Patrulla no encontrada.");
                return;
            }

            Vehiculo v = (p.getVehiculoId() > 0) ? daoV.findById(con, p.getVehiculoId()) : null;
            Ruta r = (p.getRutaId() > 0) ? daoR.findById(con, p.getRutaId()) : null;
            EquipoComunicacion e = (p.getEquipoComunicacionId() > 0) ? daoE.findById(con, p.getEquipoComunicacionId())
                    : null;
            List<Tripulante> tripulantes = daoT.findByPatrullaId(con, id);
            List<PuntoRuta> puntos = (r != null) ? daoPR.findByRutaId(con, r.getId()) : new ArrayList<>();
            List<RegistroComunicacion> comunicaciones = (e != null) ? daoRC.findByEquipoId(con, e.getId())
                    : new ArrayList<>();

            System.out.print("Fichero (Enter = informe_patrulla_" + id + ".html): ");
            String fichero = sc.nextLine().trim();
            if (fichero.isEmpty()) {
                fichero = "informe_patrulla_" + id + ".html";
            }

            escribirHTML(fichero, p, v, r, e, tripulantes, puntos, comunicaciones);
            System.out.println("Informe guardado en: " + fichero);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // Escribe el HTML completo. Genera una pagina sencilla con tablas
    // para cada seccion (tripulacion, puntos de ruta, comunicaciones).
    private static void escribirHTML(String fichero, Patrulla p, Vehiculo v, Ruta r,
            EquipoComunicacion e, List<Tripulante> tripulantes,
            List<PuntoRuta> puntos, List<RegistroComunicacion> comunicaciones) throws Exception {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(fichero));
            pw.println("<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\">");
            pw.println("<title>Informe Patrulla " + p.getCodigo() + "</title>");
            pw.println(
                    "<style>body{font-family:sans-serif;padding:20px;} table{border-collapse:collapse;width:100%;margin-bottom:20px;} th,td{border:1px solid #ccc;padding:6px 10px;} th{background:#ddd;}</style>");
            pw.println("</head><body>");

            pw.println("<h1>Informe de Mision - Patrulla " + p.getCodigo() + "</h1>");
            pw.println("<p><b>Estado:</b> " + p.getEstado().getValor() + "</p>");
            pw.println("<p><b>Vehiculo:</b> " + (v != null ? v.getCodigo() + " - " + v.getMatricula() : "Sin asignar")
                    + "</p>");
            pw.println("<p><b>Ruta:</b> "
                    + (r != null ? r.getNombre() + " (" + r.getEstado().getValor() + ")" : "Sin asignar") + "</p>");
            pw.println("<p><b>Equipo COM:</b> " + (e != null ? e.getNombre() : "Sin asignar") + "</p>");

            pw.println("<h2>Tripulacion</h2>");
            if (tripulantes.isEmpty()) {
                pw.println("<p>Sin tripulantes.</p>");
            } else {
                pw.println("<table><tr><th>NIF</th><th>Nombre</th><th>Rol</th><th>Estado</th></tr>");
                int i = 0;
                while (i < tripulantes.size()) {
                    Tripulante t = tripulantes.get(i);
                    pw.println("<tr><td>" + t.getNif() + "</td><td>" + t.getNombreCompleto()
                            + "</td><td>" + t.getRol().getValor() + "</td><td>" + t.getEstadoOperativo().getValor()
                            + "</td></tr>");
                    i++;
                }
                pw.println("</table>");
            }

            pw.println("<h2>Puntos de Ruta</h2>");
            if (puntos.isEmpty()) {
                pw.println("<p>Sin puntos.</p>");
            } else {
                pw.println(
                        "<table><tr><th>#</th><th>Nombre</th><th>Tipo</th><th>Estado</th><th>Hora estimada</th></tr>");
                int i = 0;
                while (i < puntos.size()) {
                    PuntoRuta pt = puntos.get(i);
                    pw.println("<tr><td>" + pt.getPosicion() + "</td><td>" + pt.getNombre()
                            + "</td><td>" + pt.getTipo().getValor() + "</td><td>" + pt.getEstado().getValor()
                            + "</td><td>" + (pt.getHoraEstimada() != null ? pt.getHoraEstimada() : "-") + "</td></tr>");
                    i++;
                }
                pw.println("</table>");
            }

            pw.println("<h2>Comunicaciones</h2>");
            if (comunicaciones.isEmpty()) {
                pw.println("<p>Sin comunicaciones.</p>");
            } else {
                pw.println("<table><tr><th>Hora</th><th>Tipo</th><th>Emisor</th><th>Mensaje</th></tr>");
                int i = 0;
                while (i < comunicaciones.size()) {
                    RegistroComunicacion rc = comunicaciones.get(i);
                    pw.println("<tr><td>" + rc.getHora() + "</td><td>" + rc.getTipo().getValor()
                            + "</td><td>" + rc.getEmisor() + "</td><td>" + rc.getMensaje() + "</td></tr>");
                    i++;
                }
                pw.println("</table>");
            }

            pw.println("</body></html>");
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    // =========================================================================
    // 2. EXPORTAR COMUNICACIONES A CSV
    // =========================================================================

    // Le pides el ID del equipo de comunicacion y escribe todas sus comunicaciones
    // en un fichero CSV con columnas: hora, tipo, mensaje, emisor.
    // Puedes dar un nombre de fichero o pulsar Enter para usar el nombre
    // por defecto: comunicaciones_equipo_(id).csv
    public static void exportarComunicacionesCSV(Scanner sc, Connection con,
            EquipoComunicacionDAO daoE, RegistroComunicacionDAO daoRC) {
        System.out.println("\nExportar comunicaciones a CSV ");
        System.out.print("ID del equipo: ");
        int id = -1;
        try {
            id = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID no valido.");
        }
        if (id <= 0) {
            return;
        }
        try {
            EquipoComunicacion equipo = daoE.findById(con, id);
            if (equipo == null) {
                System.out.println("Equipo no encontrado.");
                return;
            }

            List<RegistroComunicacion> lista = daoRC.findByEquipoId(con, id);
            System.out.print("Fichero (Enter = comunicaciones_equipo_" + id + ".csv): ");
            String fichero = sc.nextLine().trim();
            if (fichero.isEmpty()) {
                fichero = "comunicaciones_equipo_" + id + ".csv";
            }

            escribirCSV(fichero, lista);
            System.out.println("Exportados " + lista.size() + " registros en: " + fichero);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Escribe el CSV con una linea de cabecera y una linea por registro.
    // SEP es la constante separador en caso de querer cambiarlo
    private static void escribirCSV(String fichero, List<RegistroComunicacion> lista) throws Exception {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(fichero));
            pw.println("hora" + SEP + "tipo" + SEP + "mensaje" + SEP + "emisor");
            int i = 0;
            while (i < lista.size()) {
                RegistroComunicacion r = lista.get(i);
                pw.println(r.getHora() + SEP + r.getTipo().getValor() + SEP + r.getMensaje() + SEP + r.getEmisor());
                i++;
            }
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    // =========================================================================
    // 3. IMPORTAR COMUNICACIONES DESDE CSV
    // =========================================================================

    // Le pides el ID del equipo destino y lee automaticamente el fichero
    // comunicaciones_equipo_(id).csv (el mismo nombre que genera la exportacion).
    // Inserta en la BD cada linea valida del CSV e informa de cuantas se importaron
    // y cuantas fallaron.
    public static void importarComunicacionesCSV(Scanner sc, Connection con,
            EquipoComunicacionDAO daoE, RegistroComunicacionDAO daoRC) {
        System.out.println("\nImportar comunicaciones desde CSV ");
        System.out.print("ID del equipo destino: ");
        int id = -1;
        try {
            id = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID no valido.");
        }
        if (id <= 0) {
            return;
        }
        try {
            EquipoComunicacion equipo = daoE.findById(con, id);
            if (equipo == null) {
                System.out.println("Equipo no encontrado.");
                return;
            }

            String fichero = "comunicaciones_equipo_" + id + ".csv";
            System.out.println("Leyendo fichero: " + fichero);
            leerCSV(con, id, fichero, daoRC);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Lee el CSV linea a linea (saltando la cabecera) e inserta cada registro en BD.
    private static void leerCSV(Connection con, int equipoId, String fichero,
            RegistroComunicacionDAO daoRC) {
        BufferedReader br = null;
        int importados = 0;
        int errores = 0;
        try {
            br = new BufferedReader(new FileReader(fichero));
            br.readLine(); // cabecera, se descarta
            String linea = br.readLine();
            while (linea != null) {
                String[] c = linea.split(SEP, -1);
                if (c.length >= 4) {
                    try {
                        RegistroComunicacion r = new RegistroComunicacion();
                        r.setEquipoId(equipoId);
                        r.setHora(c[0].trim());
                        r.setTipo(RegistroComunicacion.TipoMensaje.fromString(c[1].trim()));
                        r.setMensaje(c[2].trim());
                        r.setEmisor(c[3].trim());
                        daoRC.insertar(con, r);
                        importados++;
                    } catch (Exception e) {
                        errores++;
                    }
                } else {
                    errores++;
                }
                linea = br.readLine();
            }
            System.out.println("Importados: " + importados + " | Errores: " + errores);
        } catch (FileNotFoundException e) {
            System.out.println("Fichero no encontrado: " + fichero);
        } catch (Exception e) {
            System.out.println("Error al leer: " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
