package com.waroeng.waroenggaknoermal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.waroeng.waroenggaknoermal.Adapter.PembeliWaroengAdaptor;
import com.waroeng.waroenggaknoermal.Model.PembeliWaroeng;


import java.util.ArrayList;
import java.util.List;

public class HalamanReportPembelian extends AppCompatActivity {
    //    Koneksi database unutk mengambil data untuk ditampilkan pada halaman report
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    //    Digunakan untuk menampilkan user yang login dan untuk membuat fungsi kembali.
    private ImageView kembali;
    private TextView textName;


    //    Koneksi untuk mendapatkan user yang sedang login.
    private FirebaseUser firebaseUser;


    //    Mengambil list data yang diambil.
    private List <PembeliWaroeng> list = new ArrayList < > ();
    private PembeliWaroengAdaptor pembeliwaroengadapter;
    private RecyclerView recyclerView;


    //    Membuat tampilan loding ketika user menekan button tampil report pada halaman menu admin.
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        Menentukan layout yang akan digunakan.
        setContentView(R.layout.halaman_report_pembelian);


        //        Untuk menampilkan nama user yang login pada id yang terdapat pada halaman.
        textName = findViewById(R.id.namaadmin);


        //        Digunakan untuk memilih layout yang digunakan untuk menampilkan data dari datbase.
        recyclerView = findViewById(R.id.recwar);



        //        Membuat tampilan loding pada halaman ketika User memasuki halaman report waroeng.
        progressDialog = new ProgressDialog(HalamanReportPembelian.this);
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
                Intent intent = new Intent(HalamanReportPembelian.this, HalamanPilihanReportWaroeng.class);
                startActivity(intent);
            }
        });

        //        Digunakan mengatur layout ketika data di tampilkan.
        pembeliwaroengadapter = new PembeliWaroengAdaptor(getApplicationContext(), list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(pembeliwaroengadapter);
        getData();

        pembeliwaroengadapter.setWaroeng(new PembeliWaroengAdaptor.Pembeli() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {
                        "Read Detail",
                        "Hapus Data"
                };
                AlertDialog.Builder waroengs = new AlertDialog.Builder(HalamanReportPembelian.this);
                waroengs.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:

                                Intent intent = new Intent(getApplicationContext(), ReadDetailPembelian.class);
                                intent.putExtra("id", list.get(pos).getId());
                                intent.putExtra("A_Nama_Pembeli", list.get(pos).getNamapembeliku());
                                intent.putExtra("Tanggal_Pembelian", list.get(pos).getTanggalku());
                                intent.putExtra("Nama_Produk", list.get(pos).getNamaku());
                                intent.putExtra("Harga_Produk", list.get(pos).getHargaku());
                                intent.putExtra("Jumlah_Pembelian", list.get(pos).getJumlahbeli());
                                intent.putExtra("Harga_Pembelian", list.get(pos).getHargabayarku());

                                startActivity(intent);
                                break;

                            case 1:
                                hapusProduk(list.get(pos).getId());
                                break;



                        }
                    }
                });
                waroengs.show();

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }



    private void getData() {
        progressDialog.show();
        db.collection("Pembeli")
                .get()
                .addOnCompleteListener(new OnCompleteListener < QuerySnapshot > () {
                    @Override
                    public void onComplete(@NonNull Task < QuerySnapshot > task) {
                        list.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                PembeliWaroeng prdk = new PembeliWaroeng (document.getString("A_Nama_Pembeli"),
                                        document.getString("Nama_Produk"),
                                document.getString("Harga_Produk"),
                                document.getString("Tanggal_Pembelian"),
                                document.getString("Jumlah_pembelian"),
                                        document.getString("Harga_Pembelian")


                                );
                                prdk.setId(document.getId());
                                list.add(prdk);
                            }
                            pembeliwaroengadapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(HalamanReportPembelian.this, "Data gagal diambil", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    private void hapusProduk(String id) {
        db.collection("Pembeli").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener < Void > () {
                    @Override
                    public void onComplete(@NonNull Task < Void > task) {
                        if (!task.isSuccessful()) {

                            Toast.makeText(HalamanReportPembelian.this, "Produk gagal dihapus", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                        getData();
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