package Entidad;

/**
 * Representa un vehiculo del parque movil asignable a una patrulla.
 * Almacena el tipo, la matricula, si es refrigerado y su disponibilidad actual.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class Vehiculo {

    /**
     * Tipo de vehiculo disponible en el parque movil.
     */
    public enum TipoVehiculo {
        /** Turismo o vehiculo ligero de pasajeros. */
        COCHE("Coche"),
        /** Motocicleta. */
        MOTO("Moto"),
        /** Furgoneta de carga o transporte. */
        FURGONETA("Furgoneta"),
        /** Camion de gran tonelaje. */
        CAMION("Camion");

        private final String valor;

        TipoVehiculo(String valor) { this.valor = valor; }

        /**
         * Devuelve la representacion textual del tipo de vehiculo.
         *
         * @return cadena con el valor del tipo.
         */
        public String getValor() { return valor; }

        /**
         * Convierte una cadena en la constante de tipo de vehiculo correspondiente.
         * Si no hay coincidencia devuelve {@code FURGONETA} por defecto.
         *
         * @param s cadena a convertir (sin distincion de mayusculas).
         * @return constante {@code TipoVehiculo} que coincide con la cadena.
         */
        public static TipoVehiculo fromString(String s) {
            TipoVehiculo resultado = FURGONETA;
            for (TipoVehiculo t : values()) {
                if (t.valor.equalsIgnoreCase(s)) {
                    resultado = t;
                }
            }
            return resultado;
        }
    }

    private int id;
    private String codigo;
    private TipoVehiculo tipo;
    private boolean refrigerado;
    private String matricula;
    private boolean disponible;

    /**
     * Crea una nueva instancia de Vehiculo sin datos iniciales.
     */
    public Vehiculo() {}

    /** Devuelve el identificador unico del vehiculo. */
    public int getId() { return id; }
    /** Establece el identificador unico del vehiculo. @param id nuevo identificador. */
    public void setId(int id) { this.id = id; }

    /** Devuelve el codigo interno del vehiculo. */
    public String getCodigo() { return codigo; }
    /** Establece el codigo interno del vehiculo. @param codigo nuevo codigo. */
    public void setCodigo(String codigo) { this.codigo = codigo; }

    /** Devuelve el tipo de vehiculo. */
    public TipoVehiculo getTipo() { return tipo; }
    /** Establece el tipo de vehiculo. @param tipo tipo de vehiculo asignado. */
    public void setTipo(TipoVehiculo tipo) { this.tipo = tipo; }

    /** Indica si el vehiculo dispone de sistema de refrigeracion. */
    public boolean isRefrigerado() { return refrigerado; }
    /** Establece si el vehiculo dispone de refrigeracion. @param refrigerado true si es refrigerado. */
    public void setRefrigerado(boolean refrigerado) { this.refrigerado = refrigerado; }

    /** Devuelve la matricula del vehiculo. */
    public String getMatricula() { return matricula; }
    /** Establece la matricula del vehiculo. @param matricula matricula oficial del vehiculo. */
    public void setMatricula(String matricula) { this.matricula = matricula; }

    /** Indica si el vehiculo esta disponible para ser asignado. */
    public boolean isDisponible() { return disponible; }
    /** Establece la disponibilidad del vehiculo. @param disponible true si esta libre para asignar. */
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    /**
     * Devuelve una representacion legible del vehiculo con sus datos principales.
     *
     * @return cadena con ID, codigo, tipo, refrigeracion, matricula y disponibilidad.
     */
    @Override
    public String toString() {
        String frio = refrigerado ? "(Refrigerado)" : "(Estandar)";
        String estado = disponible ? "DISPONIBLE" : "EN USO";
        return String.format("Vehiculo [ID:%d | %s] %s %s | Matricula: %s | %s",
                id, codigo, tipo.getValor(), frio, matricula, estado);
    }
}
