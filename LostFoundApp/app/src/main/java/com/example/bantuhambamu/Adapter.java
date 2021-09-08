package com.example.bantuhambamu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.bantuhambamu.model.barang.Barang;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

    List<Barang> barang, barangFilter;
    private Context context;
    private RecyclerViewClickListener mListener;

    public Adapter(List<Barang> barang, Context context, RecyclerViewClickListener listener) {
        this.barang = barang;
        this.barangFilter = barang;
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public int getItemCount() {
        return barang.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewClickListener mListener;
        private ImageView mPicture;
        private TextView mNama, mRuang, mDate, mKeterangan;
        private CardView mRowContainer;

        public MyViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mPicture = itemView.findViewById(R.id.picture);
            mNama = itemView.findViewById(R.id.nama);
            mKeterangan = itemView.findViewById(R.id.ket);
            mRuang = itemView.findViewById(R.id.ruang);
            mDate = itemView.findViewById(R.id.date);
            mRowContainer = itemView.findViewById(R.id.row_container);

            mListener = listener;
            mRowContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.row_container:
                    mListener.onRowClick(mRowContainer, getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.mNama.setText(barang.get(position).getNama());
        holder.mRuang.setText(barang.get(position).getDitemukan());
        holder.mKeterangan.setText(barang.get(position).getKeterangan());
        holder.mDate.setText(barang.get(position).getTgl_ditemukan());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.add_image);
        requestOptions.error(R.drawable.add_image);

        Glide.with(context)
                .load(barang.get(position).getPicture())
                .apply(requestOptions)
                .into(holder.mPicture);

    }

    public interface RecyclerViewClickListener {
        void onRowClick(View view, int position);
    }

}
