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

import com.example.bantuhambamu.model.history.History;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryBarangActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Adapter1 adapter;
    List<History> historyList;

    ApiInterface apiInterface;
    Adapter1.RecyclerViewClickListener listener;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_barang);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        listener = new Adapter1.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, final int position) {

                Intent intent = new Intent(HistoryBarangActivity.this, DetailHistoryActivity.class);
                intent.putExtra("id", historyList.get(position).getId());
                intent.putExtra("nama", historyList.get(position).getNama());
                intent.putExtra("jenis", historyList.get(position).getJenis());
                intent.putExtra("ditemukan", historyList.get(position).getDitemukan());
                intent.putExtra("keterangan", historyList.get(position).getKeterangan());
                intent.putExtra("status", historyList.get(position).getStatus());
                intent.putExtra("picture", historyList.get(position).getPicture());
                intent.putExtra("tgl_ditemukan", historyList.get(position).getTgl_ditemukan());
                startActivity(intent);
            }
        };
    }

    public void getHistory(){
        Call<List<History>> call = apiInterface.getHistory();
        call.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                progressBar.setVisibility(View.GONE);
                historyList = response.body();
                Log.i(HistoryBarangActivity.class.getSimpleName(), response.body().toString());
                adapter = new Adapter1(historyList, HistoryBarangActivity.this, listener);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                Toast.makeText(HistoryBarangActivity.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHistory();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
