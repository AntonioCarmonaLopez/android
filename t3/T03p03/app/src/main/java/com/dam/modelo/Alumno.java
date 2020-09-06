package com.dam.modelo;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Alumno implements Parcelable {
    /* Atributos **********************************************************************************/
    private String dni;
    // PK not null
    private String nombre;
    // not null
    private String fecNac;
    // null
    private String ciclo;

    private Bitmap imagen;

    // null
    /* Constructores ******************************************************************************/
    public Alumno() {
        dni = "";
        nombre = "";
        fecNac = "";
        ciclo = "";
        imagen = null;
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

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    //parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dni);
        dest.writeString(nombre);
        dest.writeString(fecNac);
        dest.writeString(ciclo);
    }

    protected Alumno(Parcel in) {
        dni = in.readString();
        nombre = in.readString();
        fecNac = in.readString();
        ciclo = in.readString();
    }

    public static final Creator<Alumno> CREATOR = new Creator<Alumno>() {
        @Override
        public Alumno createFromParcel(Parcel in) {
            return new Alumno(in);
        }

        @Override
        public Alumno[] newArray(int size) {
            return new Alumno[size];
        }
    };
}