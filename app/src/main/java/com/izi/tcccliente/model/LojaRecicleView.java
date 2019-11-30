package com.izi.tcccliente.model;

import java.io.Serializable;

public class LojaRecicleView implements Serializable {

    private String nome;
    private Double preco;
    private String descricao;
    private String imgUrl;
    private String idProduto;

    public LojaRecicleView() {
    }


    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String itens) {
        this.descricao = itens;
    }
}
