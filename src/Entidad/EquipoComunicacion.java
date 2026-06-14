package Entidad;

/**
 * Representa un equipo de comunicacion asignable a una patrulla.
 * Gestiona el estado operativo del equipo y permite determinar si esta en condiciones de uso.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class EquipoComunicacion {

    /**
     * Estado operativo de un equipo de comunicacion.
     */
    public enum EstadoEquipo {
        /** El equipo esta en funcionamiento normal. */
        ACTIVO("Activo"),
        /** El equipo esta averiado o en mantenimiento y no puede usarse. */
        FUERA_DE_SERVICIO("FueraDeServicio"),
        /** El equipo esta en reposo pero operativo. */
        EN_DESCANSO("EnDescanso"),
        /** El equipo no responde a las comunicaciones. */
        SIN_CONTACTO("SinContacto");

        private final String valor;

        EstadoEquipo(String valor) { this.valor = valor; }

        /**
         * Devuelve la representacion textual del estado del equipo.
         *
         * @return cadena con el valor del estado.
         */
        public String getValor() { return valor; }

        /**
         * Convierte una cadena en la constante de estado de equipo correspondiente.
         * Si no hay coincidencia devuelve {@code ACTIVO} por defecto.
         *
         * @param s cadena a convertir (sin distincion de mayusculas).
         * @return constante {@code EstadoEquipo} que coincide con la cadena.
         */
        public static EstadoEquipo fromString(String s) {
            EstadoEquipo resultado = ACTIVO;
            for (EstadoEquipo e : values()) {
                if (e.valor.equalsIgnoreCase(s)) {
                    resultado = e;
                }
            }
            return resultado;
        }
    }

    private int id;
    private String nombre;
    private EstadoEquipo estadoEquipo;

    /**
     * Crea una nueva instancia de EquipoComunicacion sin datos iniciales.
     */
    public EquipoComunicacion() {}

    /**
     * Indica si el equipo esta operativo, es decir, si su estado no es {@code FUERA_DE_SERVICIO}.
     *
     * @return {@code true} si el equipo puede usarse; {@code false} si esta fuera de servicio.
     */
    public boolean isOperativo() {
        return estadoEquipo != EstadoEquipo.FUERA_DE_SERVICIO;
    }

    /** Devuelve el identificador unico del equipo de comunicacion. */
    public int getId() { return id; }
    /** Establece el identificador unico del equipo. @param id nuevo identificador. */
    public void setId(int id) { this.id = id; }

    /** Devuelve el nombre descriptivo del equipo de comunicacion. */
    public String getNombre() { return nombre; }
    /** Establece el nombre descriptivo del equipo. @param nombre nombre del equipo. */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** Devuelve el estado operativo actual del equipo. */
    public EstadoEquipo getEstadoEquipo() { return estadoEquipo; }
    /** Establece el estado operativo del equipo. @param estadoEquipo nuevo estado del equipo. */
    public void setEstadoEquipo(EstadoEquipo estadoEquipo) { this.estadoEquipo = estadoEquipo; }

    /**
     * Devuelve una representacion legible del equipo con sus datos principales.
     *
     * @return cadena con ID, nombre y estado del equipo.
     */
    @Override
    public String toString() {
        return String.format("EquipoCOM [ID:%d] \"%s\" | Estado: %s", id, nombre, estadoEquipo.getValor());
    }
}
