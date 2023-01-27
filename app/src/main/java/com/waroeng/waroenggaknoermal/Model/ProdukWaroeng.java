package com.waroeng.waroenggaknoermal.Model;

public class ProdukWaroeng {
    private String id, nama, jenis, harga, stok, gmbr;
    public ProdukWaroeng(){

    }

    public ProdukWaroeng(String nama,String jenis, String harga,String stok, String gambar){
        this.nama = nama;
        this.jenis=jenis;
        this.harga=harga;
        this.stok=stok;
        this.gmbr=gambar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getGmbr() {
        return gmbr;
    }

    public void setGmbr(String gmbr) {
        this.gmbr = gmbr;
    }
}

