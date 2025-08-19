package com.proyecto.modelo;

public class Ciudad {

    private Long id;
    private String nombreCiudad;

    public Ciudad() {
    }

    public Ciudad(Long id, String nombreCiudad) {
        this.id = id;
        this.nombreCiudad = nombreCiudad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }  

    @Override
    public String toString() {
        return nombreCiudad;
    }
        
}
