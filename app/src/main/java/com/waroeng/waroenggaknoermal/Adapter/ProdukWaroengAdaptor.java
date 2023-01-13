package com.waroeng.waroenggaknoermal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.waroeng.waroenggaknoermal.Model.ProdukWaroeng;
import com.waroeng.waroenggaknoermal.R;

import java.util.List;

public class ProdukWaroengAdaptor extends RecyclerView.Adapter<ProdukWaroengAdaptor.MyViewHolder> {

    private Context context;
    private List<ProdukWaroeng> list;
    private Waroeng waroeng;

    public interface Waroeng{
        void onClick(int pos);
    }

    public void setWaroeng(Waroeng waroeng) {
        this.waroeng = waroeng;
    }

    public ProdukWaroengAdaptor(Context context , List<ProdukWaroeng>list){

        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tampil_data, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nama.setText(list.get(position).getNama());
        holder.jenis.setText(list.get(position).getJenis());
        holder.harga.setText(list.get(position).getHarga());
        holder.stok.setText(list.get(position).getStok());
        Glide.with(context).load(list.get(position).getGmbr()).into(holder.gmbr);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nama,jenis,harga,stok;
        ImageView gmbr;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.namapembeliku);
            jenis = itemView.findViewById(R.id.jenis1);
            harga = itemView.findViewById(R.id.hargaproduku);
            stok = itemView.findViewById(R.id.jmlhpembelian);
            gmbr = itemView.findViewById(R.id.tampilgmbr);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (waroeng!=null){
                        waroeng.onClick(getLayoutPosition());

                    }
                }
            });

        }
    }
}
