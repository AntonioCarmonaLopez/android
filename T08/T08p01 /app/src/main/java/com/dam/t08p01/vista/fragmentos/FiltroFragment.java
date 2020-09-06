package com.dam.t08p01.vista.fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.dam.t08p01.R;
import com.dam.t08p01.modelo.Departamento;
import com.dam.t08p01.modelo.Filtro;
import com.dam.t08p01.modelo.Incidencia;
import com.dam.t08p01.vistamodelo.DptosViewModel;
import com.dam.t08p01.vistamodelo.IncsViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FiltroFragment extends Fragment {

    private Spinner spDptos;
    private RadioButton rbTodas, rbResueltas, rbNoResueltas;
    private EditText etFecha;
    private Button btFecha;
    private Button btCancelar, btAceptar;
    private int idDepto, op;
    private String fecha;
    private Departamento mLogin;

    private List<Departamento> mDptos;
    private ArrayAdapter<Departamento> mAdaptadorDtpos;

    private OnFragmentFiltroListener mListener;

    public FiltroFragment() {
        // Required empty public constructor
    }


    public interface OnFragmentFiltroListener {

        void onFragmentFecha(int departamento, int filtro, Departamento mLogin);

        void onFragmentAceptar(int departamento, int estado, String fecha);

        void onFragmentCancelar();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentFiltroListener) {
            mListener = (OnFragmentFiltroListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentFiltroListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idDepto = getArguments().getInt("idDepto");
            op = getArguments().getInt("op");
            fecha = getArguments().getString("fecha");
            mLogin = getArguments().getParcelable("dpto");
        } else {
            idDepto = 0;
            op = 2;
            fecha = "";
            mLogin = null;
        }
        // Inits Dptos Observer
        DptosViewModel dptosVM = new ViewModelProvider(requireActivity()).get(DptosViewModel.class);
        dptosVM.getDptosSE().observe(this, new Observer<List<Departamento>>() {
            @Override
            public void onChanged(List<Departamento> dptos) {
                mDptos = dptos;
                mAdaptadorDtpos = new ArrayAdapter<>(requireActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        dptos);
                spDptos.setAdapter(mAdaptadorDtpos);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_filtro_incs, container, false);
        spDptos = v.findViewById(R.id.spFiltroDptos);
        etFecha = v.findViewById(R.id.etFiltroFecha);
        btFecha = v.findViewById(R.id.btFiltroFecha);
        rbTodas = v.findViewById(R.id.rbFiltroEstadoTodas);
        rbResueltas = v.findViewById(R.id.rbFiltroEstadoResueltas);
        rbNoResueltas = v.findViewById(R.id.rbFiltroEstadoNoResueltas);
        btAceptar = v.findViewById(R.id.btAceptar);
        btCancelar = v.findViewById(R.id.btCancelar);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Inits
        etFecha.setEnabled(false);
        if (fecha == null)
            etFecha.setText(MtoIncsFragment.calcularFecha());
        else
            etFecha.setText(fecha);
        spDptos.setAdapter(mAdaptadorDtpos);
        spDptos.setSelection(0);
        if (mLogin.getId() != 0) {
            spDptos.setEnabled(false);
            spDptos.setSelection(mLogin.getId());
        }
        if (op == 2)
            rbTodas.setChecked(true);
        else if (op == 1)
            rbResueltas.setChecked(true);
        else if (op == 0)
            rbNoResueltas.setChecked(true);
        else
            rbTodas.setChecked(true);

        // Listeners
        btCancelar.setOnClickListener(btClick_OnClickListener);
        btAceptar.setOnClickListener(btClick_OnClickListener);
        btFecha.setOnClickListener(btClick_OnClickListener);
    }

    private View.OnClickListener btClick_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btFiltroFecha:
                    op = radioButtonPosition();
                    mListener.onFragmentFecha((int) spDptos.getSelectedItemId(), op, mLogin);
                    break;
                case R.id.btAceptar:
                    IncsViewModel.filtroBoolean = true;
                    IncsViewModel incsVM = new ViewModelProvider(requireActivity()).get(IncsViewModel.class);
                    Filtro filtro = new Filtro();
                    filtro.setDepartamento(obtenerIdDpto());
                    op = radioButtonPosition();
                    filtro.setEstado(op);
                    filtro.setFecha(MtoIncsFragment.string2date(etFecha.getText().toString()));
                    incsVM.setmFiltro(filtro);
                    mListener.onFragmentAceptar(idDepto, op, fecha);
                    break;
                case R.id.btCancelar:
                    mListener.onFragmentCancelar();
                    break;
            }
        }
    };

    private int radioButtonPosition(){
        if (rbTodas.isChecked())
            return 2;
        else if (rbResueltas.isChecked())
            return 1;
        else
            return 0;
    }

    private int obtenerIdDpto() {
        int posDpto = spDptos.getSelectedItemPosition();
        Departamento departamento = mDptos.get(posDpto);
        return departamento.getId();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
