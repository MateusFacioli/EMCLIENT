package com.izi.tcccliente.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.izi.tcccliente.R;
import com.izi.tcccliente.model.LojaRecicleView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterLoja extends RecyclerView.Adapter<AdapterLoja.MyViewHolder> {

    private List<LojaRecicleView> loja;
   // private String context;

    public AdapterLoja(List<LojaRecicleView>lojas){
        this.loja = lojas;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_loja, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        LojaRecicleView lojaR = loja.get(i);
        holder.nomeProduto.setText(lojaR.getNome());
        holder.descricao.setText(lojaR.getDescricao());
        holder.valor.setText("R$     " +lojaR.getPreco().toString());

        String urlImagem = lojaR.getImgUrl();
        Picasso.get().load( urlImagem ).into( holder.imagemEmpresa );

    }

    @Override
    public int getItemCount() {
        return loja.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imagemEmpresa;
        TextView nomeProduto;
        TextView descricao;
        TextView valor;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagemEmpresa = itemView.findViewById(R.id.imageProdutoPedido);
            nomeProduto = itemView.findViewById(R.id.txtNomeProdutoPedido);
            descricao = itemView.findViewById(R.id.txtDescricaoPedido);
            valor = itemView.findViewById(R.id.txtPrecoPedido);
        }
    }
}
