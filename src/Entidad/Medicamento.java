package Entidad;

/**
 * Subclase de Producto que representa un medicamento en el sistema Code4Hope.
 * Implementa los métodos abstractos de Producto adaptándolos a la
 * gestión de medicamentos (principio activo, receta, vía de administración...).
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class Medicamento extends Producto {

    private String principioActivo;
    private String dosis;
    private String viaAdministracion;
    private boolean necesitaReceta;
    private double temperaturaAlmacenamiento;

    public Medicamento(int idProducto, String nombre, String descripcion,
            String unidadMedida, double precio, String categoria,
            String principioActivo, String dosis, String viaAdministracion,
            boolean necesitaReceta, double temperaturaAlmacenamiento) {
        super(idProducto, nombre, descripcion, unidadMedida, precio, categoria);
        this.principioActivo = principioActivo;
        this.dosis = dosis;
        this.viaAdministracion = viaAdministracion;
        this.necesitaReceta = necesitaReceta;
        this.temperaturaAlmacenamiento = temperaturaAlmacenamiento;
    }

    @Override
    public String getDetallesEspecificos() {
        String detalles;
        detalles = "=== DETALLES MEDICAMENTO ===" +
                "\n  Principio activo  : " + principioActivo +
                "\n  Dosis estandar    : " + dosis +
                "\n  Via administracion: " + viaAdministracion +
                "\n  Requiere receta   : " + (necesitaReceta ? "SI" : "NO") +
                "\n  Temp. almacen (grados C): " + temperaturaAlmacenamiento;

        return detalles;
    }

    /**
     * Calcula el valor total del inventario de medicamentos.
     * Los medicamentos con receta tienen un margen del 15% sobre el precio base.
     */
    @Override
    public double calcularValorTotal(int cantidad) {
        double valorBase;
        double valorFinal;
        valorBase = precio * cantidad;

        if (necesitaReceta) {
            valorFinal = valorBase * 1.15;
        } else {
            valorFinal = valorBase;
        }

        return valorFinal;
    }

    @Override
    public String generarAlertaStock(int cantidadActual, int umbralMinimo) {
        String alerta;
        String prefijo;

        if (cantidadActual <= 0) {
            prefijo = necesitaReceta ? "[CRITICO-RECETA]" : "[AGOTADO]";
            alerta = prefijo + " " + nombre + " - SIN STOCK. Contactar proveedor farmaceutico.";
        } else if (cantidadActual < umbralMinimo) {
            prefijo = necesitaReceta ? "[URGENTE]" : "[ALERTA MEDICAMENTO]";
            alerta = prefijo + " " + nombre +
                    " - Stock bajo: " + cantidadActual + " " + unidadMedida +
                    " (minimo: " + umbralMinimo + ")";
        } else {
            alerta = "[OK] " + nombre + " - Stock suficiente: " +
                    cantidadActual + " " + unidadMedida;
        }

        return alerta;
    }

    /**
     * Comprueba si el medicamento requiere refrigeración (temp <= 8 grados C).
     */
    public boolean necesitaRefrigeracion() {
        boolean resultado;

        resultado = temperaturaAlmacenamiento <= 8.0;

        return resultado;
    }

    public String getPrincipioActivo() {
        return principioActivo;
    }

    public String getDosis() {
        return dosis;
    }

    public String getViaAdministracion() {
        return viaAdministracion;
    }

    public boolean isNecesitaReceta() {
        return necesitaReceta;
    }

    public double getTemperaturaAlmacenamiento() {
        return temperaturaAlmacenamiento;
    }

    public void setPrincipioActivo(String principioActivo) {
        this.principioActivo = principioActivo;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public void setViaAdministracion(String viaAdministracion) {
        this.viaAdministracion = viaAdministracion;
    }

    public void setNecesitaReceta(boolean necesitaReceta) {
        this.necesitaReceta = necesitaReceta;
    }

    public void setTemperaturaAlmacenamiento(double temperaturaAlmacenamiento) {
        this.temperaturaAlmacenamiento = temperaturaAlmacenamiento;
    }

    @Override
    public String toString() {
        return "Medicamento{id=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", principioActivo='" + principioActivo + '\'' +
                ", dosis='" + dosis + '\'' +
                ", receta=" + necesitaReceta +
                ", precio=" + precio + '}';
    }
}
