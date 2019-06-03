package com.izi.tcccliente.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.izi.tcccliente.R;
import com.izi.tcccliente.adapter.AdapterCarrinho;
import com.izi.tcccliente.adapter.AdapterLoja;
import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.helper.UsuarioFirebase;
import com.izi.tcccliente.model.Carrinho;
import com.izi.tcccliente.model.Cliente;
import com.izi.tcccliente.model.ComercianteRecicleView;
import com.izi.tcccliente.model.LojaRecicleView;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity {

    private Intent iCarrinho;
    private Bundle bCarrinho;

    private RecyclerView recicleCarrinho;
    private AdapterCarrinho adapterCarrinho;

    private DatabaseReference mDatabase;

    private String idProduto;
    private String idComerciante;

    private Carrinho carrinho = new Carrinho();
    private List<LojaRecicleView> loja = new ArrayList<>();
    private List<Carrinho> carrinhos = new ArrayList<>();

    private FloatingActionButton floatPedido;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        inicializarComponentes();
        configuraComponentes();
        recuperarProdutos();

        floatPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(carrinho != null) {
                    for (int i = 0; i <= carrinhos.size(); i++) {
                        carrinho = carrinhos.get(i);
                        carrinho.salvarPedido();
                    }
                    DatabaseReference carrinhoDeletar = FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .getRoot()
                            .child("carrinho")
                            .child(UsuarioFirebase.getDadosUsuarioLogado().getUid());

                    carrinhoDeletar.removeValue();
                }



            }
        });


    }



    private void recuperarProdutos(){
        Query produtosRef = mDatabase
                .child("carrinho")
                .child(UsuarioFirebase.getDadosUsuarioLogado().getUid());


        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                carrinhos.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    carrinhos.add(ds.getValue(Carrinho.class));

                }




                adapterCarrinho.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void configuraComponentes(){
        recicleCarrinho.setLayoutManager(new LinearLayoutManager(this));
        recicleCarrinho.setHasFixedSize(true);
        adapterCarrinho = new AdapterCarrinho(carrinhos);
        recicleCarrinho.setAdapter(adapterCarrinho);

        if(bCarrinho != null){
            idComerciante = bCarrinho.get("idComerciante").toString();
            idProduto = bCarrinho.get("uidProduto").toString();
        }

    }
    private void inicializarComponentes(){
        recicleCarrinho = findViewById(R.id.recyclerCarrinho);
        mDatabase = ConfiguracaoFirebase.getFirebase();

        iCarrinho = getIntent();
        bCarrinho = iCarrinho.getExtras();

        floatPedido = findViewById(R.id.floatingPedido);
    }
}
