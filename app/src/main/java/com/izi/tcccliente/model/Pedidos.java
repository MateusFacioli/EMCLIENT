package com.izi.tcccliente.model;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.izi.tcccliente.config.ConfiguracaoFirebase;
import com.izi.tcccliente.helper.UsuarioFirebase;

public class Pedidos implements Serializable {

    private LojaRecicleView produto;
    private Double quantidade;
    private String categoria;
    private String estado;
    private String idPedido;





    public Pedidos() {



        setEstado("Aguardando interação");

    }

    public void salvar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("cliente")
                .child(UsuarioFirebase.getUsuarioAtual().getUid())
                .child("pedidos")
                .child( produto.getIdProduto() );

        setIdPedido(produto.getIdProduto());
        produtoRef.setValue(this);

        DatabaseReference firebaseComerciante = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoComerciante = firebaseComerciante
                .child("empresa")
                .child(UsuarioFirebase.getIdentificadorUsuario())
                .child("pedidos")
                .child( produto.getIdProduto() );

        setIdPedido(produto.getIdProduto());
        produtoComerciante.setValue(this);


    }




    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Exclude
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LojaRecicleView getProduto() {
        return produto;
    }

    public void setProduto(LojaRecicleView produto) {
        this.produto = produto;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
}
