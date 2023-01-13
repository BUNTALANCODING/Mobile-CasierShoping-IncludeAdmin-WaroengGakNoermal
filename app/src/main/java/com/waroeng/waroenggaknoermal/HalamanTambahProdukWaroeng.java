package com.waroeng.waroenggaknoermal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HalamanTambahProdukWaroeng extends AppCompatActivity {
    //    Melakukan koneksi untuk mendapatkan user yang melakukan login kedalam aplikasi.
    private FirebaseUser firebaseUser;



    //    Mengambil koneksi untuk melakukan inser data kedalam database.
    private FirebaseFirestore db = FirebaseFirestore.getInstance();



    //    digunakan untuk nanti mengambik id dari halaman UI.
    private TextView textName;
    private ImageView kembali, gmbradd, gmbrtampil;
    private TextInputEditText nama, jenis, harga, stok;
    private Button btntambah;
    private String id = "";



    //    Digunakan untuk membuat tampilan loding setelah user menekan button tambah.
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        Digunakan untuk menentukan layout yang akan digunakan.
        setContentView(R.layout.halaman_tambah_produk_waroeng);



        //        Mengambil Id yang digunakan pada layout yang digunakan.
        textName = findViewById(R.id.namaadmin);
        nama = findViewById(R.id.namaproduk);
        jenis = findViewById(R.id.jumlahbeli);
        harga = findViewById(R.id.hargaproduk);
        stok = findViewById(R.id.namapembeli);
        btntambah = findViewById(R.id.buttonadd);
        gmbradd = findViewById(R.id.tambahgambar);
        gmbrtampil = findViewById(R.id.tampilgambar);




        //        Membuat tampilan loding setelah user menekan button tambah produk.
        progressDialog = new ProgressDialog(HalamanTambahProdukWaroeng.this);
        progressDialog.setTitle("Loding");
        progressDialog.setMessage("Menyimpan Data...");


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
                Intent intent = new Intent(HalamanTambahProdukWaroeng.this, HalamanMenuAdminWaroeng.class);
                startActivity(intent);
            }
        });




        //        Membuat action listener untuk membuat perintah ketika user tidak mengisikan semua text input pada halaman tambah produk dan mejalankan fungsi addProduk.
        btntambah.setOnClickListener(view -> {
        if (nama.getText().length() > 0 && jenis.getText().length() > 0 && harga.getText().length() > 0 && stok.getText().length() > 0) {
            uploadgmbr(nama.getText().toString(), jenis.getText().toString(), harga.getText().toString(), stok.getText().toString());
        } else {
            Toast.makeText(this, "Isi semua form diatas!!", Toast.LENGTH_SHORT).show();
        }
        });

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            nama.setText(intent.getStringExtra("Nama_Produk"));
            jenis.setText(intent.getStringExtra("Jenis_Produk"));
            harga.setText(intent.getStringExtra("Harga_Produk"));
            stok.setText(intent.getStringExtra("Stok_Produk"));
            Glide.with(getApplicationContext()).load(intent.getStringExtra("Gambar_Produk")).into(gmbrtampil);
        }

        gmbradd.setOnClickListener(view -> {
                pilihgambar();
        });

    }
    private void pilihgambar() {
        final CharSequence[] dialogItem = {
                "Pilih Dari Galeri",
                "Cencel"
        };

        AlertDialog.Builder waroengs = new AlertDialog.Builder(HalamanTambahProdukWaroeng.this);
        waroengs.setItems(dialogItem, (dialog, item) -> {
        if (dialogItem[item].equals("Pilih Dari Galeri")) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 10);

        } else if (dialogItem[item].equals("Cancel")) {
            dialog.dismiss();
        }

        });
        waroengs.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            final Uri path = data.getData();
            Thread thread = new Thread(() -> {
            try {
                InputStream inputStream = getContentResolver().openInputStream(path);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                gmbrtampil.post(() -> {
                        gmbrtampil.setImageBitmap(bitmap);
                    });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            });


            thread.start();
        }
    }

    private void uploadgmbr(String nama, String jenis, String harga, String stok) {
        progressDialog.show();
        gmbrtampil.setDrawingCacheEnabled(true);
        gmbrtampil.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) gmbrtampil.getDrawable()).getBitmap();
        ByteArrayOutputStream ak = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ak);
        byte[] data = ak.toByteArray();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference reference = firebaseStorage.getReference("Gambar_Produk").child(new Date().getTime() + ".jpg");
        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HalamanTambahProdukWaroeng.this, "Gagal Tambah Produk", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener < UploadTask.TaskSnapshot > () {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null) {

                    if (taskSnapshot.getMetadata().getReference() != null) {
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener < Uri > () {
                            @Override
                            public void onComplete(@NonNull Task < Uri > task) {
                                if (task.getResult() != null) {
                                    addProduk(nama, jenis, harga, stok, task.getResult().toString());

                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(HalamanTambahProdukWaroeng.this, "Gagal Tambah Produk", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(HalamanTambahProdukWaroeng.this, "Gagal Tambah Produk", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(HalamanTambahProdukWaroeng.this, "Gagal Tambah Produk", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    //    Membuat fungsi untuk melakukan insert data kedalam database.
    private void addProduk(String nama, String jenis, String harga, String stok, String gmbrtampil) {


        //        Mebuat id yang akan menjadi unique key pada database.
        Map < String, Object > admin = new HashMap < > ();

        //        field yang akan ada pada dalam database.
        admin.put("Nama_Produk", nama);
        admin.put("Jenis_Produk", jenis);
        admin.put("Harga_Produk", harga);
        admin.put("Stok_Produk", stok);
        admin.put("Gambar_Produk", gmbrtampil);

        progressDialog.show();
        if (id != null) {
            db.collection("Produk").document(id)
                    .set(admin)
                    .addOnCompleteListener(new OnCompleteListener < Void > () {
                        @Override
                        public void onComplete(@NonNull Task < Void > task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {

            //        Fungsi untuk insert data kedalam database.
            db.collection("Produk")
                    .add(admin)
                    .addOnSuccessListener(new OnSuccessListener < DocumentReference > () {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(HalamanTambahProdukWaroeng.this, "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HalamanTambahProdukWaroeng.this, "Produk gagal ditambahkan", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
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