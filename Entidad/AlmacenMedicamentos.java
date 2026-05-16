package Entidad;

import java.util.ArrayList;
import java.util.List;

/**
 * Subclase de Almacen especializada en el almacenamiento de medicamentos.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class AlmacenMedicamentos extends Almacen {

    public AlmacenMedicamentos(String ubicacion, int stockMinimo, int stockMaximo) {
        super(ubicacion, stockMinimo, stockMaximo);
    }

    @Override
    public boolean esCompatible(Producto p) {
        boolean resultado;

        resultado = (p instanceof Medicamento);

        return resultado;
    }

    @Override
    public String getTipo() {
        return "MEDICAMENTOS";
    }

    /**
     * Verifica si este almacen puede almacenar un medicamento concreto,
     * teniendo en cuenta si requiere refrigeracion y si la camara esta disponible.
     *
     * @param medicamento Medicamento a almacenar
     * @return true si el almacen puede albergar correctamente el medicamento
     */
    public boolean puedeAlmacenar(Medicamento medicamento) {
        boolean resultado;

        if (!esCompatible(medicamento)) {
            resultado = false;
        } else if (medicamento.necesitaRefrigeracion() && !tieneCamaraActiva()) {
            resultado = false;
        } else {
            resultado = true;
        }

        return resultado;
    }

    /**
     * Recupera la lista de medicamentos almacenados que requieren refrigeracion.
     */
    public List<Medicamento> getMedicamentosRefrigerados() {
        List<Medicamento> refrigerados = new ArrayList<>();

        for (Producto p : getProductos()) {
            if (p instanceof Medicamento) {
                Medicamento m = (Medicamento) p;
                if (m.necesitaRefrigeracion()) {
                    refrigerados.add(m);
                }
            }
        }

        return refrigerados;
    }

    /**
     * Recupera la lista de medicamentos almacenados que requieren receta medica.
     */
    public List<Medicamento> getMedicamentosConReceta() {
        List<Medicamento> conReceta = new ArrayList<>();

        for (Producto p : getProductos()) {
            if (p instanceof Medicamento && ((Medicamento) p).isNecesitaReceta()) {
                conReceta.add((Medicamento) p);
            }
        }

        return conReceta;
    }

    /**
     * Verifica si la temperatura de la camara es adecuada para todos los
     * medicamentos refrigerados almacenados.
     *
     * @return true si todos los medicamentos refrigerados tienen temperatura OK
     */
    public boolean verificarCadenaDeFrio() {
        boolean todasOK = true;

        if (!tieneCamaraActiva()) {
            todasOK = getMedicamentosRefrigerados().isEmpty();
        } else {
            double tempActual;
            List<Medicamento> refrigerados;
            double tolerancia;
            tempActual = getCamara().getTemperaturaActual();
            refrigerados = getMedicamentosRefrigerados();
            tolerancia = 2.0;
            for (int i = 0; i < refrigerados.size() && todasOK; i++) {
                if (Math.abs(tempActual - refrigerados.get(i).getTemperaturaAlmacenamiento()) > tolerancia) {
                    todasOK = false;
                }
            }
        }

        return todasOK;
    }

    @Override
    public String toString() {
        return "AlmacenMedicamentos{ubicacion='" + getUbicacion() + '\'' +
                ", medicamentos=" + getProductos().size() +
                ", refrigerados=" + getMedicamentosRefrigerados().size() +
                ", conReceta=" + getMedicamentosConReceta().size() +
                ", camara=" + (tieneCamaraActiva() ? "ACTIVA" : "NO") +
                ", cadenaDeFrioOK=" + verificarCadenaDeFrio() + '}';
    }
}
