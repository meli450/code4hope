/*Subsistema Talleres de formación
 *Realizado por: Melisa
 * Monitor */
package Entidad;

/**
 * Entidad que representa a un monitor o monitora del subsistema de talleres
 */
public class Monitor extends Persona {

    private String nif;
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
        super(nombre, apellido);
        this.nif = nif;
        this.telefono = telefono;
        this.direccion = direccion;
        this.activo = true;
    }

    @Override
    public String toString() {
        return "Monitor/a{nif='" + nif + "', nombre='" + getNombreCompleto() + "', activo=" + activo + "}";
    }
}
