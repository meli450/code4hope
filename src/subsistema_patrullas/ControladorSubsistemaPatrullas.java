package subsistema_patrullas;

import DAO.*;
import Entidad.*;
import java.sql.Connection;
import java.util.Scanner;

/**
 * Controlador principal del Subsistema de Patrullas.
 * <p>
 * Gestiona la navegacion por menus y delega cada operacion a
 * {@link SubsistemaPatrullasDAO} (logica de negocio) o a {@link GestorFicheros}
 * (lectura y escritura de ficheros).
 * </p>
 *
 * @author Code4Hope Team
 * @version 1.0
 * @see SubsistemaPatrullasDAO
 * @see GestorFicheros
 */
public class ControladorSubsistemaPatrullas {

    // =========================================================================
    // MENUS
    // =========================================================================

    /** Muestra el menu principal del sistema. */
    private static void mostrarMenuPrincipal() {
        System.out.println("\n============================================================");
        System.out.println("  CODE4HOPE - Sistema de Gestion de Subsistemas");
        System.out.println("============================================================");
        System.out.println("  1. Patrullas");
        System.out.println("  0. Salir");
        System.out.println("------------------------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /** Muestra el menu del subsistema de patrullas. */
    private static void mostrarMenuPatrullas() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |        SUBSISTEMA DE PATRULLAS           |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Gestion de Patrullas");
        System.out.println("  2. Gestion de Tripulantes");
        System.out.println("  3. Gestion de Vehiculos");
        System.out.println("  4. Gestion de Rutas");
        System.out.println("  5. Gestion de Equipos de Comunicacion");
        System.out.println("  6. Gestion de Ficheros");
        System.out.println("  0. Volver al menu principal");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /** Muestra el menu de gestion de patrullas (CRUD + asignaciones). */
    private static void mostrarMenuGestionPatrullas() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |          GESTION DE PATRULLAS            |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1.  Crear patrulla");
        System.out.println("  2.  Buscar patrulla por ID");
        System.out.println("  3.  Listar todas las patrullas");
        System.out.println("  4.  Actualizar estado de patrulla");
        System.out.println("  5.  Asignar vehiculo a patrulla");
        System.out.println("  6.  Asignar ruta a patrulla");
        System.out.println("  7.  Asignar equipo COM a patrulla");
        System.out.println("  8.  Asignar tripulante a patrulla");
        System.out.println("  9.  Ver tripulantes de patrulla");
        System.out.println("  10. Añadir recurso a patrulla");
        System.out.println("  11. Ver recursos de patrulla");
        System.out.println("  12. Eliminar patrulla");
        System.out.println("  0.  Volver");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /** Muestra el menu de gestion de tripulantes. */
    private static void mostrarMenuGestionTripulantes() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |        GESTION DE TRIPULANTES            |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Crear tripulante");
        System.out.println("  2. Buscar tripulante por ID");
        System.out.println("  3. Listar todos los tripulantes");
        System.out.println("  4. Liberar tripulante de patrulla");
        System.out.println("  5. Eliminar tripulante");
        System.out.println("  0. Volver");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /** Muestra el menu de gestion de vehiculos. */
    private static void mostrarMenuGestionVehiculos() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |         GESTION DE VEHICULOS             |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Crear vehiculo");
        System.out.println("  2. Buscar vehiculo por ID");
        System.out.println("  3. Listar todos los vehiculos");
        System.out.println("  4. Listar vehiculos disponibles");
        System.out.println("  5. Eliminar vehiculo");
        System.out.println("  0. Volver");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /** Muestra el menu de gestion de rutas y puntos de ruta. */
    private static void mostrarMenuGestionRutas() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |           GESTION DE RUTAS               |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Crear ruta");
        System.out.println("  2. Buscar ruta por ID");
        System.out.println("  3. Listar todas las rutas");
        System.out.println("  4. Añadir punto a una ruta");
        System.out.println("  5. Ver puntos de una ruta");
        System.out.println("  6. Eliminar ruta");
        System.out.println("  0. Volver");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /** Muestra el menu de gestion de equipos de comunicacion. */
    private static void mostrarMenuGestionEquipos() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |    GESTION DE EQUIPOS DE COMUNICACION    |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Crear equipo");
        System.out.println("  2. Buscar equipo por ID");
        System.out.println("  3. Listar todos los equipos");
        System.out.println("  4. Registrar comunicacion");
        System.out.println("  5. Ver comunicaciones de un equipo");
        System.out.println("  6. Añadir entrada al log");
        System.out.println("  7. Ver log de un equipo");
        System.out.println("  8. Eliminar equipo");
        System.out.println("  0. Volver");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /** Muestra el menu de gestion de ficheros (HTML e importacion/exportacion CSV). */
    private static void mostrarMenuGestionFicheros() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |          GESTION DE FICHEROS             |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Generar informe de mision (HTML)");
        System.out.println("  2. Exportar comunicaciones a CSV");
        System.out.println("  3. Importar comunicaciones desde CSV");
        System.out.println("  0. Volver");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    /**
     * Lee un entero desde la entrada estandar.
     * Si la entrada no es numerica devuelve -1.
     *
     * @param sc Scanner activo para leer la entrada del usuario
     * @return Opcion introducida, o -1 si la entrada no es valida
     */
    private static int leerOpcion(Scanner sc) {
        int opcion = -1;
        try {
            opcion = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  Entrada no valida. Introduzca un numero.");
        }
        return opcion;
    }

    // =========================================================================
    // SUBMENUS
    // =========================================================================

    /**
     * Submenu de gestion de patrullas: CRUD, asignaciones y recursos.
     * Llama a los metodos correspondientes de {@link SubsistemaPatrullasDAO}.
     *
     * @param sc   Scanner para leer la entrada del usuario
     * @param con  Conexion activa a la base de datos
     * @param daoP DAO de {@link Entidad.Patrulla}
     * @param daoV DAO de {@link Entidad.Vehiculo}
     * @param daoR DAO de {@link Entidad.Ruta}
     * @param daoE DAO de {@link Entidad.EquipoComunicacion}
     * @param daoT DAO de {@link Entidad.Tripulante}
     */
    private static void menuGestionPatrullas(Scanner sc, Connection con,
            PatrullaDAO daoP, VehiculoDAO daoV, RutaDAO daoR,
            EquipoComunicacionDAO daoE, TripulanteDAO daoT) {
        boolean volver = false;
        while (!volver) {
            mostrarMenuGestionPatrullas();
            int opcion = leerOpcion(sc);
            switch (opcion) {
                case 1:  SubsistemaPatrullasDAO.opCrearPatrulla(sc, con, daoP);                          break;
                case 2:  SubsistemaPatrullasDAO.opBuscarPatrullaPorId(sc, con, daoP);                    break;
                case 3:  SubsistemaPatrullasDAO.opListarPatrullas(con, daoP);                            break;
                case 4:  SubsistemaPatrullasDAO.opActualizarEstadoPatrulla(sc, con, daoP);               break;
                case 5:  SubsistemaPatrullasDAO.opAsignarVehiculoPatrulla(sc, con, daoP, daoV);          break;
                case 6:  SubsistemaPatrullasDAO.opAsignarRutaPatrulla(sc, con, daoP, daoR);              break;
                case 7:  SubsistemaPatrullasDAO.opAsignarEquipoPatrulla(sc, con, daoP, daoE);            break;
                case 8:  SubsistemaPatrullasDAO.opAsignarTripulantePatrulla(sc, con, daoT);              break;
                case 9:  SubsistemaPatrullasDAO.opVerTripulantesPatrulla(sc, con, daoT);                 break;
                case 10: SubsistemaPatrullasDAO.opAddRecursoPatrulla(sc, con, daoP);                     break;
                case 11: SubsistemaPatrullasDAO.opVerRecursosPatrulla(sc, con, daoP);                    break;
                case 12: SubsistemaPatrullasDAO.opEliminarPatrulla(sc, con, daoP);                       break;
                case 0:  volver = true;                                                                   break;
                default: System.out.println("  Opcion no valida. Elija entre 0 y 12.");                  break;
            }
        }
    }

    /**
     * Submenu de gestion de tripulantes: CRUD y liberacion de asignacion.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Entidad.Tripulante}
     */
    private static void menuGestionTripulantes(Scanner sc, Connection con, TripulanteDAO dao) {
        boolean volver = false;
        while (!volver) {
            mostrarMenuGestionTripulantes();
            int opcion = leerOpcion(sc);
            switch (opcion) {
                case 1: SubsistemaPatrullasDAO.opCrearTripulante(sc, con, dao);       break;
                case 2: SubsistemaPatrullasDAO.opBuscarTripulantePorId(sc, con, dao); break;
                case 3: SubsistemaPatrullasDAO.opListarTripulantes(con, dao);          break;
                case 4: SubsistemaPatrullasDAO.opLiberarTripulante(sc, con, dao);     break;
                case 5: SubsistemaPatrullasDAO.opEliminarTripulante(sc, con, dao);    break;
                case 0: volver = true;                                                 break;
                default: System.out.println("  Opcion no valida. Elija entre 0 y 5."); break;
            }
        }
    }

    /**
     * Submenu de gestion de vehiculos: CRUD y consulta de disponibilidad.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Entidad.Vehiculo}
     */
    private static void menuGestionVehiculos(Scanner sc, Connection con, VehiculoDAO dao) {
        boolean volver = false;
        while (!volver) {
            mostrarMenuGestionVehiculos();
            int opcion = leerOpcion(sc);
            switch (opcion) {
                case 1: SubsistemaPatrullasDAO.opCrearVehiculo(sc, con, dao);            break;
                case 2: SubsistemaPatrullasDAO.opBuscarVehiculoPorId(sc, con, dao);      break;
                case 3: SubsistemaPatrullasDAO.opListarVehiculos(con, dao);               break;
                case 4: SubsistemaPatrullasDAO.opListarVehiculosDisponibles(con, dao);   break;
                case 5: SubsistemaPatrullasDAO.opEliminarVehiculo(sc, con, dao);         break;
                case 0: volver = true;                                                    break;
                default: System.out.println("  Opcion no valida. Elija entre 0 y 5."); break;
            }
        }
    }

    /**
     * Submenu de gestion de rutas y sus puntos de ruta.
     *
     * @param sc    Scanner para leer la entrada del usuario
     * @param con   Conexion activa a la base de datos
     * @param daoR  DAO de {@link Entidad.Ruta}
     * @param daoPR DAO de {@link Entidad.PuntoRuta}
     */
    private static void menuGestionRutas(Scanner sc, Connection con,
            RutaDAO daoR, PuntoRutaDAO daoPR) {
        boolean volver = false;
        while (!volver) {
            mostrarMenuGestionRutas();
            int opcion = leerOpcion(sc);
            switch (opcion) {
                case 1: SubsistemaPatrullasDAO.opCrearRuta(sc, con, daoR);           break;
                case 2: SubsistemaPatrullasDAO.opBuscarRutaPorId(sc, con, daoR);     break;
                case 3: SubsistemaPatrullasDAO.opListarRutas(con, daoR);              break;
                case 4: SubsistemaPatrullasDAO.opAnadirPuntoRuta(sc, con, daoPR);    break;
                case 5: SubsistemaPatrullasDAO.opVerPuntosRuta(sc, con, daoPR);      break;
                case 6: SubsistemaPatrullasDAO.opEliminarRuta(sc, con, daoR);        break;
                case 0: volver = true;                                                break;
                default: System.out.println("  Opcion no valida. Elija entre 0 y 6."); break;
            }
        }
    }

    /**
     * Submenu de gestion de equipos de comunicacion, registros y log.
     *
     * @param sc    Scanner para leer la entrada del usuario
     * @param con   Conexion activa a la base de datos
     * @param daoE  DAO de {@link Entidad.EquipoComunicacion}
     * @param daoRC DAO de {@link Entidad.RegistroComunicacion}
     */
    private static void menuGestionEquipos(Scanner sc, Connection con,
            EquipoComunicacionDAO daoE, RegistroComunicacionDAO daoRC) {
        boolean volver = false;
        while (!volver) {
            mostrarMenuGestionEquipos();
            int opcion = leerOpcion(sc);
            switch (opcion) {
                case 1: SubsistemaPatrullasDAO.opCrearEquipo(sc, con, daoE);              break;
                case 2: SubsistemaPatrullasDAO.opBuscarEquipoPorId(sc, con, daoE);        break;
                case 3: SubsistemaPatrullasDAO.opListarEquipos(con, daoE);                 break;
                case 4: SubsistemaPatrullasDAO.opRegistrarComunicacion(sc, con, daoRC);   break;
                case 5: SubsistemaPatrullasDAO.opVerComunicaciones(sc, con, daoRC);       break;
                case 6: SubsistemaPatrullasDAO.opAnadirLog(sc, con, daoE);                 break;
                case 7: SubsistemaPatrullasDAO.opVerLog(sc, con, daoE);                    break;
                case 8: SubsistemaPatrullasDAO.opEliminarEquipo(sc, con, daoE);            break;
                case 0: volver = true;                                                     break;
                default: System.out.println("  Opcion no valida. Elija entre 0 y 8."); break;
            }
        }
    }

    /**
     * Submenu de gestion de ficheros: informe HTML y CSV de comunicaciones.
     * Delega en {@link GestorFicheros}.
     *
     * @param sc    Scanner para leer la entrada del usuario
     * @param con   Conexion activa a la base de datos
     * @param daoP  DAO de {@link Entidad.Patrulla}
     * @param daoV  DAO de {@link Entidad.Vehiculo}
     * @param daoR  DAO de {@link Entidad.Ruta}
     * @param daoPR DAO de {@link Entidad.PuntoRuta}
     * @param daoT  DAO de {@link Entidad.Tripulante}
     * @param daoE  DAO de {@link Entidad.EquipoComunicacion}
     * @param daoRC DAO de {@link Entidad.RegistroComunicacion}
     */
    private static void menuGestionFicheros(Scanner sc, Connection con,
            PatrullaDAO daoP, VehiculoDAO daoV, RutaDAO daoR, PuntoRutaDAO daoPR,
            TripulanteDAO daoT, EquipoComunicacionDAO daoE, RegistroComunicacionDAO daoRC) {
        boolean volver = false;
        while (!volver) {
            mostrarMenuGestionFicheros();
            int opcion = leerOpcion(sc);
            switch (opcion) {
                case 1: GestorFicherosPatrullas.generarInformeMisionHTML(sc, con, daoP, daoV, daoR, daoPR, daoT, daoE, daoRC); break;
                case 2: GestorFicherosPatrullas.exportarComunicacionesCSV(sc, con, daoE, daoRC);                                break;
                case 3: GestorFicherosPatrullas.importarComunicacionesCSV(sc, con, daoE, daoRC);                                break;
                case 0: volver = true;                                                                                  break;
                default: System.out.println("  Opcion no valida. Elija entre 0 y 3."); break;
            }
        }
    }

    /**
     * Submenu raiz del subsistema de patrullas.
     * Distribuye la navegacion hacia cada submodulo de gestion.
     *
     * @param sc    Scanner para leer la entrada del usuario
     * @param con   Conexion activa a la base de datos
     * @param daoP  DAO de {@link Entidad.Patrulla}
     * @param daoT  DAO de {@link Entidad.Tripulante}
     * @param daoV  DAO de {@link Entidad.Vehiculo}
     * @param daoR  DAO de {@link Entidad.Ruta}
     * @param daoPR DAO de {@link Entidad.PuntoRuta}
     * @param daoE  DAO de {@link Entidad.EquipoComunicacion}
     * @param daoRC DAO de {@link Entidad.RegistroComunicacion}
     */
    private static void menuPatrullas(Scanner sc, Connection con,
            PatrullaDAO daoP, TripulanteDAO daoT, VehiculoDAO daoV,
            RutaDAO daoR, PuntoRutaDAO daoPR,
            EquipoComunicacionDAO daoE, RegistroComunicacionDAO daoRC) {
        boolean volver = false;
        while (!volver) {
            mostrarMenuPatrullas();
            int opcion = leerOpcion(sc);
            switch (opcion) {
                case 1: menuGestionPatrullas(sc, con, daoP, daoV, daoR, daoE, daoT);                      break;
                case 2: menuGestionTripulantes(sc, con, daoT);                                             break;
                case 3: menuGestionVehiculos(sc, con, daoV);                                               break;
                case 4: menuGestionRutas(sc, con, daoR, daoPR);                                            break;
                case 5: menuGestionEquipos(sc, con, daoE, daoRC);                                          break;
                case 6: menuGestionFicheros(sc, con, daoP, daoV, daoR, daoPR, daoT, daoE, daoRC);         break;
                case 0: volver = true;                                                                     break;
                default: System.out.println("  Opcion no valida. Elija entre 0 y 6."); break;
            }
        }
    }

    // =========================================================================
    // PUNTO DE ENTRADA DEL SUBSISTEMA
    // =========================================================================

    /**
     * Inicia el Subsistema de Patrullas.
     * <p>
     * Abre la conexion a la base de datos, instancia todos los DAOs y lanza
     * el bucle principal de navegacion por menus. La conexion se cierra
     * siempre en el bloque {@code finally}.
     * </p>
     */
    public static void iniciarSubsistemaPatrullas() {
        Conexion_DB conexionDB = new Conexion_DB();
        Connection conexion = null;
        Scanner sc = new Scanner(System.in);

        try {
            conexion = conexionDB.abrirConexion();

            PatrullaDAO daoPatrulla             = new PatrullaDAO();
            TripulanteDAO daoTripulante         = new TripulanteDAO();
            VehiculoDAO daoVehiculo             = new VehiculoDAO();
            RutaDAO daoRuta                     = new RutaDAO();
            PuntoRutaDAO daoPuntoRuta           = new PuntoRutaDAO();
            EquipoComunicacionDAO daoEquipo     = new EquipoComunicacionDAO();
            RegistroComunicacionDAO daoRegistro = new RegistroComunicacionDAO();

            boolean salir = false;
            while (!salir) {
                mostrarMenuPrincipal();
                int opcion = leerOpcion(sc);
                switch (opcion) {
                    case 1:
                        menuPatrullas(sc, conexion, daoPatrulla, daoTripulante,
                                daoVehiculo, daoRuta, daoPuntoRuta, daoEquipo, daoRegistro);
                        break;
                    case 0:
                        salir = true;
                        break;
                    default:
                        System.out.println("  Opcion no valida. Elija 1 o 0.");
                        break;
                }
            }

            System.out.println("\n  Hasta pronto.");

        } catch (Exception error) {
            error.printStackTrace();
        } finally {
            try {
                conexionDB.cerrarConexion(conexion);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
