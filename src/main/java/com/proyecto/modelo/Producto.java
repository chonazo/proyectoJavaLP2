
package com.proyecto.modelo;

public class Producto {
    private Long id;
    private TipoProducto tipoProducto;
    private UMedida uMedida;
    private String nombreProducto;
    private Integer precio;
    private String codBarra;

    public Producto() {
    }

    public Producto(Long id, TipoProducto tipoProducto, UMedida uMedida, String nombreProducto, Integer precio, String codBarra) {
        this.id = id;
        this.tipoProducto = tipoProducto;
        this.uMedida = uMedida;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.codBarra = codBarra;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public UMedida getuMedida() {
        return uMedida;
    }

    public void setuMedida(UMedida uMedida) {
        this.uMedida = uMedida;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

   
    
    @Override
    public String toString() {
        return nombreProducto;
    }   
    
}
