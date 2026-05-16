/*Subsistema Talleres de formación
 *Realizado por: Melisa
 * Encuesta */
package Entidad;

/**
 * Entidad que representa una encuesta de satisfaccion vinculada a un taller.
 */
public class Encuesta {

    private int cod;
    private String titulo;
    private String enlace;
    private String informe;
    private int codTaller;

    /**
     * @return codigo identificador de la encuesta
     */
    public int getCod() {
        return cod;
    }

    /**
     * @return titulo de la encuesta
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @return enlace al formulario de Google Forms
     */
    public String getEnlace() {
        return enlace;
    }

    /**
     * @return informe estadistico generado a partir de las respuestas
     */
    public String getInforme() {
        return informe;
    }

    /**
     * @return codigo del taller al que pertenece la encuesta
     */
    public int getCodTaller() {
        return codTaller;
    }

    /**
     * @param cod codigo identificador de la encuesta
     */
    public void setCod(int cod) {
        this.cod = cod;
    }

    /**
     * @param titulo titulo de la encuesta
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @param enlace enlace al formulario de Google Forms
     */
    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    /**
     * @param informe contenido del informe estadistico
     */
    public void setInforme(String informe) {
        this.informe = informe;
    }

    /**
     * @param codTaller codigo del taller asociado
     */
    public void setCodTaller(int codTaller) {
        this.codTaller = codTaller;
    }

    /**
     * Crea una encuesta sin informe asociada al taller indicado.
     *
     * @param titulo    titulo de la encuesta
     * @param enlace    enlace al formulario de Google Forms
     * @param codTaller codigo del taller al que pertenece
     */
    public Encuesta(String titulo, String enlace, int codTaller) {
        this.titulo = titulo;
        this.enlace = enlace;
        this.codTaller = codTaller;
    }

    @Override
    public String toString() {
        return "Encuesta{cod=" + cod + ", titulo='" + titulo + "', codTaller=" + codTaller + "}";
    }
}
