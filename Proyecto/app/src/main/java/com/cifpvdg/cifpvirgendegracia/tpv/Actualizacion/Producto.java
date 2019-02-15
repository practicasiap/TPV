package com.cifpvdg.cifpvirgendegracia.tpv.Actualizacion;

import java.io.Serializable;

public class Producto implements Serializable {

    private int codigo;
    private String nombre;
    private int cantidad;
    private float precioCompra;
    private float precioVenta;
    private int codBarras;
    private String descBreve;
    private String descLarga;
    private int codProdProvee;
    private int subCategoria;
    private byte[] foto;

    public Producto(int c, String n, int ca, float pC, float pV, int cB, String dB, String dL, int cPp, int sC, byte[] foto){

        this.codigo = c;
        this.nombre = n;
        this.cantidad = ca;
        this.precioCompra = pC;
        this.precioVenta = pV;
        this.codBarras = cB;
        this.descBreve = dB;
        this.descLarga = dL;
        this.codProdProvee = cPp;
        this.subCategoria = sC;
        this.foto = foto;
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

    public float getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(float precioCompra) {
        this.precioCompra = precioCompra;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(int codBarras) {
        this.codBarras = codBarras;
    }

    public String getDescBreve() {
        return descBreve;
    }

    public void setDescBreve(String descBreve) {
        this.descBreve = descBreve;
    }

    public String getDescLarga() {
        return descLarga;
    }

    public void setDescLarga(String descLarga) {
        this.descLarga = descLarga;
    }

    public int getCodProdProvee() {
        return codProdProvee;
    }

    public void setCodProdProvee(int codProdProvee) {
        this.codProdProvee = codProdProvee;
    }

    public int getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(int subCategoria) {
        this.subCategoria = subCategoria;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }


}
