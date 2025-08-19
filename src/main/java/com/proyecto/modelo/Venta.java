
package com.proyecto.modelo;

import java.sql.Date;

public class Venta {
    private Long id;
    private Cliente cliente;
    private Usuario usuario;
    private Date fecha;
    private Integer total;
    private Integer importe;
    private String estado;
    private String numFactura;
    private Integer descuento;

    public Venta() {
    }

    public Venta(Long id, Cliente cliente, Usuario usuario, Date fecha, Integer total, Integer importe, String estado, String numFactura, Integer descuento) {
        this.id = id;
        this.cliente = cliente;
        this.usuario = usuario;
        this.fecha = fecha;
        this.total = total;
        this.importe = importe;
        this.estado = estado;
        this.numFactura = numFactura;
        this.descuento = descuento;
    }
     
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getImporte() {
        return importe;
    }

    public void setImporte(Integer importe) {
        this.importe = importe;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public Integer getDescuento() {
        return descuento;
    }

    public void setDescuento(Integer descuento) {
        this.descuento = descuento;
    }
                  
}

