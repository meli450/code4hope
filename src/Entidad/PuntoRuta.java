package Entidad;

/**
 * Representa un punto geografico dentro de una ruta de mision.
 * Cada punto tiene coordenadas GPS, un tipo, un estado de avance,
 * horarios estimados y reales, y puede registrar notas de incidencia.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class PuntoRuta {

    /**
     * Clasificacion funcional de un punto dentro de la ruta.
     */
    public enum TipoPunto {
        /** Primer punto de la ruta; marca el inicio de la mision. */
        INICIO("Inicio"),
        /** Punto de control intermedio de verificacion. */
        CONTROL("Control"),
        /** Punto donde se ha registrado o se espera una incidencia. */
        INCIDENCIA("Incidencia"),
        /** Ultimo punto de la ruta; marca el fin de la mision. */
        FIN("Fin"),
        /** Parada en una gasolinera para repostar. */
        GASOLINERA("Gasolinera");

        private final String valor;

        TipoPunto(String valor) { this.valor = valor; }

        /**
         * Devuelve la representacion textual del tipo de punto.
         *
         * @return cadena con el valor del tipo.
         */
        public String getValor() { return valor; }

        /**
         * Convierte una cadena en la constante de tipo de punto correspondiente.
         * Si no hay coincidencia devuelve {@code CONTROL} por defecto.
         *
         * @param s cadena a convertir (sin distincion de mayusculas).
         * @return constante {@code TipoPunto} que coincide con la cadena.
         */
        public static TipoPunto fromString(String s) {
            TipoPunto resultado = CONTROL;
            for (TipoPunto t : values()) {
                if (t.valor.equalsIgnoreCase(s)) {
                    resultado = t;
                }
            }
            return resultado;
        }
    }

    /**
     * Estado de avance de un punto de ruta durante la mision.
     */
    public enum EstadoPunto {
        /** El punto todavia no ha sido visitado. */
        PENDIENTE("Pendiente"),
        /** El punto ya ha sido alcanzado por la patrulla. */
        ALCANZADO("Alcanzado"),
        /** El punto fue omitido durante la mision. */
        OMITIDO("Omitido");

        private final String valor;

        EstadoPunto(String valor) { this.valor = valor; }

        /**
         * Devuelve la representacion textual del estado del punto.
         *
         * @return cadena con el valor del estado.
         */
        public String getValor() { return valor; }

        /**
         * Convierte una cadena en la constante de estado de punto correspondiente.
         * Si no hay coincidencia devuelve {@code PENDIENTE} por defecto.
         *
         * @param s cadena a convertir (sin distincion de mayusculas).
         * @return constante {@code EstadoPunto} que coincide con la cadena.
         */
        public static EstadoPunto fromString(String s) {
            EstadoPunto resultado = PENDIENTE;
            for (EstadoPunto e : values()) {
                if (e.valor.equalsIgnoreCase(s)) {
                    resultado = e;
                }
            }
            return resultado;
        }
    }

    private int id;
    private int rutaId;
    private String nombre;
    private String descripcion;
    private TipoPunto tipo;
    private double latitud;
    private double longitud;
    private EstadoPunto estado;
    private String horaEstimada;
    private String horaRealLlegada;
    private String notasIncidencia;
    private boolean esGasolinera;
    private int posicion;

    /**
     * Crea una nueva instancia de PuntoRuta sin datos iniciales.
     */
    public PuntoRuta() {}

    /** Devuelve el identificador unico del punto de ruta. */
    public int getId() { return id; }
    /** Establece el identificador unico del punto de ruta. @param id nuevo identificador. */
    public void setId(int id) { this.id = id; }

    /** Devuelve el identificador de la ruta a la que pertenece este punto. */
    public int getRutaId() { return rutaId; }
    /** Establece el identificador de la ruta padre. @param rutaId ID de la ruta. */
    public void setRutaId(int rutaId) { this.rutaId = rutaId; }

    /** Devuelve el nombre del punto de ruta. */
    public String getNombre() { return nombre; }
    /** Establece el nombre del punto de ruta. @param nombre nombre descriptivo del punto. */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** Devuelve la descripcion detallada del punto de ruta. */
    public String getDescripcion() { return descripcion; }
    /** Establece la descripcion del punto de ruta. @param descripcion texto descriptivo adicional. */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /** Devuelve el tipo funcional del punto. */
    public TipoPunto getTipo() { return tipo; }
    /** Establece el tipo funcional del punto. @param tipo clasificacion del punto. */
    public void setTipo(TipoPunto tipo) { this.tipo = tipo; }

    /** Devuelve la latitud geografica del punto. */
    public double getLatitud() { return latitud; }
    /** Establece la latitud geografica del punto. @param latitud coordenada de latitud en grados decimales. */
    public void setLatitud(double latitud) { this.latitud = latitud; }

    /** Devuelve la longitud geografica del punto. */
    public double getLongitud() { return longitud; }
    /** Establece la longitud geografica del punto. @param longitud coordenada de longitud en grados decimales. */
    public void setLongitud(double longitud) { this.longitud = longitud; }

    /** Devuelve el estado de avance del punto. */
    public EstadoPunto getEstado() { return estado; }
    /** Establece el estado de avance del punto. @param estado nuevo estado del punto. */
    public void setEstado(EstadoPunto estado) { this.estado = estado; }

    /** Devuelve la hora estimada de llegada al punto. */
    public String getHoraEstimada() { return horaEstimada; }
    /** Establece la hora estimada de llegada. @param horaEstimada hora prevista en formato texto. */
    public void setHoraEstimada(String horaEstimada) { this.horaEstimada = horaEstimada; }

    /** Devuelve la hora real de llegada al punto (null si aun no ha sido alcanzado). */
    public String getHoraRealLlegada() { return horaRealLlegada; }
    /** Establece la hora real de llegada al punto. @param horaRealLlegada hora efectiva de llegada en formato texto. */
    public void setHoraRealLlegada(String horaRealLlegada) { this.horaRealLlegada = horaRealLlegada; }

    /** Devuelve las notas de incidencia registradas en el punto (null si no hay ninguna). */
    public String getNotasIncidencia() { return notasIncidencia; }
    /** Establece las notas de incidencia del punto. @param notasIncidencia texto con la descripcion de la incidencia. */
    public void setNotasIncidencia(String notasIncidencia) { this.notasIncidencia = notasIncidencia; }

    /** Indica si el punto corresponde a una parada en una gasolinera. */
    public boolean isEsGasolinera() { return esGasolinera; }
    /** Establece si el punto es una gasolinera. @param esGasolinera true si el punto es una gasolinera. */
    public void setEsGasolinera(boolean esGasolinera) { this.esGasolinera = esGasolinera; }

    /** Devuelve la posicion ordinal del punto dentro de la secuencia de la ruta. */
    public int getPosicion() { return posicion; }
    /** Establece la posicion ordinal del punto en la ruta. @param posicion numero de orden dentro de la ruta. */
    public void setPosicion(int posicion) { this.posicion = posicion; }

    /**
     * Devuelve una representacion legible del punto con sus datos principales.
     *
     * @return cadena con ID, posicion, nombre, tipo, horarios estimado y real, estado y notas.
     */
    @Override
    public String toString() {
        String horaReal = (horaRealLlegada != null) ? horaRealLlegada : "Pendiente";
        String nota = (notasIncidencia != null) ? " | Nota: " + notasIncidencia : "";
        return String.format("[ID:%d | Pos:%d] %s (%s) | Est: %s | Real: %s | Estado: %s%s",
                id, posicion, nombre, tipo.getValor(), horaEstimada, horaReal, estado.getValor(), nota);
    }
}
