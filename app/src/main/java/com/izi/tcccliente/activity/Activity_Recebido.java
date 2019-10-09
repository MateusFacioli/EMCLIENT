package com.izi.tcccliente.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;


import com.izi.tcccliente.R;
import com.izi.tcccliente.model.Cliente;


public class Activity_Recebido extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextInputEditText boxComentario;
    private Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__recebido);
        inicializarComponentes();
        configurarComponentes();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClienteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void salvarAvaliacao(){
        
    }

    private void inicializarComponentes(){
        ratingBar = findViewById(R.id.ratingBar);
        boxComentario = findViewById(R.id.boxComentario);
        btnOK = findViewById(R.id.btnOk);
    }

    private void configurarComponentes(){

    }
}
