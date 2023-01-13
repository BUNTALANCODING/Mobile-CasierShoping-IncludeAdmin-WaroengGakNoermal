package com.waroeng.waroenggaknoermal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HalamanVerifyAdmin extends AppCompatActivity {
private Button kembali,verif;
    //    Digunakan untuk membuat tampilan loding setelah user menekan button tambah.
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halaman_verify_admin);

        //        Mengatur fungsi kembali ketika user menekan icon kembali.
        kembali = findViewById(R.id.kembaliku);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HalamanVerifyAdmin.this, HalamanPilihanWaroeng.class);
                startActivity(intent);
            }
        });


        //        Membuat tampilan loding pada halaman ketika User memasuki halaman report waroeng.
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loding");
        progressDialog.setMessage("Memverifikasi...");


    }

    public void verif(View view){
        EditText edt_password = (EditText) findViewById(R.id.verify);
        String password_val = edt_password.getText().toString();

        if (password_val.equals("akbar1703")) {
            progressDialog.show();
            Intent intent = new Intent(this,HalamanMenuAdminWaroeng.class);
            startActivity(intent);
        } else {
            progressDialog.dismiss();
            Toast.makeText(this,"Password Verify Salah", Toast.LENGTH_SHORT).show();
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