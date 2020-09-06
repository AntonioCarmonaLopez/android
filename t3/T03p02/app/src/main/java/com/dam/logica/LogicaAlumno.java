package com.dam.logica;

import com.dam.modelo.Alumno;

public class LogicaAlumno {
    /* Métodos ************************************************************************************/
    public static boolean existeAlumno(Alumno a) {
        for (Alumno alumno : Datos.getInstance().gettAlumnos()) {
            if (alumno.getDni().equals(a.getDni())) {
                return true;
            }
        }
        return false;
    }
    public static boolean altaAlumno(Alumno a) {
        if (existeAlumno(a)) {
            return false;
        }
        return Datos.getInstance().gettAlumnos().add(a);
    }
    public static boolean bajaAlumno(Alumno a) {
        if (!existeAlumno(a)) {
            return false;
        }
        for (Alumno alumno: Datos.getInstance().gettAlumnos()) {
            if (alumno.getDni().equals(a.getDni())) {
                return Datos.getInstance().gettAlumnos().remove(alumno);
            }
        }
        return false;
    }
}
