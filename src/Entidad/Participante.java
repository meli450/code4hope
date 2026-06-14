/*Subsistema Talleres de formación
 *Realizado por: Melisa
 * Participante */
package Entidad;

/**
 * Entidad que representa a un participante del subsistema de talleres
 */
public class Participante extends Persona {

    private int id;
    private String genero;
    private int edad;
    private PerfilEnum perfil;
    private boolean activo;

    /**
     * @return identificador unico del o la participante
     */
    public int getId() {
        return id;
    }

    /**
     * @return genero del o la participante
     */
    public String getGenero() {
        return genero;
    }

    /**
     * @return edad del o la participante
     */
    public int getEdad() {
        return edad;
    }

    /**
     * @return perfil del o la participante
     */
    public PerfilEnum getPerfil() {
        return perfil;
    }

    /**
     * @return true si el participante esta activo, false si ha sido dado de baja
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * @param id identificador unico del o la participante
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param genero genero del o la participante
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * @param edad edad del o la participante
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * @param perfil perfil del o la participante
     */
    public void setPerfil(PerfilEnum perfil) {
        this.perfil = perfil;
    }

    /**
     * @param activo true para activar, false para dar de baja
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Crea un/a participante activo/a con los datos proporcionados
     *
     * @param nombre   nombre del o la participante
     * @param apellido apellido del o la participante
     * @param genero   genero del o la participante
     * @param edad     edad del o la participante
     * @param perfil   perfil del o la participante
     */
    public Participante(String nombre, String apellido, String genero,
            int edad, PerfilEnum perfil) {
        super(nombre, apellido);
        this.genero = genero;
        this.edad = edad;
        this.perfil = perfil;
        this.activo = true;
    }

    @Override
    public String toString() {
        return "Participante{id=" + id + ", nombre='" + getNombreCompleto()
                + "', perfil=" + perfil.name() + ", activo=" + activo + "}";
    }
}
