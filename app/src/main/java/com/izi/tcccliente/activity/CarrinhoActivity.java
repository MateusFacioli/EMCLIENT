package com.izi.tcccliente.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.izi.tcccliente.R;
import com.izi.tcccliente.adapter.AdapterCarrinho;
import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.helper.UsuarioFirebase;
import com.izi.tcccliente.listener.RecyclerItemClickListener;
import com.izi.tcccliente.model.Carrinho;
import com.izi.tcccliente.model.LojaRecicleView;
import com.izi.tcccliente.service.CheckoutActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity {

    private Intent iCarrinho;
    private Bundle bCarrinho;

    private RecyclerView recicleCarrinho;
    private AdapterCarrinho adapterCarrinho;

    private DatabaseReference mDatabase;

    private String idProduto;// preciso
    private String idComerciante;// preciso

    private Carrinho carrinho = new Carrinho();
    private List<LojaRecicleView> loja = new ArrayList<>();
    private List<Carrinho> carrinhos = new ArrayList<>();
    private FloatingActionButton floatPedido;

    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);// aqui chama activty que tem que chamar um content
        inicializarComponentes();
        configuraComponentes();
        recuperarCarrinho();


        recicleCarrinho.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recicleCarrinho,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, final int position) {


                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );



        floatPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(carrinho != null && carrinhos.size()>0)
                {
                    int i;
                    for (i=0 ; i < carrinhos.size(); i++)
                    {
                        carrinho = carrinhos.get(i);
                       // carrinho.salvarPedido();
                        carrinho.removerCarrinho();

                    }

                    Bundle bundle = new Bundle();
                    Intent checkOut = new Intent(CarrinhoActivity.this, CheckoutActivity.class);
                    bundle.putSerializable("carrinho", (Serializable) carrinhos);
                    checkOut.putExtras(bundle);
                    startActivity(checkOut);
                    finish();
                  //  Class<LojaActivity> loja = LojaActivity.class;

                }
                else
                    {
                        Toast.makeText(CarrinhoActivity.this, "Adicione algo no carrinho para prosseguir", Toast.LENGTH_SHORT).show();
                    }


            }
        });


    }



    private void recuperarCarrinho(){
        Query produtosRef = mDatabase
                .child("carrinho")
                .child(UsuarioFirebase.getDadosUsuarioLogado().getUid());// recuperar com status carrinho


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

        toolbar.setTitle("Carrinho");
        setSupportActionBar(toolbar);



    }
    private void inicializarComponentes(){
        recicleCarrinho = findViewById(R.id.recyclerCarrinho);
        mDatabase = ConfiguracaoFirebase.getFirebase();

        iCarrinho = getIntent();
        bCarrinho = iCarrinho.getExtras();

        floatPedido = findViewById(R.id.floatingFinalizar);

        toolbar = findViewById(R.id.toolbar2);

    }
}
