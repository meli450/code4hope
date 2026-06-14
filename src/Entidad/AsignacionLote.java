package Entidad;

import java.time.LocalDate;

/**
 * Representa la asignacion de un lote a una patrulla en el sistema Code4Hope.
 * Corresponde a la tabla ASIGNACION_LOTE de la base de datos.
 *
 * Punto de integracion entre el subsistema de Alimentos/Medicamentos
 * y el subsistema de Patrullas.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class AsignacionLote {

    private int idAsignacion;
    private int idLote;
    private int idPatrulla;
    private int cantidadAsignada;
    private LocalDate fechaAsignacion;

    /**
     * Constructor completo, usado al recuperar una asignacion de la BD.
     */
    public AsignacionLote(int idAsignacion, int idLote, int idPatrulla,
            int cantidadAsignada, LocalDate fechaAsignacion) {
        this.idAsignacion = idAsignacion;
        this.idLote = idLote;
        this.idPatrulla = idPatrulla;
        this.cantidadAsignada = cantidadAsignada;
        this.fechaAsignacion = fechaAsignacion;
    }

    /**
     * Constructor para crear una nueva asignacion.
     * La fecha se establece automaticamente a la fecha actual.
     * El idAsignacion se asigna a 0 y lo actualiza la BD.
     */
    public AsignacionLote(int idLote, int idPatrulla, int cantidadAsignada) {
        this(0, idLote, idPatrulla, cantidadAsignada, LocalDate.now());
    }

    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public int getIdPatrulla() {
        return idPatrulla;
    }

    public void setIdPatrulla(int idPatrulla) {
        this.idPatrulla = idPatrulla;
    }

    public int getCantidadAsignada() {
        return cantidadAsignada;
    }

    public void setCantidadAsignada(int cantidadAsignada) {
        this.cantidadAsignada = cantidadAsignada;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    @Override
    public String toString() {
        return "AsignacionLote{id=" + idAsignacion +
                ", lote=" + idLote +
                ", patrulla=" + idPatrulla +
                ", cantidad=" + cantidadAsignada +
                ", fecha=" + fechaAsignacion + '}';
    }
}
