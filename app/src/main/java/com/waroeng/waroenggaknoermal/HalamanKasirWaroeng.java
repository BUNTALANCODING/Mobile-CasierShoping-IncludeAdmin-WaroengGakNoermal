package com.waroeng.waroenggaknoermal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.waroeng.waroenggaknoermal.Adapter.ProdukWaroengAdaptor;
import com.waroeng.waroenggaknoermal.Model.ProdukWaroeng;


import java.util.ArrayList;
import java.util.List;

public class HalamanKasirWaroeng extends AppCompatActivity {
    //    Koneksi database unutk mengambil data untuk ditampilkan pada halaman report
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    //    Digunakan untuk menampilkan user yang login dan untuk membuat fungsi kembali.
    private ImageView kembali;
    private TextView textName;


    //    Koneksi untuk mendapatkan user yang sedang login.
    private FirebaseUser firebaseUser;


    //    Mengambil list data yang diambil.
    private List < ProdukWaroeng > list = new ArrayList < > ();
    private ProdukWaroengAdaptor produkwaroengadapter;
    private RecyclerView recyclerView;


    //    Membuat tampilan loding ketika user menekan button tampil report pada halaman menu admin.
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        Menentukan layout yang akan digunakan.
        setContentView(R.layout.halaman_kasir_waroeng);
        //        Untuk menampilkan nama user yang login pada id yang terdapat pada halaman.
        textName = findViewById(R.id.namaadmin);



        //        Digunakan untuk memilih layout yang digunakan untuk menampilkan data dari datbase.
        recyclerView = findViewById(R.id.recwar);



        //        Membuat tampilan loding pada halaman ketika User memasuki halaman report waroeng.
        progressDialog = new ProgressDialog(HalamanKasirWaroeng.this);
        progressDialog.setTitle("Loding");
        progressDialog.setMessage("Mengambil data");



        //        Melakukan koneksi untuk mengetahui user yang melakukan login dan untuk melakukan logout user.
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        //        Untuk mengetahui user yang melakukan login yang nanti nya username user akan di tampilkan dihalaman tampilan UI.
        if (firebaseUser != null) {
            //            Mengambil user yang melakukan login.
            textName.setText(firebaseUser.getDisplayName());

        } else {
            Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show();
        }




        //        Mengatur fungsi kembali ketika user menekan icon kembali.
        kembali = findViewById(R.id.kembaliku);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HalamanKasirWaroeng.this, HalamanPilihanWaroeng.class);
                startActivity(intent);
            }
        });

        //        Digunakan mengatur layout ketika data di tampilkan.
        produkwaroengadapter = new ProdukWaroengAdaptor(getApplicationContext(), list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(produkwaroengadapter);


//        Baris code digunakan untuk menjalankan fungsi dimana user akan memilih produk untuk dibeli.
        produkwaroengadapter.setWaroeng(new ProdukWaroengAdaptor.Waroeng() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {
                        "Beli Produk ?",
                        "Lihat Produk Lainnya"
                };
                AlertDialog.Builder waroengs = new AlertDialog.Builder(HalamanKasirWaroeng.this);
                waroengs.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:

                                Intent intent = new Intent(getApplicationContext(), HalamanInputKasirWaroeng.class);
                                intent.putExtra("id", list.get(pos).getId());
                                intent.putExtra("Nama_Produk", list.get(pos).getNama());
                                intent.putExtra("Jenis_Produk", list.get(pos).getJenis());
                                intent.putExtra("Harga_Produk", list.get(pos).getHarga());
                                intent.putExtra("Stok_Produk", list.get(pos).getStok());
                                intent.putExtra("Gambar_Produk", list.get(pos).getGmbr());

                                startActivity(intent);
                                break;

                            case 1:
                                break;
                        }
                    }
                });
                waroengs.show();
            }
        });
    }

//    Baris code digunakan untuk mendapatkan data saat data di update atau pun di hapus.
    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

//    Untul menga,bil data dari dalam database.
    private void getData() {
        progressDialog.show();
        db.collection("Produk")
                .get()
                .addOnCompleteListener(new OnCompleteListener < QuerySnapshot > () {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task < QuerySnapshot > task) {
                        list.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                ProdukWaroeng prdk = new ProdukWaroeng(document.getString("Nama_Produk"),
                                        document.getString("Jenis_Produk"),
                                        document.getString("Harga_Produk"),
                                        document.getString("Stok_Produk"),
                                        document.getString("Gambar_Produk")
                                );
                                prdk.setId(document.getId());
                                list.add(prdk);
                            }
                            produkwaroengadapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(HalamanKasirWaroeng.this, "Data gagal diambil", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed(){
        if (doubleBackToExitPressedOnce){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getText(R.string.pls_back_again), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                doubleBackToExitPressedOnce = true;}
        }, 2000);
    }

}