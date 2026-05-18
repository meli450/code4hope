package Entidad;

import java.time.LocalDate;

/**
 * Representa un lote de alimentos en el sistema Code4Hope.
 * Corresponde a las tablas LOTE + LOTE_ALIMENTOS de la base de datos.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class LoteAlimentos {

    private int idLote;
    private int idProducto;
    private int cantidad;
    private LocalDate fechaEntrada;
    private LocalDate fechaCaducidad;
    private String estado;
    private double temperaturaControl;
    private double humedadControl;
    private String codigoAlmacen;

    /**
     * Constructor completo del lote de alimentos.
     *
     * @param codigoAlmacen Codigo del AlmacenAlimentos donde esta fisicamente
     *                      el lote, o null si aun no esta asignado a un almacen
     */
    public LoteAlimentos(int idLote, int idProducto, int cantidad,
            LocalDate fechaEntrada, LocalDate fechaCaducidad,
            String estado, double temperaturaControl, double humedadControl,
            String codigoAlmacen) {
        this.idLote = idLote;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.fechaEntrada = fechaEntrada;
        this.fechaCaducidad = fechaCaducidad;
        this.estado = estado;
        this.temperaturaControl = temperaturaControl;
        this.humedadControl = humedadControl;
        this.codigoAlmacen = codigoAlmacen;
    }

    /**
     * Constructor sin idLote ni almacen, usado al insertar un nuevo lote
     * cuando aun no se conoce el almacen destino.
     */
    public LoteAlimentos(int idProducto, int cantidad,
            LocalDate fechaEntrada, LocalDate fechaCaducidad,
            double temperaturaControl, double humedadControl) {
        this(0, idProducto, cantidad, fechaEntrada, fechaCaducidad,
                "ACTIVO", temperaturaControl, humedadControl, null);
    }

    /**
     * Comprueba si el lote está caducado comparando con la fecha actual.
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
     * Calcula los días que faltan para que el lote caduque.
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
     * Comprueba si las condiciones ambientales del lote están en rango aceptable.
     */
    public boolean condicionesCorrectas(Alimento alimento) {
        boolean condicionesOK;
        boolean tempOK;
        boolean humedadOK;

        tempOK = temperaturaControl >= alimento.getTemperaturaMin()
                && temperaturaControl <= alimento.getTemperaturaMax();
        humedadOK = humedadControl <= 70.0;

        condicionesOK = tempOK && humedadOK;

        return condicionesOK;
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

    public double getTemperaturaControl() {
        return temperaturaControl;
    }

    public double getHumedadControl() {
        return humedadControl;
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

    public void setTemperaturaControl(double temperaturaControl) {
        this.temperaturaControl = temperaturaControl;
    }

    public void setHumedadControl(double humedadControl) {
        this.humedadControl = humedadControl;
    }

    public String getCodigoAlmacen() {
        return codigoAlmacen;
    }

    public void setCodigoAlmacen(String codigoAlmacen) {
        this.codigoAlmacen = codigoAlmacen;
    }

    @Override
    public String toString() {
        return "LoteAlimentos{id=" + idLote +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", caducidad=" + fechaCaducidad +
                ", estado='" + estado + '\'' +
                ", temp=" + temperaturaControl + "grados C" +
                ", humedad=" + humedadControl + "%" +
                ", almacen='" + (codigoAlmacen != null ? codigoAlmacen.substring(0, 8) + "..." : "Sin asignar") + "'"
                + '}';
    }
}
