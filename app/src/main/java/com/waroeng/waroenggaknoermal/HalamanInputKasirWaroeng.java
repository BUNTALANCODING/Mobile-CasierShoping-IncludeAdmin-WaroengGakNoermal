package com.waroeng.waroenggaknoermal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HalamanInputKasirWaroeng extends AppCompatActivity {
    //    Melakukan koneksi untuk mendapatkan user yang melakukan login kedalam aplikasi.
    private FirebaseUser firebaseUser;

    //    Mengambil koneksi untuk melakukan inser data kedalam database.
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //    digunakan untuk nanti mengambik id dari halaman UI.
    private TextView textName;
    private Button btntambah,tanggal;
    private String id = "";
    private ImageView kembali;
    private TextInputEditText nama, harga, namapembeli, jumlahbeli, hargabayar, stckuh;

    //    Digunakan untuk membuat tampilan loding setelah user menekan button tambah.
    private ProgressDialog progressDialog;


    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    private TextInputEditText jumlah, hargaku, hargabayarku, stockku;
    private TextView countsku;

    Integer stck , hargajual, jumlahjl, total;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        Digunakan untuk menentukan layout yang akan digunakan.
        setContentView(R.layout.halaman_input_kasir_waroeng);

        //        Mengambil Id yang digunakan pada layout yang digunakan.
        textName = findViewById(R.id.namaadmin);
        nama = findViewById(R.id.namaproduk);
        harga = findViewById(R.id.hargaproduk);
        namapembeli = findViewById(R.id.namapembeli);
        tanggal = findViewById(R.id.datePickerButton);
        jumlahbeli = findViewById(R.id.jumlahbeli);
        hargabayarku = findViewById(R.id.hargabayarkuy);
        btntambah = findViewById(R.id.buttonadd);


        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        //        Membuat tampilan loding setelah user menekan button tambah produk.
        progressDialog = new ProgressDialog(HalamanInputKasirWaroeng.this);
        progressDialog.setTitle("Loding");
        progressDialog.setMessage("Menyimpan Data...");

//        Melakukan koneksi untuk mengetahui user yang melakukan login dan untuk melakukan logout user.
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        jumlahbeli.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int jumlahjl = Integer.parseInt(jumlahbeli.getText().toString());
                int hargajual = Integer.parseInt(harga.getText().toString());
                total = hargajual * jumlahjl;
                hargabayarku.setText(Integer.toString(total));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



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
                Intent intent = new Intent(HalamanInputKasirWaroeng.this, HalamanKasirWaroeng.class);
                startActivity(intent);
            }
        });
        //        Membuat action listener untuk membuat perintah ketika user tidak mengisikan semua text input pada halaman tambah produk dan mejalankan fungsi addProduk.
        btntambah.setOnClickListener(view -> {
            if (nama.getText().length() > 0 && harga.getText().length() > 0 && namapembeli.getText().length() > 0 && tanggal.getText().length() > 0 && jumlahbeli.getText().length()>0 && hargabayar.getText().length()>0) {
                addPembeli(nama.getText().toString(), harga.getText().toString(), namapembeli.getText().toString(), tanggal.getText().toString(), jumlahbeli.getText().toString(), hargabayar.getText().toString());
            } else {
                Toast.makeText(this, "Isi semua form diatas!!", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            nama.setText(intent.getStringExtra("Nama_Produk"));
            harga.setText(intent.getStringExtra("Harga_Produk"));


        }
    }
    //    Membuat fungsi untuk melakukan insert data kedalam database.
    private void addPembeli(String nama, String harga, String namapembeli, String tanggal, String jumlahbeli, String hargabayar) {


        //        Mebuat id yang akan menjadi unique key pada database.
        Map<String, Object> admin = new HashMap<>();

        //        field yang akan ada pada dalam database.
        admin.put("A_Nama_Pembeli", namapembeli);
        admin.put("Nama_Produk", nama);
        admin.put("Harga_Produk", harga);
        admin.put("Tanggal_Pembelian", tanggal);
        admin.put("Jumlah_pembelian", jumlahbeli);
        admin.put("Harga_Pembelian", hargabayar);

        progressDialog.show();

        //        Fungsi untuk insert data kedalam database.
        db.collection("Pembeli")
                .add(admin)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(HalamanInputKasirWaroeng.this, "Pembelian berhasil", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HalamanInputKasirWaroeng.this, "Gagal proses", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

    }
    private void hitung(){



    }
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
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
