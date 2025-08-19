package com.proyecto.modelo;

public class DetalleCompra {

    private Producto producto;
    private Compra compra;
    private Deposito deposito;
    private Integer precio;
    private Integer cantidad;
    private int subtotal;

    public DetalleCompra() {
    }

    public DetalleCompra(Producto producto, Compra compra, Deposito deposito, Integer precio, Integer cantidad) {
        this.producto = producto;
        this.compra = compra;
        this.deposito = deposito;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

     // calculamos Subtotal directamente
    public int getSubtotal() {
        if (cantidad != null && precio != null) {
            return cantidad * precio;
        } else {
            return 0;
        }
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

}
