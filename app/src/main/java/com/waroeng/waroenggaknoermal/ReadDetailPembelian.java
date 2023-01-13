package com.waroeng.waroenggaknoermal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ReadDetailPembelian extends AppCompatActivity {
    private TextView dtlnama, dtlpembeli, dtltanggal, dtlharga, dtljumlah, dtlbayar;
    private String id = "";



    //    Digunakan untuk menampilkan user yang login dan untuk membuat fungsi kembali.
    private ImageView kembali;
    private TextView textName;


    //    Koneksi untuk mendapatkan user yang sedang login.
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_detail_pembelian);
        //        Untuk menampilkan nama user yang login pada id yang terdapat pada halaman.
        textName = findViewById(R.id.namaadmin);

        dtlnama = findViewById(R.id.namabarang3);
        dtltanggal = findViewById(R.id.tanggall3);
        dtlpembeli = findViewById(R.id.namapembeliku3);
        dtlbayar = findViewById(R.id.bayarku3);
        dtlharga = findViewById(R.id.hargaproduku3);
        dtljumlah = findViewById(R.id.jmlhpembelian3);



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
                Intent intent = new Intent(ReadDetailPembelian.this, HalamanReportPembelian.class);
                startActivity(intent);
            }
        });



        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            dtlnama.setText(intent.getStringExtra("Nama_Produk"));
            dtlharga.setText(intent.getStringExtra("Harga_Produk"));
            dtlpembeli.setText(intent.getStringExtra("A_Nama_Pembeli"));
            dtltanggal.setText(intent.getStringExtra("Tanggal_Pembelian"));
            dtljumlah.setText(intent.getStringExtra("Jumlah_Pembelian"));
            dtlbayar.setText(intent.getStringExtra("Harga_Pembelian"));
        }
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