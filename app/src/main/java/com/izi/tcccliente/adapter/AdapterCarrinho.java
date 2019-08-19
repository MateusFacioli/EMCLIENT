package com.izi.tcccliente.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.izi.tcccliente.R;
import com.izi.tcccliente.model.Carrinho;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterCarrinho extends RecyclerView.Adapter<AdapterCarrinho.MyViewHolder> {

    private List<Carrinho> carrinho = new ArrayList<>();


    public AdapterCarrinho(List<Carrinho> carrinhos){
        this.carrinho = carrinhos;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loja, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Carrinho carrinhoR = carrinho.get(i);
        holder.nomeProduto.setText(carrinhoR.getProduto().getNome());
        holder.descicao.setText(carrinhoR.getProduto().getDescricao());
        holder.valor.setText(carrinhoR.getProduto().getPreco().toString());

        //Carregar imagem
        String urlImagem = carrinhoR.getProduto().getImgUrl();
        Picasso.get().load( urlImagem ).into( holder.imagemEmpresa );

    }

    @Override
    public int getItemCount() {
        return carrinho.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imagemEmpresa;
        TextView nomeProduto;
        TextView descicao;
        TextView valor;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeProduto = itemView.findViewById(R.id.txtNomeProdutoPedido);
            descicao = itemView.findViewById(R.id.txtDescricaoPedido);
            valor = itemView.findViewById(R.id.txtPrecoPedido);
            imagemEmpresa = itemView.findViewById(R.id.imageProdutoPedido);
        }
    }
}
