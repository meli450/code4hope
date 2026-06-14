/*Subsistema Talleres de formación
 *Realizado por: Melisa
 * Taller */
package Entidad;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un taller de formacion del subsistema.
 */
public class Taller {

    private int cod;
    private String titulo;
    private String descripcion;
    private PerfilEnum perfilRequerido;
    private String etiqueta;
    private String espacio;
    private int aforoMaximo;
    private String fechaInicio;
    private String fechaFin;
    private String fechaCancelacion;
    private String incidencia;
    private String nif;
    private EstadoTallerEnum estado;
    private List<Participante> participantes;
    private List<Recurso> recursos;

    /**
     * @return codigo identificador del taller
     */
    public int getCod() {
        return cod;
    }

    /**
     * @return titulo del taller
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @return descripcion del taller
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @return perfil de participante requerido
     */
    public PerfilEnum getPerfilRequerido() {
        return perfilRequerido;
    }

    /**
     * @return etiqueta tematica del taller
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * @return espacio o lugar donde se imparte el taller
     */
    public String getEspacio() {
        return espacio;
    }

    /**
     * @return numero maximo de participantes
     */
    public int getAforoMaximo() {
        return aforoMaximo;
    }

    /**
     * @return fecha de inicio en formato yyyy-MM-dd
     */
    public String getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @return fecha de fin en formato yyyy-MM-dd
     */
    public String getFechaFin() {
        return fechaFin;
    }

    /**
     * @return fecha de cancelacion en formato yyyy-MM-dd
     */
    public String getFechaCancelacion() {
        return fechaCancelacion;
    }

    /**
     * @return motivo de cancelacion del taller
     */
    public String getIncidencia() {
        return incidencia;
    }

    /**
     * @return NIF del monitor asignado
     */
    public String getNif() {
        return nif;
    }

    /**
     * @return estado actual del taller
     */
    public EstadoTallerEnum getEstado() {
        return estado;
    }

    /**
     * @return lista de participantes inscritos
     */
    public List<Participante> getParticipantes() {
        return participantes;
    }

    /**
     * @return lista de recursos asignados al taller
     */
    public List<Recurso> getRecursos() {
        return recursos;
    }

    /**
     * @param cod codigo identificador del taller
     */
    public void setCod(int cod) {
        this.cod = cod;
    }

    /**
     * @param titulo titulo del taller
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @param descripcion descripcion del taller
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @param perfilRequerido perfil de participante requerido
     */
    public void setPerfilRequerido(PerfilEnum perfilRequerido) {
        this.perfilRequerido = perfilRequerido;
    }

    /**
     * @param etiqueta etiqueta tematica del taller
     */
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * @param espacio espacio o lugar donde se imparte el taller
     */
    public void setEspacio(String espacio) {
        this.espacio = espacio;
    }

    /**
     * @param aforoMaximo numero maximo de participantes
     */
    public void setAforoMaximo(int aforoMaximo) {
        this.aforoMaximo = aforoMaximo;
    }

    /**
     * @param fechaInicio fecha de inicio en formato yyyy-MM-dd
     */
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @param fechaFin fecha de fin en formato yyyy-MM-dd
     */
    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * @param fechaCancelacion fecha de cancelacion en formato yyyy-MM-dd
     */
    public void setFechaCancelacion(String fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    /**
     * @param incidencia motivo de cancelacion del taller
     */
    public void setIncidencia(String incidencia) {
        this.incidencia = incidencia;
    }

    /**
     * @param nif NIF del monitor asignado
     */
    public void setNif(String nif) {
        this.nif = nif;
    }

    /**
     * @param estado nuevo estado del taller
     */
    public void setEstado(EstadoTallerEnum estado) {
        this.estado = estado;
    }

    /**
     * @param participantes lista de participantes inscritos
     */
    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes;
    }

    /**
     * @param recursos lista de recursos asignados al taller
     */
    public void setRecursos(List<Recurso> recursos) {
        this.recursos = recursos;
    }

    /**
     * Crea un taller en estado ACTIVO con los datos proporcionados.
     *
     * @param titulo          titulo del taller
     * @param descripcion     descripcion del taller
     * @param perfilRequerido perfil de participante requerido
     * @param etiqueta        etiqueta tematica
     * @param espacio         lugar donde se imparte
     * @param aforoMaximo     numero maximo de participantes
     * @param fechaInicio     fecha de inicio en formato yyyy-MM-dd
     * @param nif             NIF del monitor asignado (puede ser null)
     */
    public Taller(String titulo, String descripcion, PerfilEnum perfilRequerido,
            String etiqueta, String espacio, int aforoMaximo,
            String fechaInicio, String nif) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.perfilRequerido = perfilRequerido;
        this.etiqueta = etiqueta;
        this.espacio = espacio;
        this.aforoMaximo = aforoMaximo;
        this.fechaInicio = fechaInicio;
        this.nif = nif;
        this.estado = EstadoTallerEnum.ACTIVO;
        this.participantes = new ArrayList<>();
        this.recursos = new ArrayList<>();
    }


    @Override
    public String toString() {
        return "Taller{cod=" + cod + ", titulo='" + titulo + "', perfil="
                + perfilRequerido.name() + ", aforo=" + aforoMaximo
                + ", estado=" + estado.name() + "}";
    }
}
