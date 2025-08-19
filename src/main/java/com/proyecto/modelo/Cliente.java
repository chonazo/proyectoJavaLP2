package com.proyecto.modelo;

public class Cliente {

    private Long id;
    private Ciudad ciudad;
    private String rucCI;
    private String nombreCli;
    private String apellidoCli;
    private String direccionCli;
    private String telefonoCli;

    public Cliente() {
    }

    public Cliente(Long id, Ciudad ciudad, String rucCI, String nombreCli, String apellidoCli, String direccionCli, String telefonoCli) {
        this.id = id;
        this.ciudad = ciudad;
        this.rucCI = rucCI;
        this.nombreCli = nombreCli;
        this.apellidoCli = apellidoCli;
        this.direccionCli = direccionCli;
        this.telefonoCli = telefonoCli;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public String getRucCI() {
        return rucCI;
    }

    public void setRucCI(String rucCI) {
        this.rucCI = rucCI;
    }

    public String getNombreCli() {
        return nombreCli;
    }

    public void setNombreCli(String nombreCli) {
        this.nombreCli = nombreCli;
    }

    public String getApellidoCli() {
        return apellidoCli;
    }

    public void setApellidoCli(String apellidoCli) {
        this.apellidoCli = apellidoCli;
    }

    public String getDireccionCli() {
        return direccionCli;
    }

    public void setDireccionCli(String direccionCli) {
        this.direccionCli = direccionCli;
    }

    public String getTelefonoCli() {
        return telefonoCli;
    }

    public void setTelefonoCli(String telefonoCli) {
        this.telefonoCli = telefonoCli;
    }

}
