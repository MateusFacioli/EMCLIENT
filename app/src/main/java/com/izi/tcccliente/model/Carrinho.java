package com.izi.tcccliente.model;

import com.google.firebase.database.DatabaseReference;
import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.helper.UsuarioFirebase;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {

    private ComercianteRecicleView comerciante;
    private Cliente cliente;
    private LojaRecicleView produto;
    private String idpedido;
    private String status;




    public void salvar(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference = database.child("carrinho")
                .child(UsuarioFirebase.getDadosUsuarioLogado().getUid())
                .child(getProduto().getIdProduto());

        reference.setValue(this);
    }

    public void salvarPedido(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference = database.child("pedido")
                .child(UsuarioFirebase.getDadosUsuarioLogado().getUid())
                .child(getProduto().getIdProduto());

        reference.setValue(this);

    }

    public void removerCarrinho(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference reference = database.child("carrinho")
                .child(UsuarioFirebase.getDadosUsuarioLogado().getUid())
                .child(getProduto().getIdProduto());

        reference.removeValue();

    }


    public Carrinho() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("pedidos");
        setIdpedido( produtoRef.push().getKey() );
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
