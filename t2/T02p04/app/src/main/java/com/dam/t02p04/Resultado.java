package com.dam.t02p04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.xml.transform.Result;

public class Resultado extends AppCompatActivity {
    private TextView txRes;
    private Button btVolver;
    private double resultado=0;
    private Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        //findbyid
        txRes=findViewById(R.id.txResultado);
        btVolver=findViewById(R.id.btVolver);
        //recibir datos
        datos=getIntent().getExtras();
        resultado=datos.getDouble("resultado");
        //set resultado in txtResultado
        txRes.setText(Double.toString(resultado));
        //listener
        btVolver.setOnClickListener(volver_OnClickListener);
    }

    private View.OnClickListener volver_OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            if(v.getId()==R.id.btVolver) {
                intent.putExtra("resultado", resultado);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        }
    };
}
