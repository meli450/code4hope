package DAO;

import Entidad.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.*;

/**
 * DAO de operaciones de consola del subsistema de talleres.
 * Agrupa los metodos utilitarios de lectura de entrada y todas las
 * operaciones CRUD sobre talleres, participantes, recursos, monitores y encuestas.
 * 
 * @author Melisa
 * 
 */
public class Subsistema_TalleresDAO {

    /**
     * Lee un entero desde consola. Devuelve -1 si la entrada no es valida.
     *
     * @param sc Scanner activo
     * @return entero introducido o -1 si el formato es incorrecto
     */
    public static int leerOpcion(Scanner sc) {
        int opcion = -1;
        try {
            opcion = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  Entrada no valida. Introduzca un numero.");
        }
        return opcion;
    }

    /**
     * Lee un entero desde consola. Repite hasta obtener una entrada valida.
     *
     * @param sc Scanner activo
     * @return entero leido
     */
    public static int leerEntero(Scanner sc) {
        int valor = 0;
        boolean valido = false;
        while (!valido) {
            try {
                valor = Integer.parseInt(sc.nextLine().trim());
                valido = true;
            } catch (NumberFormatException e) {
                System.out.print("  Entrada no valida. Introduzca un numero entero: ");
            }
        }
        return valor;
    }

