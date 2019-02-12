package com.cifpvdg.cifpvirgendegracia.tpv.ClasesBD;

public class Producto {
    private int codigo;
    private String nombre;
    private int cantidad;
    private double precio_compra;
    private double precio_venta;
    private String cod_barras;
    private String descripcion_breve;
    private String descripcion_larga;
    private int cod_pro_proveedero;
    private int subactegoria;

    public Producto() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(double precio_compra) {
        this.precio_compra = precio_compra;
    }

    public double getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(double precio_venta) {
        this.precio_venta = precio_venta;
    }

    public String getCod_barras() {
        return cod_barras;
    }

    public void setCod_barras(String cod_barras) {
        this.cod_barras = cod_barras;
    }

    public String getDescripcion_breve() {
        return descripcion_breve;
    }

    public void setDescripcion_breve(String descripcion_breve) {
        this.descripcion_breve = descripcion_breve;
    }

    public String getDescripcion_larga() {
        return descripcion_larga;
    }

    public void setDescripcion_larga(String descripcion_larga) {
        this.descripcion_larga = descripcion_larga;
    }

    public int getCod_pro_proveedero() {
        return cod_pro_proveedero;
    }

    public void setCod_pro_proveedero(int cod_pro_proveedero) {
        this.cod_pro_proveedero = cod_pro_proveedero;
    }

    public int getSubactegoria() {
        return subactegoria;
    }

    public void setSubactegoria(int subactegoria) {
        this.subactegoria = subactegoria;
    }
}
