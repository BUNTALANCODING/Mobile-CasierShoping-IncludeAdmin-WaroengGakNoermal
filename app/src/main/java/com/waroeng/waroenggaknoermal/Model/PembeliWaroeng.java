package com.waroeng.waroenggaknoermal.Model;

public class PembeliWaroeng {
    private String id, namaku, namapembeliku, hargaku, tanggalku, hargabayarku, jumlahbeli ;
    public PembeliWaroeng(){

    }

    public PembeliWaroeng(String namaku,String pembeliku, String hargaku,String hargabayarku, String jumlahbeli, String tanggalku) {
        this.namapembeliku = pembeliku;
        this.namaku = namaku;
        this.hargaku = hargaku;
        this.hargabayarku = hargabayarku;
        this.jumlahbeli = jumlahbeli;
        this.tanggalku = tanggalku;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaku() {
        return namaku;
    }

    public void setNamaku(String namaku) {
        this.namaku = namaku;
    }

    public String getNamapembeliku() {
        return namapembeliku;
    }

    public void setNamapembeliku(String namapembeliku) {
        this.namapembeliku = namapembeliku;
    }

    public String getHargaku() {
        return hargaku;
    }

    public void setHargaku(String hargaku) {
        this.hargaku = hargaku;
    }

    public String getTanggalku() {
        return tanggalku;
    }

    public void setTanggalku(String tanggalku) {
        this.tanggalku = tanggalku;
    }

    public String getHargabayarku() {
        return hargabayarku;
    }

    public void setHargabayarku(String hargabayarku) {
        this.hargabayarku = hargabayarku;
    }

    public String getJumlahbeli() {
        return jumlahbeli;
    }

    public void setJumlahbeli(String jumlahbeli) {
        this.jumlahbeli = jumlahbeli;
    }
}
