package com.example.bantuhambamu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bantuhambamu.model.barang.Barang;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Adapter adapter;
    List<Barang> barangList;

    ApiInterface apiInterface;
    Adapter.RecyclerViewClickListener listener;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        listener = new Adapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, final int position) {

                Intent intent = new Intent(MainActivity.this, DetailBarangActivity.class);
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

    public void getBarang(){
        Call<List<Barang>> call = apiInterface.getBarang();
        call.enqueue(new Callback<List<Barang>>() {
            @Override
            public void onResponse(Call<List<Barang>> call, Response<List<Barang>> response) {
                progressBar.setVisibility(View.GONE);
                barangList = response.body();
                Log.i(MainActivity.class.getSimpleName(), response.body().toString());
                adapter = new Adapter(barangList, MainActivity.this, listener);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Barang>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "rp :"+
                        t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBarang();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
