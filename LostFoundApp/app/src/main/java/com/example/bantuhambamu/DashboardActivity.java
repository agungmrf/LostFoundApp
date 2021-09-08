package com.example.bantuhambamu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bantuhambamu.model.barang.Barang;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Adapter adapter;
    List<Barang> barangList;

    ApiInterface apiInterface;
    Adapter.RecyclerViewClickListener listener;
    ProgressBar progressBar;

    TextView etUsername, etName, etEmail;
    SessionManager sessionManager;
    String username, name, email;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(this);

        sessionManager = new SessionManager(DashboardActivity.this);
        if (!sessionManager.isLoggedIn()) {
            moveToLogin();
            finish();
        }

        etUsername = findViewById(R.id.hellouser);
        etName = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);

        username = sessionManager.getUserDetail().get(SessionManager.NAME);
        name = sessionManager.getUserDetail().get(SessionManager.USERNAME);
        email = sessionManager.getUserDetail().get(SessionManager.EMAIL);

        etUsername.setText(username);
        etName.setText(name);
        etEmail.setText(email);

        RelativeLayout kliklihat = findViewById(R.id.lihat);
        kliklihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, MainActivity.class));

            }
        });

        RelativeLayout kliktambah = findViewById(R.id.tambah);
        kliktambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, TambahBarangActivity.class));
            }
        });

        RelativeLayout klikhistory = findViewById(R.id.history);
        klikhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, HistoryBarangActivity.class));

            }
        });

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        listener = new Adapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, final int position) {

                Intent intent = new Intent(DashboardActivity.this, DetailBarangActivity.class);
                intent.putExtra("id", barangList.get(position).getId());
                intent.putExtra("nama", barangList.get(position).getNama());
                intent.putExtra("jenis", barangList.get(position).getJenis());
                intent.putExtra("ditemukan", barangList.get(position).getDitemukan());
                intent.putExtra("keterangan", barangList.get(position).getKeterangan());
                intent.putExtra("status", barangList.get(position).getStatus());
                intent.putExtra("picture", barangList.get(position).getPicture());
                intent.putExtra("tgl_ditemukan", barangList.get(position).getTgl_ditemukan());
                startActivity(intent);

            }

        };

    }

    private void moveToLogin() {
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder tombolkeluar = new AlertDialog.Builder(DashboardActivity.this);
        tombolkeluar.setMessage("Jangan lupa untuk kembali lagi ya!");
        tombolkeluar.setTitle("Tutup Aplikasi");
        tombolkeluar.setIcon(R.drawable.alert);
        tombolkeluar.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DashboardActivity.this.finish();
            }
        });
        tombolkeluar.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        tombolkeluar.show();
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.logout:
                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(DashboardActivity.this);
                dialog.setMessage("Yakin keluar sekarang? \nKamu akan keluar dari akun. Kamu selalu dapat masuk kembali.");
                dialog.setTitle("Logout Aplikasi");
                dialog.setIcon(R.drawable.alert);
                dialog.setPositiveButton("Ya keluar sekarang", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        sessionManager.logoutSession();
                        moveToLogin();
                    }
                });

                dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }

    public void getNow(){

        Call<List<Barang>> call = apiInterface.getNow();
        call.enqueue(new Callback<List<Barang>>() {
            @Override
            public void onResponse(Call<List<Barang>> call, Response<List<Barang>> response) {
                progressBar.setVisibility(View.GONE);
                barangList = response.body();
                Log.i(MainActivity.class.getSimpleName(), response.body().toString());
                adapter = new Adapter(barangList, DashboardActivity.this, listener);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Barang>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getNow();
    }

}
