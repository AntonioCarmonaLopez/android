package com.dam.logica;

import com.dam.modelo.Alumno;

import java.util.ArrayList;
import java.util.List;

public class Datos {
/* Singleton **********************************************************************************/

    public static Datos datos;
    private List<Alumno> tAlumnos;
    /* Constructores ******************************************************************************/
    private Datos() {
        tAlumnos = new ArrayList<>();
    }
    /* Métodos ************************************************************************************/
    public static Datos getInstance() {
        if (datos == null)
            datos = new Datos();
        return datos;
    }
    public List<Alumno> gettAlumnos() {
        return tAlumnos;
    }
}