package com.example.bantuhambamu;

import com.example.bantuhambamu.model.barang.Barang;
import com.example.bantuhambamu.model.history.History;
import com.example.bantuhambamu.model.login.Login;
import com.example.bantuhambamu.model.register.Register;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<Login> loginResponse(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<Register> registerResponse(
            @Field("name") String name,
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email
    );

    @POST("tampil_barang.php")
    Call<List<Barang>> getBarang();

    @POST("tampil_histori.php")
    Call<List<History>> getHistory();

    @FormUrlEncoded
    @POST("tambah_barang.php")
    Call<Barang> insertBarang(
            @Field("key") String key,
            @Field("nama") String nama,
            @Field("jenis") String jenis,
            @Field("ditemukan") String ditemukan,
            @Field("keterangan") String keterangan,
            @Field("status") String status,
            @Field("tgl_ditemukan") String tgl_ditemukan,
            @Field("picture") String picture
    );

    @FormUrlEncoded
    @POST("ubah_barang.php")
    Call<Barang> updateBarang(
            @Field("key") String key,
            @Field("id") int id,
            @Field("nama") String nama,
            @Field("jenis") String jenis,
            @Field("ditemukan") String ditemukan,
            @Field("keterangan") String keterangan,
            @Field("status") String status,
            @Field("tgl_ditemukan") String tgl_ditemukan,
            @Field("picture") String picture
    );

    @FormUrlEncoded
    @POST("hapus_barang.php")
    Call<Barang> deleteBarang(
            @Field("key") String key,
            @Field("id") int id,
            @Field("picture") String picture
    );

    @POST("tampil_barang_sekarang.php")
    Call<List<Barang>> getNow();

    @FormUrlEncoded
    @POST("hapus_histori.php")
    Call<Barang> deleteHistory(
            @Field("key") String key,
            @Field("id") int id,
            @Field("picture") String picture
    );

}
