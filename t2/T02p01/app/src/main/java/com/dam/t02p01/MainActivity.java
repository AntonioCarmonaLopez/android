package com.dam.t02p01;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txPrincipal;
    private Button bt1,bt2,bt3,bt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(getString(R.string.app_name), getString(R.string.msg_onCreate));
        setContentView(R.layout.activity_main);
        txPrincipal=findViewById(R.id.txPrincipal);
        bt1=findViewById(R.id.bt1);
        bt2=findViewById(R.id.bt2);
        bt3=findViewById(R.id.bt3);
        bt4=findViewById(R.id.bt4);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txPrincipal.setText(R.string.msg_bt2);
            }
        });
        bt3.setOnClickListener(bt3_OnClickListener);
        bt4.setOnClickListener(new bt4_OnClickListener());
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==bt1.getId())
            txPrincipal.setText(R.string.msg_bt1);
    }

    private View.OnClickListener bt3_OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            txPrincipal.setText(R.string.msg_bt3);
        }
    };

    private class bt4_OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            txPrincipal.setText(R.string.msg_bt4);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(getString(R.string.app_name), getString(R.string.msg_onStart));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(getString(R.string.app_name), getString(R.string.msg_onResume));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(getString(R.string.app_name), getString(R.string.msg_onPause));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(getString(R.string.app_name), getString(R.string.msg_onStop));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(getString(R.string.app_name), getString(R.string.msg_onDestroy));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(getString(R.string.app_name), getString(R.string.msg_onRestart));
    }
}
