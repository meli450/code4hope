package Entidad;

import DAO.GestionAlimentosDAO;
import DAO.GestionMedicamentosDAO;

import java.io.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Clase de gestion de ficheros para el modulo de Alimentos y Medicamentos
 * del sistema Code4Hope.
 *
 * Permite exportar e importar productos (Alimentos, Medicamentos) en formato
 * CSV, y generar informes HTML de lotes (LoteAlimentos, LoteMedicamentos).
 *
 * Sigue el mismo patron que GestorFicheros: metodos estaticos, la conexion
 * se recibe como parametro en cada metodo, y se usa un ArrayList como capa
 * intermedia entre la BD y el fichero (obtenido a traves del DAO).
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class GestorFicherosAlimMed {

    private static final String SEP = ";";

    // =========================================================================
    // 1. EXPORTAR ALIMENTOS A CSV
    // =========================================================================

    // Lee todos los alimentos de la BD (mediante el DAO, que devuelve un
    // ArrayList) y los escribe en un fichero CSV con una linea de cabecera
    // y una linea por alimento.
    // Puedes dar un nombre de fichero o pulsar Enter para usar el nombre
    // por defecto: alimentos.csv
    public static void exportarAlimentosCSV(Scanner sc, Connection con,
            GestionAlimentosDAO dao) {
        System.out.println("\n Exportar alimentos a CSV ");

        List<Alimento> alimentos = dao.obtenerTodosAlimentos(con);
        if (alimentos.isEmpty()) {
            System.out.println("No hay alimentos en la base de datos.");
        } else {
            System.out.print("Fichero (Enter = alimentos.csv): ");
            String fichero = sc.nextLine().trim();
            if (fichero.isEmpty()) {
                fichero = "alimentos.csv";
            }

            try {
                escribirAlimentosCSV(fichero, alimentos);
                System.out.println("Exportados " + alimentos.size() + " alimentos en: " + fichero);
            } catch (Exception e) {
                System.out.println("Error al exportar: " + e.getMessage());
            }
        }
    }

    // Escribe la cabecera y una fila por cada Alimento del ArrayList.
    private static void escribirAlimentosCSV(String fichero,
            List<Alimento> alimentos) throws Exception {
        PrintWriter pw = null;
        int i;
        Alimento a;
        try {
            pw = new PrintWriter(new FileWriter(fichero));
            pw.println("id_producto" + SEP + "nombre" + SEP + "descripcion" + SEP +
                    "unidad_medida" + SEP + "precio" + SEP + "categoria" + SEP +
                    "calorias" + SEP + "tipo_dieta" + SEP +
                    "necesita_refrigeracion" + SEP +
                    "temperatura_min" + SEP + "temperatura_max");
            i = 0;
            while (i < alimentos.size()) {
                a = alimentos.get(i);
                pw.println(
                        a.getIdProducto() + SEP +
                        a.getNombre() + SEP +
                        a.getDescripcion() + SEP +
                        a.getUnidadMedida() + SEP +
                        a.getPrecio() + SEP +
                        a.getCategoria() + SEP +
                        a.getCalorias() + SEP +
                        a.getTipoDieta() + SEP +
                        a.isNecesitaRefrigeracion() + SEP +
                        a.getTemperaturaMin() + SEP +
                        a.getTemperaturaMax());
                i++;
            }
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    // =========================================================================
    // 2. IMPORTAR ALIMENTOS DESDE CSV
    // =========================================================================

    // Lee el fichero CSV de alimentos (el mismo formato que genera la exportacion)
    // e inserta en la BD cada linea valida. Informa de cuantos se importaron
    // y cuantos fallaron.
    // El id_producto de la primera columna se ignora: la BD asigna un ID nuevo.
    public static void importarAlimentosCSV(Scanner sc, Connection con,
            GestionAlimentosDAO dao) {
        System.out.println("\n Importar alimentos desde CSV ");
        System.out.print("Fichero (Enter = alimentos.csv): ");
        String fichero = sc.nextLine().trim();
        if (fichero.isEmpty()) {
            fichero = "alimentos.csv";
        }

        System.out.println("Leyendo fichero: " + fichero);
        leerAlimentosCSV(con, fichero, dao);
    }

    // Lee el CSV linea a linea (saltando la cabecera) y crea un Alimento
    // por cada fila valida, insertandolo en la BD mediante el DAO.
    private static void leerAlimentosCSV(Connection con, String fichero,
            GestionAlimentosDAO dao) {
        BufferedReader br = null;
        int importados = 0;
        int errores = 0;
        String linea;
        String[] c;
        Alimento a;

        try {
            br = new BufferedReader(new FileReader(fichero));
            br.readLine(); // cabecera, se descarta
            linea = br.readLine();
            while (linea != null) {
                c = linea.split(SEP, -1);
                if (c.length >= 11) {
                    try {
                        // Se usa idProducto=0 porque la BD asignara el ID automatico
                        a = new Alimento(
                                0,
                                c[1].trim(),
                                c[2].trim(),
                                c[3].trim(),
                                Double.parseDouble(c[4].trim()),
                                c[5].trim(),
                                Integer.parseInt(c[6].trim()),
                                c[7].trim(),
                                Boolean.parseBoolean(c[8].trim()),
                                Double.parseDouble(c[9].trim()),
                                Double.parseDouble(c[10].trim()));
                        dao.insertarAlimento(con, a);
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

    // =========================================================================
    // 3. EXPORTAR MEDICAMENTOS A CSV
    // =========================================================================

    // Lee todos los medicamentos de la BD (mediante el DAO, que devuelve un
    // ArrayList) y los escribe en un fichero CSV con una linea de cabecera
    // y una linea por medicamento.
    // Puedes dar un nombre de fichero o pulsar Enter para usar el nombre
    // por defecto: medicamentos.csv
    public static void exportarMedicamentosCSV(Scanner sc, Connection con,
            GestionMedicamentosDAO dao) {
        System.out.println("\n Exportar medicamentos a CSV ");

        List<Medicamento> medicamentos = dao.obtenerTodosMedicamentos(con);
        if (medicamentos.isEmpty()) {
            System.out.println("No hay medicamentos en la base de datos.");
        } else {
            System.out.print("Fichero (Enter = medicamentos.csv): ");
            String fichero = sc.nextLine().trim();
            if (fichero.isEmpty()) {
                fichero = "medicamentos.csv";
            }

            try {
                escribirMedicamentosCSV(fichero, medicamentos);
                System.out.println("Exportados " + medicamentos.size() + " medicamentos en: " + fichero);
            } catch (Exception e) {
                System.out.println("Error al exportar: " + e.getMessage());
            }
        }
    }

    // Escribe la cabecera y una fila por cada Medicamento del ArrayList.
    private static void escribirMedicamentosCSV(String fichero,
            List<Medicamento> medicamentos) throws Exception {
        PrintWriter pw = null;
        int i;
        Medicamento m;
        try {
            pw = new PrintWriter(new FileWriter(fichero));
            pw.println("id_producto" + SEP + "nombre" + SEP + "descripcion" + SEP +
                    "unidad_medida" + SEP + "precio" + SEP + "categoria" + SEP +
                    "principio_activo" + SEP + "dosis" + SEP +
                    "via_administracion" + SEP + "necesita_receta" + SEP +
                    "temperatura_almacenamiento");
            i = 0;
            while (i < medicamentos.size()) {
                m = medicamentos.get(i);
                pw.println(
                        m.getIdProducto() + SEP +
                        m.getNombre() + SEP +
                        m.getDescripcion() + SEP +
                        m.getUnidadMedida() + SEP +
                        m.getPrecio() + SEP +
                        m.getCategoria() + SEP +
                        m.getPrincipioActivo() + SEP +
                        m.getDosis() + SEP +
                        m.getViaAdministracion() + SEP +
                        m.isNecesitaReceta() + SEP +
                        m.getTemperaturaAlmacenamiento());
                i++;
            }
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    // =========================================================================
    // 4. IMPORTAR MEDICAMENTOS DESDE CSV
    // =========================================================================

    // Lee el fichero CSV de medicamentos (el mismo formato que genera la exportacion)
    // e inserta en la BD cada linea valida. Informa de cuantos se importaron
    // y cuantos fallaron.
    // El id_producto de la primera columna se ignora: la BD asigna un ID nuevo.
    public static void importarMedicamentosCSV(Scanner sc, Connection con,
            GestionMedicamentosDAO dao) {
        System.out.println("\n Importar medicamentos desde CSV ");
        System.out.print("Fichero (Enter = medicamentos.csv): ");
        String fichero = sc.nextLine().trim();
        if (fichero.isEmpty()) {
            fichero = "medicamentos.csv";
        }

        System.out.println("Leyendo fichero: " + fichero);
        leerMedicamentosCSV(con, fichero, dao);
    }

    // Lee el CSV linea a linea (saltando la cabecera) y crea un Medicamento
    // por cada fila valida, insertandolo en la BD mediante el DAO.
    private static void leerMedicamentosCSV(Connection con, String fichero,
            GestionMedicamentosDAO dao) {
        BufferedReader br = null;
        int importados = 0;
        int errores = 0;
        String linea;
        String[] c;
        Medicamento m;

        try {
            br = new BufferedReader(new FileReader(fichero));
            br.readLine(); // cabecera, se descarta
            linea = br.readLine();
            while (linea != null) {
                c = linea.split(SEP, -1);
                if (c.length >= 11) {
                    try {
                        // Se usa idProducto=0 porque la BD asignara el ID automatico
                        m = new Medicamento(
                                0,
                                c[1].trim(),
                                c[2].trim(),
                                c[3].trim(),
                                Double.parseDouble(c[4].trim()),
                                c[5].trim(),
                                c[6].trim(),
                                c[7].trim(),
                                c[8].trim(),
                                Boolean.parseBoolean(c[9].trim()),
                                Double.parseDouble(c[10].trim()));
                        dao.insertarMedicamento(con, m);
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

    // =========================================================================
    // 5. INFORME DE LOTES DE ALIMENTOS EN HTML
    // =========================================================================

    // Genera un informe HTML con todos los lotes de alimentos activos.
    // Muestra una tabla con el estado de cada lote y marca visualmente
    // los lotes caducados (rojo), proximos a caducar en menos de 30 dias
    // (naranja) y en buen estado (verde).
    // Puedes dar un nombre de fichero o pulsar Enter para usar el nombre
    // por defecto: informe_lotes_alimentos_{fecha}.html
    public static void generarInformeLotesAlimentosHTML(Scanner sc, Connection con,
            GestionAlimentosDAO dao) {
        System.out.println("\n Generar informe de lotes de alimentos (HTML) ");

        List<LoteAlimentos> lotes = dao.listarLotesAlimentosActivos(con);

        String fecha = LocalDate.now().toString();
        System.out.print("Fichero (Enter = informe_lotes_alimentos_" + fecha + ".html): ");
        String fichero = sc.nextLine().trim();
        if (fichero.isEmpty()) {
            fichero = "informe_lotes_alimentos_" + fecha + ".html";
        }

        try {
            escribirLotesAlimentosHTML(fichero, lotes, fecha);
            System.out.println("Informe guardado en: " + fichero + " (" + lotes.size() + " lotes)");
        } catch (Exception e) {
            System.out.println("Error al generar informe: " + e.getMessage());
        }
    }

    // Escribe el HTML con una tabla de lotes de alimentos.
    // Aplica color de fila segun los dias para caducar:
    // rojo (<= 0 dias), naranja (< 30 dias), verde (>= 30 dias).
    private static void escribirLotesAlimentosHTML(String fichero,
            List<LoteAlimentos> lotes, String fecha) throws Exception {
        PrintWriter pw = null;
        int i;
        LoteAlimentos l;
        long dias;
        String colorFila;

        try {
            pw = new PrintWriter(new FileWriter(fichero));
            pw.println("<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\">");
            pw.println("<title>Informe Lotes Alimentos - " + fecha + "</title>");
            pw.println("<style>");
            pw.println("body{font-family:sans-serif;padding:20px;}");
            pw.println("h1{color:#333;}");
            pw.println("table{border-collapse:collapse;width:100%;margin-bottom:20px;}");
            pw.println("th,td{border:1px solid #ccc;padding:6px 10px;text-align:left;}");
            pw.println("th{background:#ddd;}");
            pw.println(".ok{background:#d4edda;}");
            pw.println(".aviso{background:#fff3cd;}");
            pw.println(".caducado{background:#f8d7da;}");
            pw.println("</style></head><body>");

            pw.println("<h1>Informe de Lotes de Alimentos</h1>");
            pw.println("<p><b>Fecha generacion:</b> " + fecha + "</p>");
            pw.println("<p><b>Lotes activos:</b> " + lotes.size() + "</p>");

            if (lotes.isEmpty()) {
                pw.println("<p>No hay lotes de alimentos activos.</p>");
            } else {
                pw.println("<table>");
                pw.println("<tr><th>ID Lote</th><th>ID Producto</th><th>Cantidad</th>" +
                        "<th>Fecha Entrada</th><th>Fecha Caducidad</th>" +
                        "<th>Dias para Caducar</th><th>Estado</th>" +
                        "<th>Temp. Control (C)</th><th>Humedad (%)</th>" +
                        "<th>Almacen</th></tr>");
                i = 0;
                while (i < lotes.size()) {
                    l = lotes.get(i);
                    dias = l.diasParaCaducar();

                    if (dias <= 0) {
                        colorFila = "caducado";
                    } else if (dias < 30) {
                        colorFila = "aviso";
                    } else {
                        colorFila = "ok";
                    }

                    pw.println("<tr class=\"" + colorFila + "\">" +
                            "<td>" + l.getIdLote() + "</td>" +
                            "<td>" + l.getIdProducto() + "</td>" +
                            "<td>" + l.getCantidad() + "</td>" +
                            "<td>" + l.getFechaEntrada() + "</td>" +
                            "<td>" + (l.getFechaCaducidad() != null ? l.getFechaCaducidad() : "-") + "</td>" +
                            "<td>" + (l.getFechaCaducidad() != null
                                    ? (dias <= 0 ? "CADUCADO" : dias + " dias") : "-") + "</td>" +
                            "<td>" + l.getEstado() + "</td>" +
                            "<td>" + l.getTemperaturaControl() + "</td>" +
                            "<td>" + l.getHumedadControl() + "</td>" +
                            "<td>" + (l.getCodigoAlmacen() != null ? l.getCodigoAlmacen() : "Sin asignar") + "</td>" +
                            "</tr>");
                    i++;
                }
                pw.println("</table>");
                pw.println("<p>" +
                        "<span style=\"background:#d4edda;padding:2px 8px;\">Verde</span>: &gt;= 30 dias &nbsp;" +
                        "<span style=\"background:#fff3cd;padding:2px 8px;\">Naranja</span>: &lt; 30 dias &nbsp;" +
                        "<span style=\"background:#f8d7da;padding:2px 8px;\">Rojo</span>: Caducado" +
                        "</p>");
            }

            pw.println("</body></html>");
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    // =========================================================================
    // 6. INFORME DE LOTES DE MEDICAMENTOS EN HTML
    // =========================================================================

    // Genera un informe HTML con todos los lotes de medicamentos activos.
    // Muestra una tabla con el estado de cada lote y marca visualmente
    // los lotes caducados (rojo), proximos a caducar en menos de 30 dias
    // (naranja) y en buen estado (verde).
    // Puedes dar un nombre de fichero o pulsar Enter para usar el nombre
    // por defecto: informe_lotes_medicamentos_{fecha}.html
    public static void generarInformeLotesMedicamentosHTML(Scanner sc, Connection con,
            GestionMedicamentosDAO dao) {
        System.out.println("\n Generar informe de lotes de medicamentos (HTML) ");

        List<LoteMedicamentos> lotes = dao.listarLotesMedicamentosActivos(con);

        String fecha = LocalDate.now().toString();
        System.out.print("Fichero (Enter = informe_lotes_medicamentos_" + fecha + ".html): ");
        String fichero = sc.nextLine().trim();
        if (fichero.isEmpty()) {
            fichero = "informe_lotes_medicamentos_" + fecha + ".html";
        }

        try {
            escribirLotesMedicamentosHTML(fichero, lotes, fecha);
            System.out.println("Informe guardado en: " + fichero + " (" + lotes.size() + " lotes)");
        } catch (Exception e) {
            System.out.println("Error al generar informe: " + e.getMessage());
        }
    }

    // Escribe el HTML con una tabla de lotes de medicamentos.
    // Aplica color de fila segun los dias para caducar:
    // rojo (<= 0 dias), naranja (< 30 dias), verde (>= 30 dias).
    private static void escribirLotesMedicamentosHTML(String fichero,
            List<LoteMedicamentos> lotes, String fecha) throws Exception {
        PrintWriter pw = null;
        int i;
        LoteMedicamentos l;
        long dias;
        String colorFila;

        try {
            pw = new PrintWriter(new FileWriter(fichero));
            pw.println("<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\">");
            pw.println("<title>Informe Lotes Medicamentos - " + fecha + "</title>");
            pw.println("<style>");
            pw.println("body{font-family:sans-serif;padding:20px;}");
            pw.println("h1{color:#333;}");
            pw.println("table{border-collapse:collapse;width:100%;margin-bottom:20px;}");
            pw.println("th,td{border:1px solid #ccc;padding:6px 10px;text-align:left;}");
            pw.println("th{background:#ddd;}");
            pw.println(".ok{background:#d4edda;}");
            pw.println(".aviso{background:#fff3cd;}");
            pw.println(".caducado{background:#f8d7da;}");
            pw.println("</style></head><body>");

            pw.println("<h1>Informe de Lotes de Medicamentos</h1>");
            pw.println("<p><b>Fecha generacion:</b> " + fecha + "</p>");
            pw.println("<p><b>Lotes activos:</b> " + lotes.size() + "</p>");

            if (lotes.isEmpty()) {
                pw.println("<p>No hay lotes de medicamentos activos.</p>");
            } else {
                pw.println("<table>");
                pw.println("<tr><th>ID Lote</th><th>ID Producto</th><th>Cantidad</th>" +
                        "<th>Fecha Entrada</th><th>Fecha Caducidad</th>" +
                        "<th>Dias para Caducar</th><th>Estado</th>" +
                        "<th>Lote Fabricante</th><th>Condiciones Almacenamiento</th>" +
                        "<th>Almacen</th></tr>");
                i = 0;
                while (i < lotes.size()) {
                    l = lotes.get(i);
                    dias = l.diasParaCaducar();

                    if (dias <= 0) {
                        colorFila = "caducado";
                    } else if (dias < 30) {
                        colorFila = "aviso";
                    } else {
                        colorFila = "ok";
                    }

                    pw.println("<tr class=\"" + colorFila + "\">" +
                            "<td>" + l.getIdLote() + "</td>" +
                            "<td>" + l.getIdProducto() + "</td>" +
                            "<td>" + l.getCantidad() + "</td>" +
                            "<td>" + l.getFechaEntrada() + "</td>" +
                            "<td>" + (l.getFechaCaducidad() != null ? l.getFechaCaducidad() : "-") + "</td>" +
                            "<td>" + (l.getFechaCaducidad() != null
                                    ? (dias <= 0 ? "CADUCADO" : dias + " dias") : "-") + "</td>" +
                            "<td>" + l.getEstado() + "</td>" +
                            "<td>" + (l.getNumeroLoteFabricante() != null ? l.getNumeroLoteFabricante() : "-") + "</td>" +
                            "<td>" + (l.getCondicionesAlmacenamiento() != null ? l.getCondicionesAlmacenamiento() : "-") + "</td>" +
                            "<td>" + (l.getCodigoAlmacen() != null ? l.getCodigoAlmacen() : "Sin asignar") + "</td>" +
                            "</tr>");
                    i++;
                }
                pw.println("</table>");
                pw.println("<p>" +
                        "<span style=\"background:#d4edda;padding:2px 8px;\">Verde</span>: &gt;= 30 dias &nbsp;" +
                        "<span style=\"background:#fff3cd;padding:2px 8px;\">Naranja</span>: &lt; 30 dias &nbsp;" +
                        "<span style=\"background:#f8d7da;padding:2px 8px;\">Rojo</span>: Caducado" +
                        "</p>");
            }

            pw.println("</body></html>");
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}
