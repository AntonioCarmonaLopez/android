package com.dam.t07p02.vistamodelo;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dam.t07p02.modelo.Departamento;

import java.util.List;

public class DptosViewModel extends AndroidViewModel {

    /* ViewModel Dptos ****************************************************************************/

    private MutableLiveData<List<Departamento>> mDptos;
    private Context mContext;
    private Departamento mLogin;
    private Departamento mDptoAEliminar;

    public DptosViewModel(@NonNull Application application) {
        super(application);
        mDptos = new MutableLiveData<>();
        mContext = application;
        mLogin = null;
        mDptoAEliminar = null;
    }

    /* Métodos Mantenimiento Departamentos ********************************************************/

    public LiveData<List<Departamento>> getDptos() {
        mDptos.setValue(DptoLogica.recuperarDepartamentos(mContext));
        return mDptos;
    }

    public List<Departamento> getDptosNoLive() {
        return DptoLogica.recuperarDepartamentos(mContext);
    }

    public boolean altaDepartamento(Departamento dpto) {
        if (DptoLogica.altaDepartamento(mContext, dpto)) {
            mDptos.setValue(DptoLogica.recuperarDepartamentos(mContext));
            return true;
        } else {
            return false;
        }
    }

    public boolean editarDepartamento(Departamento dpto) {
        if (DptoLogica.editarDepartamento(mContext, dpto)) {
            mDptos.setValue(DptoLogica.recuperarDepartamentos(mContext));
            return true;
        } else {
            return false;
        }
    }

    public boolean bajaDepartamento(Departamento dpto) {
        if (DptoLogica.bajaDepartamento(mContext, dpto)) {
            DptoLogica.onCascade(mContext,dpto);
            mDptos.setValue(DptoLogica.recuperarDepartamentos(mContext));
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

    public Departamento getDptoAEliminar() {
        return mDptoAEliminar;
    }

    public void setDptoAEliminar(Departamento dptoAEliminar) {
        mDptoAEliminar = dptoAEliminar;
    }


}
