package Entidad;

/**
 * Representa la ruta de mision asignada a una patrulla.
 * Contiene los horarios, el grado de peligrosidad, la distancia total y
 * el indice del punto de ruta en el que se encuentra actualmente la patrulla.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class Ruta {

    /**
     * Estados posibles del ciclo de vida de una ruta de mision.
     */
    public enum EstadoRuta {
        /** La ruta esta planificada pero aun no ha comenzado. */
        PENDIENTE("Pendiente"),
        /** La ruta se esta ejecutando en este momento. */
        EN_CURSO("EnCurso"),
        /** La ruta ha sido completada con exito. */
        COMPLETADA("Completada"),
        /** La ruta fue abortada antes de su finalizacion. */
        ABORTADA("Abortada");

        private final String valor;

        EstadoRuta(String valor) { this.valor = valor; }

        /**
         * Devuelve la representacion textual del estado de la ruta.
         *
         * @return cadena con el valor del estado.
         */
        public String getValor() { return valor; }

        /**
         * Convierte una cadena en la constante de estado de ruta correspondiente.
         * Si no hay coincidencia devuelve {@code PENDIENTE} por defecto.
         *
         * @param s cadena a convertir (sin distincion de mayusculas).
         * @return constante {@code EstadoRuta} que coincide con la cadena.
         */
        public static EstadoRuta fromString(String s) {
            EstadoRuta resultado = PENDIENTE;
            for (EstadoRuta e : values()) {
                if (e.valor.equalsIgnoreCase(s)) {
                    resultado = e;
                }
            }
            return resultado;
        }
    }

    private int id;
    private String nombre;
    private EstadoRuta estado;
    private String fechaMision;
    private String horaInicio;
    private String horaFin;
    private int indicePuntoActual;
    private String gradoPeligrosidad;
    private float numKm;

    /**
     * Crea una nueva instancia de Ruta sin datos iniciales.
     */
    public Ruta() {}

    /** Devuelve el identificador unico de la ruta. */
    public int getId() { return id; }
    /** Establece el identificador unico de la ruta. @param id nuevo identificador. */
    public void setId(int id) { this.id = id; }

    /** Devuelve el nombre descriptivo de la ruta. */
    public String getNombre() { return nombre; }
    /** Establece el nombre descriptivo de la ruta. @param nombre nombre de la ruta. */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** Devuelve el estado actual de la ruta. */
    public EstadoRuta getEstado() { return estado; }
    /** Establece el estado de la ruta. @param estado nuevo estado de la ruta. */
    public void setEstado(EstadoRuta estado) { this.estado = estado; }

    /** Devuelve la fecha de la mision en formato texto. */
    public String getFechaMision() { return fechaMision; }
    /** Establece la fecha de la mision. @param fechaMision fecha en formato texto (p.ej. yyyy-MM-dd). */
    public void setFechaMision(String fechaMision) { this.fechaMision = fechaMision; }

    /** Devuelve la hora de inicio de la ruta. */
    public String getHoraInicio() { return horaInicio; }
    /** Establece la hora de inicio de la ruta. @param horaInicio hora de salida en formato texto. */
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    /** Devuelve la hora de fin de la ruta (null si aun no ha terminado). */
    public String getHoraFin() { return horaFin; }
    /** Establece la hora de fin de la ruta. @param horaFin hora de llegada en formato texto. */
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    /** Devuelve el indice del punto de ruta en el que se encuentra actualmente la patrulla. */
    public int getIndicePuntoActual() { return indicePuntoActual; }
    /** Establece el indice del punto de ruta actual. @param indicePuntoActual posicion actual en la secuencia de puntos. */
    public void setIndicePuntoActual(int indicePuntoActual) { this.indicePuntoActual = indicePuntoActual; }

    /** Devuelve el grado de peligrosidad de la ruta. */
    public String getGradoPeligrosidad() { return gradoPeligrosidad; }
    /** Establece el grado de peligrosidad de la ruta. @param gradoPeligrosidad nivel de peligro (p.ej. Alto, Medio, Bajo). */
    public void setGradoPeligrosidad(String gradoPeligrosidad) { this.gradoPeligrosidad = gradoPeligrosidad; }

    /** Devuelve la distancia total de la ruta en kilometros. */
    public float getNumKm() { return numKm; }
    /** Establece la distancia total de la ruta. @param numKm distancia en kilometros. */
    public void setNumKm(float numKm) { this.numKm = numKm; }

    /**
     * Devuelve una representacion legible de la ruta con sus datos principales.
     *
     * @return cadena con ID, nombre, fecha, estado, punto actual, horarios, distancia y peligrosidad.
     */
    @Override
    public String toString() {
        String salida = (horaInicio != null) ? horaInicio : "Pendiente";
        String llegada = (horaFin != null) ? horaFin : "En curso";
        return String.format("Ruta [ID:%d] \"%s\" | Fecha: %s | Estado: %s | Punto: %d | Salida: %s | Llegada: %s | %.1f km | Peligrosidad: %s",
                id, nombre, fechaMision, estado.getValor(), indicePuntoActual, salida, llegada, numKm, gradoPeligrosidad);
    }
}
