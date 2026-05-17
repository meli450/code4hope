package Entidad;

/**
 * Representa a un miembro de una patrulla dentro del subsistema.
 * Almacena los datos personales, el rol operativo y la patrulla a la que pertenece.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class Tripulante {

    /**
     * Rol funcional que puede desempenar un tripulante dentro de la patrulla.
     */
    public enum Rol {
        /** Responsable de conducir el vehiculo de la patrulla. */
        CONDUCTOR("Conductor"),
        /** Agente operativo de campo. */
        AGENTE("Agente"),
        /** Jefe o lider de la patrulla. */
        JEFE("Jefe"),
        /** Personal de soporte logistico o tecnico. */
        SOPORTE("Soporte");

        private final String valor;

        Rol(String valor) { this.valor = valor; }

        /**
         * Devuelve la representacion textual del rol.
         *
         * @return cadena con el valor del rol.
         */
        public String getValor() { return valor; }

        /**
         * Convierte una cadena en la constante de rol correspondiente.
         * Si no hay coincidencia devuelve {@code AGENTE} por defecto.
         *
         * @param s cadena a convertir (sin distincion de mayusculas).
         * @return constante {@code Rol} que coincide con la cadena.
         */
        public static Rol fromString(String s) {
            Rol resultado = AGENTE;
            for (Rol r : values()) {
                if (r.valor.equalsIgnoreCase(s)) {
                    resultado = r;
                }
            }
            return resultado;
        }
    }

    /**
     * Estado operativo actual de un tripulante.
     */
    public enum EstadoOperativo {
        /** El tripulante esta en servicio activo. */
        ACTIVO("Activo"),
        /** El tripulante esta de baja y no puede ser asignado. */
        DE_BAJA("DeBaja"),
        /** El tripulante esta disponible para ser asignado a una patrulla. */
        DISPONIBLE("Disponible"),
        /** El tripulante ya ha sido asignado a una patrulla. */
        ASIGNADO("Asignado");

        private final String valor;

        EstadoOperativo(String valor) { this.valor = valor; }

        /**
         * Devuelve la representacion textual del estado operativo.
         *
         * @return cadena con el valor del estado.
         */
        public String getValor() { return valor; }

        /**
         * Convierte una cadena en la constante de estado operativo correspondiente.
         * Si no hay coincidencia devuelve {@code DISPONIBLE} por defecto.
         *
         * @param s cadena a convertir (sin distincion de mayusculas).
         * @return constante {@code EstadoOperativo} que coincide con la cadena.
         */
        public static EstadoOperativo fromString(String s) {
            EstadoOperativo resultado = DISPONIBLE;
            for (EstadoOperativo e : values()) {
                if (e.valor.equalsIgnoreCase(s)) {
                    resultado = e;
                }
            }
            return resultado;
        }
    }

    private int id;
    private String nif;
    private String nombre;
    private String apellido;
    private String telefonoContacto;
    private Rol rol;
    private EstadoOperativo estadoOperativo;
    private int patrullaId;

    /**
     * Crea una nueva instancia de Tripulante sin datos iniciales.
     */
    public Tripulante() {}

    /**
     * Devuelve el nombre completo del tripulante (nombre y apellido concatenados).
     *
     * @return cadena con el nombre y apellido.
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /** Devuelve el identificador unico del tripulante. */
    public int getId() { return id; }
    /** Establece el identificador unico del tripulante. @param id nuevo identificador. */
    public void setId(int id) { this.id = id; }

    /** Devuelve el NIF del tripulante. */
    public String getNif() { return nif; }
    /** Establece el NIF del tripulante. @param nif numero de identificacion fiscal. */
    public void setNif(String nif) { this.nif = nif; }

    /** Devuelve el nombre del tripulante. */
    public String getNombre() { return nombre; }
    /** Establece el nombre del tripulante. @param nombre nombre de pila. */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** Devuelve el apellido del tripulante. */
    public String getApellido() { return apellido; }
    /** Establece el apellido del tripulante. @param apellido primer apellido. */
    public void setApellido(String apellido) { this.apellido = apellido; }

    /** Devuelve el telefono de contacto del tripulante. */
    public String getTelefonoContacto() { return telefonoContacto; }
    /** Establece el telefono de contacto del tripulante. @param telefonoContacto numero de telefono. */
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }

    /** Devuelve el rol del tripulante dentro de la patrulla. */
    public Rol getRol() { return rol; }
    /** Establece el rol del tripulante. @param rol rol funcional asignado. */
    public void setRol(Rol rol) { this.rol = rol; }

    /** Devuelve el estado operativo actual del tripulante. */
    public EstadoOperativo getEstadoOperativo() { return estadoOperativo; }
    /** Establece el estado operativo del tripulante. @param estadoOperativo nuevo estado operativo. */
    public void setEstadoOperativo(EstadoOperativo estadoOperativo) { this.estadoOperativo = estadoOperativo; }

    /** Devuelve el identificador de la patrulla a la que pertenece (0 si no esta asignado). */
    public int getPatrullaId() { return patrullaId; }
    /** Establece el identificador de la patrulla a la que pertenece. @param patrullaId ID de la patrulla. */
    public void setPatrullaId(int patrullaId) { this.patrullaId = patrullaId; }

    /**
     * Devuelve una representacion legible del tripulante con sus datos principales.
     *
     * @return cadena con ID, nombre completo, NIF, rol, estado operativo y patrulla asignada.
     */
    @Override
    public String toString() {
        String asignacion = (patrullaId > 0) ? "Patrulla ID: " + patrullaId : "Sin asignar";
        return String.format("Tripulante [ID:%d] %s | NIF: %s | Rol: %s | Estado: %s | %s",
                id, getNombreCompleto(), nif, rol.getValor(), estadoOperativo.getValor(), asignacion);
    }
}
