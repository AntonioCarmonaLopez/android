package com.dam.dialogos;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.dam.t03p02.R;

public class DlgConfirmacion extends DialogFragment {

    private int titulo;
    private int mensaje;

    public void setTitulo(int titulo) {
        this.titulo = titulo;
    }

    public void setMensaje(int mensaje) {
        this.mensaje = mensaje;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDlgConfirmacionPositiveClick(DlgConfirmacion.this);
            }
        });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDlgConfirmacionNegativeClick(DlgConfirmacion.this);
            }
        });

        return builder.create();
//        return super.onCreateDialog(savedInstanceState);
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface DlgConfirmacionListener {
        void onDlgConfirmacionPositiveClick(DialogFragment dialog);

        void onDlgConfirmacionNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    private DlgConfirmacionListener mListener;

    // Override the Fragment.onAttach() method to instantiate the DlgConfirmacionListener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the DlgConfirmacionListener so we can send events to the host
            mListener = (DlgConfirmacionListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString() + " must implement DlgConfirmacionListener");
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the DlgConfirmacionListener so we can send events to the host
//            mListener = (DlgConfirmacionListener) activity;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(activity.toString() + " must implement DlgConfirmacionListener");
//        }
//    }

    /* IMPORTANTE !!
     *
     * EN LA DOCUMENTACIÓN DE ANDROID DEVELOPER REALIZA LOS EJEMPLOS CON LA BIBLIOTECA DE COMPATIBILIDAD,
     * YA QUE LOS DIÁLOGOS APARECIERON A PARTIR DE LA API11, DE ESTA MANERA, FUNCIONARÍA TB HACIA ATRÁS.
     * PARA ELLO UTILIZA android.support.v4.app.DialogFragment.
     * YO AQUÍ HE UTILIZADO LA BIBLIOTECA DE COMPATIBILIDAD android.support.v4.app.DialogFragment NO EL SDK, EL PROBLEMA
     * ESTÁ EN EL MÉTODO onAttach, UNO CON context Y OTRO CON activity, ESTE ÚLTIMO ESTÁ DEPRECATED PERO EL
     * OTRO NO FUNCIONA A MENOS QUE UTILICES LA BIBLIOTECA DE COMPATIBILIDAD, UN POCO RARO.
     * TB EL MÉTODO getFragmentManager() CAMBIA A getSupportFragmentManager() SI USAMO LA BIBLIOTECA, INCLUSO
     * LOS MÉTODOS SOBREESCRITOS POR LOS IMPLEMENTS.
     * */

}
