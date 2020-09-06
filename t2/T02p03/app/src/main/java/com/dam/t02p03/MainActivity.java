package com.dam.t02p03;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView etDni,etNombre;
    private RadioButton rbHombre,rbMujer;
    private CheckBox chMayor;
    private Button btAceptar,btCancelar;
    private String cadena;
    private ImageView imCheck;
    private int imgok;
    private int imgko;

    final StringBuilder sb=new StringBuilder();
    String sexo,mayor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDni=findViewById(R.id.etDni);
        etNombre=findViewById(R.id.etNombre);
        rbHombre=findViewById(R.id.rdHombre);
        rbMujer=findViewById(R.id.rdMujer);
        chMayor=findViewById(R.id.chMayor);
        btAceptar=findViewById(R.id.btAceptar);
        btCancelar=findViewById(R.id.btCancelar);
        rbHombre.setChecked(true);
        chMayor.setChecked(false);
        imCheck=findViewById(R.id.imCheck);
        imgok=R.drawable.ok;
        imgko=R.drawable.ko;



        btAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNombre.getText().toString().equals("") || etDni.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.obligatorios, Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if(rbHombre.isChecked()) {
                        sexo=getResources().getString(R.string.hombre);
                    } else
                        sexo= getResources().getString(R.string.mujer);
                    if(chMayor.isChecked())
                        mayor=getResources().getString(R.string.mayor);
                    else
                        mayor=getResources().getString(R.string.menor);
                }
                sb.append(etNombre.getText().toString()).append(" ").append(etDni.getText().toString())
                        .append(" ").append(sexo).append(" ").append(mayor);
                Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
            }
        });

        etDni.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if((!hasFocus) && (etDni.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(), R.string.nombreObligatorio, Toast.LENGTH_LONG).show();
                    imCheck.setImageResource(imgko);
                    etDni.requestFocus();
                }

            }
        });

        etNombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if((!hasFocus) && (etNombre.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(), R.string.dniObligatorio, Toast.LENGTH_LONG).show();
                    imCheck.setImageResource(imgko);
                    etNombre.requestFocus();
                }
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDni.setText("");
                etNombre.setText("");
                rbHombre.setChecked(true);
                chMayor.setChecked(false);
                etDni.requestFocus();
            }
        });
    }
}
