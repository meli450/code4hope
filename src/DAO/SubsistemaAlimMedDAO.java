package DAO;

import Entidad.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Clase auxiliar que centraliza todas las operaciones interactivas del
 * subsistema de Gestion de Alimentos y Medicamentos.
 * Contiene los metodos op* (operaciones de consola) y los metodos de
 * lectura/presentacion utilizados por dichas operaciones.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class SubsistemaAlimMedDAO {

    // =========================================================================
    // METODOS AUXILIARES DE ENTRADA
    // =========================================================================

    /**
     * Lee un entero desde consola. Devuelve -1 si la entrada no es valida.
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
     * Lee un decimal desde consola. Repite hasta obtener una entrada valida.
     * Acepta tanto punto como coma como separador decimal.
     */
    public static double leerDecimal(Scanner sc) {
        double valor = 0.0;
        boolean valido = false;
        while (!valido) {
            try {
                valor = Double.parseDouble(sc.nextLine().trim().replace(",", "."));
                valido = true;
            } catch (NumberFormatException e) {
                System.out.print("  Entrada no valida. Introduzca un numero decimal: ");
            }
        }
        return valor;
    }

    /**
     * Lee un booleano desde consola. Acepta s/n, si/no, true/false.
     * Repite hasta obtener una entrada valida.
     */
    public static boolean leerBoolean(Scanner sc) {
        boolean valor = false;
        boolean valido = false;
        String entrada;
        while (!valido) {
            entrada = sc.nextLine().trim().toLowerCase();
            if ("s".equals(entrada) || "si".equals(entrada) || "true".equals(entrada)) {
                valor = true;
                valido = true;
            } else if ("n".equals(entrada) || "no".equals(entrada) || "false".equals(entrada)) {
                valor = false;
                valido = true;
            } else {
                System.out.print("  Entrada no valida. Introduzca s/n: ");
            }
        }
        return valor;
    }

    /**
     * Lee una fecha obligatoria desde consola aceptando multiples formatos.
     * Formatos validos: dd/MM/yyyy, yyyy/MM/dd, dd-MM-yyyy, yyyy-MM-dd.
     * Repite hasta obtener una fecha valida no vacia.
     */
    public static LocalDate leerFecha(Scanner sc) {
        LocalDate fecha = null;
        boolean valido = false;
        String entrada;
        DateTimeFormatter[] formatos = {
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        };
        while (!valido) {
            entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.print("  La fecha es obligatoria. Introduzca la fecha: ");
            } else {
                int i = 0;
                while (i < formatos.length && fecha == null) {
                    try {
                        fecha = LocalDate.parse(entrada, formatos[i]);
                    } catch (DateTimeParseException e) {
                        // probar siguiente formato
                    }
                    i++;
                }
                if (fecha != null) {
                    valido = true;
                } else {
                    System.out.print("  Formato no reconocido. Use DD/MM/AAAA, AAAA/MM/DD o DD-MM-AAAA: ");
                }
            }
        }
        return fecha;
    }

    /**
     * Lee una fecha opcional desde consola aceptando multiples formatos.
     * Devuelve null si el usuario no introduce nada.
     * Formatos validos: dd/MM/yyyy, yyyy/MM/dd, dd-MM-yyyy, yyyy-MM-dd.
     */
    public static LocalDate leerFechaOpcional(Scanner sc) {
        LocalDate fecha = null;
        boolean valido = false;
        String entrada;
        DateTimeFormatter[] formatos = {
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        };
        while (!valido) {
            entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                valido = true;
            } else {
                int i = 0;
                while (i < formatos.length && fecha == null) {
                    try {
                        fecha = LocalDate.parse(entrada, formatos[i]);
                    } catch (DateTimeParseException e) {
                        // probar siguiente formato
                    }
                    i++;
                }
                if (fecha != null) {
                    valido = true;
                } else {
                    System.out.print("  Formato no reconocido. Use DD/MM/AAAA, AAAA/MM/DD o DD-MM-AAAA: ");
                }
            }
        }
        return fecha;
    }

    // =========================================================================
    // SUBSISTEMA ALIMENTOS
    // =========================================================================

    /**
     * Muestra por pantalla los datos de un alimento.
     */
    public static void mostrarAlimento(Alimento alimento) {
        if (alimento == null) {
            System.out.println("  (Alimento no encontrado)");
        } else {
            System.out.println("  ID          : " + alimento.getIdProducto());
            System.out.println("  Nombre      : " + alimento.getNombre());
            System.out.println("  Descripcion : " + alimento.getDescripcion());
            System.out.println("  Unidad      : " + alimento.getUnidadMedida());
            System.out.println("  Precio      : " + alimento.getPrecio());
            System.out.println("  Categoria   : " + alimento.getCategoria());
            System.out.println("  Calorias    : " + alimento.getCalorias());
            System.out.println("  Tipo dieta  : " + alimento.getTipoDieta());
            System.out.println("  Refriger.   : " + alimento.isNecesitaRefrigeracion());
            System.out.println("  Temp. min   : " + alimento.getTemperaturaMin());
            System.out.println("  Temp. max   : " + alimento.getTemperaturaMax());
        }
    }

    /**
     * Lee los datos de un nuevo alimento por consola y lo inserta en la BD.
     */
    public static void opInsertarAlimento(Scanner sc, Connection con, GestionAlimentosDAO dao) {
        String nombre;
        String desc;
        String unidad;
        double precio;
        String cat;
        int cal;
        String dieta;
        boolean refrig;
        double tmin;
        double tmax;
        Alimento alimento;
        int id;
        System.out.println("\n  -- Insertar alimento --");
        System.out.print("  Nombre         : ");
        nombre = sc.nextLine();
        System.out.print("  Descripcion    : ");
        desc = sc.nextLine();
        System.out.print("  Unidad medida  : ");
        unidad = sc.nextLine();
        System.out.print("  Precio         : ");
        precio = leerDecimal(sc);
        System.out.print("  Categoria      : ");
        cat = sc.nextLine();
        System.out.print("  Calorias       : ");
        cal = leerEntero(sc);
        System.out.print("  Tipo dieta     : ");
        dieta = sc.nextLine();
        System.out.print("  Refrigeracion (s/n): ");
        refrig = leerBoolean(sc);
        System.out.print("  Temp. min      : ");
        tmin = leerDecimal(sc);
        System.out.print("  Temp. max      : ");
        tmax = leerDecimal(sc);

        alimento = new Alimento(0, nombre, desc, unidad, precio, cat,
                cal, dieta, refrig, tmin, tmax);
        id = dao.insertarAlimento(con, alimento);
        System.out.println("  Alimento creado con ID: " + id);
    }

    /**
     * Pide un nombre por consola, busca alimentos que lo contengan y devuelve
     * el alimento elegido por el usuario. Devuelve null si no hay resultados
     * o la eleccion no es valida.
     */
    public static Alimento seleccionarAlimento(Scanner sc, Connection con, GestionAlimentosDAO dao) {
        String nombre;
        List<Alimento> resultados;
        Alimento seleccionado;
        int eleccion;
        int i;

        System.out.print("  Nombre del alimento: ");
        nombre = sc.nextLine();
        resultados = dao.buscarAlimentosPorNombre(con, nombre);

        if (resultados.isEmpty()) {
            System.out.println("  No se encontraron alimentos con ese nombre.");
            seleccionado = null;
        } else if (resultados.size() == 1) {
            seleccionado = resultados.get(0);
            System.out.println("  Alimento encontrado: " + seleccionado.getNombre());
        } else {
            System.out.println("  Se encontraron varios alimentos:");
            i = 0;
            while (i < resultados.size()) {
                System.out.println("  [" + (i + 1) + "] " + resultados.get(i).getNombre());
                i++;
            }
            System.out.print("  Elige uno: ");
            eleccion = leerOpcion(sc);
            if (eleccion > 0 && eleccion <= resultados.size()) {
                seleccionado = resultados.get(eleccion - 1);
            } else {
                System.out.println("  Eleccion no valida. Operacion cancelada.");
                seleccionado = null;
            }
        }

        return seleccionado;
    }

    /**
     * Busca alimentos por nombre y muestra el resultado seleccionado.
     */
    public static void opObtenerAlimento(Scanner sc, Connection con, GestionAlimentosDAO dao) {
        Alimento alimento;
        System.out.println("\n  -- Buscar alimento por nombre --");
        alimento = seleccionarAlimento(sc, con, dao);
        mostrarAlimento(alimento);
    }

    /**
     * Lista todos los alimentos existentes en la BD.
     */
    public static void opListarAlimentos(Connection con, GestionAlimentosDAO dao) {
        List<Alimento> lista;
        System.out.println("\n  -- Todos los alimentos --");
        lista = dao.obtenerTodosAlimentos(con);
        int i = 0;
        while (i < lista.size()) {
            System.out.println("  ----------------------------------------");
            mostrarAlimento(lista.get(i));
            i++;
        }
        if (lista.isEmpty()) {
            System.out.println("  No hay alimentos registrados.");
        }
    }

    /**
     * Lee datos por consola y actualiza un alimento existente en la BD.
     */
    public static void opActualizarAlimento(Scanner sc, Connection con, GestionAlimentosDAO dao) {
        int id;
        Alimento existente;
        String nombre;
        String desc;
        String unidad;
        double precio;
        String cat;
        int cal;
        String dieta;
        boolean refrig;
        double tmin;
        double tmax;
        Alimento actualizado;
        boolean ok;
        int opcion;
        System.out.println("\n  -- Actualizar alimento --");
        System.out.print("  ID del alimento a actualizar: ");
        id = leerEntero(sc);
        existente = dao.obtenerAlimento(con, id);

        if (existente == null) {
            System.out.println("  Alimento no encontrado.");
        } else {
            nombre = existente.getNombre();
            desc = existente.getDescripcion();
            unidad = existente.getUnidadMedida();
            precio = existente.getPrecio();
            cat = existente.getCategoria();
            cal = existente.getCalorias();
            dieta = existente.getTipoDieta();
            refrig = existente.isNecesitaRefrigeracion();
            tmin = existente.getTemperaturaMin();
            tmax = existente.getTemperaturaMax();

            boolean guardar = false;
            while (!guardar) {
                System.out.println("\n  Que campo deseas modificar?");
                System.out.println("  1.  Nombre        (actual: " + nombre + ")");
                System.out.println("  2.  Descripcion   (actual: " + desc + ")");
                System.out.println("  3.  Unidad medida (actual: " + unidad + ")");
                System.out.println("  4.  Precio        (actual: " + precio + ")");
                System.out.println("  5.  Categoria     (actual: " + cat + ")");
                System.out.println("  6.  Calorias      (actual: " + cal + ")");
                System.out.println("  7.  Tipo dieta    (actual: " + dieta + ")");
                System.out.println("  8.  Refrigeracion (actual: " + refrig + ")");
                System.out.println("  9.  Temp. min     (actual: " + tmin + ")");
                System.out.println("  10. Temp. max     (actual: " + tmax + ")");
                System.out.println("  0.  Guardar y salir");
                System.out.print("  Opcion: ");
                opcion = leerOpcion(sc);
                switch (opcion) {
                    case 1:
                        System.out.print("  Nuevo nombre: ");
                        nombre = sc.nextLine();
                        break;
                    case 2:
                        System.out.print("  Nueva descripcion: ");
                        desc = sc.nextLine();
                        break;
                    case 3:
                        System.out.print("  Nueva unidad medida: ");
                        unidad = sc.nextLine();
                        break;
                    case 4:
                        System.out.print("  Nuevo precio: ");
                        precio = leerDecimal(sc);
                        break;
                    case 5:
                        System.out.print("  Nueva categoria: ");
                        cat = sc.nextLine();
                        break;
                    case 6:
                        System.out.print("  Nuevas calorias: ");
                        cal = leerEntero(sc);
                        break;
                    case 7:
                        System.out.print("  Nuevo tipo dieta: ");
                        dieta = sc.nextLine();
                        break;
                    case 8:
                        System.out.print("  Refrigeracion (s/n): ");
                        refrig = leerBoolean(sc);
                        break;
                    case 9:
                        System.out.print("  Nueva temp. min: ");
                        tmin = leerDecimal(sc);
                        break;
                    case 10:
                        System.out.print("  Nueva temp. max: ");
                        tmax = leerDecimal(sc);
                        break;
                    case 0:
                        guardar = true;
                        break;
                    default:
                        System.out.println("  Opcion no valida.");
                        break;
                }
            }

            actualizado = new Alimento(id, nombre, desc, unidad, precio,
                    cat, cal, dieta, refrig, tmin, tmax);
            ok = dao.actualizarAlimento(con, actualizado);
            System.out.println("  Resultado: " + (ok ? "Actualizado correctamente" : "No se pudo actualizar"));
        }
    }

    /**
     * Lee un ID por consola y elimina el alimento correspondiente.
     */
    public static void opEliminarAlimento(Scanner sc, Connection con, GestionAlimentosDAO dao) {
        int id;
        boolean ok;
        System.out.println("\n  -- Eliminar alimento --");
        System.out.print("  ID del alimento a eliminar: ");
        id = leerEntero(sc);
        ok = dao.eliminarAlimento(con, id);
        System.out.println("  Resultado: " + (ok ? "Eliminado correctamente" : "No se encontro el alimento"));
    }

    /**
     * Lee datos por consola e inserta un lote de alimentos en la BD.
     * Si el alimento requiere refrigeracion, solo muestra almacenes con camara
     * activa y obliga a elegir uno valido; si no hay ninguno o el usuario no
     * elige, cancela.
     */
    public static void opInsertarLoteAlimentos(Scanner sc, Connection con,
            GestionAlimentosDAO dao, AlmacenDAO daoAlmacenes) {
        int idProd;
        Alimento alimento;
        int cant;
        LocalDate fechaCaducidad;
        double temp;
        double hum;
        LocalDate fechaEntrada;
        LoteAlimentos lote;
        List<AlmacenAlimentos> almacenes;
        List<AlmacenAlimentos> validos;
        AlmacenAlimentos a;
        int eleccion;
        int id;
        System.out.println("\n  -- Insertar lote de alimentos --");
        System.out.print("  ID del producto (alimento): ");
        idProd = leerEntero(sc);

        alimento = dao.obtenerAlimento(con, idProd);
        if (alimento == null) {
            System.out.println("  Alimento con ID " + idProd + " no encontrado. Operacion cancelada.");
        } else {
            System.out.println("  Producto: " + alimento.getNombre() +
                    (alimento.isNecesitaRefrigeracion() ? " [REQUIERE REFRIGERACION]" : ""));

            System.out.print("  Cantidad                  : ");
            cant = leerEntero(sc);
            System.out.print("  Fecha caducidad (DD/MM/AAAA, AAAA/MM/DD, DD-MM-AAAA): ");
            fechaCaducidad = leerFecha(sc);
            System.out.print("  Temperatura control (C)   : ");
            temp = leerDecimal(sc);
            System.out.print("  Humedad control (%)        : ");
            hum = leerDecimal(sc);

            fechaEntrada = LocalDate.now();

            lote = new LoteAlimentos(idProd, cant, fechaEntrada, fechaCaducidad, temp, hum);

            almacenes = daoAlmacenes.obtenerTodosAlmacenesAlimentos(con);

            if (alimento.isNecesitaRefrigeracion()) {
                validos = new ArrayList<>();
                int i = 0;
                while (i < almacenes.size()) {
                    if (almacenes.get(i).tieneCamaraActiva()) {
                        validos.add(almacenes.get(i));
                    }
                    i++;
                }
                if (validos.isEmpty()) {
                    System.out.println("  No hay almacenes con camara frigorifica activa. Operacion cancelada.");
                } else {
                    System.out.println("\n  Almacenes compatibles (con camara activa):");
                    int j = 0;
                    while (j < validos.size()) {
                        a = validos.get(j);
                        System.out.println("  [" + (j + 1) + "] ID:" + a.getIdAlmacen() +
                                " - " + a.getUbicacion() +
                                " (stock: " + a.getStockMinimo() + "-" + a.getStockMaximo() + ")");
                        j++;
                    }
                    System.out.print("  Elige almacen (obligatorio): ");
                    eleccion = leerOpcion(sc);
                    if (eleccion > 0 && eleccion <= validos.size()) {
                        lote.setCodigoAlmacen(validos.get(eleccion - 1).getCodigo());
                        System.out.println("  Almacen asignado: " + validos.get(eleccion - 1).getUbicacion());
                        id = dao.insertarLoteAlimentos(con, lote);
                        System.out.println("  Lote creado con ID: " + id);
                    } else {
                        System.out.println("  Debe asignar un almacen con camara. Operacion cancelada.");
                    }
                }
            } else {
                if (almacenes.isEmpty()) {
                    System.out.println("  No hay almacenes de alimentos registrados. Operacion cancelada.");
                } else {
                    System.out.println("\n  Almacenes de alimentos disponibles:");
                    int i = 0;
                    while (i < almacenes.size()) {
                        a = almacenes.get(i);
                        System.out.println("  [" + (i + 1) + "] ID:" + a.getIdAlmacen() +
                                " - " + a.getUbicacion() +
                                " (stock: " + a.getStockMinimo() + "-" + a.getStockMaximo() + ")" +
                                " | Camara: " + (a.tieneCamaraActiva() ? "ACTIVA" : "NO"));
                        i++;
                    }
                    System.out.print("  Elige almacen (obligatorio): ");
                    eleccion = leerOpcion(sc);
                    if (eleccion > 0 && eleccion <= almacenes.size()) {
                        lote.setCodigoAlmacen(almacenes.get(eleccion - 1).getCodigo());
                        System.out.println("  Almacen asignado: " + almacenes.get(eleccion - 1).getUbicacion());
                        id = dao.insertarLoteAlimentos(con, lote);
                        System.out.println("  Lote creado con ID: " + id);
                    } else {
                        System.out.println("  Debe asignar un almacen. Operacion cancelada.");
                    }
                }
            }
        }
    }

    /**
     * Lista los lotes de alimentos activos, muestra las patrullas disponibles
     * y registra la asignacion del lote elegido a la patrulla elegida.
     */
    public static void opAsignarLoteAlimentos(Scanner sc, Connection con,
            GestionAlimentosDAO dao) {
        List<LoteAlimentos> lotes;
        AsignacionLoteDAO daoAsig;
        List<Patrulla> patrullas;
        int idLote;
        int idPatrulla;
        int cantidad;
        AsignacionLote asignacion;
        System.out.println("\n  -- Asignar lote de alimentos a patrulla --");

        lotes = dao.listarLotesAlimentosActivos(con);
        if (lotes.isEmpty()) {
            System.out.println("  No hay lotes de alimentos activos.");
        } else {
            System.out.println("  Lotes de alimentos activos:");
            int i = 0;
            while (i < lotes.size()) {
                System.out.println("  " + lotes.get(i).toString());
                i++;
            }

            daoAsig = new AsignacionLoteDAO();
            patrullas = daoAsig.listarPatrullas(con);

            if (patrullas.isEmpty()) {
                System.out.println("  No hay patrullas registradas en el sistema.");
            } else {
                System.out.println("\n  Patrullas disponibles:");
                int j = 0;
                while (j < patrullas.size()) {
                    System.out.println("  " + patrullas.get(j).toString());
                    j++;
                }

                System.out.print("  ID del lote a asignar  : ");
                idLote = leerEntero(sc);
                boolean loteValido = false;
                int k = 0;
                while (k < lotes.size()) {
                    if (lotes.get(k).getIdLote() == idLote) {
                        loteValido = true;
                    }
                    k++;
                }

                if (!loteValido) {
                    System.out.println("  El lote con ID " + idLote
                            + " no esta en la lista de lotes activos. Operacion cancelada.");
                } else {
                    System.out.print("  ID de la patrulla      : ");
                    idPatrulla = leerEntero(sc);
                    boolean patrullaValida = false;
                    int m = 0;
                    while (m < patrullas.size()) {
                        if (patrullas.get(m).getId() == idPatrulla) {
                            patrullaValida = true;
                        }
                        m++;
                    }

                    if (!patrullaValida) {
                        System.out.println(
                                "  La patrulla con ID " + idPatrulla + " no esta disponible. Operacion cancelada.");
                    } else {
                        System.out.print("  Cantidad a asignar     : ");
                        cantidad = leerEntero(sc);
                        asignacion = new AsignacionLote(idLote, idPatrulla, cantidad);
                        daoAsig.insertar(con, asignacion);
                    }
                }
            }
        }
    }

    // =========================================================================
    // SUBSISTEMA MEDICAMENTOS
    // =========================================================================

    /**
     * Muestra por pantalla los datos de una prescripcion.
     */
    public static void mostrarPrescripcion(Prescripcion prescripcion) {
        if (prescripcion == null) {
            System.out.println("  (Prescripcion no encontrada)");
        } else {
            System.out.println("  ID             : " + prescripcion.getIdPrescripcion());
            System.out.println("  Paciente ID    : " + prescripcion.getIdPaciente());
            System.out.println("  Medicamento ID : " + prescripcion.getIdProducto());
            System.out.println("  Dosis          : " + prescripcion.getDosis());
            System.out.println("  Frecuencia     : " + prescripcion.getFrecuencia());
            System.out.println("  Duracion       : " + prescripcion.getDuracion() + " dias");
            System.out.println("  Fecha inicio   : " + prescripcion.getFechaInicio());
            System.out.println("  Fecha fin      : " + prescripcion.getFechaFin());
            System.out.println("  Estado         : " + prescripcion.getEstado());
            System.out.println("  Dias restantes : " + prescripcion.diasRestantes());
        }
    }

    /**
     * Muestra por pantalla los datos de un medicamento.
     */
    public static void mostrarMedicamento(Medicamento medicamento) {
        if (medicamento == null) {
            System.out.println("  (Medicamento no encontrado)");
        } else {
            System.out.println("  ID             : " + medicamento.getIdProducto());
            System.out.println("  Nombre         : " + medicamento.getNombre());
            System.out.println("  Descripcion    : " + medicamento.getDescripcion());
            System.out.println("  Unidad         : " + medicamento.getUnidadMedida());
            System.out.println("  Precio         : " + medicamento.getPrecio());
            System.out.println("  Categoria      : " + medicamento.getCategoria());
            System.out.println("  Principio act. : " + medicamento.getPrincipioActivo());
            System.out.println("  Dosis          : " + medicamento.getDosis());
            System.out.println("  Via admin.     : " + medicamento.getViaAdministracion());
            System.out.println("  Receta         : " + medicamento.isNecesitaReceta());
            System.out.println("  Temp. almac.   : " + medicamento.getTemperaturaAlmacenamiento());
        }
    }

    /**
     * Lee los datos de un nuevo medicamento por consola y lo inserta en la BD.
     */
    public static void opInsertarMedicamento(Scanner sc, Connection con, GestionMedicamentosDAO dao) {
        String nombre;
        String desc;
        String unidad;
        double precio;
        String cat;
        String pa;
        String dosis;
        String via;
        boolean receta;
        double temp;
        Medicamento medicamento;
        int id;
        System.out.println("\n  -- Insertar medicamento --");
        System.out.print("  Nombre               : ");
        nombre = sc.nextLine();
        System.out.print("  Descripcion          : ");
        desc = sc.nextLine();
        System.out.print("  Unidad medida        : ");
        unidad = sc.nextLine();
        System.out.print("  Precio               : ");
        precio = leerDecimal(sc);
        System.out.print("  Categoria            : ");
        cat = sc.nextLine();
        System.out.print("  Principio activo     : ");
        pa = sc.nextLine();
        System.out.print("  Dosis                : ");
        dosis = sc.nextLine();
        System.out.print("  Via administracion   : ");
        via = sc.nextLine();
        System.out.print("  Necesita receta (s/n): ");
        receta = leerBoolean(sc);
        System.out.print("  Temp. almacenamiento : ");
        temp = leerDecimal(sc);

        medicamento = new Medicamento(0, nombre, desc, unidad, precio, cat,
                pa, dosis, via, receta, temp);
        id = dao.insertarMedicamento(con, medicamento);
        System.out.println("  Medicamento creado con ID: " + id);
    }

    /**
     * Pide un nombre por consola, busca medicamentos que lo contengan y devuelve
     * el medicamento elegido por el usuario. Devuelve null si no hay resultados
     * o la eleccion no es valida.
     */
    public static Medicamento seleccionarMedicamento(Scanner sc, Connection con, GestionMedicamentosDAO dao) {
        String nombre;
        List<Medicamento> resultados;
        Medicamento seleccionado;
        int eleccion;
        int i;

        System.out.print("  Nombre del medicamento: ");
        nombre = sc.nextLine();
        resultados = dao.buscarMedicamentosPorNombre(con, nombre);

        if (resultados.isEmpty()) {
            System.out.println("  No se encontraron medicamentos con ese nombre.");
            seleccionado = null;
        } else if (resultados.size() == 1) {
            seleccionado = resultados.get(0);
            System.out.println("  Medicamento encontrado: " + seleccionado.getNombre());
        } else {
            System.out.println("  Se encontraron varios medicamentos:");
            i = 0;
            while (i < resultados.size()) {
                System.out.println("  [" + (i + 1) + "] " + resultados.get(i).getNombre());
                i++;
            }
            System.out.print("  Elige uno: ");
            eleccion = leerOpcion(sc);
            if (eleccion > 0 && eleccion <= resultados.size()) {
                seleccionado = resultados.get(eleccion - 1);
            } else {
                System.out.println("  Eleccion no valida. Operacion cancelada.");
                seleccionado = null;
            }
        }

        return seleccionado;
    }

    /**
     * Busca medicamentos por nombre y muestra el resultado seleccionado.
     */
    public static void opObtenerMedicamento(Scanner sc, Connection con, GestionMedicamentosDAO dao) {
        Medicamento medicamento;
        System.out.println("\n  -- Buscar medicamento por nombre --");
        medicamento = seleccionarMedicamento(sc, con, dao);
        mostrarMedicamento(medicamento);
    }

    /**
     * Lista todos los medicamentos existentes en la BD.
     */
    public static void opListarMedicamentos(Connection con, GestionMedicamentosDAO dao) {
        List<Medicamento> lista;
        System.out.println("\n  -- Todos los medicamentos --");
        lista = dao.obtenerTodosMedicamentos(con);
        int i = 0;
        while (i < lista.size()) {
            System.out.println("  ----------------------------------------");
            mostrarMedicamento(lista.get(i));
            i++;
        }
        if (lista.isEmpty()) {
            System.out.println("  No hay medicamentos registrados.");
        }
    }

    /**
     * Lee datos por consola y actualiza un medicamento existente en la BD.
     */
    public static void opActualizarMedicamento(Scanner sc, Connection con, GestionMedicamentosDAO dao) {
        int id;
        Medicamento existente;
        String nombre;
        String desc;
        String unidad;
        double precio;
        String cat;
        String pa;
        String dosis;
        String via;
        boolean receta;
        double temp;
        Medicamento actualizado;
        boolean ok;
        int opcion;
        System.out.println("\n  -- Actualizar medicamento --");
        System.out.print("  ID del medicamento a actualizar: ");
        id = leerEntero(sc);
        existente = dao.obtenerMedicamento(con, id);

        if (existente == null) {
            System.out.println("  Medicamento no encontrado.");
        } else {
            nombre = existente.getNombre();
            desc = existente.getDescripcion();
            unidad = existente.getUnidadMedida();
            precio = existente.getPrecio();
            cat = existente.getCategoria();
            pa = existente.getPrincipioActivo();
            dosis = existente.getDosis();
            via = existente.getViaAdministracion();
            receta = existente.isNecesitaReceta();
            temp = existente.getTemperaturaAlmacenamiento();

            boolean guardar = false;
            while (!guardar) {
                System.out.println("\n  Que campo deseas modificar?");
                System.out.println("  1.  Nombre               (actual: " + nombre + ")");
                System.out.println("  2.  Descripcion          (actual: " + desc + ")");
                System.out.println("  3.  Unidad medida        (actual: " + unidad + ")");
                System.out.println("  4.  Precio               (actual: " + precio + ")");
                System.out.println("  5.  Categoria            (actual: " + cat + ")");
                System.out.println("  6.  Principio activo     (actual: " + pa + ")");
                System.out.println("  7.  Dosis                (actual: " + dosis + ")");
                System.out.println("  8.  Via administracion   (actual: " + via + ")");
                System.out.println("  9.  Necesita receta      (actual: " + receta + ")");
                System.out.println("  10. Temp. almacenamiento (actual: " + temp + ")");
                System.out.println("  0.  Guardar y salir");
                System.out.print("  Opcion: ");
                opcion = leerOpcion(sc);
                switch (opcion) {
                    case 1:
                        System.out.print("  Nuevo nombre: ");
                        nombre = sc.nextLine();
                        break;
                    case 2:
                        System.out.print("  Nueva descripcion: ");
                        desc = sc.nextLine();
                        break;
                    case 3:
                        System.out.print("  Nueva unidad medida: ");
                        unidad = sc.nextLine();
                        break;
                    case 4:
                        System.out.print("  Nuevo precio: ");
                        precio = leerDecimal(sc);
                        break;
                    case 5:
                        System.out.print("  Nueva categoria: ");
                        cat = sc.nextLine();
                        break;
                    case 6:
                        System.out.print("  Nuevo principio activo: ");
                        pa = sc.nextLine();
                        break;
                    case 7:
                        System.out.print("  Nueva dosis: ");
                        dosis = sc.nextLine();
                        break;
                    case 8:
                        System.out.print("  Nueva via administracion: ");
                        via = sc.nextLine();
                        break;
                    case 9:
                        System.out.print("  Necesita receta (s/n): ");
                        receta = leerBoolean(sc);
                        break;
                    case 10:
                        System.out.print("  Nueva temp. almacenamiento: ");
                        temp = leerDecimal(sc);
                        break;
                    case 0:
                        guardar = true;
                        break;
                    default:
                        System.out.println("  Opcion no valida.");
                        break;
                }
            }

            actualizado = new Medicamento(id, nombre, desc, unidad, precio,
                    cat, pa, dosis, via, receta, temp);
            ok = dao.actualizarMedicamento(con, actualizado);
            System.out.println("  Resultado: " + (ok ? "Actualizado correctamente" : "No se pudo actualizar"));
        }
    }

    /**
     * Lee un ID por consola y elimina el medicamento correspondiente.
     */
    public static void opEliminarMedicamento(Scanner sc, Connection con, GestionMedicamentosDAO dao) {
        int id;
        boolean ok;
        System.out.println("\n  -- Eliminar medicamento --");
        System.out.print("  ID del medicamento a eliminar: ");
        id = leerEntero(sc);
        ok = dao.eliminarMedicamento(con, id);
        System.out.println("  Resultado: " + (ok ? "Eliminado correctamente" : "No se encontro el medicamento"));
    }

    /**
     * Lee datos por consola e inserta un lote de medicamentos en la BD.
     * Si el medicamento requiere refrigeracion, solo muestra almacenes con camara
     * activa y obliga a elegir uno valido; si no hay ninguno o el usuario no
     * elige, cancela.
     */
    public static void opInsertarLoteMedicamentos(Scanner sc, Connection con,
            GestionMedicamentosDAO dao, AlmacenDAO daoAlmacenes) {
        int idProd;
        Medicamento medicamento;
        int cant;
        LocalDate fechaCaducidad;
        String numLote;
        String cond;
        LocalDate fechaEntrada;
        LoteMedicamentos lote;
        List<AlmacenMedicamentos> almacenes;
        List<AlmacenMedicamentos> validos;
        AlmacenMedicamentos a;
        int eleccion;
        int id;
        System.out.println("\n  -- Insertar lote de medicamentos --");
        System.out.print("  ID del producto (medicamento) : ");
        idProd = leerEntero(sc);

        medicamento = dao.obtenerMedicamento(con, idProd);
        if (medicamento == null) {
            System.out.println("  Medicamento con ID " + idProd + " no encontrado. Operacion cancelada.");
        } else {
            System.out.println("  Producto: " + medicamento.getNombre() +
                    (medicamento.necesitaRefrigeracion() ? " [REQUIERE REFRIGERACION]" : ""));

            System.out.print("  Cantidad                      : ");
            cant = leerEntero(sc);
            System.out.print("  Fecha caducidad (DD/MM/AAAA, AAAA/MM/DD, DD-MM-AAAA): ");
            fechaCaducidad = leerFecha(sc);
            System.out.print("  Numero lote fabricante        : ");
            numLote = sc.nextLine();
            System.out.print("  Condiciones almacenamiento    : ");
            cond = sc.nextLine();

            fechaEntrada = LocalDate.now();

            lote = new LoteMedicamentos(idProd, cant, fechaEntrada, fechaCaducidad, numLote, cond);

            almacenes = daoAlmacenes.obtenerTodosAlmacenesMedicamentos(con);

            if (medicamento.necesitaRefrigeracion()) {
                validos = new ArrayList<>();
                int i = 0;
                while (i < almacenes.size()) {
                    if (almacenes.get(i).tieneCamaraActiva()) {
                        validos.add(almacenes.get(i));
                    }
                    i++;
                }
                if (validos.isEmpty()) {
                    System.out.println("  No hay almacenes con camara frigorifica activa. Operacion cancelada.");
                } else {
                    System.out.println("\n  Almacenes compatibles (con camara activa):");
                    int j = 0;
                    while (j < validos.size()) {
                        a = validos.get(j);
                        System.out.println("  [" + (j + 1) + "] ID:" + a.getIdAlmacen() +
                                " - " + a.getUbicacion() +
                                " (stock: " + a.getStockMinimo() + "-" + a.getStockMaximo() + ")");
                        j++;
                    }
                    System.out.print("  Elige almacen (obligatorio): ");
                    eleccion = leerOpcion(sc);
                    if (eleccion > 0 && eleccion <= validos.size()) {
                        lote.setCodigoAlmacen(validos.get(eleccion - 1).getCodigo());
                        System.out.println("  Almacen asignado: " + validos.get(eleccion - 1).getUbicacion());
                        id = dao.insertarLoteMedicamentos(con, lote);
                        System.out.println("  Lote creado con ID: " + id);
                    } else {
                        System.out.println("  Debe asignar un almacen con camara. Operacion cancelada.");
                    }
                }
            } else {
                if (almacenes.isEmpty()) {
                    System.out.println("  No hay almacenes de medicamentos registrados. Operacion cancelada.");
                } else {
                    System.out.println("\n  Almacenes de medicamentos disponibles:");
                    int i = 0;
                    while (i < almacenes.size()) {
                        a = almacenes.get(i);
                        System.out.println("  [" + (i + 1) + "] ID:" + a.getIdAlmacen() +
                                " - " + a.getUbicacion() +
                                " (stock: " + a.getStockMinimo() + "-" + a.getStockMaximo() + ")" +
                                " | Camara: " + (a.tieneCamaraActiva() ? "ACTIVA" : "NO"));
                        i++;
                    }
                    System.out.print("  Elige almacen (obligatorio): ");
                    eleccion = leerOpcion(sc);
                    if (eleccion > 0 && eleccion <= almacenes.size()) {
                        lote.setCodigoAlmacen(almacenes.get(eleccion - 1).getCodigo());
                        System.out.println("  Almacen asignado: " + almacenes.get(eleccion - 1).getUbicacion());
                        id = dao.insertarLoteMedicamentos(con, lote);
                        System.out.println("  Lote creado con ID: " + id);
                    } else {
                        System.out.println("  Debe asignar un almacen. Operacion cancelada.");
                    }
                }
            }
        }
    }

    /**
     * Muestra los medicamentos que requieren cadena de frio (temp <= 8 C).
     */
    public static void opMedicamentosRefrigeracion(Connection con, GestionMedicamentosDAO dao) {
        List<Medicamento> lista;
        System.out.println("\n  -- Medicamentos con refrigeracion --");
        lista = dao.obtenerMedicamentosConRefrigeracion(con);
        int i = 0;
        while (i < lista.size()) {
            System.out.println("  ----------------------------------------");
            mostrarMedicamento(lista.get(i));
            i++;
        }
    }

    /**
     * Lee datos de un paciente por consola y lo inserta en la BD.
     */
    public static void opInsertarPaciente(Scanner sc, Connection con, GestionMedicamentosDAO dao) {
        String nombre;
        String apell;
        LocalDate fechaNac;
        String alerg;
        String histor;
        Paciente paciente;
        int id;
        System.out.println("\n  -- Insertar paciente --");
        System.out.print("  Nombre                         : ");
        nombre = sc.nextLine();
        System.out.print("  Apellidos                      : ");
        apell = sc.nextLine();
        System.out.print("  Fecha nacimiento (DD/MM/AAAA, o vacio): ");
        fechaNac = leerFechaOpcional(sc);
        System.out.print("  Alergias                       : ");
        alerg = sc.nextLine();
        System.out.print("  Historial medico               : ");
        histor = sc.nextLine();

        paciente = new Paciente(nombre, apell, fechaNac, alerg, histor);
        id = dao.insertarPaciente(con, paciente);
        System.out.println("  Paciente creado con ID: " + id);
    }

    /**
     * Lee datos de una prescripcion por consola y la inserta en la BD.
     * Verifica que el paciente y el medicamento existan antes de continuar.
     */
    public static void opInsertarPrescripcion(Scanner sc, Connection con, GestionMedicamentosDAO dao) {
        int idPac;
        Paciente paciente;
        int idMed;
        Medicamento medicamento;
        String dosis;
        String frec;
        int dur;
        LocalDate fechaInicio;
        Prescripcion prescripcion;
        int id;
        System.out.println("\n  -- Insertar prescripcion --");
        System.out.print("  ID del paciente              : ");
        idPac = leerEntero(sc);
        paciente = dao.obtenerPaciente(con, idPac);

        if (paciente == null) {
            System.out.println("  Paciente con ID " + idPac + " no encontrado. Operacion cancelada.");
        } else {
            System.out.println("  Paciente: " + paciente.getNombreCompleto());
            System.out.print("  ID del medicamento           : ");
            idMed = leerEntero(sc);
            medicamento = dao.obtenerMedicamento(con, idMed);

            if (medicamento == null) {
                System.out.println("  Medicamento con ID " + idMed + " no encontrado. Operacion cancelada.");
            } else {
                System.out.println("  Medicamento: " + medicamento.getNombre());
                System.out.print("  Dosis                        : ");
                dosis = sc.nextLine();
                System.out.print("  Frecuencia                   : ");
                frec = sc.nextLine();
                System.out.print("  Duracion (dias)              : ");
                dur = leerEntero(sc);
                System.out.print("  Fecha inicio (DD/MM/AAAA, o vacio para hoy): ");
                fechaInicio = leerFechaOpcional(sc);
                if (fechaInicio == null) {
                    fechaInicio = LocalDate.now();
                }

                prescripcion = new Prescripcion(idPac, idMed, dosis, frec, dur, fechaInicio);
                id = dao.insertarPrescripcion(con, prescripcion);
                System.out.println("  Prescripcion creada con ID: " + id);
            }
        }
    }

    /**
     * Lee un ID de paciente y muestra sus prescripciones activas.
     */
    public static void opPrescripcionesActivas(Scanner sc, Connection con, GestionMedicamentosDAO dao) {
        int idPaciente;
        List<Prescripcion> lista;
        System.out.println("\n  -- Prescripciones activas de un paciente --");
        System.out.print("  ID del paciente: ");
        idPaciente = leerEntero(sc);
        lista = dao.obtenerPrescripcionesActivas(con, idPaciente);
        if (lista.isEmpty()) {
            System.out.println("  No hay prescripciones activas para este paciente.");
        } else {
            int i = 0;
            while (i < lista.size()) {
                System.out.println("  ----------------------------------------");
                mostrarPrescripcion(lista.get(i));
                i++;
            }
        }
    }

    /**
     * Lista los lotes de medicamentos activos, muestra las patrullas disponibles
     * y registra la asignacion del lote elegido a la patrulla elegida.
     */
    public static void opAsignarLoteMedicamentos(Scanner sc, Connection con,
            GestionMedicamentosDAO dao) {
        List<LoteMedicamentos> lotes;
        AsignacionLoteDAO daoAsig;
        List<Patrulla> patrullas;
        int idLote;
        int idPatrulla;
        int cantidad;
        AsignacionLote asignacion;
        System.out.println("\n  -- Asignar lote de medicamentos a patrulla --");

        lotes = dao.listarLotesMedicamentosActivos(con);
        if (lotes.isEmpty()) {
            System.out.println("  No hay lotes de medicamentos activos.");
        } else {
            System.out.println("  Lotes de medicamentos activos:");
            int i = 0;
            while (i < lotes.size()) {
                System.out.println("  " + lotes.get(i).toString());
                i++;
            }

            daoAsig = new AsignacionLoteDAO();
            patrullas = daoAsig.listarPatrullas(con);

            if (patrullas.isEmpty()) {
                System.out.println("  No hay patrullas registradas en el sistema.");
            } else {
                System.out.println("\n  Patrullas disponibles:");
                int j = 0;
                while (j < patrullas.size()) {
                    System.out.println("  " + patrullas.get(j).toString());
                    j++;
                }

                System.out.print("  ID del lote a asignar  : ");
                idLote = leerEntero(sc);
                boolean loteValido = false;
                int k = 0;
                while (k < lotes.size()) {
                    if (lotes.get(k).getIdLote() == idLote) {
                        loteValido = true;
                    }
                    k++;
                }

                if (!loteValido) {
                    System.out.println("  El lote con ID " + idLote
                            + " no esta en la lista de lotes activos. Operacion cancelada.");
                } else {
                    System.out.print("  ID de la patrulla      : ");
                    idPatrulla = leerEntero(sc);
                    boolean patrullaValida = false;
                    int m = 0;
                    while (m < patrullas.size()) {
                        if (patrullas.get(m).getId() == idPatrulla) {
                            patrullaValida = true;
                        }
                        m++;
                    }

                    if (!patrullaValida) {
                        System.out.println(
                                "  La patrulla con ID " + idPatrulla + " no esta disponible. Operacion cancelada.");
                    } else {
                        System.out.print("  Cantidad a asignar     : ");
                        cantidad = leerEntero(sc);
                        asignacion = new AsignacionLote(idLote, idPatrulla, cantidad);
                        daoAsig.insertar(con, asignacion);
                    }
                }
            }
        }
    }

    // =========================================================================
    // SUBSISTEMA ALMACENES
    // =========================================================================

    /**
     * Lee los datos de un nuevo almacen de alimentos por consola y lo inserta en
     * la BD. Pregunta al usuario si el almacen tiene camara de refrigeracion; si
     * es asi, recoge los datos de la camara y la asocia antes de persistir el
     * almacen.
     */
    public static void opInsertarAlmacenAlimentos(Scanner sc, Connection con, AlmacenDAO dao) {
        String ubicacion;
        int stockMin;
        int stockMax;
        AlmacenAlimentos almacen;
        String respCamara;
        double capCam;
        double tminCam;
        double tmaxCam;
        double tactCam;
        CamaraRefrigeracion camara;
        boolean ok;
        System.out.println("\n  -- Insertar almacen de alimentos --");
        System.out.print("  Ubicacion    : ");
        ubicacion = sc.nextLine();
        System.out.print("  Stock minimo : ");
        stockMin = leerEntero(sc);
        System.out.print("  Stock maximo : ");
        stockMax = leerEntero(sc);

        almacen = new AlmacenAlimentos(ubicacion, stockMin, stockMax);

        System.out.print("  Tiene camara de refrigeracion? (s/n): ");
        respCamara = sc.nextLine().trim().toLowerCase();
        if ("s".equals(respCamara)) {
            System.out.print("  Capacidad camara (m3)       : ");
            capCam = leerDecimal(sc);
            System.out.print("  Temperatura minima (C)      : ");
            tminCam = leerDecimal(sc);
            System.out.print("  Temperatura maxima (C)      : ");
            tmaxCam = leerDecimal(sc);
            System.out.print("  Temperatura actual (C)      : ");
            tactCam = leerDecimal(sc);
            camara = new CamaraRefrigeracion(
                    UUID.randomUUID().toString(), capCam, tminCam, tmaxCam, tactCam, true);
            almacen.almacenarCamara(camara);
            System.out.println("  Camara de refrigeracion configurada.");
        }

        ok = dao.insertarAlmacenAlimentos(con, almacen);
        System.out.println(
                "  Resultado: " + (ok ? "Almacen creado -> ID: " + almacen.getIdAlmacen() : "No se pudo crear el almacen"));
    }

    /**
     * Lee los datos de un nuevo almacen de medicamentos por consola y lo inserta
     * en la BD. Pregunta al usuario si el almacen tiene camara de refrigeracion;
     * si es asi, recoge los datos de la camara y la asocia antes de persistir el
     * almacen.
     */
    public static void opInsertarAlmacenMedicamentos(Scanner sc, Connection con, AlmacenDAO dao) {
        String ubicacion;
        int stockMin;
        int stockMax;
        AlmacenMedicamentos almacen;
        String respCamara;
        double capCam;
        double tminCam;
        double tmaxCam;
        double tactCam;
        CamaraRefrigeracion camara;
        boolean ok;
        System.out.println("\n  -- Insertar almacen de medicamentos --");
        System.out.print("  Ubicacion    : ");
        ubicacion = sc.nextLine();
        System.out.print("  Stock minimo : ");
        stockMin = leerEntero(sc);
        System.out.print("  Stock maximo : ");
        stockMax = leerEntero(sc);

        almacen = new AlmacenMedicamentos(ubicacion, stockMin, stockMax);

        System.out.print("  Tiene camara de refrigeracion? (s/n): ");
        respCamara = sc.nextLine().trim().toLowerCase();
        if ("s".equals(respCamara)) {
            System.out.print("  Capacidad camara (m3)       : ");
            capCam = leerDecimal(sc);
            System.out.print("  Temperatura minima (C)      : ");
            tminCam = leerDecimal(sc);
            System.out.print("  Temperatura maxima (C)      : ");
            tmaxCam = leerDecimal(sc);
            System.out.print("  Temperatura actual (C)      : ");
            tactCam = leerDecimal(sc);
            camara = new CamaraRefrigeracion(
                    UUID.randomUUID().toString(), capCam, tminCam, tmaxCam, tactCam, true);
            almacen.almacenarCamara(camara);
            System.out.println("  Camara de refrigeracion configurada.");
        }

        ok = dao.insertarAlmacenMedicamentos(con, almacen);
        System.out.println(
                "  Resultado: " + (ok ? "Almacen creado -> ID: " + almacen.getIdAlmacen() : "No se pudo crear el almacen"));
    }

    /**
     * Lista todos los almacenes existentes en la BD mostrando su tipo y datos.
     */
    public static void opListarAlmacenes(Connection con, AlmacenDAO dao) {
        List<Almacen> lista;
        Almacen a;
        System.out.println("\n  -- Todos los almacenes --");
        lista = dao.obtenerTodosAlmacenes(con);
        int i = 0;
        while (i < lista.size()) {
            a = lista.get(i);
            System.out.println("  ----------------------------------------");
            System.out.println("  ID       : " + a.getIdAlmacen());
            System.out.println("  Tipo     : " + a.getTipo());
            System.out.println("  Ubicacion: " + a.getUbicacion());
            System.out.println("  Stock    : min=" + a.getStockMinimo() + " max=" + a.getStockMaximo());
            System.out.println("  Camara   : " + (a.tieneCamaraActiva() ? "ACTIVA" : "NO"));
            i++;
        }
        if (lista.isEmpty()) {
            System.out.println("  (No hay almacenes registrados)");
        }
    }

    /**
     * Muestra la lista de almacenes y elimina el seleccionado por ID.
     */
    public static void opEliminarAlmacen(Scanner sc, Connection con, AlmacenDAO dao) {
        List<Almacen> lista;
        Almacen a;
        int idAlmacen;
        boolean ok;
        System.out.println("\n  -- Eliminar almacen --");
        lista = dao.obtenerTodosAlmacenes(con);
        int i = 0;
        while (i < lista.size()) {
            a = lista.get(i);
            System.out.println("  [" + a.getIdAlmacen() + "] " + a.getTipo() + " - " + a.getUbicacion());
            i++;
        }
        if (lista.isEmpty()) {
            System.out.println("  (No hay almacenes registrados)");
        } else {
            System.out.print("  ID del almacen a eliminar: ");
            idAlmacen = leerEntero(sc);
            ok = dao.eliminarAlmacen(con, idAlmacen);
            System.out.println("  Resultado: " + (ok ? "Eliminado correctamente" : "No se encontro el almacen"));
        }
    }
}
