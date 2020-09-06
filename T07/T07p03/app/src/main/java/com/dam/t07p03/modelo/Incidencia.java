package com.dam.t07p03.modelo;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "Incidencias",
        primaryKeys = {"id"}
)
@ForeignKey(entity = Departamento.class, parentColumns = "id", childColumns = "idDpto", onDelete = CASCADE)

public class Incidencia implements Parcelable {

    /* Atributos **********************************************************************************/

    @NonNull
    private String id;//pk
    @NonNull
    private int idDpto;//pk
    @NonNull
    private String fecha;//pk
    private String dptoNombre;
    private String descipcion;
    private String tipo;
    private int estado;
    private String resolucion;



    public enum Tipo {RMI, RMA}

    ;
    /* Constructor ********************************************************************************/

    public Incidencia() {
        StringBuilder sb = new StringBuilder();
        Calendar c = GregorianCalendar.getInstance();
        sb.append("INC-").append(c.get(Calendar.HOUR)).append(c.get(Calendar.MINUTE)).append(c.get(Calendar.SECOND));
        idDpto = 0;
        dptoNombre = "";
        fecha = "";
        id = sb.toString();
        tipo = null;
        estado = 0;
        resolucion = "";
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

    public String getDptoNombre() {
        return dptoNombre;
    }

    public void setDptoNombre(String dptoNombre) {
        this.dptoNombre = dptoNombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescipcion() {
        return descipcion;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
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

    /* Métodos Parcelable *************************************************************************/
    protected Incidencia(Parcel in) {
        id = in.readString();
        idDpto = in.readInt();
        fecha = in.readString();
        dptoNombre = in.readString();
        descipcion = in.readString();
        tipo = in.readString();
        estado = in.readInt();
        resolucion = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(idDpto);
        dest.writeString(fecha);
        dest.writeString(dptoNombre);
        dest.writeString(descipcion);
        dest.writeString(tipo);
        dest.writeInt(estado);
        dest.writeString(resolucion);
    }

    @Override
    public int describeContents() {
        return 0;
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