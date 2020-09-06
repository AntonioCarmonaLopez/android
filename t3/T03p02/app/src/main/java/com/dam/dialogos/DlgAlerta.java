package com.dam.dialogos;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.dam.t03p02.R;


public class DlgAlerta extends DialogFragment {

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
                ;
            }
        });

        return builder.create();
//        return super.onCreateDialog(savedInstanceState);
    }



}
