package com.dam.t07p02.vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.dam.t07p02.R;
import com.dam.t07p02.modelo.Departamento;
import com.dam.t07p02.modelo.Filtro;
import com.dam.t07p02.modelo.Incidencia;
import com.dam.t07p02.vista.dialogos.DlgConfirmacion;
import com.dam.t07p02.vista.dialogos.DlgSeleccionFecha;
import com.dam.t07p02.vista.fragmentos.BusDptosFragment;
import com.dam.t07p02.vista.fragmentos.BusIncsFragment;
import com.dam.t07p02.vista.fragmentos.FiltroFragment;
import com.dam.t07p02.vista.fragmentos.MtoDptosFragment;
import com.dam.t07p02.vista.fragmentos.MtoIncsFragment;
import com.dam.t07p02.vistamodelo.DptoLogica;
import com.dam.t07p02.vistamodelo.DptosViewModel;
import com.dam.t07p02.vistamodelo.IncLogica;
import com.dam.t07p02.vistamodelo.IncsViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class IncsActivity extends AppCompatActivity
        implements BusIncsFragment.BusIncsFragInterface,
        MtoIncsFragment.MtoIncsFragInterface,
        DlgConfirmacion.DlgConfirmacionListener,
        DlgSeleccionFecha.DlgSeleccionFechaListener,
        FiltroFragment.OnFragmentFiltroListener {

    private NavController mNavC;
    private MutableLiveData<List<Incidencia>> mIncs;


    private IncsViewModel mIncsVM;
    private Departamento mLogin;
    private int op,idDepartamento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // FindViewByIds

        // Inits
        mIncs = new MutableLiveData<>();
        mNavC = Navigation.findNavController(this, R.id.navhostfrag_incs);
        mIncsVM = new ViewModelProvider(this).get(IncsViewModel.class);

        // Recuperamos el dpto login
        Intent i = getIntent();
        if (i != null) {
            Bundle b = i.getExtras();
            if (b != null) {
                mLogin = b.getParcelable("login");
                mIncsVM.setLogin(mLogin);      // Guardamos el login en el ViewModel
            }
        }
        if (mLogin == null) {
            Snackbar.make(findViewById(android.R.id.content), R.string.msg_NoLogin, Snackbar.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bus_incs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuFiltro:
                // Lanzo frag. Filtrar
                Bundle b=new Bundle();
                b.putParcelable("dpto",mLogin);
                mNavC.navigateUp();
                mNavC.navigate(R.id.action_busIncsFragment_to_filtroFragment,b);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCrearBusIncsFrag() {
        //recuperar departamento y enviarlos al fragmento MtoIncsFragment
        Bundle datos = new Bundle();
        datos=this.getIntent().getExtras();
        if(datos!=null) {
            Departamento dpto = datos.getParcelable("login");
            datos.putParcelable("dpto",dpto);
            datos.putInt("op", MtoIncsFragment.OP_CREAR);
            //MtoIncsFragment.newInstance(datos);
        }
        mNavC.navigate(R.id.action_busIncsFragment_to_mtoIncsFragment, datos);
    }

    @Override
    public void onEditarBusIncsFrag(Incidencia incidencia) {
        // Lanzamos MtoIncsFragment
        Bundle bundle = new Bundle();
        bundle.putInt("op", MtoIncsFragment.OP_EDITAR);
        Departamento d=DptoLogica.recuperarDepartamentoFiltro(this,String.valueOf(incidencia.getIdDpto()));
        bundle.putString("nombreDpto",d.getNombre());
        bundle.putParcelable("incidencia", incidencia);
        mNavC.navigate(R.id.action_busIncsFragment_to_mtoIncsFragment, bundle);
    }

    @Override
    public void onEliminarBusIncsFrag(Incidencia incidencia) {
        // Guardamos la incidencia a eliminar en el ViewModel
        mIncsVM.setIncAEliminar(incidencia);
        // Lanzamos DlgConfirmacion
        Bundle bundle = new Bundle();
        bundle.putInt("titulo", R.string.app_name);
        bundle.putInt("mensaje", R.string.msg_DlgConfirmacion_Eliminar);
        mNavC.navigate(R.id.action_global_dlgConfirmacionIncs, bundle);
    }

    @Override
    public void onFiltroBusIncFrag() {

    }

    @Override
    public void onCancelarMtoIncsFrag() {
        // Cerramos MtoIncsFragment
        mNavC.navigateUp();
    }

    @Override
    public void onDlgSeleccionFechaClick(DialogFragment dialog, String fecha) {
        Bundle b = new Bundle();
        switch (dialog.getTag()) {
            case "fechaFiltro":
                //lanzo filtoFragment con fecha,idDpto,filtro y login
                b.putString("fecha",fecha);
                b.putInt("idDepto",idDepartamento);
                b.putInt("op",op);
                b.putParcelable("dpto",mLogin);
                mNavC.navigate(R.id.action_busIncsFragment_to_filtroFragment,b);
        }
    }

    @Override
    public void onDlgSeleccionFechaCancel(DialogFragment dialog) {

    }

    @Override
    public void onAceptarMtoIncsFrag(int op, Incidencia incidencia) {
        switch (op) {
            case MtoIncsFragment.OP_CREAR:
                if (mIncsVM.altaIncidencia(incidencia)) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.msg_AltaIncidenciaOK, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.msg_AltaIncidenciaKO, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case MtoIncsFragment.OP_EDITAR:
                if (mIncsVM.editarIncidencia(incidencia)) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.msg_EditarIncidenciaOK, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.msg_EditarIncidenciaKO, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case MtoIncsFragment.OP_ELIMINAR:
                break;
        }

        // Cerramos MtoIncsFragment
        mNavC.navigateUp();
    }

    @Override
    public void onDlgConfirmacionPositiveClick(DialogFragment dialog) {
        // Recuperamos la incidencia a eliminar del ViewModel
        Incidencia incAEliminar = mIncsVM.getIncAEliminar();
        if (mIncsVM.bajaIncidencia(incAEliminar)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.msg_BajaIncidenciaOK, Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(findViewById(android.R.id.content), R.string.msg_BajaIncidenciaKO, Snackbar.LENGTH_SHORT).show();
        }
        // Eliminamos la incidencia a eliminar del ViewModel
        mIncsVM.setIncAEliminar(null);
    }

    @Override
    public void onDlgConfirmacionNegativeClick(DialogFragment dialog) {
        // Eliminamos la incidencia a eliminar del ViewModel
        mIncsVM.setIncAEliminar(null);
    }

    @Override
    public void onFragmentFecha(int departamento,int op,Departamento mLogin) {
        mNavC.navigateUp();
        idDepartamento=departamento;
        this.op=op;
        this.mLogin=mLogin;
        DlgSeleccionFecha df=new DlgSeleccionFecha();
        df.show(getSupportFragmentManager(),"fechaFiltro");

    }

    @Override
    public void onFragmentAceptar() {
        mNavC.navigate(R.id.action_filtroFragment_to_busIncsFragment);
    }

    @Override
    public void onFragmentCancelar() {
        mNavC.navigate(R.id.action_filtroFragment_to_busIncsFragment);
    }
}

