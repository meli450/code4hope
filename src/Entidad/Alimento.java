package Entidad;

/**
 * Subclase de Producto que representa un alimento en el sistema Code4Hope.
 * Implementa los métodos abstractos de Producto adaptándolos a la
 * gestión de alimentos (calorías, refrigeración, tipo de dieta...).
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class Alimento extends Producto {

    private int calorias;
    private String tipoDieta;
    private boolean necesitaRefrigeracion;
    private double temperaturaMin;
    private double temperaturaMax;

    public Alimento(int idProducto, String nombre, String descripcion,
            String unidadMedida, double precio, String categoria,
            int calorias, String tipoDieta, boolean necesitaRefrigeracion,
            double temperaturaMin, double temperaturaMax) {
        super(idProducto, nombre, descripcion, unidadMedida, precio, categoria);
        this.calorias = calorias;
        this.tipoDieta = tipoDieta;
        this.necesitaRefrigeracion = necesitaRefrigeracion;
        this.temperaturaMin = temperaturaMin;
        this.temperaturaMax = temperaturaMax;
    }

    @Override
    public String getDetallesEspecificos() {
        String detalles;
        detalles = "=== DETALLES ALIMENTO ===" +
                "\n  Calorias/unidad  : " + calorias +
                "\n  Tipo de dieta    : " + tipoDieta +
                "\n  Refrigeracion    : " + (necesitaRefrigeracion ? "SI" : "NO");

        if (necesitaRefrigeracion) {
            detalles += "\n  Temp. min (grados C): " + temperaturaMin +
                    "\n  Temp. max (grados C): " + temperaturaMax;
        }

        return detalles;
    }

    /**
     * Calcula el valor total del inventario de alimentos.
     * Aplica un descuento del 5% si la cantidad supera las 1000 unidades.
     */
    @Override
    public double calcularValorTotal(int cantidad) {
        double valorBase;
        double valorFinal;
        valorBase = precio * cantidad;

        if (cantidad > 1000) {
            valorFinal = valorBase * 0.95;
        } else {
            valorFinal = valorBase;
        }

        return valorFinal;
    }

    @Override
    public String generarAlertaStock(int cantidadActual, int umbralMinimo) {
        String alerta;

        if (cantidadActual <= 0) {
            alerta = "[AGOTADO] " + nombre + " - SIN STOCK. Solicitar reposicion inmediata.";
        } else if (cantidadActual < umbralMinimo) {
            alerta = "[ALERTA ALIMENTO] " + nombre +
                    " - Stock bajo: " + cantidadActual + " " + unidadMedida +
                    " (minimo: " + umbralMinimo + ")";
        } else {
            alerta = "[OK] " + nombre + " - Stock suficiente: " +
                    cantidadActual + " " + unidadMedida;
        }

        return alerta;
    }

    public int getCalorias() {
        return calorias;
    }

    public String getTipoDieta() {
        return tipoDieta;
    }

    public boolean isNecesitaRefrigeracion() {
        return necesitaRefrigeracion;
    }

    public double getTemperaturaMin() {
        return temperaturaMin;
    }

    public double getTemperaturaMax() {
        return temperaturaMax;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }

    public void setTipoDieta(String tipoDieta) {
        this.tipoDieta = tipoDieta;
    }

    public void setNecesitaRefrigeracion(boolean nR) {
        this.necesitaRefrigeracion = nR;
    }

    public void setTemperaturaMin(double temperaturaMin) {
        this.temperaturaMin = temperaturaMin;
    }

    public void setTemperaturaMax(double temperaturaMax) {
        this.temperaturaMax = temperaturaMax;
    }

    @Override
    public String toString() {
        return "Alimento{id=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", calorias=" + calorias +
                ", tipoDieta='" + tipoDieta + '\'' +
                ", refrigeracion=" + necesitaRefrigeracion +
                ", precio=" + precio + '}';
    }
}