    /**
     * Lee una cadena de texto desde consola mostrando un prompt.
     *
     * @param sc     Scanner activo
     * @param prompt texto que se muestra antes de leer
     * @return cadena introducida sin espacios al inicio ni al final
     */
    public static String leerTexto(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    /**
     * Lee un texto opcional: si el usuario pulsa Enter se mantiene el valor actual.
     *
     * @param sc          Scanner activo
     * @param prompt      etiqueta del campo
     * @param valorActual valor que se conserva si la entrada esta vacia
     * @return nuevo valor introducido o valorActual si el usuario no escribe nada
     */
    public static String leerTextoOpcional(Scanner sc, String prompt, String valorActual) {
        System.out.print("  " + prompt + " [" + valorActual + "]: ");
        String entrada = sc.nextLine().trim();
        String resultado = valorActual;
        if (!entrada.isEmpty()) {
            resultado = entrada;
        }
        return resultado;
    }

    /**
     * Lee una fecha en formato yyyy-MM-dd, permitiendo entrada vacia para fechas opcionales.
     *
     * @param sc     Scanner activo
     * @param prompt etiqueta del campo
     * @return fecha en formato yyyy-MM-dd o cadena vacia si el usuario omite el valor
     */
    public static String leerFecha(Scanner sc, String prompt) {
        String fecha = "";
        boolean valida = false;
        while (!valida) {
            System.out.print("  " + prompt + " (yyyy-MM-dd): ");
            fecha = sc.nextLine().trim();
            if (fecha.isEmpty() || (fecha.length() == 10 && fecha.charAt(4) == '-' && fecha.charAt(7) == '-')) {
                valida = true;
            } else {
                System.out.println("  Formato incorrecto. Use yyyy-MM-dd o pulse Enter para omitir.");
            }
        }
        return fecha;
    }

    /**
     * Muestra los perfiles disponibles y devuelve el seleccionado por el usuario.
     *
     * @param sc Scanner activo
     * @return perfil elegido o TODOS si la seleccion no es valida
     */
    public static PerfilEnum seleccionarPerfil(Scanner sc) {
        System.out.println("  Perfiles:");
        PerfilEnum[] perfiles = PerfilEnum.values();
        for (int i = 0; i < perfiles.length; i++) {
            System.out.println("  " + (i + 1) + ". " + perfiles[i].name());
        }
        System.out.print("  Seleccione perfil (1-" + perfiles.length + "): ");
        int op = leerOpcion(sc);
        PerfilEnum resultado = PerfilEnum.TODOS;
        if (op >= 1 && op <= perfiles.length) {
            resultado = perfiles[op - 1];
        }
        return resultado;
    }

    /**
     * Muestra los estados de recurso disponibles y devuelve el seleccionado por el usuario.
     *
     * @param sc Scanner activo
     * @return estado elegido o DISPONIBLE si la seleccion no es valida
     */
    public static EstadoRecursoEnum seleccionarEstadoRecurso(Scanner sc) {
        System.out.println("  Estados:");
        EstadoRecursoEnum[] estados = EstadoRecursoEnum.values();
        for (int i = 0; i < estados.length; i++) {
            System.out.println("  " + (i + 1) + ". " + estados[i].name());
        }
        System.out.print("  Seleccione estado (1-" + estados.length + "): ");
        int op = leerOpcion(sc);
        EstadoRecursoEnum resultado = EstadoRecursoEnum.DISPONIBLE;
        if (op >= 1 && op <= estados.length) {
            resultado = estados[op - 1];
        }
        return resultado;
    }


    /**
     * Solicita los datos por consola y crea un nuevo taller en la bd.
     *
     * @param sc         Scanner activo
     * @param con        conexion a la bd
     * @param dao        DAO de talleres
     * @param daoMonitor DAO de monitores
     */
    public static void opCrearTaller(Scanner sc, Connection con,
            TallerDAO dao, MonitorDAO daoMonitor) {
        System.out.println("\n  --- CREAR TALLER ---");
        opListarMonitores(con, daoMonitor);
        String titulo       = leerTexto(sc, "  Titulo: ");
        String descripcion  = leerTexto(sc, "  Descripcion: ");
        PerfilEnum perfil   = seleccionarPerfil(sc);
        String etiqueta     = leerTexto(sc, "  Etiqueta: ");
        String espacio      = leerTexto(sc, "  Espacio: ");
        System.out.print("  Aforo maximo: ");
        int aforo           = leerEntero(sc);
        String fechaInicio  = leerFecha(sc, "Fecha inicio");
        String nif          = leerTexto(sc, "  NIF monitor (Enter para ninguno): ");

        Taller taller = new Taller(titulo, descripcion, perfil, etiqueta, espacio, aforo, fechaInicio, nif);
        int id = dao.insertarTaller(con, taller);
        if (id > 0) {
            System.out.println("  Taller creado con ID: " + id);
        } else {
            System.out.println("  Error al crear el taller.");
        }
    }

    /**
     * Muestra los datos actuales del taller y permite modificarlos campo a campo.
     *
     * @param sc         Scanner activo
     * @param con        conexion a la bd
     * @param dao        DAO de talleres
     * @param daoMonitor DAO de monitores
     * @param codTaller  codigo del taller a modificar
     */
    public static void opModificarDatosTaller(Scanner sc, Connection con,
            TallerDAO dao, MonitorDAO daoMonitor, int codTaller) {
        System.out.println("\n  --- MODIFICAR DATOS DEL TALLER ---");
        Taller taller = dao.obtenerTaller(con, codTaller);

        if (taller == null) {
            System.out.println("  Taller no encontrado.");
        } else {
            opListarMonitores(con, daoMonitor);
            taller.setTitulo(leerTextoOpcional(sc, "Titulo", taller.getTitulo()));
            taller.setDescripcion(leerTextoOpcional(sc, "Descripcion", taller.getDescripcion()));
            taller.setEtiqueta(leerTextoOpcional(sc, "Etiqueta", taller.getEtiqueta()));
            taller.setEspacio(leerTextoOpcional(sc, "Espacio", taller.getEspacio()));

            System.out.print("  Cambiar perfil destinatario? (s/n): ");
            if (sc.nextLine().trim().equalsIgnoreCase("s")) {
                taller.setPerfilRequerido(seleccionarPerfil(sc));
            }

            System.out.print("  Aforo maximo [" + taller.getAforoMaximo() + "]: ");
            String aforoStr = sc.nextLine().trim();
            if (!aforoStr.isEmpty()) {
                try {
                    taller.setAforoMaximo(Integer.parseInt(aforoStr));
                } catch (NumberFormatException e) {
                    System.out.println("  Valor no valido. Se mantiene el aforo actual.");
                }
            }

            taller.setFechaInicio(leerTextoOpcional(sc, "Fecha inicio", taller.getFechaInicio() != null ? taller.getFechaInicio() : ""));
            taller.setFechaFin(leerTextoOpcional(sc, "Fecha fin", taller.getFechaFin() != null ? taller.getFechaFin() : ""));
            taller.setNif(leerTextoOpcional(sc, "NIF monitor", taller.getNif() != null ? taller.getNif() : ""));

            boolean ok = dao.actualizarTaller(con, taller);
            if (ok) {
                System.out.println("  Taller actualizado correctamente.");
            } else {
                System.out.println("  Error al actualizar el taller.");
            }
        }
    }

    /**
     * Elimina un taller de la bd tras mostrar sus datos y solicitar confirmacion al usuario.
     * Muestra el titulo y estado del taller antes de pedir confirmacion para evitar eliminaciones accidentales.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de talleres
     */
    public static void opEliminarTaller(Scanner sc, Connection con, TallerDAO dao) {
        System.out.println("\n  --- ELIMINAR TALLER ---");
        opListarTalleres(con, dao);
        System.out.print("  ID del taller a eliminar: ");
        int cod = leerEntero(sc);
        Taller taller = dao.obtenerTaller(con, cod);
        if (taller == null) {
            System.out.println("  Taller no encontrado.");
            return;
        }
        System.out.println("  Taller seleccionado: " + taller.getTitulo() + " | Estado: " + taller.getEstado().name());
        System.out.print("  Confirmar eliminacion (s/n): ");
        String confirma = sc.nextLine().trim();

        if (confirma.equalsIgnoreCase("s")) {
            boolean ok = dao.eliminarTaller(con, cod);
            if (ok) {
                System.out.println("  Taller eliminado correctamente.");
            } else {
                System.out.println("  Error al eliminar el taller.");
            }
        } else {
            System.out.println("  Operacion cancelada.");
        }
    }

    /**
     * Genera un fichero HTML con el listado completo de talleres.
     *
     * @param con        conexion a la bd
     * @param dao        DAO de talleres
     * @param daoMonitor DAO de monitores para mostrar el nombre del monitor asignado
     */
    public static void opExportarTalleresHTML(Connection con, TallerDAO dao, MonitorDAO daoMonitor) {
        List<Taller> talleres = dao.obtenerTodosTalleres(con);
        String nombreFichero = "talleres.html";
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(nombreFichero));
            pw.println("<html>");
            pw.println("<head> <meta charset='utf-8'> <title>Talleres Code4Hope</title> </head>");
            pw.println("<body>");
            pw.println("<h1>Listado de Talleres</h1>");
            pw.println("<table border='1'>");
            pw.println("<tr><th>ID</th> <th>Titulo</th> <th>Perfil</th> <th>Aforo</th> <th>Estado</th> <th>Monitor/a</th> </tr>");
            for (int i = 0; i < talleres.size(); i++) {
                Taller t = talleres.get(i);
                String nombreMonitor = "-";
                if (t.getNif() != null && !t.getNif().isEmpty()) {
                    Monitor m = daoMonitor.obtenerMonitor(con, t.getNif());
                    if (m != null) {
                        nombreMonitor = m.getNombreCompleto();
                    }
                }
                pw.println("<tr> <td>" + t.getCod() + "</td> <td>" + t.getTitulo() + "</td> <td>"
                        + t.getPerfilRequerido().name() + "</td> <td>" + t.getAforoMaximo()
                        + "</td> <td>" + t.getEstado().name() + "</td> <td>" + nombreMonitor + "</td> </tr>");
            }
            pw.println("</table>");
            pw.println("</body> </html>");
            System.out.println("  Fichero generado: " + nombreFichero);
        } catch (IOException e) {
            System.out.println("  Error al generar el fichero: " + e.getMessage());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    /**
     * Muestra en consola la lista de todos los talleres con sus plazas ocupadas.
     *
     * @param con conexion a la bd
     * @param dao DAO de talleres
     */
    public static void opListarTalleres(Connection con, TallerDAO dao) {
        List<Taller> talleres = dao.obtenerTodosTalleres(con);
        if (talleres.isEmpty()) {
            System.out.println("  No hay talleres registrados.");
        } else {
            System.out.println("\n ID |             Titulo               |       Perfil         | Plazas  | Estado   ");
            System.out.println("  ----|----------------------------------|----------------------|---------|----------");
            for (int i = 0; i < talleres.size(); i++) {
                Taller t = talleres.get(i);
                int ocupadas = dao.contarParticipantes(con, t.getCod());
                System.out.printf("  %-4d| %-33s| %-21s| %2d/%-4d | %s%n",
                        t.getCod(), t.getTitulo(), t.getPerfilRequerido().name(),
                        ocupadas, t.getAforoMaximo(), t.getEstado().name());
            }
        }
    }

    /**
     * Cancela un taller solicitando el motivo y actualizando la bd con la fecha actual.
     * Solo permite cancelar talleres en estado ACTIVO.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de talleres
     */
    public static void opCancelarTaller(Scanner sc, Connection con, TallerDAO dao) {
        System.out.println("\n  --- CANCELAR TALLER ---");
        opListarTalleres(con, dao);
        System.out.print("  ID del taller a cancelar: ");
        int cod = leerEntero(sc);
        Taller taller = dao.obtenerTaller(con, cod);

        if (taller == null) {
            System.out.println("  Taller no encontrado.");
        } else if (taller.getEstado() == EstadoTallerEnum.CANCELADO) {
            System.out.println("  El taller ya esta cancelado.");
        } else if (taller.getEstado() == EstadoTallerEnum.FINALIZADO) {
            System.out.println("  No se puede cancelar un taller ya finalizado.");
        } else {
            String incidencia = leerTexto(sc, "  Motivo de cancelacion: ");
            boolean ok = dao.cancelarTaller(con, cod, incidencia);
            if (ok) {
                System.out.println("  Taller cancelado correctamente.");
            } else {
                System.out.println("  Error al cancelar el taller.");
            }
        }
    }

    /**
     * Finaliza un taller activo solicitando confirmacion al usuario.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de talleres
     */
    public static void opFinalizarTaller(Scanner sc, Connection con, TallerDAO dao) {
        System.out.println("\n  --- FINALIZAR TALLER ---");
        opListarTalleres(con, dao);
        System.out.print("  ID del taller a finalizar: ");
        int cod = leerEntero(sc);
        Taller taller = dao.obtenerTaller(con, cod);

        if (taller == null) {
            System.out.println("  Taller no encontrado.");
        } else if (taller.getEstado() != EstadoTallerEnum.ACTIVO) {
            System.out.println("  El taller no esta activo. Estado actual: " + taller.getEstado().name());
        } else {
            System.out.print("  Confirmar finalizacion (s/n): ");
            String confirma = sc.nextLine().trim();
            if (confirma.equalsIgnoreCase("s")) {
                String fechaFin = (taller.getFechaFin() != null && !taller.getFechaFin().isEmpty())
                        ? taller.getFechaFin()
                        : java.time.LocalDate.now().toString();
                boolean ok = dao.finalizarTaller(con, cod, fechaFin);
                if (ok) {
                    System.out.println("  Taller finalizado correctamente.");
                } else {
                    System.out.println("  Error al finalizar el taller.");
                }
            } else {
                System.out.println("  Operacion cancelada.");
            }
        }
    }

    /**
     * Muestra los talleres filtrados por el estado seleccionado por el usuario.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de talleres
     */
    public static void opFiltrarTalleresPorEstado(Scanner sc, Connection con, TallerDAO dao) {
        System.out.println("\n  --- FILTRAR TALLERES POR ESTADO ---");
        System.out.println("  Estados:");
        EstadoTallerEnum[] estados = EstadoTallerEnum.values();
        for (int i = 0; i < estados.length; i++) {
            System.out.println("  " + (i + 1) + ". " + estados[i].name());
        }
        System.out.print("  Seleccione estado (1-" + estados.length + "): ");
        int op = leerOpcion(sc);
        EstadoTallerEnum estadoFiltro = EstadoTallerEnum.ACTIVO;
        if (op >= 1 && op <= estados.length) {
            estadoFiltro = estados[op - 1];
        }

        List<Taller> talleres = dao.obtenerTalleresPorEstado(con, estadoFiltro);
        if (talleres.isEmpty()) {
            System.out.println("  No hay talleres con estado: " + estadoFiltro.name());
        } else {
            System.out.println("\n  ID  | Titulo                           | Perfil               | Plazas  | Fecha inicio");
            System.out.println("  ----|----------------------------------|----------------------|---------|------------");
            for (int i = 0; i < talleres.size(); i++) {
                Taller t = talleres.get(i);
                int ocupadas = dao.contarParticipantes(con, t.getCod());
                System.out.printf("  %-4d| %-33s| %-21s| %2d/%-4d | %s%n",
                        t.getCod(), t.getTitulo(), t.getPerfilRequerido().name(),
                        ocupadas, t.getAforoMaximo(),
                        t.getFechaInicio() != null ? t.getFechaInicio() : "-");
            }
        }
    }

    /**
     * Inscribe un participante en un taller con validaciones de estado, aforo y perfil.
     *
     * @param sc        Scanner activo
     * @param con       conexion a la bd
     * @param dao       DAO de talleres
     * @param daoP      DAO de participantes
     * @param codTaller codigo del taller en el que inscribir
     */
    public static void opInscribirParticipante(Scanner sc, Connection con,
            TallerDAO dao, ParticipanteDAO daoP, int codTaller) {
        System.out.println("\n  --- INSCRIBIR PARTICIPANTE EN TALLER ---");
        Taller taller = dao.obtenerTaller(con, codTaller);

        if (taller == null) {
            System.out.println("  Taller no encontrado.");
        } else if (taller.getEstado() != EstadoTallerEnum.ACTIVO) {
            System.out.println("  El taller no esta activo. Estado actual: " + taller.getEstado().name());
        } else {
            int ocupadas = dao.contarParticipantes(con, codTaller);
            if (ocupadas >= taller.getAforoMaximo()) {
                System.out.println("  Aforo completo (" + ocupadas + "/" + taller.getAforoMaximo() + "). No se pueden inscribir mas participantes.");
            } else {
                opListarParticipantes(con, daoP);
                System.out.print("  ID del participante: ");
                int idParticipante = leerEntero(sc);
                Participante participante = daoP.obtenerParticipante(con, idParticipante);

                if (participante == null) {
                    System.out.println("  Participante no encontrado.");
                } else {
                    PerfilEnum perfilTaller = taller.getPerfilRequerido();
                    boolean perfilCompatible = (perfilTaller == PerfilEnum.TODOS)
                            || (perfilTaller == participante.getPerfil());
                    if (!perfilCompatible) {
                        System.out.println("  El perfil del participante (" + participante.getPerfil().name()
                                + ") no coincide con el del taller (" + perfilTaller.name() + ").");
                    } else {
                        String fechaInicio = leerFecha(sc, "Fecha inicio participacion");
                        String fechaFin    = leerFecha(sc, "Fecha fin participacion (opcional, Enter para omitir)");
                        boolean ok = dao.inscribirParticipante(con, codTaller, idParticipante, fechaInicio, fechaFin);
                        if (ok) {
                            System.out.println("  Participante inscrito correctamente. Plazas: " + (ocupadas + 1) + "/" + taller.getAforoMaximo());
                        } else {
                            System.out.println("  Error al inscribir el participante.");
                        }
                    }
                }
            }
        }
    }

    /**
     * Da de baja a un participante de un taller concreto.
     *
     * @param sc        Scanner activo
     * @param con       conexion a la bd
     * @param dao       DAO de talleres
     * @param codTaller codigo del taller del que se da de baja al participante
     */
    public static void opDarDeBajaParticipanteTaller(Scanner sc, Connection con, TallerDAO dao, int codTaller) {
        System.out.println("\n  --- DAR DE BAJA PARTICIPANTE DE TALLER ---");
        List<Participante> participantes = dao.obtenerParticipantesDeTaller(con, codTaller);
        if (participantes.isEmpty()) {
            System.out.println("  No hay participantes inscritos en este taller.");
        } else {
            for (int i = 0; i < participantes.size(); i++) {
                Participante p = participantes.get(i);
                System.out.println("  ID: " + p.getId() + " | " + p.getNombreCompleto());
            }
            System.out.print("  ID del participante a dar de baja: ");
            int idParticipante = leerEntero(sc);
            boolean ok = dao.darDeBajaParticipanteEnTaller(con, codTaller, idParticipante);
            if (ok) {
                System.out.println("  Participante dado de baja del taller.");
            } else {
                System.out.println("  Error al dar de baja al participante.");
            }
        }
    }

    /**
     * Muestra la lista de participantes inscritos en un taller.
     *
     * @param con       conexion a la bd
     * @param dao       DAO de talleres
     * @param codTaller codigo del taller cuyos participantes se muestran
     */
    public static void opVerParticipantesTaller(Connection con, TallerDAO dao, int codTaller) {
        System.out.println("\n  --- PARTICIPANTES DEL TALLER ---");
        List<Participante> participantes = dao.obtenerParticipantesDeTaller(con, codTaller);
        if (participantes.isEmpty()) {
            System.out.println("  No hay participantes inscritos.");
        } else {
            System.out.println("\n  ID  | Nombre                    | Perfil");
            System.out.println("  ----|---------------------------|------------------");
            for (int i = 0; i < participantes.size(); i++) {
                Participante p = participantes.get(i);
                System.out.printf("  %-4d| %-26s| %s%n",
                        p.getId(), p.getNombreCompleto(), p.getPerfil().name());
            }
        }
    }

    /**
     * Asigna un recurso disponible a un taller con fechas de inicio y fin opcionales.
     * Solo permite asignar recursos a talleres en estado ACTIVO.
     * Tras la asignacion, actualiza el estado del recurso a EN_USO.
     *
     * @param sc        Scanner activo
     * @param con       conexion a la bd
     * @param dao       DAO de talleres
     * @param daoR      DAO de recursos
     * @param codTaller codigo del taller al que se asigna el recurso
     */
    public static void opAsignarRecursoTaller(Scanner sc, Connection con,
            TallerDAO dao, RecursoDAO daoR, int codTaller) {
        System.out.println("\n  --- ASIGNAR RECURSO AL TALLER ---");
        Taller taller = dao.obtenerTaller(con, codTaller);
        if (taller == null) {
            System.out.println("  Taller no encontrado.");
        } else if (taller.getEstado() != EstadoTallerEnum.ACTIVO) {
            System.out.println("  No se puede asignar un recurso a un taller que no esta activo. Estado actual: " + taller.getEstado().name());
        } else {
            opListarRecursos(con, daoR);
            System.out.print("  ID del recurso: ");
            int idRecurso = leerEntero(sc);
            Recurso recurso = daoR.obtenerRecurso(con, idRecurso);
            if (recurso == null) {
                System.out.println("  Recurso no encontrado.");
            } else if (recurso.getEstado() != EstadoRecursoEnum.DISPONIBLE) {
                System.out.println("  El recurso no esta disponible. Estado actual: " + recurso.getEstado().name());
            } else {
                String fechaInicio = leerFecha(sc, "Fecha inicio uso");
                String fechaFin    = leerFecha(sc, "Fecha fin uso (opcional, Enter para omitir)");
                boolean ok = dao.asignarRecurso(con, codTaller, idRecurso, fechaInicio, fechaFin);
                if (ok) {
                    daoR.modificarEstado(con, idRecurso, EstadoRecursoEnum.EN_USO);
                    System.out.println("  Recurso asignado al taller correctamente.");
                } else {
                    System.out.println("  Error al asignar el recurso.");
                }
            }
        }
    }

    /**
     * Elimina la asignacion de un recurso de un taller.
     * Tras la eliminacion, actualiza el estado del recurso a DISPONIBLE.
     *
     * @param sc        Scanner activo
     * @param con       conexion a la bd
     * @param dao       DAO de talleres
     * @param daoR      DAO de recursos
     * @param codTaller codigo del taller del que se elimina el recurso
     */
    public static void opEliminarRecursoTaller(Scanner sc, Connection con, TallerDAO dao, RecursoDAO daoR, int codTaller) {
        System.out.println("\n  --- ELIMINAR RECURSO DEL TALLER ---");
        List<Recurso> recursos = dao.obtenerRecursosDeTaller(con, codTaller);
        if (recursos.isEmpty()) {
            System.out.println("  No hay recursos asignados a este taller.");
        } else {
            for (int i = 0; i < recursos.size(); i++) {
                Recurso r = recursos.get(i);
                System.out.println("  ID: " + r.getId() + " | " + r.getTipo());
            }
            System.out.print("  ID del recurso a eliminar: ");
            int idRecurso = leerEntero(sc);
            boolean ok = dao.eliminarRecursoDeTaller(con, codTaller, idRecurso);
            if (ok) {
                daoR.modificarEstado(con, idRecurso, EstadoRecursoEnum.DISPONIBLE);
                System.out.println("  Recurso eliminado del taller.");
            } else {
                System.out.println("  Error al eliminar el recurso.");
            }
        }
    }

    /**
     * Muestra los recursos asignados a un taller.
     *
     * @param con       conexion a la bd
     * @param dao       DAO de talleres
     * @param codTaller codigo del taller cuyos recursos se muestran
     */
    public static void opVerRecursosTaller(Connection con, TallerDAO dao, int codTaller) {
        System.out.println("\n  --- RECURSOS DEL TALLER ---");
        List<Recurso> recursos = dao.obtenerRecursosDeTaller(con, codTaller);
        if (recursos.isEmpty()) {
            System.out.println("  No hay recursos asignados a este taller.");
        } else {
            System.out.println("\n  ID  | Tipo                      | Cantidad | Estado");
            System.out.println("  ----|---------------------------|----------|--------------------");
            for (int i = 0; i < recursos.size(); i++) {
                Recurso r = recursos.get(i);
                System.out.printf("  %-4d| %-26s| %-9d| %s%n",
                        r.getId(), r.getTipo(), r.getCantidad(), r.getEstado().name());
            }
        }
    }

    /**
     * Registra una nueva encuesta vinculando el enlace de Google Forms al taller.
     *
     * @param sc        Scanner activo
     * @param con       conexion a la bd
     * @param dao       DAO de encuestas
     * @param codTaller codigo del taller al que se asocia la encuesta
     */
    public static void opRegistrarEncuesta(Scanner sc, Connection con, EncuestaDAO dao, int codTaller) {
        System.out.println("\n  --- REGISTRAR ENCUESTA ---");
        String titulo = leerTexto(sc, "  Titulo de la encuesta: ");
        String enlace = leerTexto(sc, "  Enlace Google Forms: ");

        Encuesta encuesta = new Encuesta(titulo, enlace, codTaller);
        int id = dao.insertarEncuesta(con, encuesta);
        if (id > 0) {
            System.out.println("  Encuesta registrada con ID: " + id);
        } else {
            System.out.println("  Error al registrar la encuesta.");
        }
    }

    /**
     * Muestra el titulo y enlace de una encuesta seleccionada por el usuario.
     *
     * @param sc        Scanner activo
     * @param con       conexion a la bd
     * @param dao       DAO de encuestas
     * @param codTaller codigo del taller cuyas encuestas se listan para seleccionar
     */
    public static void opMostrarEnlaceEncuesta(Scanner sc, Connection con, EncuestaDAO dao, int codTaller) {
        System.out.println("\n  --- MOSTRAR ENLACE DE ENCUESTA ---");
        List<Encuesta> encuestas = dao.obtenerEncuestasDeTaller(con, codTaller);
        if (encuestas.isEmpty()) {
            System.out.println("  No hay encuestas para este taller.");
        } else {
            for (int i = 0; i < encuestas.size(); i++) {
                Encuesta e = encuestas.get(i);
                System.out.println("  ID: " + e.getCod() + " | " + e.getTitulo());
            }
            System.out.print("  ID de la encuesta: ");
            int codEncuesta = leerEntero(sc);
            Encuesta encuesta = dao.obtenerEncuesta(con, codEncuesta);
            if (encuesta == null) {
                System.out.println("  Encuesta no encontrada.");
            } else {
                System.out.println("  Titulo: " + encuesta.getTitulo());
                System.out.println("  Enlace: " + encuesta.getEnlace());
            }
        }
    }

    /**
     * Lee el CSV exportado de Google Forms, genera el informe estadistico y lo guarda en la bd.
     * Muestra primero las encuestas del taller para que el usuario seleccione una.
     *
     * @param sc        Scanner activo
     * @param con       conexion a la bd
     * @param dao       DAO de encuestas
     * @param codTaller codigo del taller cuyas encuestas se muestran
     */
    public static void opImportarYGenerarInforme(Scanner sc, Connection con, EncuestaDAO dao, int codTaller) {
        System.out.println("\n  --- IMPORTAR RESPUESTAS Y GENERAR INFORME ---");
        List<Encuesta> encuestas = dao.obtenerEncuestasDeTaller(con, codTaller);
        if (encuestas.isEmpty()) {
            System.out.println("  No hay encuestas para este taller.");
            return;
        }
        for (int i = 0; i < encuestas.size(); i++) {
            Encuesta e = encuestas.get(i);
            System.out.println("  ID: " + e.getCod() + " | " + e.getTitulo());
        }
        System.out.print("  ID de la encuesta: ");
        int codEncuesta = leerEntero(sc);
        String rutaCSV = leerTexto(sc, "  Ruta del fichero CSV: ");

        List<String[]> filas = dao.leerCSV(rutaCSV);

        if (filas.size() > 1) {
            String informe = procesarCSV(filas);
            boolean ok = dao.guardarInforme(con, codEncuesta, informe);
            if (ok) {
                System.out.println("\n  === INFORME ESTADISTICO ===");
                System.out.println(informe);
                System.out.println("  Informe guardado en la BD correctamente.");
                guardarInformeEnFichero(informe, codEncuesta);
            } else {
                System.out.println("  Error al guardar el informe.");
            }
        } else {
            System.out.println("  El fichero CSV no contiene respuestas.");
        }
    }

    /**
     * Guarda el informe estadistico en un fichero de texto.
     *
     * @param informe     contenido del informe
     * @param codEncuesta identificador de la encuesta usado en el nombre del fichero
     */
    private static void guardarInformeEnFichero(String informe, int codEncuesta) {
        String nombreFichero = "informe_encuesta_" + codEncuesta + ".txt";
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(nombreFichero));
            pw.println(informe);
            System.out.println("  Informe exportado a: " + nombreFichero);
        } catch (IOException e) {
            System.out.println("  Error al guardar el fichero: " + e.getMessage());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    /**
     * Genera un resumen estadistico a partir de las filas del csv con conteos y % por pregunta
     *
     * @param filas lista de filas del CSV donde la primera fila contiene las cabeceras
     * @return cadena con el informe estadistico formateado
     */
    private static String procesarCSV(List<String[]> filas) {
        StringBuilder sb = new StringBuilder();
        String[] cabeceras = filas.get(0);
        int totalRespuestas = filas.size() - 1;

        sb.append("Total de respuestas: ").append(totalRespuestas).append("\n");
        sb.append("--------------------------------------------\n");

        for (int col = 1; col < cabeceras.length; col++) {
            sb.append("\nPregunta: ").append(cabeceras[col]).append("\n");

            List<String> valores = new ArrayList<>();
            List<Integer> conteos = new ArrayList<>();

            for (int fila = 1; fila < filas.size(); fila++) {
                if (col < filas.get(fila).length) {
                    String valor = filas.get(fila)[col].trim().replace("\"", "");
                    int pos = valores.indexOf(valor);
                    if (pos >= 0) {
                        conteos.set(pos, conteos.get(pos) + 1);
                    } else {
                        valores.add(valor);
                        conteos.add(1);
                    }
                }
            }
            for (int i = 0; i < valores.size(); i++) {
                double porcentaje = (conteos.get(i) * 100.0) / totalRespuestas;
                sb.append("  ").append(valores.get(i)).append(": ")
                        .append(conteos.get(i)).append(" (")
                        .append(String.format("%.0f", porcentaje)).append("%)\n");
            }
        }
        return sb.toString();
    }

    /**
     * Muestra las encuestas de un taller indicando si tienen informe generado.
     *
     * @param con       conexion a la bd
     * @param dao       DAO de encuestas
     * @param codTaller codigo del taller cuyas encuestas se listan
     */
    public static void opVerEncuestasTaller(Connection con, EncuestaDAO dao, int codTaller) {
        System.out.println("\n  --- ENCUESTAS DEL TALLER ---");
        List<Encuesta> encuestas = dao.obtenerEncuestasDeTaller(con, codTaller);

        if (encuestas.isEmpty()) {
            System.out.println("  No hay encuestas para este taller.");
        } else {
            for (int i = 0; i < encuestas.size(); i++) {
                Encuesta e = encuestas.get(i);
                String estadoInforme = (e.getInforme() != null && !e.getInforme().isEmpty())
                        ? "[Con informe]" : "[Sin informe]";
                System.out.println("  ID: " + e.getCod() + " | " + e.getTitulo()
                        + " | " + estadoInforme);
            }
        }
    }

    /**
     * Solicita los datos por consola y crea un nuevo participante en la bd.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de participantes
     */
    public static void opCrearParticipante(Scanner sc, Connection con, ParticipanteDAO dao) {
        System.out.println("\n  --- CREAR PARTICIPANTE ---");
        String nombre   = leerTexto(sc, "  Nombre: ");
        String apellido = leerTexto(sc, "  Apellido: ");
        String genero   = leerTexto(sc, "  Genero: ");
        System.out.print("  Edad: ");
        int edad        = leerEntero(sc);
        PerfilEnum perfil = seleccionarPerfil(sc);

        Participante p = new Participante(nombre, apellido, genero, edad, perfil);
        int id = dao.insertarParticipante(con, p);
        if (id > 0) {
            System.out.println("  Participante creado con ID: " + id);
        } else {
            System.out.println("  Error al crear el participante.");
        }
    }

    /**
     * Muestra los datos actuales del participante y permite modificarlos campo a campo.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de participantes
     */
    public static void opModificarParticipante(Scanner sc, Connection con, ParticipanteDAO dao) {
        System.out.println("\n  --- MODIFICAR PARTICIPANTE ---");
        opListarParticipantes(con, dao);
        System.out.print("  ID del participante a modificar: ");
        int id = leerEntero(sc);
        Participante p = dao.obtenerParticipante(con, id);

        if (p == null) {
            System.out.println("  Participante no encontrado.");
        } else {
            p.setNombre(leerTextoOpcional(sc, "Nombre", p.getNombre()));
            p.setApellido(leerTextoOpcional(sc, "Apellido", p.getApellido()));
            p.setGenero(leerTextoOpcional(sc, "Genero", p.getGenero()));

            System.out.print("  Edad [" + p.getEdad() + "]: ");
            String edadStr = sc.nextLine().trim();
            if (!edadStr.isEmpty()) {
                try {
                    p.setEdad(Integer.parseInt(edadStr));
                } catch (NumberFormatException e) {
                    System.out.println("  Valor no valido. Se mantiene la edad actual.");
                }
            }

            boolean ok = dao.actualizarParticipante(con, p);
            if (ok) {
                System.out.println("  Participante actualizado correctamente.");
            } else {
                System.out.println("  Error al actualizar el participante.");
            }
        }
    }

    /**
     * Realiza la baja logica de un participante estableciendo activo a false.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de participantes
     */
    public static void opDarDeBajaParticipante(Scanner sc, Connection con, ParticipanteDAO dao) {
        System.out.println("\n  --- DAR DE BAJA PARTICIPANTE ---");
        opListarParticipantes(con, dao);
        System.out.print("  ID del participante a dar de baja: ");
        int id = leerEntero(sc);
        boolean ok = dao.darDeBaja(con, id);
        if (ok) {
            System.out.println("  Participante dado de baja. Historial conservado en la BD.");
        } else {
            System.out.println("  Error al dar de baja al participante.");
        }
    }

    /**
     * Muestra en consola la lista de participantes activos.
     *
     * @param con conexion a la bd
     * @param dao DAO de participantes
     */
    public static void opListarParticipantes(Connection con, ParticipanteDAO dao) {
        List<Participante> participantes = dao.obtenerTodosParticipantes(con);
        if (participantes.isEmpty()) {
            System.out.println("  No hay participantes activos.");
        } else {
            System.out.println("\n  ID  | Nombre                    | Edad | Perfil");
            System.out.println("  ----|---------------------------|------|------------------");
            for (int i = 0; i < participantes.size(); i++) {
                Participante p = participantes.get(i);
                System.out.printf("  %-4d| %-26s| %-5d| %s%n",
                        p.getId(), p.getNombreCompleto(), p.getEdad(),
                        p.getPerfil().name());
            }
        }
    }

    /**
     * Solicita los datos por consola y crea un nuevo recurso en la bd.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de recursos
     */
    public static void opCrearRecurso(Scanner sc, Connection con, RecursoDAO dao) {
        System.out.println("\n  --- CREAR RECURSO ---");
        String tipo = leerTexto(sc, "  Tipo/Nombre del recurso: ");
        System.out.print("  Cantidad: ");
        int cantidad = leerEntero(sc);
        System.out.print("  Es fungible (s/n): ");
        boolean esFungible = sc.nextLine().trim().equalsIgnoreCase("s");

        Recurso r = new Recurso(tipo, cantidad, esFungible);
        int id = dao.insertarRecurso(con, r);
        if (id > 0) {
            System.out.println("  Recurso creado con ID: " + id);
        } else {
            System.out.println("  Error al crear el recurso.");
        }
    }

    /**
     * Asigna un recurso a una patrulla actualizando su estado a EN_USO.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de recursos
     */
    public static void opAsignarRecursoPatrulla(Scanner sc, Connection con, RecursoDAO dao) {
        System.out.println("\n  --- ASIGNAR RECURSO A PATRULLA ---");
        opListarRecursos(con, dao);
        System.out.print("  ID del recurso: ");
        int idRecurso = leerEntero(sc);
        System.out.print("  ID de la patrulla: ");
        int idPatrulla = leerEntero(sc);

        boolean ok = dao.asignarAPatrulla(con, idRecurso, idPatrulla);
        if (ok) {
            System.out.println("  Recurso asignado a la patrulla correctamente.");
        } else {
            System.out.println("  Error al asignar el recurso.");
        }
    }

    /**
     * Cambia el estado de disponibilidad de un recurso.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de recursos
     */
    public static void opModificarEstadoRecurso(Scanner sc, Connection con, RecursoDAO dao) {
        System.out.println("\n  --- MODIFICAR ESTADO DE RECURSO ---");
        opListarRecursos(con, dao);
        System.out.print("  ID del recurso: ");
        int idRecurso = leerEntero(sc);
        EstadoRecursoEnum nuevoEstado = seleccionarEstadoRecurso(sc);

        boolean ok = dao.modificarEstado(con, idRecurso, nuevoEstado);
        if (ok) {
            System.out.println("  Estado actualizado correctamente.");
        } else {
            System.out.println("  Error al actualizar el estado.");
        }
    }

    /**
     * Elimina un recurso de la bd tras solicitar confirmacion al usuario.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de recursos
     */
    public static void opEliminarRecurso(Scanner sc, Connection con, RecursoDAO dao) {
        System.out.println("\n  --- ELIMINAR RECURSO ---");
        opListarRecursos(con, dao);
        System.out.print("  ID del recurso a eliminar: ");
        int id = leerEntero(sc);
        System.out.print("  Confirmar eliminacion (s/n): ");
        String confirma = sc.nextLine().trim();

        if (confirma.equalsIgnoreCase("s")) {
            boolean ok = dao.eliminarRecurso(con, id);
            if (ok) {
                System.out.println("  Recurso eliminado correctamente.");
            } else {
                System.out.println("  Error al eliminar el recurso.");
            }
        } else {
            System.out.println("  Operacion cancelada.");
        }
    }

    /**
     * Libera un recurso de la patrulla asignada y lo marca como disponible.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de recursos
     */
    public static void opLiberarRecurso(Scanner sc, Connection con, RecursoDAO dao) {
        System.out.println("\n  --- LIBERAR RECURSO DE PATRULLA ---");
        opListarRecursos(con, dao);
        System.out.print("  ID del recurso a liberar: ");
        int idRecurso = leerEntero(sc);
        Recurso recurso = dao.obtenerRecurso(con, idRecurso);

        if (recurso == null) {
            System.out.println("  Recurso no encontrado.");
        } else if (recurso.getIdPatrulla() <= 0) {
            System.out.println("  El recurso no esta asignado a ninguna patrulla.");
        } else {
            boolean ok = dao.liberarDePatrulla(con, idRecurso);
            if (ok) {
                System.out.println("  Recurso liberado. Estado: disponible.");
            } else {
                System.out.println("  Error al liberar el recurso.");
            }
        }
    }

    /**
     * Muestra en consola la lista de todos los recursos con su estado y disponibilidad.
     *
     * @param con conexion a la bd
     * @param dao DAO de recursos
     */
    public static void opListarRecursos(Connection con, RecursoDAO dao) {
        List<Recurso> recursos = dao.obtenerTodosRecursos(con);
        if (recursos.isEmpty()) {
            System.out.println("  No hay recursos registrados.");
        } else {
            System.out.println("\n  ID  | Tipo                      | Cant. | Estado                | Fungible");
            System.out.println("  ----|---------------------------|-------|-----------------------|---------");
            for (int i = 0; i < recursos.size(); i++) {
                Recurso r = recursos.get(i);
                System.out.printf("  %-4d| %-26s| %-6d| %-22s| %s%n",
                        r.getId(), r.getTipo(), r.getCantidad(),
                        r.getEstado().name(), r.isEsFungible() ? "Si" : "No");
            }
        }
    }

    /**
     * Solicita los datos por consola y crea un nuevo monitor en la bd.
     * Si el NIF esta vacio o supera los 9 caracteres, la operacion se cancela.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de monitores
     */
    public static void opCrearMonitor(Scanner sc, Connection con, MonitorDAO dao) {
        System.out.println("\n  --- CREAR MONITOR ---");
        String nif       = leerTexto(sc, "  NIF (max 20 caracteres): ");
        if (nif.isEmpty() || nif.length() > 20) {
            System.out.println("  NIF no válido. Debe tener entre 1 y 20 caracteres.");
            return;
        }
        String nombre    = leerTexto(sc, "  Nombre: ");
        String apellido  = leerTexto(sc, "  Apellido: ");
        String telefono  = leerTexto(sc, "  Telefono: ");
        String direccion = leerTexto(sc, "  Direccion: ");

        Monitor m = new Monitor(nif, nombre, apellido, telefono, direccion);
        boolean ok = dao.insertarMonitor(con, m);
        if (ok) {
            System.out.println("  Monitor creado correctamente.");
        } else {
            System.out.println("  Error al crear el monitor.");
        }
    }

    /**
     * Muestra los datos actuales del monitor y permite modificarlos campo a campo.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de monitores
     */
    public static void opModificarMonitor(Scanner sc, Connection con, MonitorDAO dao) {
        System.out.println("\n  --- MODIFICAR MONITOR ---");
        opListarMonitores(con, dao);
        String nif = leerTexto(sc, "  NIF del monitor a modificar: ");
        Monitor m = dao.obtenerMonitor(con, nif);

        if (m == null) {
            System.out.println("  Monitor no encontrado.");
        } else {
            m.setNombre(leerTextoOpcional(sc, "Nombre", m.getNombre()));
            m.setApellido(leerTextoOpcional(sc, "Apellido", m.getApellido()));
            m.setTelefono(leerTextoOpcional(sc, "Telefono", m.getTelefono()));
            m.setDireccion(leerTextoOpcional(sc, "Direccion", m.getDireccion()));

            boolean ok = dao.actualizarMonitor(con, m);
            if (ok) {
                System.out.println("  Monitor actualizado correctamente.");
            } else {
                System.out.println("  Error al actualizar el monitor.");
            }
        }
    }

    /**
     * Realiza la baja logica de un monitor estableciendo activo a false.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de monitores
     */
    public static void opDarDeBajaMonitor(Scanner sc, Connection con, MonitorDAO dao) {
        System.out.println("\n  --- DAR DE BAJA MONITOR ---");
        opListarMonitores(con, dao);
        String nif = leerTexto(sc, "  NIF del monitor a dar de baja: ");
        boolean ok = dao.darDeBaja(con, nif);
        if (ok) {
            System.out.println("  Monitor dado de baja. Historial conservado en la BD.");
        } else {
            System.out.println("  Error al dar de baja al monitor.");
        }
    }

    /**
     * Muestra en consola la lista de monitores activos.
     *
     * @param con conexion a la bd
     * @param dao DAO de monitores
     */
    public static void opListarMonitores(Connection con, MonitorDAO dao) {
        List<Monitor> monitores = dao.obtenerTodosMonitores(con);
        if (monitores.isEmpty()) {
            System.out.println("  No hay monitores activos.");
        } else {
            System.out.println("\n  NIF       | Nombre                    | Telefono");
            System.out.println("  ----------|---------------------------|-----------");
            for (int i = 0; i < monitores.size(); i++) {
                Monitor m = monitores.get(i);
                System.out.printf("  %-10s| %-26s| %s%n",
                        m.getNif(), m.getNombreCompleto(), m.getTelefono());
            }
        }
    }
}
