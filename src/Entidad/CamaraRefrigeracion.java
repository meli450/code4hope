package Entidad;

/**
 * Representa una camara de refrigeracion asociada a un almacen del sistema
 * Code4Hope. Permite gestionar la cadena de frio necesaria para conservar
 * correctamente los productos que requieren bajas temperaturas.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class CamaraRefrigeracion implements IRefrigeracion {

    private String codigo;
    private double capacidad;
    private double temperaturaMinima;
    private double temperaturaMaxima;
    private double temperaturaActual;
    private boolean activo;

    public CamaraRefrigeracion(String codigo, double capacidad,
            double temperaturaMinima, double temperaturaMaxima,
            double temperaturaActual, boolean activo) {
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.temperaturaMinima = temperaturaMinima;
        this.temperaturaMaxima = temperaturaMaxima;
        this.temperaturaActual = temperaturaActual;
        this.activo = activo;
    }

    /**
     * Verifica si una temperatura dada esta dentro del rango aceptable.
     *
     * @param temperatura Temperatura a verificar (grados C)
     * @return true si esta dentro del rango; false en caso contrario
     */
    public boolean verificarTemperatura(double temperatura) {
        boolean resultado;

        resultado = temperatura >= temperaturaMinima && temperatura <= temperaturaMaxima;

        return resultado;
    }

    /**
     * Comprueba si la temperatura actual esta dentro de su propio rango operativo.
     */
    public boolean temperaturaActualCorrecta() {
        boolean resultado;

        resultado = verificarTemperatura(temperaturaActual);

        return resultado;
    }

    @Override
    public boolean requiereRefrigeracion() {
        return true;
    }

    public boolean estaActivo() {
        boolean resultado;

        resultado = activo;

        return resultado;
    }

    public void setActivo(boolean estado) {
        this.activo = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getCapacidad() {
        return capacidad;
    }

    public double getTemperaturaMinima() {
        return temperaturaMinima;
    }

    public double getTemperaturaMaxima() {
        return temperaturaMaxima;
    }

    public double getTemperaturaActual() {
        return temperaturaActual;
    }

    public void setTemperaturaActual(double temperaturaActual) {
        this.temperaturaActual = temperaturaActual;
    }

    public void setTemperaturaMinima(double temperaturaMinima) {
        this.temperaturaMinima = temperaturaMinima;
    }

    public void setTemperaturaMaxima(double temperaturaMaxima) {
        this.temperaturaMaxima = temperaturaMaxima;
    }

    @Override
    public String toString() {
        return "CamaraRefrigeracion{codigo='" + codigo + '\'' +
                ", capacidad=" + capacidad +
                ", rango=[" + temperaturaMinima + "grados C, " + temperaturaMaxima + "grados C]" +
                ", actual=" + temperaturaActual + "grados C" +
                ", activo=" + activo + '}';
    }
}
