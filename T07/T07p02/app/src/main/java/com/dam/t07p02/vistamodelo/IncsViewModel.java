package com.dam.t07p02.vistamodelo;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dam.t07p02.modelo.Departamento;
import com.dam.t07p02.modelo.Filtro;
import com.dam.t07p02.modelo.Incidencia;
import com.dam.t07p02.vista.fragmentos.MtoIncsFragment;

import java.util.List;

public class IncsViewModel extends AndroidViewModel {

    /* ViewModel incs ****************************************************************************/

    private MutableLiveData<List<Incidencia>> mIncs;
    private Context mContext;
    private Departamento mLogin;
    private Incidencia mIncAEliminar;
    private Filtro filtro;
    private boolean filtroActivo;

    public IncsViewModel(@NonNull Application application) {
        super(application);
        mIncs = new MutableLiveData<>();
        mContext = application;
        mLogin = null;
        mIncAEliminar = null;
    }

    /* Métodos Mantenimiento Incidencias ********************************************************/

    public LiveData<List<Incidencia>> getIncs(Departamento departamento) {
        mIncs.setValue(IncLogica.recuperarIncidencias(mContext,departamento));
        return mIncs;
    }

    public LiveData<List<Incidencia>> getIncsFilto(Filtro filtro, Departamento mLogin) {
        mIncs.setValue(IncLogica.recuperarIncidenciasFiltro(mContext,filtro));
        return mIncs;
    }

    public List<Incidencia> getIncsNoLive() {
        return IncLogica.recuperarIncidencias(mContext,mLogin);
    }

    public boolean altaIncidencia(Incidencia incidencia) {
        if (IncLogica.altaIncidencia(mContext, incidencia)) {
            mIncs.setValue(IncLogica.recuperarIncidencias(mContext,mLogin));
            return true;
        } else {
            return false;
        }
    }

    public boolean editarIncidencia(Incidencia incidencia) {
        if (IncLogica.editarIncidencia(mContext, incidencia)) {
            mIncs.setValue(IncLogica.recuperarIncidencias(mContext,mLogin));
            return true;
        } else {
            return false;
        }
    }

    public boolean bajaIncidencia(Incidencia incidencia) {
        if (IncLogica.bajaIncidencia(mContext, incidencia)) {
            mIncs.setValue(IncLogica.recuperarIncidencias(mContext,mLogin
            ));
            return true;
        } else {
            return false;
        }
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

