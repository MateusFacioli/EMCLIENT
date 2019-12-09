package com.izi.tcccliente.activity;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import  com.izi.tcccliente.R;

import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.helper.UsuarioFirebase;
import com.izi.tcccliente.model.Pedidos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VisualizarPedidoActivity extends AppCompatActivity {

    private Intent iPedido;
    private Bundle bPedido;

    private String idPedido;


    private FirebaseAuth autenticacao;
    private DatabaseReference firebaseRef;
    private String idUsuarioLogado;

    private TextView txtDate;
    private TextView txtNome;
    private TextView txtItemDoPedido;
    private TextView txtQuantidadePedido;
    private TextView txtValor;
    private ImageView imageView;



    private List<Pedidos> pedidos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizar_pedidos);
        inicializarComponentes();

        if(bPedido != null){
            idPedido = bPedido.get("idPedido").toString();

        }
        recuperarPedidos();
    }

    private void recuperarPedidos() {

        DatabaseReference databaseReference=firebaseRef.child("pedidos");


        Query qDatabaseReference = databaseReference.orderByChild("idpedido").equalTo(idPedido);

        qDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pedidos.clear();

                Pedidos pedido = new Pedidos();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //if(ds.getValue(Pedidos.class).getComerciante().getUid().equals(idComerciante))

                    pedidos.add(ds.getValue(Pedidos.class));
                }

                pedido = pedidos.get(0);

                //txtDate.setText(new Date().toString()); ? nao pode ser a data atual
                //txtNome.setText();
                txtItemDoPedido.setText(pedido.getProduto().getNome());
                txtQuantidadePedido.setText("1");
                txtValor.setText(pedido.getProduto().getPreco().toString());

                String urlImagem = pedidos.get(0).getProduto().getImgUrl();
                Picasso.get().load( urlImagem ).into( imageView );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void inicializarComponentes(){

        autenticacao = ConfiguracaoFirebase.getFirebaseInstance();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getDadosUsuarioLogado().getUid();

        iPedido = getIntent();
        bPedido = iPedido.getExtras();

        txtDate = findViewById(R.id.txtDate);
        txtNome = findViewById(R.id.txtNome);
        txtItemDoPedido = findViewById(R.id.txtItemDoPedido);
        txtQuantidadePedido = findViewById(R.id.txtQuantidadePedido);
        txtValor = findViewById(R.id.txtValor);
        imageView = findViewById(R.id.imgVisualizarPedidos);



    }
}
