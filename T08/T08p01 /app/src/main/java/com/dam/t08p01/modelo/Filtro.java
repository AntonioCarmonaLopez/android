package com.dam.t08p01.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Filtro implements Parcelable {
    private int departamento,estado;
    private String fecha;

    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    String hoy=String.format("%02d/%02d/%4d", day, month + 1, year);

    public Filtro() {
        departamento = 0;
        estado = 0;
        fecha = hoy;
    }

    public int getDepartamento() {
        return departamento;
    }

    public void setDepartamento(int departamento) {
        this.departamento = departamento;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
//metodos parcel
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(departamento);
        dest.writeInt(estado);
        dest.writeString(fecha);
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeString(hoy);
    }

    protected Filtro(Parcel in) {
        departamento = in.readInt();
        estado = in.readInt();
        fecha = in.readString();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        hoy = in.readString();
    }

    public static final Creator<Filtro> CREATOR = new Creator<Filtro>() {
        @Override
        public Filtro createFromParcel(Parcel in) {
            return new Filtro(in);
        }

        @Override
        public Filtro[] newArray(int size) {
            return new Filtro[size];
        }
    };
}

