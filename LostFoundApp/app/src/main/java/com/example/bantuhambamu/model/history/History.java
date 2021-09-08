package com.example.bantuhambamu.model.history;

import com.google.gson.annotations.SerializedName;

public class History {

    @SerializedName("id")
    private int id;
    @SerializedName("nama")
    private String nama;
    @SerializedName("jenis")
    private String jenis;
    @SerializedName("ditemukan")
    private String ditemukan;
    @SerializedName("keterangan")
    private String keterangan;
    @SerializedName("status")
    private String status;
    @SerializedName("tgl_ditemukan")
    private String tgl_ditemukan;
    @SerializedName("picture")
    private String picture;
    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getDitemukan() {
        return ditemukan;
    }

    public void setDitemukan(String ditemukan) {
        this.ditemukan = ditemukan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTgl_ditemukan() {
        return tgl_ditemukan;
    }

    public void getTgl_ditemukan(String tgl_ditemukan) {
        this.tgl_ditemukan = tgl_ditemukan;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
