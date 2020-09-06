package com.dam.t07p02.vista.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dam.t07p02.R;
import com.dam.t07p02.modelo.Departamento;
import com.dam.t07p02.modelo.Incidencia;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MtoIncsFragment extends Fragment {
    private TextView tvCabecera;
    private EditText etIdDpto,etDeptoNombre,etId, etFecha,etDescripcion,etResolucion;
    private RadioButton rbRMA, rbRMI;
    private CheckBox cbEstado;
    private Button btCancelar, btAceptar;

    private int mOp;    // Operación a realizar
    private Incidencia mInc;
    private String nombreDpto;
    private Departamento mDpto;

    public static final int OP_ELIMINAR = 1;
    public static final int OP_EDITAR = 2;
    public static final int OP_CREAR = 3;

    public static final String TAG = "MtoIncsFragment";
    private MtoIncsFragment.MtoIncsFragInterface mListener;

    public interface MtoIncsFragInterface {

        void onCancelarMtoIncsFrag();

        void onAceptarMtoIncsFrag(int op, Incidencia incidencia);

    }

    public MtoIncsFragment() {
        // Required empty public constructor
    }

    public static MtoIncsFragment newInstance(Bundle arguments) {
        MtoIncsFragment frag = new MtoIncsFragment();
        if (arguments != null) {
            frag.setArguments(arguments);

        }
        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MtoIncsFragment.MtoIncsFragInterface) {
            mListener = (MtoIncsFragment.MtoIncsFragInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement MtoIncsFragInterface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOp = getArguments().getInt("op");
            mDpto = getArguments().getParcelable("dpto");
            mInc=getArguments().getParcelable("incidencia");
            nombreDpto=getArguments().getString("nombreDpto");
        } else {
            mOp = -1;
            mDpto = null;
            mInc=null;
            nombreDpto = "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mto_incs, container, false);

        // FindViewByIds
        tvCabecera = v.findViewById(R.id.tvIncCabecera);
        etIdDpto = v.findViewById(R.id.etIncDptoId);
        etDeptoNombre = v.findViewById(R.id.etIncDptoNombre);
        etId = v.findViewById(R.id.etIncId);
        etFecha=v.findViewById(R.id.etIncFecha);

        etDescripcion=v.findViewById(R.id.etIncDescripcion);
        rbRMA=v.findViewById(R.id.rbIncTipoRMA);
        rbRMI=v.findViewById(R.id.rbIncTipoRMI);
        cbEstado=v.findViewById(R.id.cbIncEstado);
        etResolucion=v.findViewById(R.id.etIncResolucion);
        btCancelar = v.findViewById(R.id.btCancelar);
        btAceptar = v.findViewById(R.id.btAceptar);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inits
        if (mOp != -1) {                    // MtoIncsFragment requiere una operación válida!!
            btCancelar.setEnabled(true);
            btAceptar.setEnabled(true);

            switch (mOp) {
                case OP_CREAR:
                    Incidencia inc = new Incidencia();
                    tvCabecera.setText(getString(R.string.tv_Inc_Cabecera_Crear));
                    etIdDpto.setText(String.valueOf(mDpto.getId()));
                    etDeptoNombre.setText(mDpto.getNombre());
                    inc.setDptoNombre(mDpto.getNombre());
                    etId.setText(inc.getId());
                    etId.setEnabled(false);
                    etIdDpto.setEnabled(false);
                    etDeptoNombre.setEnabled(false);
                    etFecha.setFocusable(false);
                    etFecha.setText(calcularFecha());
                    etResolucion.setEnabled(false);
                    break;
                case OP_EDITAR:
                    tvCabecera.setText(getString(R.string.tv_Inc_Cabecera_Editar));
                    etIdDpto.setText(String.valueOf(mInc.getIdDpto()));
                    etIdDpto.setEnabled(false);
                    etDeptoNombre.setText(nombreDpto);
                    etDeptoNombre.setEnabled(false);
                    etId.setText(mInc.getId());
                    etId.setEnabled(false);
                    etFecha.setText(mInc.getFecha());
                    etFecha.setEnabled(false);
                    etDescripcion.setText(mInc.getDescripcion());
                    etDescripcion.setEnabled(false);
                    if (mInc.getTipo().equals("RMA"))
                        rbRMA.setChecked(true);
                    else
                        rbRMI.setChecked(true);
                    if (mInc.isEstado())
                        cbEstado.setChecked(true);
                    etResolucion.setText(mInc.getResolucion());
                    break;
            }

            // Listeners
            btCancelar.setOnClickListener(btCancelar_OnClickListener);
            btAceptar.setOnClickListener(btAceptar_OnClickListener);

        } else {
            btCancelar.setEnabled(false);
            btAceptar.setEnabled(false);
        }
    }

    public static String calcularFecha() {
        Calendar fecha = new GregorianCalendar();
        int anio = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH)+1;
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        return String.format("%02d"+"/"+"%02d"+"/"+"%4d",dia,mes,anio);
    }

    public static String string2date(String fecha) {
        String[] trozos = fecha.split("/");
        return trozos[2] + "-" + trozos[1] + "-" + trozos[0];
    }

    public static String date2string(String fecha) {
        String anyo, mes, dia = null;
        anyo = fecha.substring(0, 4);
        mes = fecha.substring(6, 7);
        if (Integer.parseInt(mes) <= 9) {
            mes = "0" + mes;
            dia = fecha.substring(8, 9);
        } else {
            dia = fecha.substring(9, 10);
        }
        return dia + "/" + mes + "/" + anyo;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private View.OnClickListener btCancelar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            if (mListener != null) {
                mListener.onCancelarMtoIncsFrag();
            }
        }
    };

    private View.OnClickListener btAceptar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            if (mListener != null) {
                if (!etId.getText().toString().equals("") &&
                        !etIdDpto.getText().toString().equals("") &&
                        !etFecha.getText().toString().equals("") &&
                        !etDescripcion.getText().toString().equals("")){

                    // Creamos una nueva Incidencia, independientemente de la operación a realizar!!
                    mInc = new Incidencia();
                    mInc.setIdDpto(Integer.parseInt(etIdDpto.getText().toString()));
                    mInc.setDptoNombre(etDeptoNombre.getText().toString());
                    mInc.setId(etId.getText().toString());
                    mInc.setFecha(string2date(etFecha.getText().toString()));
                    mInc.setDescripcion(etDescripcion.getText().toString());
                    if (rbRMA.isChecked())
                        mInc.setTipo("RMA");
                    else
                        mInc.setTipo("RMI");
                    if (cbEstado.isChecked())
                        mInc.setEstado(true);
                    else
                        mInc.setEstado(false);
                    mInc.setResolucion("");
                    mListener.onAceptarMtoIncsFrag(mOp, mInc);

                } else {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.msg_FaltanDatosObligatorios, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    };
}
