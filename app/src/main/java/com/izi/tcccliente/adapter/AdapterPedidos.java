package com.izi.tcccliente.adapter;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.izi.tcccliente.R;
import com.izi.tcccliente.model.Carrinho;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPedidos extends RecyclerView.Adapter<AdapterPedidos.MyViewHoder> {

    private List<Carrinho> carrinho;
    private AlertDialog alerta;


    public AdapterPedidos(List<Carrinho> carrinhos){
        this.carrinho = carrinhos;
    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pedidos, parent, false);
        return new MyViewHoder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int i) {

        Carrinho carrinhoR = carrinho.get(i);
        holder.nomeProduto.setText(carrinhoR.getProduto().getNome());
        holder.descricao.setText(carrinhoR.getProduto().getDescricao());
        holder.valor.setText("R$     " +carrinhoR.getProduto().getPreco().toString());

        //Carregar imagem
          String urlImagem = carrinhoR.getProduto().getImgUrl();
          Picasso.get().load( urlImagem ).into( holder.imagemEmpresa );


    }



    @Override
    public int getItemCount() {
        return carrinho.size();
    }

    public class MyViewHoder extends RecyclerView.ViewHolder{

        ImageView imagemEmpresa;
        TextView nomeProduto;
        TextView descricao;
        TextView valor;
        Button delete_item;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            imagemEmpresa = itemView.findViewById(R.id.imageProdutoPedido);
            nomeProduto = itemView.findViewById(R.id.txtNomeProdutoPedido);
            descricao = itemView.findViewById(R.id.txtDescricaoPedido);
            valor = itemView.findViewById(R.id.txtPrecoPedido);


        }
    }
}
