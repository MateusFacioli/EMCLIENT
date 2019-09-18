package com.izi.tcccliente.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.izi.tcccliente.R;
import com.izi.tcccliente.adapter.AdapterCarrinho;
import com.izi.tcccliente.adapter.AdapterPedidos;
import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.helper.UsuarioFirebase;
import com.izi.tcccliente.model.Carrinho;
import com.izi.tcccliente.model.LojaRecicleView;

import java.util.ArrayList;
import java.util.List;

public class AcompanharPedidoActivity extends AppCompatActivity {

    private RecyclerView recicleCarrinho;
    private AdapterPedidos adapterPedidos;

    private Toolbar toolbar;
    private List<LojaRecicleView> produtos = new ArrayList<>();
    private List<Carrinho> carrinhos = new ArrayList<>();
    private AlertDialog alerta;

    private FloatingActionButton floatFinalizar;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanhar_pedido);

        inicializarComponentes();
        configuraComponentes();
        recuperarProdutos();

        floatFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Msg_alertas("Deseja finalizar as compras?",1);
     }
        });


    }

    private void Msg_alertas(String texto, int n) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AVISO");
        builder.setMessage(texto);

        switch (n)
        {

            case 1://pedidos
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent qrcode = new Intent(AcompanharPedidoActivity.this, qr_codeGeneratorActivity.class);
                        startActivity(qrcode);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                break;
            case 2://excluir
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Carrinho produtoSelecionado= carrinhos.get(adapterPedidos.getItemViewType());
                        //Cardapio produtoSelecionado = produtos.get(getAdapterPosition());
                        //produtoSelecionado.remover();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                break;


        }

        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
    private void recuperarProdutos(){
        Query produtosRef = mDatabase
                .child("pedido")
                .child(UsuarioFirebase.getDadosUsuarioLogado().getUid());


        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                carrinhos.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    carrinhos.add(ds.getValue(Carrinho.class));

                }


                adapterPedidos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void configuraComponentes(){
        recicleCarrinho.setLayoutManager(new LinearLayoutManager(this));
        recicleCarrinho.setHasFixedSize(true);
        adapterPedidos = new AdapterPedidos(carrinhos);
        recicleCarrinho.setAdapter(adapterPedidos);


        //toolbar.setTitle("Carrinho");
        //setSupportActionBar(toolbar);



    }

    private void inicializarComponentes(){
        recicleCarrinho = findViewById(R.id.reciclePedido);
        mDatabase = ConfiguracaoFirebase.getFirebase();

        floatFinalizar = findViewById(R.id.floatingFinalizar);

        toolbar = findViewById(R.id.tb_acompanha);


    }

}
