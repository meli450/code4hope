package Entidad;

/**
 * Contrato para las clases que necesiten validar si requieren cadena de frio
 * para su correcto almacenamiento o transporte.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public interface IRefrigeracion {

    /**
     * Indica si el objeto requiere condiciones de refrigeracion.
     *
     * @return true si se necesita cadena de frio; false en caso contrario
     */
    boolean requiereRefrigeracion();
}
