package com.dam.t08p01.vistamodelo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dam.t08p01.modelo.Departamento;
import com.dam.t08p01.modelo.Filtro;
import com.dam.t08p01.modelo.Incidencia;
import com.dam.t08p01.repositorio.IncsRepository;

import java.util.List;

public class IncsViewModel extends AndroidViewModel {

    /* ViewModel Incs ****************************************************************************/
    private static List<Incidencia> incs;
    private IncsRepository mIncsRep;
    private LiveData<List<Incidencia>> mIncs;

    private Departamento mLogin;
    private Incidencia mIncAEliminar;
    private Filtro mFiltro;
    public static boolean filtroBoolean;


    public IncsViewModel(@NonNull Application application) {
        super(application);
        mIncsRep = IncsRepository.getInstance(application);
        mLogin = null;
        mIncAEliminar = null;
        mFiltro = null;
    }

    public static void setDatos(List<Incidencia> tIncs) {
        incs=tIncs;
    }

    public static List<Incidencia> getIncs(){
        return incs;
    }
    /* Métodos Mantenimiento Incidencias ********************************************************/

    public LiveData<List<Incidencia>> getIncsME(Departamento departamento) {      // Multiple Events

        mIncs = mIncsRep.recuperarIncidenciasME(mLogin);
        return mIncs;
    }

    public LiveData<List<Incidencia>> getIncsSE(Filtro filtro) {      // Single Event
        //if (mIncs == null)
            mIncs = mIncsRep.recuperarIncidenciasSE(filtro);
        return mIncs;
    }

    public LiveData<List<Incidencia>> getIncsToMarker() {             // Single event
        mIncs = mIncsRep.recuperarIncsToMarkerSE();
        return mIncs;
    }

    public void eliminarEventosGetIncsME() {
        mIncsRep.eliminarEventosGetIncsME();
    }

    public boolean altaIncidencia(Incidencia inc) {
        return mIncsRep.altaIncidencia(inc);
    }

    public boolean editarIncidencia(Incidencia inc) {
        return mIncsRep.editarIncidencia(inc);
    }

    public boolean bajaIncidencia(Incidencia inc) {
        return mIncsRep.bajaIncidencia(inc);
    }

    public boolean bajaIncidenciasDepartamento(Departamento dptoAEliminar) {
        return mIncsRep.bajaIncidenciasDepartamento(dptoAEliminar);
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

    public Filtro getmFiltro() {
        return mFiltro;
    }

    public void setmFiltro(Filtro mFiltro) {
        this.mFiltro = mFiltro;
    }

}

