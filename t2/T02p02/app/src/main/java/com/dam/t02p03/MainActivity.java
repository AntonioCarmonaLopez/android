package com.dam.t02p03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText etDni, etNombre;
    private RadioButton rbHombre, rbMujer;
    private CheckBox chMayor;
    private Button btAceptar, btCancelar;

    final String obligatorios="Faltan datos obligatorios";
    String sexo,mayor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre=findViewById(R.id.etNombre);
        etDni=findViewById(R.id.etDni);
        rbHombre=findViewById(R.id.rbHombre);
        rbMujer=findViewById(R.id.rbMujer);
        chMayor=findViewById(R.id.chMayor);
        btAceptar=findViewById(R.id.btAceptar);
        btCancelar=findViewById(R.id.btCanclelar);
        rbHombre.setChecked(true);
        rbMujer.setChecked(false);
        chMayor.setChecked(false);

        final StringBuilder sb=new StringBuilder();

        btAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNombre.getText().toString().equals("") || etDni.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), obligatorios, Toast.LENGTH_SHORT).show();
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

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNombre.setText("");
                etDni.setText("");
                rbHombre.setChecked(true);
                rbMujer.setChecked(false);
                chMayor.setChecked(false);
            }
        });
    }
}
