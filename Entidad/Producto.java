package Entidad;

import java.time.LocalDate;

/**
 * Clase base del sistema Code4Hope para todos los tipos de producto.
 * Define la estructura común y los métodos abstractos que deben implementar
 * las subclases Alimento y Medicamento.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public abstract class Producto {

    protected int idProducto;
    protected String nombre;
    protected String descripcion;
    protected String unidadMedida;
    protected double precio;
    protected String categoria;
    protected String proveedor;
    protected LocalDate fechaCaducidad;
    protected int stockMinimo;
    protected boolean requiereRefrigeracion;

    public Producto(int idProducto, String nombre, String descripcion,
            String unidadMedida, double precio, String categoria,
            String proveedor, LocalDate fechaCaducidad,
            int stockMinimo, boolean requiereRefrigeracion) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.precio = precio;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.fechaCaducidad = fechaCaducidad;
        this.stockMinimo = stockMinimo;
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    public Producto(int idProducto, String nombre, String descripcion,
            String unidadMedida, double precio, String categoria) {
        this(idProducto, nombre, descripcion, unidadMedida, precio, categoria,
                null, null, 0, false);
    }

    public abstract String getDetallesEspecificos();

    public abstract double calcularValorTotal(int cantidad);

    public abstract String generarAlertaStock(int cantidadActual, int umbralMinimo);

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getProveedor() {
        return proveedor;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public boolean isRequiereRefrigeracion() {
        return requiereRefrigeracion;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public void setFechaCaducidad(LocalDate f) {
        this.fechaCaducidad = f;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void setRequiereRefrigeracion(boolean rR) {
        this.requiereRefrigeracion = rR;
    }

    @Override
    public String toString() {
        return "Producto{id=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", unidad='" + unidadMedida + '\'' +
                ", precio=" + precio +
                ", categoria='" + categoria + '\'' + '}';
    }
}
