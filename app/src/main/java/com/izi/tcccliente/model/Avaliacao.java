package com.izi.tcccliente.model;

import java.io.Serializable;

public class Avaliacao implements Serializable {

    private String comentario;
    private Float avaliacao;



    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Float getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Float avaliacao) {
        this.avaliacao = avaliacao;
    }
}
