/*Subsistema Talleres de formación
 *Realizado por: Melisa
 * Monitor */
package Entidad;

/**
 * Entidad que representa a un monitor o monitora del subsistema de talleres
 */
public class Monitor {

    private String nif;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private boolean activo;

    /**
     * @return NIF del monitor/a
     */
    public String getNif() {
        return nif;
    }

    /**
     * @return nombre del monitor/a
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return apellido del monitor/a
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @return telefono de contacto
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @return direccion postal del monitor/a
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @return true si el monitor/a esta activo, false si ha sido dado de baja
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * @param nombre nombre del monitor/a
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @param apellido apellido del monitor/a
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * @param telefono telefono de contacto
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @param direccion direccion postal del monitor/a
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @param activo true para activar, false para dar de baja
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Crea un monitor/a activo con los datos proporcionados
     *
     * @param nif       NIF del monitor/a (maximo 9 caracteres)
     * @param nombre    nombre del monitor/a
     * @param apellido  apellido del monitor/a
     * @param telefono  telefono de contacto
     * @param direccion direccion postal
     */
    public Monitor(String nif, String nombre, String apellido,
            String telefono, String direccion) {
        this.nif = nif;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
        this.activo = true;
    }

    /**
     * Devuelve nombre y apellido concatenados
     *
     * @return nombre completo del monitor/a
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return "Monitor/a{nif='" + nif + "', nombre='" + getNombreCompleto() + "', activo=" + activo + "}";
    }
}
