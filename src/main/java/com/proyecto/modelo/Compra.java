package com.proyecto.modelo;

import java.sql.Date;

public class Compra {

    private Long id;
    private Proveedor proveedor;
    private String numFactura;
    private Date fechaCompra;
    private String estado;
    private Usuario usuario;
    private Date fechaRegistro;

    public Compra() {
    }

    public Compra(Long id, Proveedor proveedor, String numFactura, Date fechaCompra, String estado, Usuario usuario, Date fechaRegistro) {
        this.id = id;
        this.proveedor = proveedor;
        this.numFactura = numFactura;
        this.fechaCompra = fechaCompra;
        this.estado = estado;
        this.usuario = usuario;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
   
}
