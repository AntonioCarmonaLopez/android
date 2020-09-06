package com.dam.t02p04;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etNum1,etNum2;
    private Button btSuma,btResta,btMulti,btDiv,btLimpiar,btSalir;
    private final int ID_APP=1;
    private double resultado;
    private View view;
    private boolean lanzar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //findbyid
        etNum1=findViewById(R.id.etNum1);
        etNum2=findViewById(R.id.etNum2);
        btSuma=findViewById(R.id.btSuma);
        btResta=findViewById(R.id.btResta);
        btMulti=findViewById(R.id.btmulti);
        btDiv=findViewById(R.id.btDiv);
        btLimpiar=findViewById(R.id.btLimpiar);
        btSalir=findViewById(R.id.btCerrar);

        //listener

            //click
        btSuma.setOnClickListener(operaciones_OnClickListener);
        btResta.setOnClickListener(operaciones_OnClickListener);
        btMulti.setOnClickListener(operaciones_OnClickListener);
        btDiv.setOnClickListener(operaciones_OnClickListener);
        btLimpiar.setOnClickListener(operaciones_OnClickListener);
        btSalir.setOnClickListener(operaciones_OnClickListener);

            //longclick
        btSuma.setOnLongClickListener(operaciones_OnLongClickListener);
        btResta.setOnLongClickListener(operaciones_OnLongClickListener);
        btMulti.setOnLongClickListener(operaciones_OnLongClickListener);
        btDiv.setOnLongClickListener(operaciones_OnLongClickListener);
        btLimpiar.setOnLongClickListener(operaciones_OnLongClickListener);
        btSalir.setOnLongClickListener(operaciones_OnLongClickListener);
    }

    private View.OnClickListener operaciones_OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btSuma:
                    if(check()) {
                        resultado = Double.parseDouble(etNum1.getText().toString()) + Double.parseDouble(etNum2.getText().toString());
                        lanzar=true;
                        break;
                    }
                case R.id.btResta:
                    if(check()) {
                        resultado = Double.parseDouble(etNum1.getText().toString()) - Double.parseDouble(etNum2.getText().toString());
                        lanzar=true;
                        break;
                    }
                case R.id.btmulti:
                    if(check()) {
                        resultado = Double.parseDouble(etNum1.getText().toString()) * Double.parseDouble(etNum2.getText().toString());
                        lanzar=true;
                        break;
                    }
                case R.id.btDiv:
                    if(check()) {
                        resultado = Double.parseDouble(etNum1.getText().toString()) - Double.parseDouble(etNum2.getText().toString());
                        lanzar=true;
                        break;
                    }
                case R.id.btLimpiar:
                        etNum1.setText("");
                        etNum2.setText("");
                    lanzar=false;
                        break;
                case R.id.btCerrar:
                    finish();
                    lanzar=false;
                        break;
            }
            if(lanzar)
                lanzarResultado(v,ID_APP);
        }
    };

    private View.OnLongClickListener operaciones_OnLongClickListener=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()){
                case R.id.btSuma:
                    if(check()) {
                        resultado = Double.parseDouble(etNum1.getText().toString()) + Double.parseDouble(etNum2.getText().toString());
                        break;
                    }
                case R.id.btResta:
                    if(check()) {
                        resultado = Double.parseDouble(etNum1.getText().toString()) - Double.parseDouble(etNum2.getText().toString());
                        break;
                    }
                case R.id.btmulti:
                    if(check()) {
                        resultado = Double.parseDouble(etNum1.getText().toString()) * Double.parseDouble(etNum2.getText().toString());
                        break;
                    }
                case R.id.btDiv:
                    if(check()) {
                        resultado = Double.parseDouble(etNum1.getText().toString()) - Double.parseDouble(etNum2.getText().toString());
                        break;
                    }
                case R.id.btLimpiar:
                    etNum1.setText("");
                    etNum2.setText("");
                    break;
                case R.id.btCerrar:
                    finish();
                    break;
            }
            lanzarResultado(v,ID_APP);
            return true;
        }
    };

    private boolean check(){
        if(etNum1.getText().toString().isEmpty() || etNum2.getText().toString().isEmpty()){
            Toast.makeText(this,getResources().getText(R.string.obligatorios),Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void lanzarResultado(View v,int id){
        Intent intent=new Intent(this,Resultado.class);
        intent.putExtra("resultado",resultado);
        startActivityForResult(intent,id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ID_APP && resultCode == Activity.RESULT_OK){
            resultado=data.getExtras().getDouble("resultado");
            Toast.makeText(this,Double.toString(resultado),Toast.LENGTH_LONG).show();
        }  else
            Toast.makeText(this, getResources().getString(R.string.cancel), Toast.LENGTH_LONG).show();
    }
}
