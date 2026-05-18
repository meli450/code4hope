package Entidad;

/**
 * Subclase de Almacen especializada en el almacenamiento de alimentos.
 * Implementa el metodo polimorfico esCompatible() para aceptar unicamente
 * productos de tipo Alimento.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class AlmacenAlimentos extends Almacen {

    public AlmacenAlimentos(String ubicacion, int stockMinimo, int stockMaximo) {
        super(ubicacion, stockMinimo, stockMaximo);
    }

    @Override
    public boolean esCompatible(Producto p) {
        boolean resultado;

        resultado = (p instanceof Alimento);

        return resultado;
    }

    @Override
    public String getTipo() {
        return "ALIMENTOS";
    }

    /**
     * Verifica si este almacen puede almacenar un alimento concreto teniendo en
     * cuenta si requiere refrigeracion y si la camara esta disponible y activa.
     *
     * @param alimento Alimento a almacenar
     * @return true si el almacen puede albergar correctamente el alimento
     */
    public boolean puedeAlmacenar(Alimento alimento) {
        boolean resultado;

        if (!esCompatible(alimento)) {
            resultado = false;
        } else if (alimento.isNecesitaRefrigeracion() && !tieneCamaraActiva()) {
            resultado = false;
        } else {
            resultado = true;
        }

        return resultado;
    }

    /**
     * Cuenta cuantos alimentos almacenados requieren refrigeracion.
     *
     * @return Numero de alimentos refrigerados en este almacen
     */
    public int contarAlimentosRefrigerados() {
        int contador = 0;

        for (Producto p : getProductos()) {
            if (p instanceof Alimento && ((Alimento) p).isNecesitaRefrigeracion()) {
                contador++;
            }
        }

        return contador;
    }

    @Override
    public String toString() {
        return "AlmacenAlimentos{ubicacion='" + getUbicacion() + '\'' +
                ", productos=" + getProductos().size() +
                ", refrigerados=" + contarAlimentosRefrigerados() +
                ", camara=" + (tieneCamaraActiva() ? "ACTIVA" : "NO") + '}';
    }
}
