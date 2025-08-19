
package com.proyecto.modelo;

public class Stock {
    private Deposito deposito;
    private Producto producto;
    private Integer cantidad;

    public Stock() {
    }

    public Stock(Deposito deposito, Producto producto, Integer cantidad) {
        this.deposito = deposito;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
          
    
}
