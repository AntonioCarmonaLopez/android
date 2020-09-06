package com.dam.t08p01.modelo;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.dam.t08p01.R;

import java.util.Calendar;


public class Incidencia implements Parcelable {

    /* Atributos **********************************************************************************/

    @NonNull
    private String id;//pk
    @NonNull
    private int idDpto;//pk
    @NonNull
    private String fecha;//pk
    @NonNull
    private String descripcion;
    private String deptoNombre;
    private String tipo;
    private int estado;
    private String resolucion;
    private String longitud;
    private String latitud;
    private String foto;


    public enum Tipo {RMI, RMA}

    ;
    /* Constructor ********************************************************************************/

    public Incidencia() {
        idDpto = 0;
        fecha = "";
        id = "";
        tipo = "";
        descripcion = "";
        deptoNombre = "";
        estado = 0;
        resolucion = "";
        longitud = "";
        latitud = "";
        foto = "";
    }

    /* Métodos getters & setters ******************************************************************/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdDpto() {
        return idDpto;
    }

    public void setIdDpto(int idDpto) {
        this.idDpto = idDpto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descipcion) {
        this.descripcion = descipcion;
    }

    public String getDeptoNombre() {
        return deptoNombre;
    }

    public void setDeptoNombre(String deptoNombre) {
        this.deptoNombre = deptoNombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getIcono(Incidencia inc) {
        int recurso;
        switch (inc.getEstado()) {
            case 0:
                recurso = R.mipmap.ic_icon_ko_round;
                break;
            case 1:
                recurso = R.mipmap.ic_icon_ok_round;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + inc.getEstado());
        }
        return recurso;
    }


    /* Métodos Parcelable *************************************************************************/
    protected Incidencia(Parcel in) {
        id = in.readString();
        idDpto = in.readInt();
        fecha = in.readString();
        descripcion = in.readString();
        deptoNombre = in.readString();
        tipo = in.readString();
        estado = in.readInt();
        resolucion = in.readString();
        longitud = in.readString();
        latitud = in.readString();
        foto = in.readString();
    }

    public static final Creator<Incidencia> CREATOR = new Creator<Incidencia>() {
        @Override
        public Incidencia createFromParcel(Parcel in) {
            return new Incidencia(in);
        }

        @Override
        public Incidencia[] newArray(int size) {
            return new Incidencia[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(idDpto);
        dest.writeString(fecha);
        dest.writeString(descripcion);
        dest.writeString(deptoNombre);
        dest.writeString(tipo);
        dest.writeInt(estado);
        dest.writeString(resolucion);
        dest.writeString(longitud);
        dest.writeString(latitud);
        dest.writeString(foto);
    }

    @Override
    public String toString() {
        return String.format("\tID: %s\n DPTO: %s Fecha: %s Tipo: %s Descripción %s",id,deptoNombre,fecha,tipo,descripcion);
    }
}