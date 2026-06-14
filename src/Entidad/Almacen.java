package Entidad;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Clase base que representa un almacen del sistema Code4Hope.
 * Gestiona una coleccion de productos y puede tener asociada una
 * camara de refrigeracion.
 *
 * @author Code4Hope Team
 * @version 1.0
 */
public abstract class Almacen {

    protected int idAlmacen;
    protected String codigo;
    protected String ubicacion;
    protected int stockMinimo;
    protected int stockMaximo;
    protected List<Producto> productos;
    protected CamaraRefrigeracion camara;

    public Almacen(String ubicacion, int stockMinimo, int stockMaximo) {
        this.codigo = UUID.randomUUID().toString();
        this.ubicacion = ubicacion;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
        this.productos = new ArrayList<>();
        this.camara = null;
    }

    /**
     * Comprueba si un producto es compatible con este tipo de almacen.
     *
     * @param p Producto a verificar
     * @return true si el producto puede almacenarse en este almacen
     */
    public abstract boolean esCompatible(Producto p);

    /**
     * Devuelve el tipo de almacen como cadena descriptiva.
     *
     * @return String con el tipo de almacen
     */
    public abstract String getTipo();

    /**
     * Añade un producto al almacen.
     * Solo añade el producto si es compatible y no existe ya.
     *
     * @param p Producto a añadir
     * @return true si se añadio; false si no es compatible o ya existe
     */
    public boolean añadirProducto(Producto p) {
        boolean resultado;

        if (p == null || !esCompatible(p)) {
            resultado = false;
        } else if (contiene(p.getIdProducto())) {
            resultado = false;
        } else {
            productos.add(p);
            resultado = true;
        }

        return resultado;
    }

    /**
     * Elimina un producto del almacen por su ID.
     *
     * @param idProducto Identificador del producto a eliminar
     * @return true si se elimino; false si no se encontro
     */
    public boolean eliminarProducto(int idProducto) {
        boolean resultado = false;

        for (int i = 0; i < productos.size() && !resultado; i++) {
            if (productos.get(i).getIdProducto() == idProducto) {
                productos.remove(i);
                resultado = true;
            }
        }

        return resultado;
    }

    /**
     * Busca un producto en el almacen por nombre (busqueda parcial, insensible
     * a mayusculas).
     *
     * @param nombre Nombre o parte del nombre a buscar
     * @return Primer producto encontrado, o null si no existe
     */
    public Producto buscarProducto(String nombre) {
        Producto encontrado = null;

        for (int i = 0; i < productos.size() && encontrado == null; i++) {
            if (productos.get(i).getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                encontrado = productos.get(i);
            }
        }

        return encontrado;
    }

    /**
     * Recupera todos los productos cuya fecha de caducidad es anterior
     * o igual a la fecha indicada.
     *
     * @param fecha Fecha limite para la busqueda
     * @return Lista de productos con fecha de caducidad <= fecha indicada
     */
    public List<Producto> obtenerFechasCaducidad(LocalDate fecha) {
        List<Producto> caducados = new ArrayList<>();

        for (Producto p : productos) {
            if (p.getFechaCaducidad() != null && !p.getFechaCaducidad().isAfter(fecha)) {
                caducados.add(p);
            }
        }

        return caducados;
    }

    /**
     * Comprueba si el almacen contiene un producto con el ID indicado.
     *
     * @param idProducto Identificador del producto a buscar
     * @return true si el producto esta en el almacen
     */
    public boolean contiene(int idProducto) {
        boolean resultado = false;

        for (int i = 0; i < productos.size() && !resultado; i++) {
            if (productos.get(i).getIdProducto() == idProducto) {
                resultado = true;
            }
        }

        return resultado;
    }

    public int getStockTotal() {
        int total;

        total = productos.size();

        return total;
    }

    public boolean necesitaReposicion() {
        boolean resultado;

        resultado = productos.size() < stockMinimo;

        return resultado;
    }

    public void almacenarCamara(CamaraRefrigeracion camara) {
        this.camara = camara;
    }

    public boolean tieneCamaraActiva() {
        boolean resultado;

        resultado = camara != null && camara.estaActivo();

        return resultado;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getCodigo() {
        return codigo;
    }

    /**
     * Permite sobreescribir el codigo generado por UUID con el valor
     * leido desde la base de datos al reconstruir el objeto.
     *
     * @param codigo Codigo UUID leido de la BD
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public int getStockMaximo() {
        return stockMaximo;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public CamaraRefrigeracion getCamara() {
        return camara;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    @Override
    public String toString() {
        return "Almacen{tipo='" + getTipo() + '\'' +
                ", codigo='" + codigo.substring(0, 8) + "...'" +
                ", ubicacion='" + ubicacion + '\'' +
                ", productos=" + productos.size() +
                ", camara=" + (camara != null ? "SI" : "NO") + '}';
    }
}
