import DAO.*;
import Entidad.*;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

/**
 * Capa de operaciones del Subsistema de Patrullas.
 * <p>
 * Contiene todos los metodos de presentacion de entidades y de interaccion
 * con el usuario para cada operacion CRUD y de negocio. Es invocada
 * exclusivamente desde {@link ControladorSubsistemaPatrullas}.
 * </p>
 *
 * @author Code4Hope Team
 * @version 1.0
 * @see ControladorSubsistemaPatrullas
 */
public class SubsistemaPatrullasDAO {

    // =========================================================================
    // PRESENTACION DE ENTIDADES
    // =========================================================================

    /**
     * Muestra por consola los datos de una patrulla.
     *
     * @param p Patrulla a mostrar, o {@code null} si no se encontro
     */
    public static void mostrarPatrulla(Patrulla p) {
        if (p == null) {
            System.out.println("  (Patrulla no encontrada)");
        } else {
            System.out.println("  ID          : " + p.getId());
            System.out.println("  Codigo      : " + p.getCodigo());
            System.out.println("  Estado      : " + p.getEstado().getValor());
            System.out.println("  Vehiculo ID : " + (p.getVehiculoId() > 0 ? p.getVehiculoId() : "Sin asignar"));
            System.out.println("  Ruta ID     : " + (p.getRutaId() > 0 ? p.getRutaId() : "Sin asignar"));
            System.out.println("  Equipo ID   : " + (p.getEquipoComunicacionId() > 0 ? p.getEquipoComunicacionId() : "Sin asignar"));
        }
    }

    /**
     * Muestra por consola los datos de un tripulante.
     *
     * @param t Tripulante a mostrar, o {@code null} si no se encontro
     */
    public static void mostrarTripulante(Tripulante t) {
        if (t == null) {
            System.out.println("  (Tripulante no encontrado)");
        } else {
            System.out.println("  ID          : " + t.getId());
            System.out.println("  NIF         : " + t.getNif());
            System.out.println("  Nombre      : " + t.getNombreCompleto());
            System.out.println("  Telefono    : " + t.getTelefonoContacto());
            System.out.println("  Rol         : " + t.getRol().getValor());
            System.out.println("  Estado      : " + t.getEstadoOperativo().getValor());
            System.out.println("  Patrulla ID : " + (t.getPatrullaId() > 0 ? t.getPatrullaId() : "Sin asignar"));
        }
    }

    /**
     * Muestra por consola los datos de un vehiculo.
     *
     * @param v Vehiculo a mostrar, o {@code null} si no se encontro
     */
    public static void mostrarVehiculo(Vehiculo v) {
        if (v == null) {
            System.out.println("  (Vehiculo no encontrado)");
        } else {
            System.out.println("  ID          : " + v.getId());
            System.out.println("  Codigo      : " + v.getCodigo());
            System.out.println("  Tipo        : " + v.getTipo().getValor());
            System.out.println("  Matricula   : " + v.getMatricula());
            System.out.println("  Refrigerado : " + v.isRefrigerado());
            System.out.println("  Disponible  : " + v.isDisponible());
        }
    }

    /**
     * Muestra por consola los datos de una ruta.
     *
     * @param r Ruta a mostrar, o {@code null} si no se encontro
     */
    public static void mostrarRuta(Ruta r) {
        if (r == null) {
            System.out.println("  (Ruta no encontrada)");
        } else {
            System.out.println("  ID           : " + r.getId());
            System.out.println("  Nombre       : " + r.getNombre());
            System.out.println("  Estado       : " + r.getEstado().getValor());
            System.out.println("  Fecha mision : " + r.getFechaMision());
            System.out.println("  Hora inicio  : " + r.getHoraInicio());
            System.out.println("  Hora fin     : " + r.getHoraFin());
            System.out.println("  Punto actual : " + r.getIndicePuntoActual());
            System.out.println("  Peligrosidad : " + r.getGradoPeligrosidad());
            System.out.println("  Km           : " + r.getNumKm());
        }
    }

