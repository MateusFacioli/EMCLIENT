package com.izi.tcccliente.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.izi.tcccliente.R;
import com.izi.tcccliente.adapter.AdapterEmpresa;
import com.izi.tcccliente.adapter.AdapterLoja;
import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.helper.UsuarioFirebase;
import com.izi.tcccliente.listener.RecyclerItemClickListener;
import com.izi.tcccliente.model.Carrinho;
import com.izi.tcccliente.model.Cliente;
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

    private List<LojaRecicleView> lojaCarrinho = new ArrayList<>();
    private List<ComercianteRecicleView> comeciante = new ArrayList<>();
    private List<Cliente> usuario = new ArrayList<>();
    private Carrinho carrinho = new Carrinho();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loja);
        inicializarComponentes();
        configuraComponentes();
        recuperarProdutos();
        recuperarComerciante();
        recuperarUsuario();
        getSupportActionBar().hide();


        recycleLoja.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recycleLoja,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent lojaIntent = new Intent(LojaActivity.this, CarrinhoActivity.class);
                                LojaRecicleView produtoSelecionado = loja.get(position);

                                String uidProduto = produtoSelecionado.getIdProduto();
                                lojaIntent.putExtra("uidProduto", uidProduto);
                                lojaIntent.putExtra("idComerciante", idComerciante);
                                carrinho.setProduto(loja.get(position));
                                carrinho.salvar();
                                startActivity(lojaIntent);



                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    private void recuperarComerciante(){
        Query comercianteRef = mDatabase
                .child("comerciante")
                .orderByChild("uid").equalTo(idComerciante);

        comercianteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comeciante.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    comeciante.add(ds.getValue(ComercianteRecicleView.class));


                }

                carrinho.setComerciante(comeciante.get(0));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void recuperarUsuario(){
        Query usuarioRef = mDatabase
                .child("cliente")
                .orderByChild("uid").equalTo(UsuarioFirebase.getDadosUsuarioLogado().getUid());
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    usuario.add(ds.getValue(Cliente.class));
                }

                carrinho.setCliente(usuario.get(0));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
