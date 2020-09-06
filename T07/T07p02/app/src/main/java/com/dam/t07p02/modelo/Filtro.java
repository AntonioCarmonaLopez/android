package com.dam.t07p02.modelo;

public class Filtro {
    private String fecha;
    private int idDepto;
    private int estado;

    public Filtro() {
        fecha = "";
        idDepto = 0;
        estado = 0;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdDepto() {
        return idDepto;
    }

    public void setIdDepto(int idDepto) {
        this.idDepto = idDepto;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
