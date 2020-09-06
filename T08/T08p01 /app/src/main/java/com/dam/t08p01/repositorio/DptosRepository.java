package com.dam.t08p01.repositorio;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dam.t08p01.modelo.AppDatabase;
import com.dam.t08p01.modelo.Departamento;
import com.dam.t08p01.modelo.Incidencia;
import com.dam.t08p01.vista.DptosActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DptosRepository {

    /* Singleton **********************************************************************************/

    private static DptosRepository mRepo = null;

    private MutableLiveData<List<Departamento>> mDptos;
    private AppDatabase mAppDB;

    private DptosRepository(Application application) {
        mDptos = new MutableLiveData<>();
        mAppDB = AppDatabase.getAppDatabase(application);
    }

    public static DptosRepository getInstance(Application application) {
        if (mRepo == null) {
            mRepo = new DptosRepository(application);
        }
        return mRepo;
    }

    /* Métodos Lógica Dptos ***********************************************************************/

    public LiveData<List<Departamento>> recuperarDepartamentosSE() {
        mAppDB.getRefFB().child("Dptos").orderByKey().addListenerForSingleValueEvent(dptosSE_ValueEventListener);
        return mDptos;
    }

    private ValueEventListener dptosSE_ValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Departamento> tDptos = new ArrayList<>();
            if (dataSnapshot.exists()) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Departamento dpto = ds.getValue(Departamento.class);
                    tDptos.add(dpto);
                }
            }
            mDptos.setValue(tDptos);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            ;
        }
    };

    public LiveData<List<Departamento>> recuperarDepartamentosME() {
        mAppDB.getRefFB().child("Dptos").orderByKey().addValueEventListener(dptosME_ValueEventListener);
        return mDptos;
    }

    public void eliminarEventosGetDptosME() {
        mAppDB.getRefFB().child("Dptos").removeEventListener(dptosME_ValueEventListener);
    }

    private ValueEventListener dptosME_ValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Departamento> tDptos = new ArrayList<>();
            if (dataSnapshot.exists()) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Departamento dpto = ds.getValue(Departamento.class);
                    tDptos.add(dpto);
                }
            }
            mDptos.setValue(tDptos);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            ;
        }
    };

    private boolean existeDepartamento(Departamento dpto) {
        return false;
    }

    public boolean altaDepartamento(final Departamento dpto) {
        // Comprobamos previamente la existencia!!
        mAppDB.getRefFB().child("Dptos").child(String.valueOf(dpto.getId())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Departamento tmpDpto = dataSnapshot.getValue(Departamento.class);
                if (tmpDpto == null) {
                    mAppDB.getRefFB().child("Dptos").child(String.valueOf(dpto.getId())).setValue(dpto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ;
            }
        });
        return true;
    }

    public boolean editarDepartamento(Departamento dpto) {
        mAppDB.getRefFB().child("Dptos").child(String.valueOf(dpto.getId())).setValue(dpto);
        return true;
    }

    public boolean bajaDepartamento(DptosActivity dptosActivity,Departamento dpto) {
        new AuthGoogle(dptosActivity).borrarUsuario(dpto.getNombre()+"@chirinos.com",dpto.getClave());
        mAppDB.getRefFB().child("Dptos").child(String.valueOf(dpto.getId())).removeValue();
        return true;
    }

    public Departamento recuperarDepartamento(final Incidencia inc) {
        final Departamento[] departamento = {new Departamento()};
        Query mQueryRef = mAppDB.getRefFB().child("Dptos")
                .orderByKey();
        mQueryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Departamento dptoTmp = ds.getValue(Departamento.class);
                    if (dptoTmp.getId() == (inc.getIdDpto()))
                        departamento[0] = dptoTmp;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });
        return departamento[0];
    }
}
