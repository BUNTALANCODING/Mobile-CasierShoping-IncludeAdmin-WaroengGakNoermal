package com.waroeng.waroenggaknoermal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HalamanAwalWaroeng extends AppCompatActivity {
    private ImageView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Baris code untuk menentukan layout yang akan digunakan.
        setContentView(R.layout.halaman_awal_waroeng);

//        Baris code yang digunakan untuk mengambil id dari ImageView.
        next = findViewById(R.id.belanja);

//        Baris code yang digunakan untuk membuat on click listener ketika user menekan button.
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HalamanAwalWaroeng.this, HalamanLoginAdminWaroeng.class);
                startActivity(intent);
            }
        });
    }
}