package com.izi.tcccliente.model;

import com.google.firebase.database.DatabaseReference;
import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.helper.UsuarioFirebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Carrinho {

    private ComercianteRecicleView comerciante;
    private Cliente cliente;
    private LojaRecicleView produto;
    private String idpedido;
    private String status;
    private String dataX;


    public Carrinho(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("pedidos");
        setIdpedido( produtoRef.push().getKey() );

        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        setDataX(formataData.format(data));
    }



    public void salvar(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference = database.child("carrinho")
                .child(UsuarioFirebase.getDadosUsuarioLogado().getUid())
                .child(getProduto().getIdProduto());

        reference.setValue(this);
    }

    public void salvarPedido(){
        DatabaseReference database3 = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference3 = database3
                .child("pedidos")
                .child(getProduto().getIdProduto());

        reference3.setValue(this);

        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference = database
                .child("cliente")
                .child(UsuarioFirebase.getDadosUsuarioLogado().getUid())
                .child("pedidos")
                .child(getProduto().getIdProduto());

        reference.setValue(this);

        DatabaseReference database1 = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference1 = database1
                .child("comerciante")
                .child(comerciante.getUid())
                .child("pedidos")
                .child(getProduto().getIdProduto());

        reference1.setValue(this);

    }

    public void removerCarrinho(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference = database.child("carrinho")
                .child(UsuarioFirebase.getDadosUsuarioLogado().getUid())
                .child(getProduto().getIdProduto());

        reference.removeValue();

    }



    public String getDataX() {
        return dataX;
    }

    public void setDataX(String dataX) {
        this.dataX = dataX;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(String idpedido) {
        this.idpedido = idpedido;
    }

    public ComercianteRecicleView getComerciante() {
        return comerciante;
    }

    public void setComerciante(ComercianteRecicleView comerciante) {
        this.comerciante = comerciante;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LojaRecicleView getProduto() {
        return produto;
    }

    public void setProduto(LojaRecicleView produto) {
        this.produto = produto;
    }
}
