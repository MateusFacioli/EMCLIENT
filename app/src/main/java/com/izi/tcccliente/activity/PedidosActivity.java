package com.izi.tcccliente.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.izi.tcccliente.R;
import com.izi.tcccliente.adapter.AdapterEmpresa;
import com.izi.tcccliente.adapter.AdapterPedidos;
import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.helper.UsuarioFirebase;
import com.izi.tcccliente.model.Carrinho;
import com.izi.tcccliente.model.ComercianteRecicleView;

import java.util.ArrayList;
import java.util.List;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos123;
    private AdapterPedidos adapterPedidos;
    private List<Carrinho> carrinhos = new ArrayList<>();
    private Intent iPedidos;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        inicializarComponentes();
        configurarComponentes();
        recuperarPedidos();
    }



    private void recuperarPedidos(){
        carrinhos.clear();
        Query produtosRef = mDatabase
                .child("cliente")
                .child(UsuarioFirebase.getDadosUsuarioLogado().getUid())
                .child("pedidos");


        produtosRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        carrinhos.add(ds.getValue(Carrinho.class));

                    }

                    adapterPedidos.notifyDataSetChanged();
                    }



                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    private void configurarComponentes(){
       //carrinhos = (List<Carrinho>) iPedidos.getSerializableExtra("carrinhos");
        recyclerPedidos123.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos123.setHasFixedSize(true);
        adapterPedidos = new AdapterPedidos(carrinhos);
        recyclerPedidos123.setAdapter(adapterPedidos);
    }

    private void inicializarComponentes(){

      //  iPedidos = getIntent();
        recyclerPedidos123 = findViewById(R.id.reciclerPedidos123);
        mDatabase = ConfiguracaoFirebase.getFirebase();

    }
}
