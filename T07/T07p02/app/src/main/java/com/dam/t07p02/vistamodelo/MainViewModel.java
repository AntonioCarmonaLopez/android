package com.dam.t07p02.vistamodelo;

import androidx.lifecycle.ViewModel;

import com.dam.t07p02.modelo.Departamento;

public class MainViewModel extends ViewModel {

    private Departamento mLogin;

    /* ViewModel Main *****************************************************************************/

    public MainViewModel() {
        super();
        mLogin = null;
    }

    /* Getters & Setters Objetos Persistentes *****************************************************/

    public Departamento getLogin() {
        return mLogin;
    }

    public void setLogin(Departamento login) {
        mLogin = login;
    }

}
