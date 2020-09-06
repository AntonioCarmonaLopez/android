package com.dam.t03p03;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.dam.dialogos.DlgAlerta;
import com.dam.dialogos.DlgConfirmacion;
import com.dam.dialogos.DlgSeleccionFecha;
import com.dam.logica.LogicaAlumno;
import com.dam.modelo.Alumno;


import java.util.Locale;

public class AltaAlumno extends AppCompatActivity
        implements DlgConfirmacion.DlgConfirmacionListener, DlgSeleccionFecha.DlgSeleccionFechaListener {

    private EditText etDni, etNombre, etFecNac;
    private Spinner spCiclo;
    private Button btAceptar, btCancelar, btFecha;
    private int id;
    private Alumno a;
    private Intent intent;

    private ImageView ivFoto;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_alumno);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        id = getIntent().getExtras().getInt("id", -1);


       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //findbyid
        etDni = findViewById(R.id.etDni);
        etNombre = findViewById(R.id.etNombre);
        etFecNac = findViewById(R.id.etFecNac);
        spCiclo = findViewById(R.id.spCiclo);
        btAceptar = findViewById(R.id.btAceptar);
        btCancelar = findViewById(R.id.btCancelar);
        btFecha = findViewById(R.id.btChoice);
        ivFoto = findViewById(R.id.ivFoto);

        //init
        etFecNac.setFocusable(false);
        etFecNac.setClickable(true);

        //listeners
        btAceptar.setOnClickListener(op_OnClickListener);
        btCancelar.setOnClickListener(op_OnClickListener);
        btFecha.setOnLongClickListener(op_OnLongClickListener);
        etFecNac.setOnLongClickListener(op_OnLongClickListener);


        //inflate spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ciclos, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCiclo.setAdapter(adapter);

        if (id == 3) {
            Bundle datos = this.getIntent().getExtras();
            Alumno a = datos.getParcelable("student");
            byte[] byteArray = getIntent().getByteArrayExtra("imagen");
            setTitle(R.string.editar);
            etDni.setEnabled(false);
            etDni.setText(a.getDni());
            etNombre.setText(a.getNombre());
            etFecNac.setText(a.getFecNac());
            spCiclo.setSelection(adapter.getPosition(a.getCiclo()));
            if(byteArray != null) {

                image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                ivFoto.setImageBitmap(image);
            }
        }
    }

    private View.OnLongClickListener op_OnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.btChoice:
                    DlgSeleccionFecha dsf = new DlgSeleccionFecha();
                    dsf.show(getSupportFragmentManager(), "tagSeleccionFecha");
                    break;
                case R.id.etFecNac:
                    PopupMenu popupMenu = new PopupMenu(getApplicationContext(), etFecNac);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_pop_up:
                                    etFecNac.setText("");
                                    break;
                            }
                            return false;
                        }
                    });
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.pop_up_menu, popupMenu.getMenu());
                    popupMenu.show();

            }
            return false;
        }
    };

    private View.OnClickListener op_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btAceptar:
                    if (!check())
                        return;
                    intent = new Intent();
                    a = new Alumno();
                    if (id == 1) {
                        a.setDni(etDni.getText().toString());
                        a.setNombre(etNombre.getText().toString());
                        a.setFecNac(etFecNac.getText().toString());
                        a.setCiclo(spCiclo.getSelectedItem().toString());
                        a.setImagen(image);
                        if (LogicaAlumno.altaAlumno(a)) {
                            intent.putExtra("resultado", getString(R.string.altaOK));
                            setResult(RESULT_OK, intent);
                        } else {
                            intent.putExtra("resultado", getString(R.string.altaKO));
                            setResult(RESULT_CANCELED, intent);
                        }
                    } else if (id == 3) {
                        intent = new Intent();
                        a.setDni(etDni.getText().toString());
                        a.setNombre(etNombre.getText().toString());
                        a.setFecNac(etFecNac.getText().toString());
                        a.setCiclo(spCiclo.getSelectedItem().toString());
                        a.setImagen(image);
                        if (LogicaAlumno.editarAlumno(a)) {
                            intent.putExtra("resultado", getString(R.string.editar_ok));
                            setResult(RESULT_OK, intent);
                        } else {
                            intent.putExtra("resultado", getString(R.string.editar_ko));
                            setResult(RESULT_CANCELED, intent);
                        }
                    }
                    break;
                case R.id.btCancelar:

                    confCancelar();
                    break;
            }
            finish();
        }
    };


    private boolean check() {
        if (etDni.getText().toString().equals("") || etDni.getText().toString().equals((""))) {
            DlgAlerta dlgAlerta = new DlgAlerta();
            dlgAlerta.setTitulo(R.string.title_activity_alta_alumno);
            dlgAlerta.setMensaje(R.string.obligatorios);
            dlgAlerta.show(getSupportFragmentManager(), "alert");
            return false;
        }
        return true;
    }

    private void confCancelar() {
        DlgConfirmacion dc = new DlgConfirmacion();
        dc.setTitulo(R.string.title_activity_alta_alumno);
        dc.setMensaje(R.string.confCancelar);
        dc.setCancelable(false);
        dc.show(getSupportFragmentManager(), "tagCancelar");
        intent=new Intent();
        intent.putExtra("resultado", getString(R.string.operacionKO));
        setResult(RESULT_CANCELED, intent);
    }

    @Override
    public void onDlgConfirmacionPositiveClick(DialogFragment dialog) {
        switch (dialog.getTag()) {
            case "tagCancelar":
                finish();
        }
        finish();
    }

    @Override
    public void onDlgConfirmacionNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onBackPressed() {
        intent=new Intent();
        intent.putExtra("resultado", getString(R.string.operacionKO));
        setResult(RESULT_CANCELED, intent);
        confCancelar();
    }

    @Override
    public void onDlgSeleccionFechaClick(DialogFragment dialog, int year, int month, int day) {
        String fecha = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);
        etFecNac.setText(fecha);

    }

}
