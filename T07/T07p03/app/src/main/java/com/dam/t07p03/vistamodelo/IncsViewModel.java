package com.dam.t07p03.vistamodelo;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dam.t07p03.modelo.Departamento;
import com.dam.t07p03.modelo.Filtro;
import com.dam.t07p03.modelo.Incidencia;

import java.util.List;

public class IncsViewModel extends AndroidViewModel {

    /* ViewModel Dptos ****************************************************************************/

    private LiveData<List<Incidencia>> mIncs;
    private Context mContext;
    private Departamento mLogin;
    private Incidencia mIncAEliminar;
    private Filtro filtro;
    private boolean filtroActivo;

    public IncsViewModel(@NonNull Application application) {
        super(application);
        mContext = application;
        mLogin = null;
        mIncAEliminar = null;
    }

    /* Métodos Mantenimiento Incidencias ********************************************************/

    public LiveData<List<Incidencia>> getIncs() {
        if (mIncs == null)
            mIncs = IncLogica.recuperarIncidencias(mContext, getLogin());
        return mIncs;
    }

    public List<Incidencia> getIncsNoLive() {
        return IncLogica.recuperarIncidenciasNoLive(mContext);
    }

    public boolean altaIncidencia(Incidencia inc) {
        return IncLogica.altaIncidencia(mContext, inc);
    }

    public boolean editarIncidencia(Incidencia inc) {
        return IncLogica.editarIncidencia(mContext, inc);
    }

    public boolean bajaIncidencia(Incidencia inc) {
        return IncLogica.bajaIncidencia(mContext, inc);
    }

    public Departamento recuperarDepartamento(Incidencia inc) {
        return IncLogica.recuperarDepartamento(mContext, inc);
    }

    public LiveData<List<Incidencia>> recuperarFiltro(Filtro filtro) {
        return IncLogica.recuperarIncidenciasFiltro(mContext, filtro);
    }
    /* Getters & Setters Objetos Persistentes *****************************************************/

    public Departamento getLogin() {
        return mLogin;
    }

    public void setLogin(Departamento login) {
        mLogin = login;
    }

    public Incidencia getIncAEliminar() {
        return mIncAEliminar;
    }

    public void setIncAEliminar(Incidencia incAEliminar) {
        mIncAEliminar = incAEliminar;
    }

    public Filtro getFiltro() {
        return filtro;
    }

    public void setFiltro(Filtro filtro) {
        this.filtro = filtro;
    }

    public boolean isFiltroActivo() {
        return filtroActivo;
    }

    public void setFiltroActivo(boolean filtroActivo) {
        this.filtroActivo = filtroActivo;
    }

}
