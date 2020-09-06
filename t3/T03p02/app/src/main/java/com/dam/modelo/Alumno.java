package com.dam.modelo;

public class Alumno {
    /* Atributos **********************************************************************************/
    private String dni;
    // PK not null
    private String nombre;
    // not null
    private String fecNac;
    // null
    private String ciclo;
    // null
    /* Constructores ******************************************************************************/
    public Alumno() {
        dni = "";
        nombre = "";
        fecNac = "";
        ciclo = "";
    }
    /* Getters & Setters **************************************************************************/
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getFecNac() {
        return fecNac;
    }
    public void setFecNac(String fecNac) {
        this.fecNac = fecNac;
    }

    public String getCiclo() {
        return ciclo;
    }
    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }
}