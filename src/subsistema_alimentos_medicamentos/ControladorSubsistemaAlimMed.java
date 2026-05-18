package subsistema_alimentos_medicamentos;

import DAO.AlmacenDAO;
import DAO.Conexion_DB;
import DAO.GestionAlimentosDAO;
import DAO.GestionMedicamentosDAO;
import Entidad.GestorFicherosAlimMed;
import DAO.SubsistemaAlimMedDAO;
import java.sql.Connection;
import java.util.Scanner;

/**
 * Punto de entrada del Subsistema de Gestion de Alimentos y Medicamentos.
 * Presenta un menu interactivo con dos subsistemas: Alimentos y Medicamentos.
 * Las operaciones de negocio se delegan a SubsistemaAlimMedDAO.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class ControladorSubsistemaAlimMed {

    // =========================================================================
    // METODOS AUXILIARES DE PRESENTACION
    // =========================================================================

    private static void mostrarMenuPrincipal() {
        System.out.println("\n============================================================");
        System.out.println("  CODE4HOPE - Subsistema Alimentos y Medicamentos");
        System.out.println("============================================================");
        System.out.println("  1. Alimentos");
        System.out.println("  2. Medicamentos");
        System.out.println("  3. Almacenes");
        System.out.println("  4. Informes y Ficheros");
        System.out.println("  0. Salir");
        System.out.println("------------------------------------------------------------");
        System.out.print("  Opcion: ");
    }

    private static void mostrarMenuAlimentos() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |       GESTION DE ALIMENTOS               |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Insertar alimento");
        System.out.println("  2. Obtener alimento por ID");
        System.out.println("  3. Listar todos los alimentos");
        System.out.println("  4. Actualizar alimento");
        System.out.println("  5. Eliminar alimento");
        System.out.println("  6. Insertar lote de alimentos");
        System.out.println("  7. Asignar lote a patrulla");
        System.out.println("  0. Volver al menu principal");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    private static void mostrarMenuMedicamentos() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |      GESTION DE MEDICAMENTOS             |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1.  Insertar medicamento");
        System.out.println("  2.  Obtener medicamento por ID");
        System.out.println("  3.  Listar todos los medicamentos");
        System.out.println("  4.  Actualizar medicamento");
        System.out.println("  5.  Eliminar medicamento");
        System.out.println("  6.  Insertar lote de medicamentos");
        System.out.println("  7.  Medicamentos con refrigeracion");
        System.out.println("  8.  Insertar paciente");
        System.out.println("  9.  Insertar prescripcion");
        System.out.println("  10. Prescripciones activas de un paciente");
        System.out.println("  11. Asignar lote a patrulla");
        System.out.println("  0.  Volver al menu principal");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    private static void mostrarMenuAlmacenes() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |         GESTION DE ALMACENES             |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  1. Insertar almacen de alimentos");
        System.out.println("  2. Insertar almacen de medicamentos");
        System.out.println("  3. Listar todos los almacenes");
        System.out.println("  4. Eliminar almacen");
        System.out.println("  0. Volver al menu principal");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    private static void mostrarMenuInformes() {
        System.out.println("\n  +------------------------------------------+");
        System.out.println("  |        INFORMES Y FICHEROS               |");
        System.out.println("  +------------------------------------------+");
        System.out.println("  -- Productos (CSV) --");
        System.out.println("  1. Exportar alimentos a CSV");
        System.out.println("  2. Importar alimentos desde CSV");
        System.out.println("  3. Exportar medicamentos a CSV");
        System.out.println("  4. Importar medicamentos desde CSV");
        System.out.println("  -- Lotes (HTML) --");
        System.out.println("  5. Informe de lotes de alimentos");
        System.out.println("  6. Informe de lotes de medicamentos");
        System.out.println("  0. Volver al menu principal");
        System.out.println("  ------------------------------------------");
        System.out.print("  Opcion: ");
    }

    // =========================================================================
    // SUBMENUS
    // =========================================================================

    /**
     * Submenu de gestion de alimentos. Permanece activo hasta que el usuario
     * elige 0 para volver al menu principal.
     */
    private static void menuAlimentos(Scanner sc, Connection con,
            GestionAlimentosDAO dao, AlmacenDAO daoAlmacenes) {
        boolean volver = false;
        int opcion;
        while (!volver) {
            mostrarMenuAlimentos();
            opcion = SubsistemaAlimMedDAO.leerOpcion(sc);
            switch (opcion) {
                case 1:
                    SubsistemaAlimMedDAO.opInsertarAlimento(sc, con, dao);
                    break;
                case 2:
                    SubsistemaAlimMedDAO.opObtenerAlimento(sc, con, dao);
                    break;
                case 3:
                    SubsistemaAlimMedDAO.opListarAlimentos(con, dao);
                    break;
                case 4:
                    SubsistemaAlimMedDAO.opActualizarAlimento(sc, con, dao);
                    break;
                case 5:
                    SubsistemaAlimMedDAO.opEliminarAlimento(sc, con, dao);
                    break;
                case 6:
                    SubsistemaAlimMedDAO.opInsertarLoteAlimentos(sc, con, dao, daoAlmacenes);
                    break;
                case 7:
                    SubsistemaAlimMedDAO.opAsignarLoteAlimentos(sc, con, dao);
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
     * Submenu de gestion de medicamentos. Permanece activo hasta que el usuario
     * elige 0 para volver al menu principal.
     */
    private static void menuMedicamentos(Scanner sc, Connection con,
            GestionMedicamentosDAO dao, AlmacenDAO daoAlmacenes) {
        boolean volver = false;
        int opcion;
        while (!volver) {
            mostrarMenuMedicamentos();
            opcion = SubsistemaAlimMedDAO.leerOpcion(sc);
            switch (opcion) {
                case 1:
                    SubsistemaAlimMedDAO.opInsertarMedicamento(sc, con, dao);
                    break;
                case 2:
                    SubsistemaAlimMedDAO.opObtenerMedicamento(sc, con, dao);
                    break;
                case 3:
                    SubsistemaAlimMedDAO.opListarMedicamentos(con, dao);
                    break;
                case 4:
                    SubsistemaAlimMedDAO.opActualizarMedicamento(sc, con, dao);
                    break;
                case 5:
                    SubsistemaAlimMedDAO.opEliminarMedicamento(sc, con, dao);
                    break;
                case 6:
                    SubsistemaAlimMedDAO.opInsertarLoteMedicamentos(sc, con, dao, daoAlmacenes);
                    break;
                case 7:
                    SubsistemaAlimMedDAO.opMedicamentosRefrigeracion(con, dao);
                    break;
                case 8:
                    SubsistemaAlimMedDAO.opInsertarPaciente(sc, con, dao);
                    break;
                case 9:
                    SubsistemaAlimMedDAO.opInsertarPrescripcion(sc, con, dao);
                    break;
                case 10:
                    SubsistemaAlimMedDAO.opPrescripcionesActivas(sc, con, dao);
                    break;
                case 11:
                    SubsistemaAlimMedDAO.opAsignarLoteMedicamentos(sc, con, dao);
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("  Opcion no valida. Introduzca un numero entre 0 y 11.");
                    break;
            }
        }
    }

    /**
     * Submenu de gestion de almacenes. Permanece activo hasta que el usuario
     * elige 0 para volver al menu principal.
     */
    private static void menuAlmacenes(Scanner sc, Connection con, AlmacenDAO dao) {
        boolean volver = false;
        int opcion;
        while (!volver) {
            mostrarMenuAlmacenes();
            opcion = SubsistemaAlimMedDAO.leerOpcion(sc);
            switch (opcion) {
                case 1:
                    SubsistemaAlimMedDAO.opInsertarAlmacenAlimentos(sc, con, dao);
                    break;
                case 2:
                    SubsistemaAlimMedDAO.opInsertarAlmacenMedicamentos(sc, con, dao);
                    break;
                case 3:
                    SubsistemaAlimMedDAO.opListarAlmacenes(con, dao);
                    break;
                case 4:
                    SubsistemaAlimMedDAO.opEliminarAlmacen(sc, con, dao);
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
     * Submenu de informes y ficheros. Permite exportar e importar productos
     * en CSV y generar informes HTML de lotes. Permanece activo hasta que
     * el usuario elige 0 para volver al menu principal.
     */
    private static void menuInformes(Scanner sc, Connection con,
            GestionAlimentosDAO daoAlimentos, GestionMedicamentosDAO daoMedicamentos) {
        boolean volver = false;
        int opcion;
        while (!volver) {
            mostrarMenuInformes();
            opcion = SubsistemaAlimMedDAO.leerOpcion(sc);
            switch (opcion) {
                case 1:
                    GestorFicherosAlimMed.exportarAlimentosCSV(sc, con, daoAlimentos);
                    break;
                case 2:
                    GestorFicherosAlimMed.importarAlimentosCSV(sc, con, daoAlimentos);
                    break;
                case 3:
                    GestorFicherosAlimMed.exportarMedicamentosCSV(sc, con, daoMedicamentos);
                    break;
                case 4:
                    GestorFicherosAlimMed.importarMedicamentosCSV(sc, con, daoMedicamentos);
                    break;
                case 5:
                    GestorFicherosAlimMed.generarInformeLotesAlimentosHTML(sc, con, daoAlimentos);
                    break;
                case 6:
                    GestorFicherosAlimMed.generarInformeLotesMedicamentosHTML(sc, con, daoMedicamentos);
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

    // =========================================================================
    // METODO PRINCIPAL
    // =========================================================================

    /**
     * Metodo principal. Gestiona la conexion a traves de Conexion_DB y
     * presenta el menu interactivo del subsistema.
     *
     * @param args Argumentos de linea de comandos (no se usan)
     * @throws Exception Si la conexion con la BD no puede establecerse
     */
    public static void iniciarSubsistemaAlimMed() throws Exception {
        Conexion_DB conexionDB = new Conexion_DB();
        Connection conexion = null;
        Scanner sc = new Scanner(System.in);

        GestionAlimentosDAO daoAlimentos;
        GestionMedicamentosDAO daoMedicamentos;
        AlmacenDAO daoAlmacenes;
        int opcion;

        try {
            conexion = conexionDB.abrirConexion();

            daoAlimentos = new GestionAlimentosDAO();
            daoMedicamentos = new GestionMedicamentosDAO();
            daoAlmacenes = new AlmacenDAO();

            boolean salir = false;
            while (!salir) {
                mostrarMenuPrincipal();
                opcion = SubsistemaAlimMedDAO.leerOpcion(sc);
                switch (opcion) {
                    case 1:
                        menuAlimentos(sc, conexion, daoAlimentos, daoAlmacenes);
                        break;
                    case 2:
                        menuMedicamentos(sc, conexion, daoMedicamentos, daoAlmacenes);
                        break;
                    case 3:
                        menuAlmacenes(sc, conexion, daoAlmacenes);
                        break;
                    case 4:
                        menuInformes(sc, conexion, daoAlimentos, daoMedicamentos);
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
