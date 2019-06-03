package com.izi.tcccliente.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.izi.tcccliente.R;
import com.izi.tcccliente.adapter.AdapterEmpresa;
import com.izi.tcccliente.adapter.AdapterLoja;
import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.model.ComercianteRecicleView;
import com.izi.tcccliente.model.LojaRecicleView;

import java.util.ArrayList;
import java.util.List;

public class LojaActivity extends AppCompatActivity {

    private RecyclerView recycleLoja;


    private DatabaseReference mDatabase;

    private AdapterLoja adapterLoja;
    private List<LojaRecicleView> loja = new ArrayList<>();

    private Intent iLoja;
    private Bundle bLoja;
    private String idComerciante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loja);
        inicializarComponentes();
        configuraComponentes();
        recuperarProdutos();
        getSupportActionBar().hide();



    }

    private void recuperarProdutos(){
        DatabaseReference produtosRef = mDatabase
                .child("cardapio")
                .child(idComerciante);

        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loja.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    loja.add(ds.getValue(LojaRecicleView.class));
                }

                adapterLoja.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void configuraComponentes(){
        recycleLoja.setLayoutManager(new LinearLayoutManager(this));
        recycleLoja.setHasFixedSize(true);
        adapterLoja = new AdapterLoja(loja);
        recycleLoja.setAdapter(adapterLoja);

        if(bLoja != null){
            idComerciante = bLoja.get("idComerciante").toString();
        }

    }
    private void inicializarComponentes(){
        recycleLoja = findViewById(R.id.recyclerLoja);
        mDatabase = ConfiguracaoFirebase.getFirebase();

        iLoja = getIntent();
        bLoja = iLoja.getExtras();
    }
}
