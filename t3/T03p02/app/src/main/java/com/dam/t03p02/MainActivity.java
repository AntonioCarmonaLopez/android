package com.dam.t03p02;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.adaptadores.AdaptadorAlumnosRv;
import com.dam.dialogos.DlgConfirmacion;
import com.dam.logica.Datos;
import com.dam.modelo.Alumno;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity
        implements DlgConfirmacion.DlgConfirmacionListener {

    private final int ID_ALTA = 1;
    private final int ID_BAJA = 2;
    private int pos;
    private Context context;
    private String resultado;
    //declarar reciclerview y adaptador para alumnos
    private RecyclerView rvAlumnos;

    private AdaptadorAlumnosRv mAdaptadorAlumnos;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos=mAdaptadorAlumnos.getItemPos();
                if(pos<0){
                    snackbar(getString(R.string.noChoice));

                } else
                snackbar("DNI: "+Datos.getInstance().gettAlumnos().get(pos).getDni()+" Nombre: "+Datos.getInstance().gettAlumnos().get(pos).getNombre()
                    +" Fecha de Nacimiento: "+Datos.getInstance().gettAlumnos().get(pos).getFecNac()+" Ciclo: "+Datos.getInstance().gettAlumnos().get(pos).getCiclo());
            }
        });
        // finviewbyid
        rvAlumnos = findViewById(R.id.rvAlumnos);

        //init
        context = this;

        //recicler
        rvAlumnos.setHasFixedSize(true);
        rvAlumnos.setLayoutManager(new LinearLayoutManager(this));
        rvAlumnos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdaptadorAlumnos = new AdaptadorAlumnosRv(Datos.getInstance().gettAlumnos());
        rvAlumnos.setAdapter(mAdaptadorAlumnos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnAlta:
                Intent intent = new Intent(context, AltaAlumno.class);
                lanzarIntent(intent, ID_ALTA);
                return true;
            case R.id.mnBaja:
                intent = new Intent(context, BajaAlumno.class);
                lanzarIntent(intent, ID_BAJA);
                return true;
            case R.id.mnSalir:
                confirmacionSalir();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void lanzarIntent(Intent intent, int ID) {
        startActivityForResult(intent, ID);
    }

    private void confirmacionSalir() {
        DlgConfirmacion dc = new DlgConfirmacion();
        dc.setTitulo(R.string.app_name);
        dc.setMensaje(R.string.confSalir);
        dc.setCancelable(false);
        dc.show(getSupportFragmentManager(), "tagConfirmacion");
    }

    @Override
    public void onDlgConfirmacionPositiveClick(DialogFragment dialog) {
        finish();
    }

    @Override
    public void onDlgConfirmacionNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onBackPressed() {
        confirmacionSalir();
        //super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ID_ALTA:
            case ID_BAJA:
                resultado = data.getExtras().getString("resultado");
                mAdaptadorAlumnos.notifyDataSetChanged();
                rvAlumnos.smoothScrollToPosition(mAdaptadorAlumnos.getItemCount() - 1);
                break;
        }
        snackbar(resultado);
    }

    private void snackbar(String txt) {
        Snackbar.make(findViewById(android.R.id.content), txt, Snackbar.LENGTH_LONG).show();
    }
}
