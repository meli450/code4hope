package Entidad;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una patrulla operativa dentro del subsistema.
 * Agrupa el vehiculo, la ruta, el equipo de comunicacion y los recursos
 * asignados para una mision concreta.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class Patrulla {

    /**
     * Estados posibles del ciclo de vida de una patrulla.
     */
    public enum Estado {
        /** La patrulla esta inactiva y sin asignacion. */
        INACTIVA("Inactiva"),
        /** La patrulla esta preparada y lista para partir. */
        PREPARADA("Preparada"),
        /** La patrulla se encuentra actualmente en mision. */
        EN_MISION("EnMision"),
        /** La mision ha sido completada con exito. */
        COMPLETADA("Completada"),
        /** La mision fue abortada antes de su finalizacion. */
        ABORTADA("Abortada");

        private final String valor;

        Estado(String valor) { this.valor = valor; }

        /**
         * Devuelve la representacion textual del estado.
         *
         * @return cadena con el valor del estado.
         */
        public String getValor() { return valor; }

        /**
         * Convierte una cadena en la constante de estado correspondiente.
         * Si no hay coincidencia devuelve {@code INACTIVA} por defecto.
         *
         * @param s cadena a convertir (sin distincion de mayusculas).
         * @return constante {@code Estado} que coincide con la cadena.
         */
        public static Estado fromString(String s) {
            Estado resultado = INACTIVA;
            for (Estado e : values()) {
                if (e.valor.equalsIgnoreCase(s)) {
                    resultado = e;
                }
            }
            return resultado;
        }
    }

    private int id;
    private String codigo;
    private Estado estado;
    private int vehiculoId;
    private int rutaId;
    private int equipoComunicacionId;
    private List<String> recursos;

    /**
     * Crea una nueva instancia de Patrulla con la lista de recursos vacia.
     */
    public Patrulla() {
        this.recursos = new ArrayList<>();
    }

    /** Devuelve el identificador unico de la patrulla. */
    public int getId() { return id; }
    /** Establece el identificador unico de la patrulla. @param id nuevo identificador. */
    public void setId(int id) { this.id = id; }

    /** Devuelve el codigo alfanumerico de la patrulla. */
    public String getCodigo() { return codigo; }
    /** Establece el codigo alfanumerico de la patrulla. @param codigo nuevo codigo. */
    public void setCodigo(String codigo) { this.codigo = codigo; }

    /** Devuelve el estado actual de la patrulla. */
    public Estado getEstado() { return estado; }
    /** Establece el estado de la patrulla. @param estado nuevo estado. */
    public void setEstado(Estado estado) { this.estado = estado; }

    /** Devuelve el identificador del vehiculo asignado (0 si no hay ninguno). */
    public int getVehiculoId() { return vehiculoId; }
    /** Establece el identificador del vehiculo asignado. @param vehiculoId ID del vehiculo. */
    public void setVehiculoId(int vehiculoId) { this.vehiculoId = vehiculoId; }

    /** Devuelve el identificador de la ruta asignada (0 si no hay ninguna). */
    public int getRutaId() { return rutaId; }
    /** Establece el identificador de la ruta asignada. @param rutaId ID de la ruta. */
    public void setRutaId(int rutaId) { this.rutaId = rutaId; }

    /** Devuelve el identificador del equipo de comunicacion asignado (0 si no hay ninguno). */
    public int getEquipoComunicacionId() { return equipoComunicacionId; }
    /** Establece el identificador del equipo de comunicacion asignado. @param equipoComunicacionId ID del equipo. */
    public void setEquipoComunicacionId(int equipoComunicacionId) { this.equipoComunicacionId = equipoComunicacionId; }

    /**
     * Devuelve una copia defensiva de la lista de recursos de la patrulla.
     *
     * @return lista de recursos asignados.
     */
    public List<String> getRecursos() { return new ArrayList<>(recursos); }
    /** Establece la lista de recursos de la patrulla. @param recursos nueva lista de recursos. */
    public void setRecursos(List<String> recursos) { this.recursos = recursos; }

    /**
     * Devuelve una representacion legible de la patrulla con sus datos principales.
     *
     * @return cadena con ID, codigo, estado, vehiculo, ruta, equipo y numero de recursos.
     */
    @Override
    public String toString() {
        String veh = (vehiculoId > 0) ? String.valueOf(vehiculoId) : "Sin vehiculo";
        String rut = (rutaId > 0) ? String.valueOf(rutaId) : "Sin ruta";
        String eqp = (equipoComunicacionId > 0) ? String.valueOf(equipoComunicacionId) : "Sin COM";
        return String.format("Patrulla [ID:%d | %s] Estado: %s | Vehiculo: %s | Ruta: %s | Equipo: %s | Recursos: %d",
                id, codigo, estado.getValor(), veh, rut, eqp, recursos.size());
    }
}
