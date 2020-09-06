package com.dam.t07p02.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Incidencia implements Parcelable {

    /* Atributos **********************************************************************************/

    private int idDpto;                 // PK
    private transient String dptoNombre;
    private String id;                  // PK
    private String fecha;               // PK
    private String descripcion;         // OB
    private String tipo;                  // OB
    private boolean estado;             // OB
    private String resolucion;         // OB

    public enum TIPO {RMI,RMA};

    /* Constructor ********************************************************************************/

    public Incidencia() {
        StringBuilder sb=new StringBuilder();
        Calendar c = GregorianCalendar.getInstance();
        sb.append("INC-").append(c.get(Calendar.HOUR)).append(c.get(Calendar.MINUTE)).append(c.get(Calendar.SECOND));
        idDpto=0;
        id = sb.toString();
        dptoNombre = "";
        fecha = "";
        descripcion = "";
        tipo = null;
        estado = false;
        resolucion = "";
    }
    /* Métodos Getters&Setters ********************************************************************/

    public int getIdDpto() {
        return idDpto;
    }

    public void setIdDpto(int idDpto) {
        this.idDpto = idDpto;
    }

    public String getDptoNombre() {
        return dptoNombre;
    }

    public void setDptoNombre(String dptoNombre) {
        this.dptoNombre = dptoNombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }


    /* Métodos Parcelable *************************************************************************/

    @Override
    public int describeContents() {
        return 0;
    }

    protected Incidencia(Parcel in) {
        idDpto = in.readInt();
        id = in.readString();
        fecha = in.readString();
        descripcion = in.readString();
        tipo = in.readString();
        estado = in.readByte() != 0;
        resolucion = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idDpto);
        dest.writeString(id);
        dest.writeString(fecha);
        dest.writeString(descripcion);
        dest.writeString(tipo == null ? null : tipo);
        dest.writeByte((byte) (estado ? 1 : 0));
        dest.writeString(resolucion);
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
}
