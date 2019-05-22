package com.izi.tcccliente.model;

public class LojaRecicleView {

    private String nome;
    private Double preco;
    private String descricao;
    private String imgUrl;

    public LojaRecicleView() {
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