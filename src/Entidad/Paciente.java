package Entidad;

import java.time.LocalDate;
import java.time.Period;

/**
 * Representa a un paciente registrado en el sistema Code4Hope.
 * Corresponde a la tabla PACIENTE de la base de datos.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public class Paciente {

    private int idPaciente;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String alergias;
    private String historialMedico;

    public Paciente(int idPaciente, String nombre, String apellidos,
            LocalDate fechaNacimiento, String alergias, String historialMedico) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.alergias = alergias;
        this.historialMedico = historialMedico;
    }

    public Paciente(String nombre, String apellidos,
            LocalDate fechaNacimiento, String alergias, String historialMedico) {
        this(0, nombre, apellidos, fechaNacimiento, alergias, historialMedico);
    }

    /**
     * Calcula la edad actual del paciente en años completos.
     */
    public int calcularEdad() {
        int edad;

        if (fechaNacimiento == null) {
            edad = -1;
        } else {
            edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        }

        return edad;
    }

    /**
     * Comprueba si el paciente es menor de edad (menos de 18 años).
     */
    public boolean esMenorDeEdad() {
        boolean resultado;
        int edad;

        edad = calcularEdad();
        resultado = (edad >= 0) && (edad < 18);

        return resultado;
    }

    /**
     * Comprueba si el paciente tiene alergia a un principio activo concreto.
     */
    public boolean tieneAlergia(String principioActivo) {
        boolean resultado;

        if (alergias == null || alergias.isEmpty() || principioActivo == null) {
            resultado = false;
        } else {
            resultado = alergias.toLowerCase().contains(principioActivo.toLowerCase());
        }

        return resultado;
    }

    /**
     * Devuelve el nombre completo del paciente.
     */
    public String getNombreCompleto() {
        String nombreCompleto;

        if (apellidos == null || apellidos.isEmpty()) {
            nombreCompleto = nombre;
        } else {
            nombreCompleto = nombre + " " + apellidos;
        }

        return nombreCompleto;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getAlergias() {
        return alergias;
    }

    public String getHistorialMedico() {
        return historialMedico;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setFechaNacimiento(LocalDate fn) {
        this.fechaNacimiento = fn;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public void setHistorialMedico(String historialMedico) {
        this.historialMedico = historialMedico;
    }

    @Override
    public String toString() {
        return "Paciente{id=" + idPaciente +
                ", nombre='" + getNombreCompleto() + '\'' +
                ", edad=" + calcularEdad() +
                ", alergias='" + (alergias != null ? alergias : "Ninguna") + '\'' + '}';
    }
}
