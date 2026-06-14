/*Subsistema Talleres de formación
 *Realizado por: Melisa
 * Recurso */
package Entidad;

/**
 * Entidad que representa un recurso material del subsistema de talleres.
 */
public class Recurso {

    private int id;
    private String tipo;
    private int cantidad;
    private boolean esFungible;
    private EstadoRecursoEnum estado;
    private int idPatrulla;

    /**
     * @return identificador unico del recurso
     */
    public int getId() {
        return id;
    }

    /**
     * @return tipo o nombre del recurso
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @return cantidad disponible del recurso
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * @return true si el recurso se consume con el uso
     */
    public boolean isEsFungible() {
        return esFungible;
    }

    /**
     * @return estado de disponibilidad del recurso
     */
    public EstadoRecursoEnum getEstado() {
        return estado;
    }

    /**
     * @return identificador de la patrulla asignada o 0 si no tiene
     */
    public int getIdPatrulla() {
        return idPatrulla;
    }

    /**
     * @param id identificador unico del recurso
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param tipo tipo o nombre del recurso
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @param cantidad cantidad disponible del recurso
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @param esFungible true si el recurso se consume con el uso
     */
    public void setEsFungible(boolean esFungible) {
        this.esFungible = esFungible;
    }

    /**
     * @param estado estado de disponibilidad del recurso
     */
    public void setEstado(EstadoRecursoEnum estado) {
        this.estado = estado;
    }

    /**
     * @param idPatrulla identificador de la patrulla asignada
     */
    public void setIdPatrulla(int idPatrulla) {
        this.idPatrulla = idPatrulla;
    }

    /**
     * Crea un recurso disponible con los datos proporcionados.
     *
     * @param tipo        tipo o nombre del recurso
     * @param cantidad    cantidad disponible
     * @param esFungible  true si el recurso se consume con el uso
     */
    public Recurso(String tipo, int cantidad, boolean esFungible) {
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.esFungible = esFungible;
        this.estado = EstadoRecursoEnum.DISPONIBLE;
        this.idPatrulla = 0;
    }

    /**
     * Indica si el recurso esta disponible para asignarse.
     *
     * @return true si el estado es DISPONIBLE y hay cantidad mayor que cero
     */
    public boolean isDisponible() {
        return estado == EstadoRecursoEnum.DISPONIBLE && cantidad > 0;
    }

    @Override
    public String toString() {
        return "Recurso{id=" + id + ", tipo='" + tipo + "', cantidad=" + cantidad
                + ", estado=" + estado.name() + ", idPatrulla=" + idPatrulla + "}";
    }
}
