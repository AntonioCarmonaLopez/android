package com.dam.t08p01.vista.fragmentos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.dam.t08p01.R;
import com.dam.t08p01.modelo.Departamento;
import com.dam.t08p01.modelo.Incidencia;
import com.dam.t08p01.repositorio.LocGoogle;
import com.dam.t08p01.vista.adaptadores.AdaptadorIncs;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import java.util.GregorianCalendar;

public class MtoIncsFragment extends Fragment implements LocGoogle.Listener {

    private TextView tvCabecera, tvLongitud, tvLatitud;
    private EditText etIdDpto, etDeptoNombre, etId, etFecha, etDescripcion, etResolucion;
    private RadioButton rbRMA, rbRMI;
    private CheckBox cbEstado;
    private ImageView ivFoto;
    private Button btCancelar, btAceptar;

    private int mOp;    // Operación a realizar
    private Incidencia mInc;
    private Departamento mLogin;
    private String lat, lon;
    private LocGoogle lg;

    public static final int OP_ELIMINAR = 1;
    public static final int OP_EDITAR = 2;
    public static final int OP_CREAR = 3;
    public static final int OP_VER=4;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String urlFoto;

    public static final String TAG = "MtoIncsFragment";

    private MtoIncsFragInterface mListener;

    @Override
    public void onLocationChange(double latitud, double longitud) {
        lat = String.valueOf(latitud);
        lon = String.valueOf(longitud);
        trocear();
    }

    public interface MtoIncsFragInterface {

        void onCancelarMtoIncsFrag();

