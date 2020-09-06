package com.dam.t08p01.repositorio;

import android.app.Application;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dam.t08p01.modelo.AppDatabase;
import com.dam.t08p01.modelo.Departamento;
import com.dam.t08p01.modelo.Filtro;
import com.dam.t08p01.modelo.Incidencia;
import com.dam.t08p01.vista.fragmentos.MtoIncsFragment;
import com.dam.t08p01.vistamodelo.IncsViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class IncsRepository {

    /* Singleton **********************************************************************************/

    private static IncsRepository mRepo = null;

    private MutableLiveData<List<Incidencia>> mIncs;
    private AppDatabase mAppDB;
    private Filtro mFiltro;
    private Departamento mLogin;


    private IncsRepository(Application application) {
        mIncs = new MutableLiveData<>();
        mAppDB = AppDatabase.getAppDatabase(application);
    }

    public static IncsRepository getInstance(Application application) {
        if (mRepo == null) {
            mRepo = new IncsRepository(application);
        }
        return mRepo;
    }

    /* Métodos Lógica Incs ***********************************************************************/

    public LiveData<List<Incidencia>> recuperarIncsToMarkerSE() {
        mAppDB.getRefFB().child("Incs").addListenerForSingleValueEvent(incsToMarkerSE_ValueEventListener);
        return mIncs;
    }

    private ValueEventListener incsToMarkerSE_ValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Incidencia> tIncs = new ArrayList<>();
            Log.i("t08p01", "Antes del listener!");
            if (dataSnapshot.exists()) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        for (DataSnapshot ds2 : ds1.getChildren()) {
                            //catching values
                            Incidencia inc = ds2.getValue(Incidencia.class);
                            Log.i("t08p01","change"+inc.getId());

                        }
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            ;
        }
    };


    public LiveData<List<Incidencia>> recuperarIncidenciasSE(Filtro filtro) {
        mFiltro = filtro;
        mAppDB.getRefFB().child("Incs").orderByChild("fecha").addListenerForSingleValueEvent(incsSE_ValueEventListener);
        return mIncs;
    }

    private ValueEventListener incsSE_ValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Incidencia> tIncs = new ArrayList<>();
            Log.i("t08p01", "Antes del listener!");
            if (dataSnapshot.exists()) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        for (DataSnapshot ds2 : ds1.getChildren()) {
                            //catching values
                            Incidencia inc = ds2.getValue(Incidencia.class);
                            if (inc.getIdDpto() == mFiltro.getDepartamento()) {
                                if (MtoIncsFragment.string2date(inc.getFecha()).compareTo(mFiltro.getFecha()) > 0) {
                                    if (mFiltro.getEstado() == 2) {
                                        tIncs.add(inc);
                                    } else if (inc.getEstado() == mFiltro.getEstado()) {
                                        tIncs.add(inc);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            mIncs.setValue(tIncs);
            IncsViewModel.setDatos(tIncs);
            IncsViewModel.filtroBoolean = false;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            ;
        }
    };

    public LiveData<List<Incidencia>> recuperarIncidenciasME(Departamento mLogin) {
        this.mLogin = mLogin;

        mAppDB.getRefFB().child("Incs").orderByChild("fecha").addValueEventListener(incsME_ValueEventListener);

        return mIncs;
    }

    public void eliminarEventosGetIncsME() {
        mAppDB.getRefFB().child("Incs").removeEventListener(incsME_ValueEventListener);
    }

    private ValueEventListener incsME_ValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Incidencia> tIncs = new ArrayList<>();
            if (dataSnapshot.exists()) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        for (DataSnapshot ds2 : ds1.getChildren()) {
                            //catching values
                            Incidencia inc = ds2.getValue(Incidencia.class);
                            if (mLogin.getId() == 0) {
                                tIncs.add(inc);
                            } else {
                                if (inc.getIdDpto() == mLogin.getId())
                                    tIncs.add(inc);
                            }
                        }
                    }
                }
            }
            mIncs.setValue(tIncs);
            IncsViewModel.setDatos(tIncs);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            ;
        }
    };

    private boolean existeIncidencia(Incidencia inc) {
        return false;
    }

    public boolean altaIncidencia(final Incidencia inc) {
        // Comprobamos previamente la existencia!!
        mAppDB.getRefFB().child("Incs").child(String.valueOf(inc.getIdDpto())).child(MtoIncsFragment.string2date(inc.getFecha()))
                .child(inc.getId()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Incidencia tmpInc = dataSnapshot.getValue(Incidencia.class);
                if (tmpInc == null) {
                    mAppDB.getRefFB().child("Incs").child(String.valueOf(inc.getIdDpto()))
                            .child(MtoIncsFragment.string2date(inc.getFecha()))
                            .child(MtoIncsFragment.cleanId(inc.getId())).setValue(inc);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ;
            }
        });
        return true;
    }

    public boolean editarIncidencia(Incidencia inc) {
        mAppDB.getRefFB().child("Incs").child(String.valueOf(inc.getIdDpto()))
                .child(MtoIncsFragment.string2date(inc.getFecha()))
                .child(MtoIncsFragment.cleanId(inc.getId())).setValue(inc);
        return true;
    }

    public boolean bajaIncidencia(Incidencia inc) {
        mAppDB.getRefFB().child("Incs").child(String.valueOf(inc.getIdDpto()))
                .child(MtoIncsFragment.string2date(inc.getFecha()))
                .child(MtoIncsFragment.cleanId(inc.getId())).removeValue();
        return true;
    }

    public boolean bajaIncidenciasDepartamento(Departamento dptoAEliminar) {
        mAppDB.getRefFB().child("Incs").child(String.valueOf(dptoAEliminar.getId())).removeValue();
        return true;
    }
}

