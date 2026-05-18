package Entidad;

/**
 * Representa un mensaje registrado en el historial de comunicaciones de un equipo.
 * Almacena el emisor, la hora, el tipo de mensaje y el contenido del mismo.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class RegistroComunicacion {

    /**
     * Clasificacion del tipo de mensaje de comunicacion.
     */
    public enum TipoMensaje {
        /** Mensaje de texto ordinario. */
        TEXTO("Texto"),
        /** Alerta que requiere atencion pero no es critica. */
        ALERTA("Alerta"),
        /** Confirmacion de una orden o evento. */
        CONFIRMACION("Confirmacion"),
        /** Situacion de emergencia que requiere respuesta inmediata. */
        EMERGENCIA("Emergencia");

        private final String valor;

        TipoMensaje(String valor) { this.valor = valor; }

        /**
         * Devuelve la representacion textual del tipo de mensaje.
         *
         * @return cadena con el valor del tipo.
         */
        public String getValor() { return valor; }

        /**
         * Convierte una cadena en la constante de tipo de mensaje correspondiente.
         * Si no hay coincidencia devuelve {@code TEXTO} por defecto.
         *
         * @param s cadena a convertir (sin distincion de mayusculas).
         * @return constante {@code TipoMensaje} que coincide con la cadena.
         */
        public static TipoMensaje fromString(String s) {
            TipoMensaje resultado = TEXTO;
            for (TipoMensaje t : values()) {
                if (t.valor.equalsIgnoreCase(s)) {
                    resultado = t;
                }
            }
            return resultado;
        }
    }

    private int id;
    private int equipoId;
    private String hora;
    private TipoMensaje tipo;
    private String mensaje;
    private String emisor;

    /**
     * Crea una nueva instancia de RegistroComunicacion sin datos iniciales.
     */
    public RegistroComunicacion() {}

    /** Devuelve el identificador unico del registro. */
    public int getId() { return id; }
    /** Establece el identificador unico del registro. @param id nuevo identificador. */
    public void setId(int id) { this.id = id; }

    /** Devuelve el identificador del equipo que genero este registro. */
    public int getEquipoId() { return equipoId; }
    /** Establece el identificador del equipo. @param equipoId ID del equipo de comunicacion. */
    public void setEquipoId(int equipoId) { this.equipoId = equipoId; }

    /** Devuelve la hora en que se emitio el mensaje. */
    public String getHora() { return hora; }
    /** Establece la hora del mensaje. @param hora marca de tiempo en formato texto. */
    public void setHora(String hora) { this.hora = hora; }

    /** Devuelve el tipo de mensaje. */
    public TipoMensaje getTipo() { return tipo; }
    /** Establece el tipo de mensaje. @param tipo clasificacion del mensaje. */
    public void setTipo(TipoMensaje tipo) { this.tipo = tipo; }

    /** Devuelve el contenido del mensaje. */
    public String getMensaje() { return mensaje; }
    /** Establece el contenido del mensaje. @param mensaje texto del mensaje emitido. */
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    /** Devuelve el identificador o nombre del emisor del mensaje. */
    public String getEmisor() { return emisor; }
    /** Establece el emisor del mensaje. @param emisor nombre o codigo del emisor. */
    public void setEmisor(String emisor) { this.emisor = emisor; }

    /**
     * Devuelve una representacion legible del registro de comunicacion.
     *
     * @return cadena con la hora, tipo, emisor y contenido del mensaje.
     */
    @Override
    public String toString() {
        return String.format("[%s] (%s) %s -> \"%s\"", hora, tipo.getValor(), emisor, mensaje);
    }
}
