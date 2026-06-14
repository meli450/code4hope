package Entidad;

import java.time.LocalDate;

/**
 * Representa una prescripción médica en el sistema Code4Hope.
 * Corresponde a la tabla PRESCRIPCION de la base de datos.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class Prescripcion {

    public static final String ESTADO_ACTIVA = "ACTIVA";
    public static final String ESTADO_COMPLETADA = "COMPLETADA";
    public static final String ESTADO_CANCELADA = "CANCELADA";

    private int idPrescripcion;
    private int idPaciente;
    private int idProducto;
    private String dosis;
    private String frecuencia;
    private int duracion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;

    public Prescripcion(int idPrescripcion, int idPaciente, int idProducto,
            String dosis, String frecuencia, int duracion,
            LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        this.idPrescripcion = idPrescripcion;
        this.idPaciente = idPaciente;
        this.idProducto = idProducto;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.duracion = duracion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    /**
     * Constructor sin idPrescripcion. La fecha de fin se calcula: fechaInicio +
     * duracion días.
     */
    public Prescripcion(int idPaciente, int idProducto, String dosis,
            String frecuencia, int duracion, LocalDate fechaInicio) {
        this(0, idPaciente, idProducto, dosis, frecuencia, duracion,
                fechaInicio, fechaInicio.plusDays(duracion), ESTADO_ACTIVA);
    }

    /**
     * Comprueba si la prescripción está actualmente vigente.
     */
    public boolean estaVigente() {
        boolean vigente;
        LocalDate hoy;
        boolean enEstadoActivo;
        boolean fechaValida;

        hoy = LocalDate.now();
        enEstadoActivo = ESTADO_ACTIVA.equals(estado);
        fechaValida = (fechaInicio != null) &&
                !hoy.isBefore(fechaInicio) &&
                (fechaFin == null || !hoy.isAfter(fechaFin));

        vigente = enEstadoActivo && fechaValida;

        return vigente;
    }

    /**
     * Calcula los días restantes de tratamiento desde hoy.
     */
    public long diasRestantes() {
        long dias;
        long calculado;

        if (fechaFin == null) {
            dias = -1;
        } else {
            calculado = LocalDate.now().until(fechaFin,
                    java.time.temporal.ChronoUnit.DAYS);
            dias = Math.max(0, calculado);
        }

        return dias;
    }

    /**
     * Cancela la prescripción. Solo se puede cancelar si está ACTIVA.
     */
    public boolean cancelar() {
        boolean resultado;

        if (ESTADO_ACTIVA.equals(estado)) {
            estado = ESTADO_CANCELADA;
            resultado = true;
        } else {
            resultado = false;
        }

        return resultado;
    }

    /**
     * Marca la prescripción como completada. Solo si está ACTIVA.
     */
    public boolean completar() {
        boolean resultado;

        if (ESTADO_ACTIVA.equals(estado)) {
            estado = ESTADO_COMPLETADA;
            resultado = true;
        } else {
            resultado = false;
        }

        return resultado;
    }

    public int getIdPrescripcion() {
        return idPrescripcion;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getDosis() {
        return dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public int getDuracion() {
        return duracion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setIdPrescripcion(int idPrescripcion) {
        this.idPrescripcion = idPrescripcion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    @Override
    public String toString() {
        return "Prescripcion{id=" + idPrescripcion +
                ", paciente=" + idPaciente +
                ", medicamento=" + idProducto +
                ", dosis='" + dosis + '\'' +
                ", frecuencia='" + frecuencia + '\'' +
                ", duracion=" + duracion + " dias" +
                ", estado='" + estado + '\'' + '}';
    }
}
