
package com.proyecto.modelo;

public class Usuario {
    private Long id;
    private String nombre;
    private String nick;
    private String clave;
    private String nivel;
    private String estado;

    public Usuario() {
    }

    public Usuario(Long id, String nombre, String nick, String clave, String nivel, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.nick = nick;
        this.clave = clave;
        this.nivel = nivel;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
}
