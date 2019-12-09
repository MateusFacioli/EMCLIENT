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


            if(empresa.getReserva() != null)//empresas.get(j).getReserva() nao foi
            {   //Avaliacao avaliacao= new Avaliacao();
                holder.aval.setText(empresa.getAvaliacao().getComentario()+" "+empresa.getAvaliacao().getAvaliacao());
                holder.disp.setText(empresa.getReserva().getDate()+" "+empresa.getReserva().getTimeInicio()+" - "+empresa.getReserva().getTimeFinal());
            }
            else
            {

                holder.aval.setText("Esse comerciante ainda não tem avaliações");
                holder.disp.setText("Horário aberto para prestação dos seus serviços");
            }




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
        TextView aval;
        TextView disp;



        public MyViewHolder(View itemView) {
            super(itemView);

            nomeEmpresa = itemView.findViewById(R.id.textRestaurante);
            aval= itemView.findViewById(R.id.aval);
            disp=itemView.findViewById(R.id.disponibilidade);




        }
    }
}
