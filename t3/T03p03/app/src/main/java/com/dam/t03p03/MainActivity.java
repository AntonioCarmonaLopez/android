package com.dam.t03p03;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity
        implements DlgConfirmacion.DlgConfirmacionListener {

    private final int ID_ALTA = 1;
    //private final int ID_BAJA = 2;
    private final int ID_EDITAR = 3;
    private final int ID_FOTO = 4;

    private ImageView ivFoto;

    private int pos;
    private String resultado;
    //declarar reciclerview y adaptador para alumnos
    private RecyclerView rvAlumnos;
    private Intent intent;

    public AdaptadorAlumnosRv mAdaptadorAlumnos;
    private LinearLayout linearLayout;
    private Bitmap image;
    private Alumno a;
    private byte[] byteArray;

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
                pos = mAdaptadorAlumnos.getItemPos();
                if (pos < 0) {
                    snackbar(getString(R.string.noChoice));

                } else
                    snackbar("DNI: " + Datos.getInstance().gettAlumnos().get(pos).getDni() + " Nombre: " + Datos.getInstance().gettAlumnos().get(pos).getNombre()
                            + " Fecha de Nacimiento: " + Datos.getInstance().gettAlumnos().get(pos).getFecNac() + " Ciclo: " + Datos.getInstance().gettAlumnos().get(pos).getCiclo());
            }
        });
        // finviewbyid
        rvAlumnos = findViewById(R.id.rvAlumnos);
        linearLayout = findViewById(R.id.llAlumnoItem);

        //init

        //recicler
        rvAlumnos.setHasFixedSize(true);
        rvAlumnos.setLayoutManager(new LinearLayoutManager(this));
        rvAlumnos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdaptadorAlumnos = new AdaptadorAlumnosRv(Datos.getInstance().gettAlumnos());
        rvAlumnos.setAdapter(mAdaptadorAlumnos);

        //listeners

        // Register the students for Context menu
        //registerForContextMenu(linearLayout);

    }


    /*//inflate context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu , menu);
    }*/
    //handle item click for context_menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        intent = new Intent(this, AltaAlumno.class);
        pos = item.getGroupId();
        a = Datos.getInstance().gettAlumnos().get(pos);
        switch (item.getItemId()) {
            case 0:
                intent.putExtra("id", ID_EDITAR);
                intent.putExtra("student", a);
                if (a.getImagen() != null)
                    byteArray = convertirBitmap();
                intent.putExtra("imagen", byteArray);
                lanzarIntent(intent,ID_EDITAR);
                break;
            case 1:
                confBorrar();
                break;
            case 2:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                    lanzarIntent(takePictureIntent, ID_FOTO);
        }
        return true;
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
                Intent intent = new Intent(this, AltaAlumno.class);
                intent.putExtra("id", ID_ALTA);
                lanzarIntent(intent, ID_ALTA);
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
        dc.show(getSupportFragmentManager(), "TagSalir");
    }


    @Override
    public void onDlgConfirmacionPositiveClick(DialogFragment dialog) {
        switch (dialog.getTag()) {
            case "TagSalir":
                finish();
            case "TagBorrar":
                Datos.getInstance().gettAlumnos().remove(a);
                mAdaptadorAlumnos.notifyItemRemoved(pos);
        }
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
                resultado = data.getExtras().getString("resultado");
                mAdaptadorAlumnos.notifyItemInserted(pos);
                break;
            case ID_EDITAR:
                resultado = data.getExtras().getString("resultado");
                mAdaptadorAlumnos.notifyItemChanged(pos);
                if (resultCode == RESULT_OK)
                    rvAlumnos.smoothScrollToPosition(mAdaptadorAlumnos.getItemCount() - 1);
                break;
            case ID_FOTO:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        image = (Bitmap) extras.get("data");
                        a.setImagen(image);
                        mAdaptadorAlumnos.notifyItemChanged(pos);
                        resultado = getString(R.string.foto_ok);
                    }
                }
        }
        snackbar(resultado);
    }

    public void snackbar(String txt) {
        Snackbar.make(findViewById(android.R.id.content), txt, Snackbar.LENGTH_LONG).show();
    }

    private void confBorrar() {
        DlgConfirmacion dc = new DlgConfirmacion();
        dc.setTitulo(R.string.title_activity_baja_alumno);
        dc.setMensaje(R.string.confBorrar);
        dc.setCancelable(false);
        dc.show(getSupportFragmentManager(), "TagBorrar");
    }

    private byte[] convertirBitmap() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;

    }

}
