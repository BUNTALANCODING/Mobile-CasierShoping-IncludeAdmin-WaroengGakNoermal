package com.waroeng.waroenggaknoermal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HalamanPilihanReportWaroeng extends AppCompatActivity {
    //    Mengambil koneksi ke firebase.
    private FirebaseUser firebaseUser;


    //    Digunakan sebagai tempat untuk menampilkan user yang sedang login.
    private TextView textName;
    private ImageView kembali;


    //    Untuk menghubungkan halaman menu admin dengan halaman tambah produk dan report.
    private ImageButton btn1, btn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        Untuk Memanggil tampilan yang akan di gunakan.
        setContentView(R.layout.halaman_pilihan_report_waroeng);



        //        Memanggil Id dari tampilan UI.
        textName = findViewById(R.id.namaadmin);



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
                Intent intent = new Intent(HalamanPilihanReportWaroeng.this, HalamanMenuAdminWaroeng.class);
                startActivity(intent);
            }
        });




        //        Mengambil Id yang ada pada tampilan UI.
        btn1 = findViewById(R.id.menuadmin);
        //        Membuat action listener ketika user menekan img button pada halaman  menu admin.
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //                Mehubungkan halaman menu admin dengan halaman tambah produk.
                Intent intent = new Intent(HalamanPilihanReportWaroeng.this, HalamanReportPembelian.class);
                startActivity(intent);
            }
        });



        //        Mengambil Id yang ada pada tampilan UI.
        btn2 = findViewById(R.id.kasir);
        //        Membuat action listener ketika user menekan img button pada halaman  menu admin.
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //                Mehubungkan halaman menu admin dengan halaman report waroeng.
                Intent intent = new Intent(HalamanPilihanReportWaroeng.this, HalamanReportsWaroeng.class);
                startActivity(intent);
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