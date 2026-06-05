package Entidad;

/**
 * Entidad abstracta que representa a una persona en el sistema Code4Hope.
 * Clase base para {@link Monitor}, {@link Paciente}, {@link Participante} y {@link Tripulante}.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public abstract class Persona {

    private String nombre;
    private String apellido;

    /**
     * Crea una persona sin datos iniciales.
     */
    protected Persona() {}

    /**
     * Crea una persona con nombre y apellido.
     *
     * @param nombre   nombre de pila
     * @param apellido primer apellido
     */
    protected Persona(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    /**
     * @return nombre de pila
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre nombre de pila
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return primer apellido
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @param apellido primer apellido
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Devuelve nombre y apellido concatenados.
     *
     * @return nombre completo
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
