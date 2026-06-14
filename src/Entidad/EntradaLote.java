package Entidad;

/**
 * Representa una entrada individual de producto dentro de un lote.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class EntradaLote {

    private Producto producto;
    private int cantidad;

    public EntradaLote(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    /**
     * Añade mas unidades a esta entrada del lote.
     * Solo se añaden si la cantidad a añadir es positiva.
     *
     * @param cantidadAdicional Unidades a incrementar
     * @return true si se añadieron correctamente; false si la cantidad no es valida
     */
    public boolean añadirUnidades(int cantidadAdicional) {
        boolean resultado;

        if (cantidadAdicional > 0) {
            this.cantidad += cantidadAdicional;
            resultado = true;
        } else {
            resultado = false;
        }

        return resultado;
    }

    /**
     * Reduce las unidades de la entrada del lote (consumo o salida).
     * No permite que la cantidad quede negativa.
     *
     * @param cantidadSalida Unidades a reducir
     * @return true si se redujo correctamente; false si no hay suficiente stock
     */
    public boolean reducirUnidades(int cantidadSalida) {
        boolean resultado;

        if (cantidadSalida > 0 && cantidadSalida <= this.cantidad) {
            this.cantidad -= cantidadSalida;
            resultado = true;
        } else {
            resultado = false;
        }

        return resultado;
    }

    /**
     * Comprueba si el producto de esta entrada requiere refrigeracion.
     */
    public boolean productoRequiereRefrigeracion() {
        boolean resultado;

        if (producto instanceof Alimento) {
            resultado = ((Alimento) producto).isNecesitaRefrigeracion();
        } else if (producto instanceof Medicamento) {
            resultado = ((Medicamento) producto).necesitaRefrigeracion();
        } else {
            resultado = false;
        }

        return resultado;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad >= 0) {
            this.cantidad = cantidad;
        }
    }

    @Override
    public String toString() {
        return "EntradaLote{producto='" + (producto != null ? producto.getNombre() : "null") +
                "', cantidad=" + cantidad + '}';
    }
}
