
package com.proyecto.modelo;

public class Deposito {
    private Long id;
    private String descriDeposito;

    public Deposito() {
    }

    public Deposito(Long id, String descriDeposito) {
        this.id = id;
        this.descriDeposito = descriDeposito;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescriDeposito() {
        return descriDeposito;
    }

    public void setDescriDeposito(String descriDeposito) {
        this.descriDeposito = descriDeposito;
    }

    @Override
    public String toString() {
        return descriDeposito;
    }
    
    
}
