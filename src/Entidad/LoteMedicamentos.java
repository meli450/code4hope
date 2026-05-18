package Entidad;

import java.time.LocalDate;

/**
 * Representa un lote de medicamentos en el sistema Code4Hope.
 * Corresponde a las tablas LOTE + LOTE_MEDICAMENTOS de la base de datos.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class LoteMedicamentos {

    private int idLote;
    private int idProducto;
    private int cantidad;
    private LocalDate fechaEntrada;
    private LocalDate fechaCaducidad;
    private String estado;
    private String numeroLoteFabricante;
    private String condicionesAlmacenamiento;
    private String codigoAlmacen;

    /**
     * Constructor completo del lote de medicamentos.
     *
     * @param codigoAlmacen Codigo del AlmacenMedicamentos donde esta fisicamente
     *                      el lote, o null si aun no esta asignado a un almacen
     */
    public LoteMedicamentos(int idLote, int idProducto, int cantidad,
            LocalDate fechaEntrada, LocalDate fechaCaducidad,
            String estado, String numeroLoteFabricante,
            String condicionesAlmacenamiento, String codigoAlmacen) {
        this.idLote = idLote;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.fechaEntrada = fechaEntrada;
        this.fechaCaducidad = fechaCaducidad;
        this.estado = estado;
        this.numeroLoteFabricante = numeroLoteFabricante;
        this.condicionesAlmacenamiento = condicionesAlmacenamiento;
        this.codigoAlmacen = codigoAlmacen;
    }

    /**
     * Constructor sin idLote ni almacen, usado al insertar un nuevo lote
     * cuando aun no se conoce el almacen destino.
     */
    public LoteMedicamentos(int idProducto, int cantidad,
            LocalDate fechaEntrada, LocalDate fechaCaducidad,
            String numeroLoteFabricante, String condicionesAlmacenamiento) {
        this(0, idProducto, cantidad, fechaEntrada, fechaCaducidad,
                "ACTIVO", numeroLoteFabricante, condicionesAlmacenamiento, null);
    }

    /**
     * Comprueba si el lote de medicamentos está caducado.
     */
    public boolean estaCaducado() {
        boolean resultado;

        if (fechaCaducidad == null) {
            resultado = false;
        } else {
            resultado = LocalDate.now().isAfter(fechaCaducidad);
        }

        return resultado;
    }

    /**
     * Calcula los días restantes hasta la caducidad del lote.
     */
    public long diasParaCaducar() {
        long dias;

        if (fechaCaducidad == null) {
            dias = Long.MAX_VALUE;
        } else {
            dias = LocalDate.now().until(fechaCaducidad,
                    java.time.temporal.ChronoUnit.DAYS);
        }

        return dias;
    }

    /**
     * Verifica si la temperatura actual del almacén es adecuada para el medicamento
     * con una tolerancia de +/-2 grados C.
     */
    public boolean temperaturaAdecuada(Medicamento medicamento, double temperaturaActual) {
        boolean resultado;
        double tempRequerida;
        double tolerancia;

        tempRequerida = medicamento.getTemperaturaAlmacenamiento();
        tolerancia = 2.0;

        resultado = Math.abs(temperaturaActual - tempRequerida) <= tolerancia;

        return resultado;
    }

    public int getIdLote() {
        return idLote;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public String getEstado() {
        return estado;
    }

    public String getNumeroLoteFabricante() {
        return numeroLoteFabricante;
    }

    public String getCondicionesAlmacenamiento() {
        return condicionesAlmacenamiento;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setNumeroLoteFabricante(String nLote) {
        this.numeroLoteFabricante = nLote;
    }

    public void setCondicionesAlmacenamiento(String condiciones) {
        this.condicionesAlmacenamiento = condiciones;
    }

    public String getCodigoAlmacen() {
        return codigoAlmacen;
    }

    public void setCodigoAlmacen(String codigoAlmacen) {
        this.codigoAlmacen = codigoAlmacen;
    }

    @Override
    public String toString() {
        return "LoteMedicamentos{id=" + idLote +
                ", medicamento=" + idProducto +
                ", cantidad=" + cantidad +
                ", caducidad=" + fechaCaducidad +
                ", estado='" + estado + '\'' +
                ", loteRef='" + numeroLoteFabricante + '\'' +
                ", almacen='" + (codigoAlmacen != null ? codigoAlmacen.substring(0, 8) + "..." : "Sin asignar") + "'"
                + '}';
    }
}
