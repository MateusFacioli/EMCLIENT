package com.izi.tcccliente.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.izi.tcccliente.R;
import com.izi.tcccliente.activity.ClienteActivity;
import com.izi.tcccliente.model.ComercianteRecicleView;
import com.squareup.picasso.Picasso;

import java.util.List;




public class AdapterEmpresa extends RecyclerView.Adapter<AdapterEmpresa.MyViewHolder> {

    private List<ComercianteRecicleView> empresas;

    public AdapterEmpresa(List<ComercianteRecicleView> empresas) {
        this.empresas = empresas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_restaurante, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        ComercianteRecicleView empresa = empresas.get(i);
        holder.nomeEmpresa.setText(empresa.getNome());

        //Carregar imagem
      //  String urlImagem = empresa.getUrlImagem();
      //  Picasso.get().load( urlImagem ).into( holder.imagemEmpresa );

    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView nomeEmpresa;


        public MyViewHolder(View itemView) {
            super(itemView);

            nomeEmpresa = itemView.findViewById(R.id.textRestaurante);

        }
    }
}
