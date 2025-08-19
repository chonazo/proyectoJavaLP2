package com.proyecto.modelo;

public class DetalleVenta {

    private Producto producto;
    private Venta venta;
    private Deposito deposito;
    private int precioUnitario;
    private int cantidad;

    public DetalleVenta() {
    }

    public DetalleVenta(Producto producto, Venta venta, Deposito deposito, int precioUnitario, int cantidad) {
        this.producto = producto;
        this.venta = venta;
        this.deposito = deposito;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public int getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(int precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
