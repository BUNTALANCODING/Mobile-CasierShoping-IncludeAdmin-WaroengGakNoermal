package com.waroeng.waroenggaknoermal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class HalamanRegistrasiWaroeng extends AppCompatActivity {
    private EditText userdisplay, userreg, passreg, passcon;
    private Button btnreg;
    //    Membuat tampilan loding.
    private ProgressDialog progressDialog;
    private TextView kembali;
    //    Untuk koneksi Autentikasi dari firebase.
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halaman_registrasi_waroeng);

        userreg = findViewById(R.id.verify);
        passreg = findViewById(R.id.passwordres);
        passcon = findViewById(R.id.passwordcon);
        userdisplay = findViewById(R.id.userdisplay);
        btnreg = findViewById(R.id.login);
        //        Mengambil koneksi untuk melakukan autentikasi.
        mAuth = FirebaseAuth.getInstance();

        //        Membuat tampilan loding setelah user menekan button login.
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loding");
        progressDialog.setMessage("Silahkan Tunggu");
        progressDialog.setCancelable(false);

//        Mengatur fungsi kembali ketika user menekan icon kembali.
        kembali = findViewById(R.id.regiswoy2);

        kembali.setOnClickListener(view -> {

            startActivity(new Intent(getApplicationContext(), HalamanLoginAdminWaroeng.class));

        });

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userdisplay.getText().toString().length()>0 && userreg.getText().toString().length()>0 && passreg.getText().toString().length()>0 && passcon.getText().toString().length()>0){
                    if (passreg.getText().toString().equals(passcon.getText().toString())){
                        register(userdisplay.getText().toString(), userreg.getText().toString(), passreg.getText().toString());
                    }else{
                        Toast.makeText(HalamanRegistrasiWaroeng.this, "Masukan Password yang sama !!", Toast.LENGTH_SHORT).show();
                    }
                    
                }else{
                    Toast.makeText(HalamanRegistrasiWaroeng.this, "Isi semuaa data !!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void register(String userdisplay, String userreg, String passreg){
        progressDialog.show();
 mAuth.createUserWithEmailAndPassword(userreg , passreg).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
     @Override
     public void onComplete(@NonNull Task<AuthResult> task) {
         if (task.isSuccessful() && task.getResult()!=null){
             FirebaseUser firebaseUser = task.getResult().getUser();
             if (firebaseUser!=null){
                 UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                         .setDisplayName(userdisplay)
                         .build();
                 firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
regis();
                     }
                 });

             }else{
                 Toast.makeText(HalamanRegistrasiWaroeng.this, "Gagal register", Toast.LENGTH_SHORT).show();
             }

         }else{
             Toast.makeText(HalamanRegistrasiWaroeng.this, "Gagal register", Toast.LENGTH_SHORT).show();
         }
     }
 });



    }


    //    Membuat tujuan halaman aplikasi selanjut nya setelah user melakukan login.
    private void regis() {
        Intent intent = new Intent(HalamanRegistrasiWaroeng.this, HalamanRegistrasiConfirm.class);
        startActivity(intent);
    }

    //    Membuat session user yang melakukan login.
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser curenntUser = mAuth.getCurrentUser();
        if (curenntUser != null) {
            regis();
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