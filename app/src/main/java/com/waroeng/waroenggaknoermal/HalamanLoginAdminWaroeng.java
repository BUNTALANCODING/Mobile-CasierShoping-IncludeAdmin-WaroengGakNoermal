package com.waroeng.waroenggaknoermal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HalamanLoginAdminWaroeng extends AppCompatActivity {


    //    Untuk memanggil ID yang digunakan pada tampilan UI.
    private EditText editUser, editPass;
    private Button btnLogin;
    private TextView register;



    //    Membuat tampilan loding.
    private ProgressDialog progressDialog;



    //    Untuk koneksi Autentikasi dari firebase.
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        Untuk Memanggil tampilan yang akan di gunakan.
        setContentView(R.layout.halaman_login_admin_waroeng);



        //        Memanggil Id dari tampilan UI.
        editUser = findViewById(R.id.usernameres);
        editPass = findViewById(R.id.passwordres);
        btnLogin = findViewById(R.id.login);
        register = findViewById(R.id.regiswoy);



        //        Mengambil koneksi untuk melakukan autentikasi.
        mAuth = FirebaseAuth.getInstance();



        //        Membuat tampilan loding setelah user menekan button login.
        progressDialog = new ProgressDialog(HalamanLoginAdminWaroeng.this);
        progressDialog.setTitle("Loding");
        progressDialog.setMessage("Silahkan Tunggu");
        progressDialog.setCancelable(false);



        //        Membuat perintah user untuk mengisikan semua text input pada halaman login sebelum user melakukan login.
        btnLogin.setOnClickListener(view ->  {
        if (editUser.getText().length() > 0 && editPass.getText().length() > 0) {

            login(editUser.getText().toString(), editPass.getText().toString());
        } else {
            Toast.makeText(this, "Masukan Username dan Password", Toast.LENGTH_SHORT).show();

        }
        });

        register.setOnClickListener(view -> {

            startActivity(new Intent(getApplicationContext(), HalamanRegistrasiWaroeng.class));

        });
    }



    //    Membuat fungsi login user.
    private void login(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener < AuthResult > () {
            @Override

            //            Jika user benar mengisikan username dan password sesuai dengan yang ada pada database maka user akan diarahkan ke halaman menu admin.
            public void onComplete(@NonNull Task < AuthResult > task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    if (task.getResult().getUser() != null) {
                        masuk();
                    }
                    //                    Jika user salah memasukan username dan password maka user akan mendapatkan pemberitahuan melalui toast.
                    else {
                        Toast.makeText(HalamanLoginAdminWaroeng.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(HalamanLoginAdminWaroeng.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    //    Membuat tujuan halaman aplikasi selanjut nya setelah user melakukan login.
    private void masuk() {
        startActivity(new Intent(getApplicationContext(), HalamanPilihanWaroeng.class));

    }



    //    Membuat session user yang melakukan login.
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser curenntUser = mAuth.getCurrentUser();
        if (curenntUser != null) {
            masuk();
        }

    }
}