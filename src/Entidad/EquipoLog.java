package Entidad;

/**
 * Representa una entrada del registro de actividad (log) de un equipo de comunicacion.
 * Cada entrada almacena el texto del evento y la fecha y hora en que se produjo.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class EquipoLog {

    private int id;
    private int equipoId;
    private String entrada;
    private String fechaHora;

    /**
     * Crea una nueva instancia de EquipoLog sin datos iniciales.
     */
    public EquipoLog() {}

    /** Devuelve el identificador unico de la entrada de log. */
    public int getId() { return id; }
    /** Establece el identificador unico de la entrada. @param id nuevo identificador. */
    public void setId(int id) { this.id = id; }

    /** Devuelve el identificador del equipo al que pertenece esta entrada. */
    public int getEquipoId() { return equipoId; }
    /** Establece el identificador del equipo. @param equipoId ID del equipo de comunicacion. */
    public void setEquipoId(int equipoId) { this.equipoId = equipoId; }

    /** Devuelve el texto de la entrada de log. */
    public String getEntrada() { return entrada; }
    /** Establece el texto de la entrada de log. @param entrada descripcion del evento registrado. */
    public void setEntrada(String entrada) { this.entrada = entrada; }

    /** Devuelve la fecha y hora en que se registro la entrada. */
    public String getFechaHora() { return fechaHora; }
    /** Establece la fecha y hora de la entrada. @param fechaHora marca de tiempo en formato texto. */
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    /**
     * Devuelve una representacion legible de la entrada de log.
     *
     * @return cadena con la fecha/hora y el texto de la entrada.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", fechaHora, entrada);
    }
}