    /**
     * Muestra por consola los datos de un punto de ruta.
     *
     * @param p PuntoRuta a mostrar, o {@code null} si no se encontro
     */
    public static void mostrarPuntoRuta(PuntoRuta p) {
        if (p == null) {
            System.out.println("  (Punto de ruta no encontrado)");
        } else {
            System.out.println("  ID          : " + p.getId());
            System.out.println("  Ruta ID     : " + p.getRutaId());
            System.out.println("  Posicion    : " + p.getPosicion());
            System.out.println("  Nombre      : " + p.getNombre());
            System.out.println("  Tipo        : " + p.getTipo().getValor());
            System.out.println("  Estado      : " + p.getEstado().getValor());
            System.out.println("  Lat/Lon     : " + p.getLatitud() + " / " + p.getLongitud());
            System.out.println("  Hora est.   : " + p.getHoraEstimada());
            System.out.println("  Hora real   : " + p.getHoraRealLlegada());
            System.out.println("  Gasolinera  : " + p.isEsGasolinera());
            System.out.println("  Notas       : " + p.getNotasIncidencia());
        }
    }

    /**
     * Muestra por consola los datos de un equipo de comunicacion.
     *
     * @param e EquipoComunicacion a mostrar, o {@code null} si no se encontro
     */
    public static void mostrarEquipo(EquipoComunicacion e) {
        if (e == null) {
            System.out.println("  (Equipo no encontrado)");
        } else {
            System.out.println("  ID     : " + e.getId());
            System.out.println("  Nombre : " + e.getNombre());
            System.out.println("  Estado : " + e.getEstadoEquipo().getValor());
        }
    }

    // =========================================================================
    // OPERACIONES DE PATRULLAS
    // =========================================================================

