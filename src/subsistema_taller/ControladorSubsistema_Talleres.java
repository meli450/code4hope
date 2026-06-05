/*Subsistema Talleres de Formacion
 * Realizado por: Melisa
 * Punto de entrada del subsistema */
package subsistema_taller;

import DAO.Conexion_DB;
import DAO.EncuestaDAO;
import DAO.Subsistema_TalleresDAO;
import DAO.MonitorDAO;
import DAO.ParticipanteDAO;
import DAO.RecursoDAO;
import DAO.TallerDAO;
import java.sql.Connection;
import java.util.Scanner;

/**
 * Punto de entrada del subsistema de talleres de formacion.
 * Gestiona los menus interactivos y delega las operaciones en {@link DAO.Subsistema_TalleresDAO}.
 *
 * @author Melisa
 */
public class ControladorSubsistema_Talleres {

    /**
     * Muestra el menu principal con los modulos disponibles.
     */
    private static void mostrarMenuPrincipal() {
        System.out.println("\n============================================================");
        System.out.println("  CODE4HOPE - Subsistema de Talleres");
        System.out.println("============================================================");
        System.out.println("  1. Talleres");
        System.out.println("  2. Participantes");
        System.out.println("  3. Recursos");
        System.out.println("  4. Monitores");
        System.out.println("  0. Salir");
        System.out.println("------------------------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /**
     * Muestra el menu de gestion de talleres.
     */
    private static void mostrarMenuTaller() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |          GESTION DE TALLERES             |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Crear taller");
        System.out.println("  2. Modificar taller");
        System.out.println("  3. Eliminar taller");
        System.out.println("  4. Ver todos los talleres");
        System.out.println("  5. Cancelar taller");
        System.out.println("  6. Filtrar por estado");
        System.out.println("  7. Exportar talleres a HTML");
        System.out.println("  0. Volver al menu principal");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /**
     * Muestra el menu de modificacion de un taller concreto.
     */
    private static void mostrarMenuModificarTaller() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |         MODIFICAR TALLER                 |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Modificar datos del taller");
        System.out.println("  2. Inscribir participante");
        System.out.println("  3. Dar de baja participante del taller");
        System.out.println("  4. Ver participantes inscritos");
        System.out.println("  5. Asignar recurso al taller");
        System.out.println("  6. Eliminar recurso del taller");
        System.out.println("  7. Ver recursos del taller");
        System.out.println("  8. Encuestas del taller");
        System.out.println("  0. Volver");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /**
     * Muestra el menu de gestion de encuestas.
     */
    private static void mostrarMenuEncuesta() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |        GESTION DE ENCUESTAS              |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Registrar encuesta (en Google Forms)");
        System.out.println("  2. Mostrar enlace");
        System.out.println("  3. Importar respuestas y generar informe");
        System.out.println("  4. Ver encuestas del taller");
        System.out.println("  0. Volver");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /**
     * Muestra el menu de gestion de participantes.
     */
    private static void mostrarMenuParticipante() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |       GESTION DE PARTICIPANTES           |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Crear participante");
        System.out.println("  2. Modificar participante");
        System.out.println("  3. Dar de baja participante");
        System.out.println("  4. Ver todos los participantes");
        System.out.println("  0. Volver al menu principal");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /**
     * Muestra el menu de gestion de recursos.
     */
    private static void mostrarMenuRecurso() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |         GESTION DE RECURSOS              |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Crear recurso");
        System.out.println("  2. Asignar recurso a patrulla");
        System.out.println("  3. Modificar estado del recurso");
        System.out.println("  4. Eliminar recurso");
        System.out.println("  5. Ver todos los recursos");
        System.out.println("  6. Liberar recurso de patrulla");
        System.out.println("  0. Volver al menu principal");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /**
     * Muestra el menu de gestion de monitores.
     */
    private static void mostrarMenuMonitor() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |         GESTION DE MONITORES             |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Crear monitor");
        System.out.println("  2. Modificar monitor");
        System.out.println("  3. Dar de baja monitor");
        System.out.println("  4. Ver todos los monitores");
        System.out.println("  0. Volver al menu principal");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /**
     * Submenu de gestion de encuestas de un taller.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de encuestas
     */
    private static void menuEncuesta(Scanner sc, Connection con, EncuestaDAO dao) {
        boolean volver = false;
        int opcion;
        while (!volver) {
            mostrarMenuEncuesta();
            opcion = Subsistema_TalleresDAO.leerOpcion(sc);
            switch (opcion) {
                case 1:
                    Subsistema_TalleresDAO.opRegistrarEncuesta(sc, con, dao);
                    break;
                case 2:
                    Subsistema_TalleresDAO.opMostrarEnlaceEncuesta(sc, con, dao);
                    break;
                case 3:
                    Subsistema_TalleresDAO.opImportarYGenerarInforme(sc, con, dao);
                    break;
                case 4:
                    Subsistema_TalleresDAO.opVerEncuestasTaller(sc, con, dao);
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("  Opcion no valida. Introduzca un numero entre 0 y 4.");
                    break;
            }
        }
    }

    /**
     * Submenu para modificar un taller: datos, participantes, recursos y encuestas.
     *
     * @param sc          Scanner activo
     * @param con         conexion a la bd
     * @param dao         DAO de talleres
     * @param daoMonitor  DAO de monitores
     * @param daoP        DAO de participantes
     * @param daoR        DAO de recursos
     * @param daoEncuesta DAO de encuestas
     */
    private static void menuModificarTaller(Scanner sc, Connection con,
            TallerDAO dao, MonitorDAO daoMonitor,
            ParticipanteDAO daoP, RecursoDAO daoR,
            EncuestaDAO daoEncuesta) {
        boolean volver = false;
        int opcion;
        while (!volver) {
            mostrarMenuModificarTaller();
            opcion = Subsistema_TalleresDAO.leerOpcion(sc);
            switch (opcion) {
                case 1:
                    Subsistema_TalleresDAO.opModificarDatosTaller(sc, con, dao, daoMonitor);
                    break;
                case 2:
                    Subsistema_TalleresDAO.opInscribirParticipante(sc, con, dao, daoP);
                    break;
                case 3:
                    Subsistema_TalleresDAO.opDarDeBajaParticipanteTaller(sc, con, dao);
                    break;
                case 4:
                    Subsistema_TalleresDAO.opVerParticipantesTaller(sc, con, dao);
                    break;
                case 5:
                    Subsistema_TalleresDAO.opAsignarRecursoTaller(sc, con, dao, daoR);
                    break;
                case 6:
                    Subsistema_TalleresDAO.opEliminarRecursoTaller(sc, con, dao);
                    break;
                case 7:
                    Subsistema_TalleresDAO.opVerRecursosTaller(sc, con, dao);
                    break;
                case 8:
                    menuEncuesta(sc, con, daoEncuesta);
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("  Opcion no valida. Introduzca un numero entre 0 y 8.");
                    break;
            }
        }
    }

    /**
     * Submenu de gestion de talleres.
     *
     * @param sc          Scanner activo
     * @param con         conexion a la bd
     * @param dao         DAO de talleres
     * @param daoMonitor  DAO de monitores
     * @param daoP        DAO de participantes
     * @param daoR        DAO de recursos
     * @param daoEncuesta DAO de encuestas
     */
    private static void menuTaller(Scanner sc, Connection con,
            TallerDAO dao, MonitorDAO daoMonitor,
            ParticipanteDAO daoP, RecursoDAO daoR,
            EncuestaDAO daoEncuesta) {
        boolean volver = false;
        int opcion;
        while (!volver) {
            mostrarMenuTaller();
            opcion = Subsistema_TalleresDAO.leerOpcion(sc);
            switch (opcion) {
                case 1:
                    Subsistema_TalleresDAO.opCrearTaller(sc, con, dao, daoMonitor);
                    break;
                case 2:
                    menuModificarTaller(sc, con, dao, daoMonitor, daoP, daoR, daoEncuesta);
                    break;
                case 3:
                    Subsistema_TalleresDAO.opEliminarTaller(sc, con, dao);
                    break;
                case 4:
                    Subsistema_TalleresDAO.opListarTalleres(con, dao);
                    break;
                case 5:
                    Subsistema_TalleresDAO.opCancelarTaller(sc, con, dao);
                    break;
                case 6:
                    Subsistema_TalleresDAO.opFiltrarTalleresPorEstado(sc, con, dao);
                    break;
                case 7:
                    Subsistema_TalleresDAO.opExportarTalleresHTML(con, dao, daoMonitor);
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("  Opcion no valida. Introduzca un numero entre 0 y 7.");
                    break;
            }
        }
    }

    /**
     * Submenu de gestion de participantes.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de participantes
     */
    private static void menuParticipante(Scanner sc, Connection con, ParticipanteDAO dao) {
        boolean volver = false;
        int opcion;
        while (!volver) {
            mostrarMenuParticipante();
            opcion = Subsistema_TalleresDAO.leerOpcion(sc);
            switch (opcion) {
                case 1:
                    Subsistema_TalleresDAO.opCrearParticipante(sc, con, dao);
                    break;
                case 2:
                    Subsistema_TalleresDAO.opModificarParticipante(sc, con, dao);
                    break;
                case 3:
                    Subsistema_TalleresDAO.opDarDeBajaParticipante(sc, con, dao);
                    break;
                case 4:
                    Subsistema_TalleresDAO.opListarParticipantes(con, dao);
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("  Opcion no valida. Introduzca un numero entre 0 y 4.");
                    break;
            }
        }
    }

    /**
     * Submenu de gestion de recursos.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de recursos
     */
    private static void menuRecurso(Scanner sc, Connection con, RecursoDAO dao) {
        boolean volver = false;
        int opcion;
        while (!volver) {
            mostrarMenuRecurso();
            opcion = Subsistema_TalleresDAO.leerOpcion(sc);
            switch (opcion) {
                case 1:
                    Subsistema_TalleresDAO.opCrearRecurso(sc, con, dao);
                    break;
                case 2:
                    Subsistema_TalleresDAO.opAsignarRecursoPatrulla(sc, con, dao);
                    break;
                case 3:
                    Subsistema_TalleresDAO.opModificarEstadoRecurso(sc, con, dao);
                    break;
                case 4:
                    Subsistema_TalleresDAO.opEliminarRecurso(sc, con, dao);
                    break;
                case 5:
                    Subsistema_TalleresDAO.opListarRecursos(con, dao);
                    break;
                case 6:
                    Subsistema_TalleresDAO.opLiberarRecurso(sc, con, dao);
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("  Opcion no valida. Introduzca un numero entre 0 y 6.");
                    break;
            }
        }
    }

    /**
     * Submenu de gestion de monitores.
     *
     * @param sc  Scanner activo
     * @param con conexion a la bd
     * @param dao DAO de monitores
     */
    private static void menuMonitor(Scanner sc, Connection con, MonitorDAO dao) {
        boolean volver = false;
        int opcion;
        while (!volver) {
            mostrarMenuMonitor();
            opcion = Subsistema_TalleresDAO.leerOpcion(sc);
            switch (opcion) {
                case 1:
                    Subsistema_TalleresDAO.opCrearMonitor(sc, con, dao);
                    break;
                case 2:
                    Subsistema_TalleresDAO.opModificarMonitor(sc, con, dao);
                    break;
                case 3:
                    Subsistema_TalleresDAO.opDarDeBajaMonitor(sc, con, dao);
                    break;
                case 4:
                    Subsistema_TalleresDAO.opListarMonitores(con, dao);
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("  Opcion no valida. Introduzca un numero entre 0 y 4.");
                    break;
            }
        }
    }

    /**
     * Abre la conexion a la bd y presenta el menu interactivo principal.
     *
     * @param args argumentos de linea de comandos (no utilizados)
     * @throws Exception si se produce un error en la conexion a la bd
     */
    public static void main(String[] args) throws Exception {
        Conexion_DB conexionDB = new Conexion_DB();
        Connection conexion = null;
        Scanner sc = new Scanner(System.in);

        TallerDAO daoTaller;
        MonitorDAO daoMonitor;
        ParticipanteDAO daoParticipante;
        RecursoDAO daoRecurso;
        EncuestaDAO daoEncuesta;
        int opcion;

        try {
            conexion = conexionDB.abrirConexion();

            daoTaller = new TallerDAO();
            daoMonitor = new MonitorDAO();
            daoParticipante = new ParticipanteDAO();
            daoRecurso = new RecursoDAO();
            daoEncuesta = new EncuestaDAO();

            boolean salir = false;
            while (!salir) {
                mostrarMenuPrincipal();
                opcion = Subsistema_TalleresDAO.leerOpcion(sc);
                switch (opcion) {
                    case 1:
                        menuTaller(sc, conexion, daoTaller, daoMonitor,
                                daoParticipante, daoRecurso, daoEncuesta);
                        break;
                    case 2:
                        menuParticipante(sc, conexion, daoParticipante);
                        break;
                    case 3:
                        menuRecurso(sc, conexion, daoRecurso);
                        break;
                    case 4:
                        menuMonitor(sc, conexion, daoMonitor);
                        break;
                    case 0:
                        salir = true;
                        break;
                    default:
                        System.out.println("  Opcion no valida. Elija 1, 2, 3, 4 o 0.");
                        break;
                }
            }

            System.out.println("\n  Hasta pronto.");

        } catch (Exception error) {
            error.printStackTrace();
        } finally {
            sc.close();
            conexionDB.CerrarConexion(conexion);
        }
    }
}
