package com.waroeng.waroenggaknoermal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waroeng.waroenggaknoermal.Model.PembeliWaroeng;
import com.waroeng.waroenggaknoermal.R;

import java.util.List;

public class PembeliWaroengAdaptor extends RecyclerView.Adapter<PembeliWaroengAdaptor.MyViewHolder> {

    private Context context;
    private List<PembeliWaroeng> list;
    private Pembeli pembeli;

    public interface Pembeli{
        void onClick(int pos);
    }

    public void setWaroeng(Pembeli pembeli) {
        this.pembeli = pembeli;
    }

    public PembeliWaroengAdaptor(Context context , List<PembeliWaroeng>list){

        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pembeli_waroeng, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.namapembeli.setText(list.get(position).getNamapembeliku());
        holder.nama.setText(list.get(position).getNamaku());
        holder.harga.setText(list.get(position).getHargaku());
        holder.tanggalku.setText(list.get(position).getTanggalku());
        holder.jumlahbeli.setText(list.get(position).getJumlahbeli());
        holder.hargabayar.setText(list.get(position).getHargabayarku());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama, namapembeli, harga, tanggalku, jumlahbeli, hargabayar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            namapembeli = itemView.findViewById(R.id.namapembeliku);
            nama = itemView.findViewById(R.id.namabarang);
            harga = itemView.findViewById(R.id.hargaproduku);
            tanggalku = itemView.findViewById(R.id.tanggall);
            jumlahbeli = itemView.findViewById(R.id.jmlhpembelian);
            hargabayar = itemView.findViewById(R.id.bayarku);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pembeli != null) {
                        pembeli.onClick(getLayoutPosition());

                    }
                }

            });
        }
    }
}