    /**
     * Solicita un codigo al usuario y crea una nueva patrulla en estado INACTIVA.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Patrulla}
     */
    public static void opCrearPatrulla(Scanner sc, Connection con, PatrullaDAO dao) {
        System.out.println("\n  -- Crear patrulla --");
        System.out.print("  Codigo : ");
        String codigo = sc.nextLine();
        Patrulla p = new Patrulla();
        p.setCodigo(codigo);
        p.setEstado(Patrulla.Estado.INACTIVA);
        try {
            dao.insertar(con, p);
            System.out.println("  Patrulla creada con ID: " + p.getId());
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Busca una patrulla por su ID y muestra sus datos.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Patrulla}
     */
    public static void opBuscarPatrullaPorId(Scanner sc, Connection con, PatrullaDAO dao) {
        System.out.println("\n  -- Buscar patrulla por ID --");
        System.out.print("  ID: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            mostrarPatrulla(dao.findById(con, id));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Lista todas las patrullas registradas en la base de datos.
     *
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Patrulla}
     */
    public static void opListarPatrullas(Connection con, PatrullaDAO dao) {
        System.out.println("\n  -- Todas las patrullas --");
        try {
            List<Patrulla> lista = dao.findAll(con);
            int i = 0;
            while (i < lista.size()) {
                System.out.println("  ----------------------------------------");
                mostrarPatrulla(lista.get(i));
                i++;
            }
            if (lista.isEmpty()) { System.out.println("  (No hay patrullas registradas)"); }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Permite cambiar el estado operativo de una patrulla existente.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Patrulla}
     */
    public static void opActualizarEstadoPatrulla(Scanner sc, Connection con, PatrullaDAO dao) {
        System.out.println("\n  -- Actualizar estado de patrulla --");
        System.out.print("  ID de la patrulla: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            Patrulla p = dao.findById(con, id);
            if (p == null) {
                System.out.println("  Patrulla no encontrada.");
            } else {
                System.out.println("  Estado actual: " + p.getEstado().getValor());
                System.out.println("  Estados: Inactiva / Preparada / EnMision / Completada / Abortada");
                System.out.print("  Nuevo estado : ");
                p.setEstado(Patrulla.Estado.fromString(sc.nextLine()));
                boolean ok = dao.actualizar(con, p);
                System.out.println("  Resultado: " + (ok ? "Actualizado correctamente" : "No se pudo actualizar"));
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Muestra los vehiculos disponibles y asigna el elegido a una patrulla.
     *
     * @param sc   Scanner para leer la entrada del usuario
     * @param con  Conexion activa a la base de datos
     * @param daoP DAO de {@link Patrulla}
     * @param daoV DAO de {@link Vehiculo}
     */
    public static void opAsignarVehiculoPatrulla(Scanner sc, Connection con,
            PatrullaDAO daoP, VehiculoDAO daoV) {
        System.out.println("\n  -- Asignar vehiculo a patrulla --");
        System.out.print("  ID de la patrulla: ");
        int idPatrulla = Integer.parseInt(sc.nextLine());
        try {
            List<Vehiculo> disponibles = daoV.findDisponibles(con);
            if (disponibles.isEmpty()) {
                System.out.println("  No hay vehiculos disponibles.");
            } else {
                System.out.println("  Vehiculos disponibles:");
                int i = 0;
                while (i < disponibles.size()) {
                    System.out.println("  " + disponibles.get(i).toString());
                    i++;
                }
                System.out.print("  ID del vehiculo: ");
                int idVehiculo = Integer.parseInt(sc.nextLine());
                boolean ok = daoP.asignarVehiculo(con, idPatrulla, idVehiculo);
                if (ok) {
                    daoV.setDisponible(con, idVehiculo, false);
                    System.out.println("  Vehiculo asignado correctamente.");
                } else {
                    System.out.println("  No se pudo asignar el vehiculo.");
                }
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Muestra las rutas disponibles y asigna la elegida a una patrulla.
     *
     * @param sc   Scanner para leer la entrada del usuario
     * @param con  Conexion activa a la base de datos
     * @param daoP DAO de {@link Patrulla}
     * @param daoR DAO de {@link Ruta}
     */
    public static void opAsignarRutaPatrulla(Scanner sc, Connection con,
            PatrullaDAO daoP, RutaDAO daoR) {
        System.out.println("\n  -- Asignar ruta a patrulla --");
        System.out.print("  ID de la patrulla: ");
        int idPatrulla = Integer.parseInt(sc.nextLine());
        try {
            List<Ruta> rutas = daoR.findAll(con);
            if (rutas.isEmpty()) {
                System.out.println("  No hay rutas registradas.");
            } else {
                System.out.println("  Rutas disponibles:");
                int i = 0;
                while (i < rutas.size()) {
                    System.out.println("  " + rutas.get(i).toString());
                    i++;
                }
                System.out.print("  ID de la ruta: ");
                int idRuta = Integer.parseInt(sc.nextLine());
                boolean ok = daoP.asignarRuta(con, idPatrulla, idRuta);
                System.out.println("  Resultado: " + (ok ? "Ruta asignada correctamente" : "No se pudo asignar"));
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Muestra los equipos de comunicacion y asigna el elegido a una patrulla.
     *
     * @param sc   Scanner para leer la entrada del usuario
     * @param con  Conexion activa a la base de datos
     * @param daoP DAO de {@link Patrulla}
     * @param daoE DAO de {@link EquipoComunicacion}
     */
    public static void opAsignarEquipoPatrulla(Scanner sc, Connection con,
            PatrullaDAO daoP, EquipoComunicacionDAO daoE) {
        System.out.println("\n  -- Asignar equipo COM a patrulla --");
        System.out.print("  ID de la patrulla: ");
        int idPatrulla = Integer.parseInt(sc.nextLine());
        try {
            List<EquipoComunicacion> equipos = daoE.findAll(con);
            if (equipos.isEmpty()) {
                System.out.println("  No hay equipos registrados.");
            } else {
                System.out.println("  Equipos disponibles:");
                int i = 0;
                while (i < equipos.size()) {
                    System.out.println("  " + equipos.get(i).toString());
                    i++;
                }
                System.out.print("  ID del equipo: ");
                int idEquipo = Integer.parseInt(sc.nextLine());
                boolean ok = daoP.asignarEquipoComunicacion(con, idPatrulla, idEquipo);
                System.out.println("  Resultado: " + (ok ? "Equipo asignado correctamente" : "No se pudo asignar"));
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Asigna un tripulante a una patrulla por sus respectivos IDs.
     *
     * @param sc   Scanner para leer la entrada del usuario
     * @param con  Conexion activa a la base de datos
     * @param daoT DAO de {@link Tripulante}
     */
    public static void opAsignarTripulantePatrulla(Scanner sc, Connection con, TripulanteDAO daoT) {
        System.out.println("\n  -- Asignar tripulante a patrulla --");
        System.out.print("  ID del tripulante : ");
        int idTripulante = Integer.parseInt(sc.nextLine());
        System.out.print("  ID de la patrulla : ");
        int idPatrulla = Integer.parseInt(sc.nextLine());
        try {
            boolean ok = daoT.asignarAPatrulla(con, idTripulante, idPatrulla);
            System.out.println("  Resultado: " + (ok ? "Tripulante asignado correctamente" : "No se pudo asignar"));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Lista todos los tripulantes asignados a una patrulla.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Tripulante}
     */
    public static void opVerTripulantesPatrulla(Scanner sc, Connection con, TripulanteDAO dao) {
        System.out.println("\n  -- Tripulantes de una patrulla --");
        System.out.print("  ID de la patrulla: ");
        int idPatrulla = Integer.parseInt(sc.nextLine());
        try {
            List<Tripulante> lista = dao.findByPatrullaId(con, idPatrulla);
            int i = 0;
            while (i < lista.size()) {
                System.out.println("  ----------------------------------------");
                mostrarTripulante(lista.get(i));
                i++;
            }
            if (lista.isEmpty()) { System.out.println("  (La patrulla no tiene tripulantes asignados)"); }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Añade un recurso (cadena de texto) a los recursos de una patrulla.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Patrulla}
     */
    public static void opAddRecursoPatrulla(Scanner sc, Connection con, PatrullaDAO dao) {
        System.out.println("\n  -- Añadir recurso a patrulla --");
        System.out.print("  ID de la patrulla : ");
        int idPatrulla = Integer.parseInt(sc.nextLine());
        System.out.print("  Recurso           : ");
        String recurso = sc.nextLine();
        try {
            boolean ok = dao.addRecurso(con, idPatrulla, recurso);
            System.out.println("  Resultado: " + (ok ? "Recurso añadido correctamente" : "No se pudo añadir"));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Lista todos los recursos asignados a una patrulla.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Patrulla}
     */
    public static void opVerRecursosPatrulla(Scanner sc, Connection con, PatrullaDAO dao) {
        System.out.println("\n  -- Recursos de una patrulla --");
        System.out.print("  ID de la patrulla: ");
        int idPatrulla = Integer.parseInt(sc.nextLine());
        try {
            List<String> recursos = dao.getRecursos(con, idPatrulla);
            int i = 0;
            while (i < recursos.size()) {
                System.out.println("  - " + recursos.get(i));
                i++;
            }
            if (recursos.isEmpty()) { System.out.println("  (La patrulla no tiene recursos asignados)"); }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Elimina una patrulla de la base de datos por su ID.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Patrulla}
     */
    public static void opEliminarPatrulla(Scanner sc, Connection con, PatrullaDAO dao) {
        System.out.println("\n  -- Eliminar patrulla --");
        System.out.print("  ID de la patrulla a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            boolean ok = dao.eliminar(con, id);
            System.out.println("  Resultado: " + (ok ? "Eliminada correctamente" : "No se encontro la patrulla"));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // OPERACIONES DE TRIPULANTES
    // =========================================================================

    /**
     * Solicita los datos al usuario y crea un nuevo tripulante en estado DISPONIBLE.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Tripulante}
     */
    public static void opCrearTripulante(Scanner sc, Connection con, TripulanteDAO dao) {
        System.out.println("\n  -- Crear tripulante --");
        System.out.print("  NIF      : ");
        String nif = sc.nextLine();
        System.out.print("  Nombre   : ");
        String nombre = sc.nextLine();
        System.out.print("  Apellido : ");
        String apellido = sc.nextLine();
        System.out.print("  Telefono : ");
        String telefono = sc.nextLine();
        System.out.println("  Rol (Conductor / Agente / Jefe / Soporte):");
        System.out.print("  Rol      : ");
        String rolStr = sc.nextLine();
        Tripulante t = new Tripulante();
        t.setNif(nif);
        t.setNombre(nombre);
        t.setApellido(apellido);
        t.setTelefonoContacto(telefono);
        t.setRol(Tripulante.Rol.fromString(rolStr));
        t.setEstadoOperativo(Tripulante.EstadoOperativo.DISPONIBLE);
        try {
            dao.insertar(con, t);
            System.out.println("  Tripulante creado con ID: " + t.getId());
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Busca un tripulante por su ID y muestra sus datos.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Tripulante}
     */
    public static void opBuscarTripulantePorId(Scanner sc, Connection con, TripulanteDAO dao) {
        System.out.println("\n  -- Buscar tripulante por ID --");
        System.out.print("  ID: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            mostrarTripulante(dao.findById(con, id));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Lista todos los tripulantes registrados en la base de datos.
     *
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Tripulante}
     */
    public static void opListarTripulantes(Connection con, TripulanteDAO dao) {
        System.out.println("\n  -- Todos los tripulantes --");
        try {
            List<Tripulante> lista = dao.findAll(con);
            int i = 0;
            while (i < lista.size()) {
                System.out.println("  ----------------------------------------");
                mostrarTripulante(lista.get(i));
                i++;
            }
            if (lista.isEmpty()) { System.out.println("  (No hay tripulantes registrados)"); }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Libera a un tripulante de su patrulla asignada y lo pone como DISPONIBLE.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Tripulante}
     */
    public static void opLiberarTripulante(Scanner sc, Connection con, TripulanteDAO dao) {
        System.out.println("\n  -- Liberar tripulante de patrulla --");
        System.out.print("  ID del tripulante: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            boolean ok = dao.liberar(con, id);
            System.out.println("  Resultado: " + (ok ? "Tripulante liberado correctamente" : "No se pudo liberar"));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Elimina un tripulante de la base de datos por su ID.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Tripulante}
     */
    public static void opEliminarTripulante(Scanner sc, Connection con, TripulanteDAO dao) {
        System.out.println("\n  -- Eliminar tripulante --");
        System.out.print("  ID del tripulante a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            boolean ok = dao.eliminar(con, id);
            System.out.println("  Resultado: " + (ok ? "Eliminado correctamente" : "No se encontro el tripulante"));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // OPERACIONES DE VEHICULOS
    // =========================================================================

    /**
     * Solicita los datos al usuario y crea un nuevo vehiculo disponible.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Vehiculo}
     */
    public static void opCrearVehiculo(Scanner sc, Connection con, VehiculoDAO dao) {
        System.out.println("\n  -- Crear vehiculo --");
        System.out.print("  Codigo    : ");
        String codigo = sc.nextLine();
        System.out.println("  Tipo (Coche / Moto / Furgoneta / Camion):");
        System.out.print("  Tipo      : ");
        String tipoStr = sc.nextLine();
        System.out.print("  Matricula : ");
        String matricula = sc.nextLine();
        System.out.print("  Refrigerado (true/false): ");
        boolean refrigerado = Boolean.parseBoolean(sc.nextLine());
        Vehiculo v = new Vehiculo();
        v.setCodigo(codigo);
        v.setTipo(Vehiculo.TipoVehiculo.fromString(tipoStr));
        v.setMatricula(matricula);
        v.setRefrigerado(refrigerado);
        v.setDisponible(true);
        try {
            dao.insertar(con, v);
            System.out.println("  Vehiculo creado con ID: " + v.getId());
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Busca un vehiculo por su ID y muestra sus datos.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Vehiculo}
     */
    public static void opBuscarVehiculoPorId(Scanner sc, Connection con, VehiculoDAO dao) {
        System.out.println("\n  -- Buscar vehiculo por ID --");
        System.out.print("  ID: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            mostrarVehiculo(dao.findById(con, id));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Lista todos los vehiculos registrados en la base de datos.
     *
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Vehiculo}
     */
    public static void opListarVehiculos(Connection con, VehiculoDAO dao) {
        System.out.println("\n  -- Todos los vehiculos --");
        try {
            List<Vehiculo> lista = dao.findAll(con);
            int i = 0;
            while (i < lista.size()) {
                System.out.println("  ----------------------------------------");
                mostrarVehiculo(lista.get(i));
                i++;
            }
            if (lista.isEmpty()) { System.out.println("  (No hay vehiculos registrados)"); }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Lista unicamente los vehiculos con disponibilidad en true.
     *
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Vehiculo}
     */
    public static void opListarVehiculosDisponibles(Connection con, VehiculoDAO dao) {
        System.out.println("\n  -- Vehiculos disponibles --");
        try {
            List<Vehiculo> lista = dao.findDisponibles(con);
            int i = 0;
            while (i < lista.size()) {
                System.out.println("  ----------------------------------------");
                mostrarVehiculo(lista.get(i));
                i++;
            }
            if (lista.isEmpty()) { System.out.println("  (No hay vehiculos disponibles)"); }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Elimina un vehiculo de la base de datos por su ID.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Vehiculo}
     */
    public static void opEliminarVehiculo(Scanner sc, Connection con, VehiculoDAO dao) {
        System.out.println("\n  -- Eliminar vehiculo --");
        System.out.print("  ID del vehiculo a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            boolean ok = dao.eliminar(con, id);
            System.out.println("  Resultado: " + (ok ? "Eliminado correctamente" : "No se encontro el vehiculo"));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // OPERACIONES DE RUTAS
    // =========================================================================

    /**
     * Solicita los datos al usuario y crea una nueva ruta en estado PENDIENTE.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Ruta}
     */
    public static void opCrearRuta(Scanner sc, Connection con, RutaDAO dao) {
        System.out.println("\n  -- Crear ruta --");
        System.out.print("  Nombre                   : ");
        String nombre = sc.nextLine();
        System.out.print("  Fecha mision (AAAA-MM-DD): ");
        String fecha = sc.nextLine();
        System.out.print("  Grado peligrosidad       : ");
        String peligro = sc.nextLine();
        System.out.print("  Kilometros               : ");
        float km = Float.parseFloat(sc.nextLine());
        Ruta r = new Ruta();
        r.setNombre(nombre);
        r.setEstado(Ruta.EstadoRuta.PENDIENTE);
        r.setFechaMision(fecha);
        r.setGradoPeligrosidad(peligro);
        r.setNumKm(km);
        r.setIndicePuntoActual(0);
        try {
            dao.insertar(con, r);
            System.out.println("  Ruta creada con ID: " + r.getId());
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Busca una ruta por su ID y muestra sus datos.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Ruta}
     */
    public static void opBuscarRutaPorId(Scanner sc, Connection con, RutaDAO dao) {
        System.out.println("\n  -- Buscar ruta por ID --");
        System.out.print("  ID: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            mostrarRuta(dao.findById(con, id));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Lista todas las rutas registradas en la base de datos.
     *
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Ruta}
     */
    public static void opListarRutas(Connection con, RutaDAO dao) {
        System.out.println("\n  -- Todas las rutas --");
        try {
            List<Ruta> lista = dao.findAll(con);
            int i = 0;
            while (i < lista.size()) {
                System.out.println("  ----------------------------------------");
                mostrarRuta(lista.get(i));
                i++;
            }
            if (lista.isEmpty()) { System.out.println("  (No hay rutas registradas)"); }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Solicita los datos al usuario y añade un nuevo punto a una ruta existente.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link PuntoRuta}
     */
    public static void opAnadirPuntoRuta(Scanner sc, Connection con, PuntoRutaDAO dao) {
        System.out.println("\n  -- Añadir punto a ruta --");
        System.out.print("  ID de la ruta            : ");
        int idRuta = Integer.parseInt(sc.nextLine());
        System.out.print("  Nombre del punto         : ");
        String nombre = sc.nextLine();
        System.out.print("  Descripcion              : ");
        String desc = sc.nextLine();
        System.out.println("  Tipo (Inicio / Control / Incidencia / Fin / Gasolinera):");
        System.out.print("  Tipo                     : ");
        String tipoStr = sc.nextLine();
        System.out.print("  Latitud                  : ");
        double lat = Double.parseDouble(sc.nextLine());
        System.out.print("  Longitud                 : ");
        double lon = Double.parseDouble(sc.nextLine());
        System.out.print("  Hora estimada (HH:mm:ss) : ");
        String horaEst = sc.nextLine();
        System.out.print("  Posicion en ruta         : ");
        int posicion = Integer.parseInt(sc.nextLine());
        System.out.print("  Es gasolinera (true/false): ");
        boolean esGas = Boolean.parseBoolean(sc.nextLine());
        PuntoRuta p = new PuntoRuta();
        p.setRutaId(idRuta);
        p.setNombre(nombre);
        p.setDescripcion(desc);
        p.setTipo(PuntoRuta.TipoPunto.fromString(tipoStr));
        p.setLatitud(lat);
        p.setLongitud(lon);
        p.setEstado(PuntoRuta.EstadoPunto.PENDIENTE);
        p.setHoraEstimada(horaEst);
        p.setPosicion(posicion);
        p.setEsGasolinera(esGas);
        try {
            dao.insertar(con, p);
            System.out.println("  Punto creado con ID: " + p.getId());
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Lista todos los puntos de ruta de una ruta concreta.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link PuntoRuta}
     */
    public static void opVerPuntosRuta(Scanner sc, Connection con, PuntoRutaDAO dao) {
        System.out.println("\n  -- Puntos de una ruta --");
        System.out.print("  ID de la ruta: ");
        int idRuta = Integer.parseInt(sc.nextLine());
        try {
            List<PuntoRuta> lista = dao.findByRutaId(con, idRuta);
            int i = 0;
            while (i < lista.size()) {
                System.out.println("  ----------------------------------------");
                mostrarPuntoRuta(lista.get(i));
                i++;
            }
            if (lista.isEmpty()) { System.out.println("  (La ruta no tiene puntos definidos)"); }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Elimina una ruta de la base de datos por su ID.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link Ruta}
     */
    public static void opEliminarRuta(Scanner sc, Connection con, RutaDAO dao) {
        System.out.println("\n  -- Eliminar ruta --");
        System.out.print("  ID de la ruta a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            boolean ok = dao.eliminar(con, id);
            System.out.println("  Resultado: " + (ok ? "Eliminada correctamente" : "No se encontro la ruta"));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // OPERACIONES DE EQUIPOS DE COMUNICACION
    // =========================================================================

    /**
     * Solicita el nombre al usuario y crea un nuevo equipo de comunicacion en estado ACTIVO.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link EquipoComunicacion}
     */
    public static void opCrearEquipo(Scanner sc, Connection con, EquipoComunicacionDAO dao) {
        System.out.println("\n  -- Crear equipo de comunicacion --");
        System.out.print("  Nombre : ");
        String nombre = sc.nextLine();
        EquipoComunicacion e = new EquipoComunicacion();
        e.setNombre(nombre);
        e.setEstadoEquipo(EquipoComunicacion.EstadoEquipo.ACTIVO);
        try {
            dao.insertar(con, e);
            System.out.println("  Equipo creado con ID: " + e.getId());
        } catch (Exception ex) {
            System.out.println("  Error: " + ex.getMessage());
        }
    }

    /**
     * Busca un equipo de comunicacion por su ID y muestra sus datos.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link EquipoComunicacion}
     */
    public static void opBuscarEquipoPorId(Scanner sc, Connection con, EquipoComunicacionDAO dao) {
        System.out.println("\n  -- Buscar equipo por ID --");
        System.out.print("  ID: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            mostrarEquipo(dao.findById(con, id));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Lista todos los equipos de comunicacion registrados.
     *
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link EquipoComunicacion}
     */
    public static void opListarEquipos(Connection con, EquipoComunicacionDAO dao) {
        System.out.println("\n  -- Todos los equipos de comunicacion --");
        try {
            List<EquipoComunicacion> lista = dao.findAll(con);
            int i = 0;
            while (i < lista.size()) {
                System.out.println("  ----------------------------------------");
                mostrarEquipo(lista.get(i));
                i++;
            }
            if (lista.isEmpty()) { System.out.println("  (No hay equipos registrados)"); }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Solicita los datos al usuario y registra una nueva comunicacion en un equipo.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link RegistroComunicacion}
     */
    public static void opRegistrarComunicacion(Scanner sc, Connection con, RegistroComunicacionDAO dao) {
        System.out.println("\n  -- Registrar comunicacion --");
        System.out.print("  ID del equipo    : ");
        int idEquipo = Integer.parseInt(sc.nextLine());
        System.out.print("  Hora (HH:mm:ss)  : ");
        String hora = sc.nextLine();
        System.out.println("  Tipo (Texto / Alerta / Confirmacion / Emergencia):");
        System.out.print("  Tipo             : ");
        String tipoStr = sc.nextLine();
        System.out.print("  Mensaje          : ");
        String mensaje = sc.nextLine();
        System.out.print("  Emisor           : ");
        String emisor = sc.nextLine();
        RegistroComunicacion r = new RegistroComunicacion();
        r.setEquipoId(idEquipo);
        r.setHora(hora);
        r.setTipo(RegistroComunicacion.TipoMensaje.fromString(tipoStr));
        r.setMensaje(mensaje);
        r.setEmisor(emisor);
        try {
            dao.insertar(con, r);
            System.out.println("  Registro creado con ID: " + r.getId());
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Lista todas las comunicaciones registradas en un equipo.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link RegistroComunicacion}
     */
    public static void opVerComunicaciones(Scanner sc, Connection con, RegistroComunicacionDAO dao) {
        System.out.println("\n  -- Comunicaciones de un equipo --");
        System.out.print("  ID del equipo: ");
        int idEquipo = Integer.parseInt(sc.nextLine());
        try {
            List<RegistroComunicacion> lista = dao.findByEquipoId(con, idEquipo);
            int i = 0;
            while (i < lista.size()) {
                System.out.println("  " + lista.get(i).toString());
                i++;
            }
            if (lista.isEmpty()) { System.out.println("  (El equipo no tiene comunicaciones registradas)"); }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Solicita una entrada al usuario y la añade al log de un equipo.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link EquipoComunicacion}
     */
    public static void opAnadirLog(Scanner sc, Connection con, EquipoComunicacionDAO dao) {
        System.out.println("\n  -- Añadir entrada al log --");
        System.out.print("  ID del equipo : ");
        int idEquipo = Integer.parseInt(sc.nextLine());
        System.out.print("  Entrada       : ");
        String entrada = sc.nextLine();
        EquipoLog log = new EquipoLog();
        log.setEquipoId(idEquipo);
        log.setEntrada(entrada);
        try {
            boolean ok = dao.insertarLog(con, log);
            System.out.println("  Resultado: " + (ok ? "Entrada añadida al log" : "No se pudo añadir"));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Lista todas las entradas del log de un equipo de comunicacion.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link EquipoComunicacion}
     */
    public static void opVerLog(Scanner sc, Connection con, EquipoComunicacionDAO dao) {
        System.out.println("\n  -- Log de un equipo --");
        System.out.print("  ID del equipo: ");
        int idEquipo = Integer.parseInt(sc.nextLine());
        try {
            List<EquipoLog> lista = dao.getLog(con, idEquipo);
            int i = 0;
            while (i < lista.size()) {
                System.out.println("  " + lista.get(i).toString());
                i++;
            }
            if (lista.isEmpty()) { System.out.println("  (El equipo no tiene entradas en el log)"); }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /**
     * Elimina un equipo de comunicacion de la base de datos por su ID.
     *
     * @param sc  Scanner para leer la entrada del usuario
     * @param con Conexion activa a la base de datos
     * @param dao DAO de {@link EquipoComunicacion}
     */
    public static void opEliminarEquipo(Scanner sc, Connection con, EquipoComunicacionDAO dao) {
        System.out.println("\n  -- Eliminar equipo de comunicacion --");
        System.out.print("  ID del equipo a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());
        try {
            boolean ok = dao.eliminar(con, id);
            System.out.println("  Resultado: " + (ok ? "Eliminado correctamente" : "No se encontro el equipo"));
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }
}