        void onAceptarMtoIncsFrag(int op, Incidencia inc);

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
        if (context instanceof MtoIncsFragInterface) {
            mListener = (MtoIncsFragInterface) context;

        } else {
            throw new RuntimeException(context.toString() + " must implement MtoIncsFragInterface");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        lg = new LocGoogle(MtoIncsFragment.this);
        lg.getCoor();

        if (getArguments() != null) {
            mOp = getArguments().getInt("op");
            mInc = getArguments().getParcelable("inc");
            mLogin = getArguments().getParcelable("login");
            lat = getArguments().getString("lat");
            lon = getArguments().getString("lon");
            urlFoto = getArguments().getString("url");
        } else {
            mOp = -1;
            mInc = null;
            mLogin = null;
            lat = "";
            lon = "";
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_mto_incs, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuFoto:
                takePhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivFoto.setImageBitmap(bitmap);
            encodeBitmapAndSaveToFirebase(bitmap);
        }
    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        urlFoto=imageEncoded;
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
        etFecha = v.findViewById(R.id.etIncFecha);
        etDescripcion = v.findViewById(R.id.etIncDescripcion);
        rbRMA = v.findViewById(R.id.rbIncTipoRMA);
        rbRMI = v.findViewById(R.id.rbIncTipoRMI);
        cbEstado = v.findViewById(R.id.cbIncEstado);
        etResolucion = v.findViewById(R.id.etIncResolucion);
        tvLatitud = v.findViewById(R.id.tvIncLat);
        tvLongitud = v.findViewById(R.id.tvIncLong);
        ivFoto = v.findViewById(R.id.ivFoto);
        btCancelar = v.findViewById(R.id.btCancelar);
        btAceptar = v.findViewById(R.id.btAceptar);

        //listener

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
                    mInc = new Incidencia();
                    StringBuilder sb = new StringBuilder();
                    Calendar c = GregorianCalendar.getInstance();
                    sb.append("INC-").append(c.get(Calendar.HOUR)).append(c.get(Calendar.MINUTE)).append(c.get(Calendar.SECOND));
                    tvCabecera.setText(getString(R.string.tv_Inc_Cabecera_Crear));
                    etIdDpto.setText(String.valueOf(mLogin.getId()));
                    etDeptoNombre.setText(mLogin.getNombre());
                    etId.setText(sb.toString());
                    etId.setEnabled(false);
                    etIdDpto.setEnabled(false);
                    etDeptoNombre.setEnabled(false);
                    etFecha.setFocusable(false);
                    etFecha.setText(calcularFecha());
                    etResolucion.setEnabled(false);
                    tvLatitud.setText(lat);
                    tvLongitud.setText(lon);
                    mInc.setIdDpto(Integer.parseInt(etIdDpto.getText().toString()));
                    mInc.setId(etId.getText().toString());
                    mInc.setFecha(etFecha.getText().toString());
                    mInc.setDescripcion(etDescripcion.getText().toString());
                    mInc.setDeptoNombre(etDeptoNombre.getText().toString());
                    if (rbRMA.isChecked())
                        mInc.setTipo(Incidencia.Tipo.RMA.name());
                    else
                        mInc.setTipo(Incidencia.Tipo.RMI.name());
                    if (cbEstado.isChecked())
                        mInc.setEstado(1);
                    else
                        mInc.setEstado(0);
                    mInc.setFoto(urlFoto);
                    mInc.setLongitud(tvLongitud.getText().toString());
                    mInc.setLatitud(tvLatitud.getText().toString());
                    break;
                case OP_EDITAR:
                    tvCabecera.setText(getString(R.string.tv_Inc_Cabecera_Editar));
                    etIdDpto.setText(String.valueOf(mInc.getIdDpto()));
                    etIdDpto.setEnabled(false);
                    etDeptoNombre.setText(mInc.getDeptoNombre());
                    etDeptoNombre.setEnabled(false);
                    etId.setText(mInc.getId());
                    etId.setEnabled(false);
                    etFecha.setText(mInc.getFecha());
                    etFecha.setClickable(false);
                    etFecha.setEnabled(false);
                    etDescripcion.setText(mInc.getDescripcion());
                    etDescripcion.setEnabled(false);
                    if (mInc.getTipo().equals(Incidencia.Tipo.RMA.name()))
                        rbRMA.setChecked(true);
                    else
                        rbRMI.setChecked(true);
                    if (mInc.getEstado() == 1)
                        cbEstado.setChecked(true);
                    etResolucion.setText(mInc.getResolucion());
                    tvLatitud.setText(mInc.getLatitud());
                    tvLongitud.setText(mInc.getLongitud());
                    urlFoto=mInc.getFoto();
                    break;
                case OP_VER:
                    tvCabecera.setText(getString(R.string.tv_Inc_Cabecera_Info));
                    etIdDpto.setText(String.valueOf(mInc.getIdDpto()));
                    etIdDpto.setEnabled(false);
                    etDeptoNombre.setText(mInc.getDeptoNombre());
                    etDeptoNombre.setEnabled(false);
                    etId.setText(mInc.getId());
                    etId.setEnabled(false);
                    etFecha.setText(mInc.getFecha());;
                    etFecha.setEnabled(false);
                    etDescripcion.setText(mInc.getDescripcion());
                    etDescripcion.setEnabled(false);
                    if (mInc.getTipo().equals(Incidencia.Tipo.RMA.name()))
                        rbRMA.setChecked(true);
                    else
                        rbRMI.setChecked(true);
                    rbRMA.setEnabled(false);
                    rbRMI.setEnabled(false);
                    if (mInc.getEstado() == 1)
                        cbEstado.setChecked(true);
                    else
                        cbEstado.setChecked(false);
                    cbEstado.setEnabled(false);
                    etResolucion.setText(mInc.getResolucion());
                    etResolucion.setEnabled(false);
                    tvLatitud.setText(mInc.getLatitud());
                    tvLongitud.setText(mInc.getLongitud());
                    urlFoto=mInc.getFoto();
                    Bitmap bitmap= decodeFromFirebaseBase64(urlFoto);
                    ivFoto.setImageBitmap(bitmap);
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

    public Bitmap decodeFromFirebaseBase64(String image)  {
        byte[] decodedByteArray = android.util.Base64.decode(image, 0);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public static String calcularFecha() {
        Calendar fecha = new GregorianCalendar();
        int anio = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH)+1;
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        return String.format("%02d"+"/"+"%02d"+"/"+"%4d",dia,mes,anio);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lg.stopCoor();
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
                        !etDescripcion.getText().toString().equals("")) {

                    // Creamos una nueva Incidencia, independientemente de la operación a realizar!!
                    mInc = new Incidencia();
                    mInc.setIdDpto(Integer.parseInt(etIdDpto.getText().toString()));
                    mInc.setId(etId.getText().toString());
                    mInc.setFecha(etFecha.getText().toString());
                    mInc.setDescripcion(etDescripcion.getText().toString());
                    mInc.setDeptoNombre(etDeptoNombre.getText().toString());
                    if (rbRMA.isChecked())
                        mInc.setTipo(Incidencia.Tipo.RMA.name());
                    else
                        mInc.setTipo(Incidencia.Tipo.RMI.name());
                    if (cbEstado.isChecked())
                        mInc.setEstado(1);
                    else
                        mInc.setEstado(0);
                    mInc.setResolucion(etResolucion.getText().toString());
                    mInc.setLongitud(tvLongitud.getText().toString());
                    mInc.setLatitud(tvLatitud.getText().toString());
                    mInc.setFoto(urlFoto);

                    mListener.onAceptarMtoIncsFrag(mOp, mInc);

                } else {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.msg_FaltanDatosObligatorios, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    };

    public static String string2date(String fecha) {
        String[] trozos = fecha.split("/");
        return trozos[2] + trozos[1] + trozos[0];
    }

    public static String cleanId(String id) {
        return id.substring(4);
    }

    private void trocear() {
        tvLongitud.setText(lon);
        tvLatitud.setText(lat);
    }


}
