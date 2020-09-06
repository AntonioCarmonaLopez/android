package com.dam.t03p02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.dam.dialogos.DlgAlerta;
import com.dam.dialogos.DlgConfirmacion;
import com.dam.logica.LogicaAlumno;
import com.dam.modelo.Alumno;

public class BajaAlumno extends AppCompatActivity
        implements DlgConfirmacion.DlgConfirmacionListener {
    private EditText etDni;
    private Button btAceptar,btCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baja_alumno);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //findbyid
        etDni=findViewById(R.id.etDni);
        btAceptar=findViewById(R.id.btAceptar);
        btCancelar=findViewById(R.id.btCancelar);
        //listeners
        btAceptar.setOnClickListener(op_OnClickListener);
        btCancelar.setOnClickListener(op_OnClickListener);
    }

    private View.OnClickListener op_OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btAceptar:
                    if(check())
                        confBorrar();
                    return;
                case R.id.btCancelar:
                    confCancelar();
            }
        }
    };

    private boolean check(){
        if(etDni.getText().toString().equals("")){
            DlgAlerta dlgAlerta=new DlgAlerta();
            dlgAlerta.setTitulo(R.string.title_activity_baja_alumno);
            dlgAlerta.setMensaje(R.string.obligatorios);
            dlgAlerta.show(getSupportFragmentManager(),"alert");
            return false;
        }
        return true;
    }

    private void confBorrar(){
        DlgConfirmacion dc=new DlgConfirmacion();
        dc.setTitulo(R.string.title_activity_baja_alumno);
        dc.setMensaje(R.string.confBorrar);
        dc.setCancelable(false);
        dc.show(getSupportFragmentManager(),"tagBorrar");
    }

    private void confCancelar(){
        DlgConfirmacion dc=new DlgConfirmacion();
        dc.setTitulo(R.string.title_activity_baja_alumno);
        dc.setMensaje(R.string.confCancelar);
        dc.setCancelable(false);
        dc.show(getSupportFragmentManager(),"tagCancelar");
    }

    @Override
    public void onDlgConfirmacionPositiveClick(DialogFragment dialog) {
        Intent intent=new Intent();
        switch (dialog.getTag()){
            case "tagBorrar":
                Alumno a=new Alumno();
                a.setDni(etDni.getText().toString());
                if(LogicaAlumno.bajaAlumno(a)) {
                    intent.putExtra("resultado",getString(R.string.bajaOK));
                    setResult(RESULT_OK,intent);
                }else {
                    setResult(RESULT_CANCELED, intent);
                    intent.putExtra("resultado", getString(R.string.bajaKO));
                }
                finish();
            case "tagCancelar":
                intent.putExtra("resultado", getString(R.string.operacionKO));
                setResult(RESULT_CANCELED,intent);
                finish();
        }
    }

    @Override
    public void onDlgConfirmacionNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        confCancelar();
    }
}

