
package com.proyecto.modelo;

public class VentaPago {
    private Long id;
    private Venta venta;
    private String formaPago;
    private Integer monto;
    private String descripcion;
    private String numeroTarjeta;

    public VentaPago() {
    }

    public VentaPago(Long id, Venta venta, String formaPago, Integer monto, String descripcion, String numeroTarjeta) {
        this.id = id;
        this.venta = venta;
        this.formaPago = formaPago;
        this.monto = monto;
        this.descripcion = descripcion;
        this.numeroTarjeta = numeroTarjeta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }
            
}
