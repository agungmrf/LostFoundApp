package com.example.bantuhambamu;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.bantuhambamu.model.barang.Barang;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahBarangActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mNama, mKeterangan, mTgl_ditemukan, mStatus, mJenis, mDitemukan;
    private ImageView mPicture;
    private FloatingActionButton mFabChoosePic;
    private Button btnSimpan;

    Calendar myCalendar = Calendar.getInstance();

    private String nama, jenis, ditemukan, keterangan, status, picture, tgl_ditemukan;
    private int id;

    private Bitmap bitmap;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);

        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(this);

        mNama = findViewById(R.id.nama);
        mJenis = findViewById(R.id.jenis);
        mDitemukan = findViewById(R.id.ditemukan);
        mKeterangan = findViewById(R.id.keterangan);
        mPicture = findViewById(R.id.picture);
        mFabChoosePic = findViewById(R.id.fabChoosePic);
        mStatus = findViewById(R.id.status);
        mTgl_ditemukan = findViewById(R.id.tgl_ditemukan);

        mTgl_ditemukan.setFocusableInTouchMode(false);
        mTgl_ditemukan.setFocusable(false);
        mTgl_ditemukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TambahBarangActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mFabChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        nama = intent.getStringExtra("nama");
        jenis = intent.getStringExtra("jenis");
        ditemukan = intent.getStringExtra("ditemukan");
        keterangan = intent.getStringExtra("keterangan");
        tgl_ditemukan = intent.getStringExtra("tgl_ditemukan");
        picture = intent.getStringExtra("picture");
        status = intent.getStringExtra("status");

        setDataFromIntentExtra();

    }

    private void setDataFromIntentExtra() {

        if (id != 0) {

            readMode();

            mNama.setText(nama);
            mJenis.setText(jenis);
            mDitemukan.setText(ditemukan);
            mKeterangan.setText(keterangan);
            mStatus.setText(status);
            mTgl_ditemukan.setText(tgl_ditemukan);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.skipMemoryCache(true);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.placeholder(R.drawable.add_image);
            requestOptions.error(R.drawable.add_image);

            Glide.with(TambahBarangActivity.this)
                    .load(picture)
                    .apply(requestOptions)
                    .into(mPicture);

        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setTgl_ditemukan();
        }

    };

    private void setTgl_ditemukan() {
        String myFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mTgl_ditemukan.setText(sdf.format(myCalendar.getTime()));
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                mPicture.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void postData(final String key) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        readMode();

        String nama = mNama.getText().toString().trim();
        String jenis = mJenis.getText().toString().trim();
        String ditemukan = mDitemukan.getText().toString().trim();
        String keterangan = mKeterangan.getText().toString().trim();
        String status = mStatus.getText().toString().trim();
        String tgl_ditemukan = mTgl_ditemukan.getText().toString().trim();
        String picture = null;
        if (bitmap == null) {
            picture = "";
        } else {
            picture = getStringImage(bitmap);
        }

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Barang> call = apiInterface.insertBarang(key, nama, jenis, ditemukan, keterangan, status, tgl_ditemukan, picture);

        call.enqueue(new Callback<Barang>() {
            @Override
            public void onResponse(Call<Barang> call, Response<Barang> response) {

                progressDialog.dismiss();

                Log.i(TambahBarangActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")) {
                    finish();
                } else {
                    Toast.makeText(TambahBarangActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Barang> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TambahBarangActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void readMode() {

        mNama.setFocusableInTouchMode(false);
        mJenis.setFocusableInTouchMode(false);
        mDitemukan.setFocusableInTouchMode(false);
        mKeterangan.setFocusableInTouchMode(false);

        mStatus.setFocusableInTouchMode(false);
        mTgl_ditemukan.setFocusableInTouchMode(false);

        mFabChoosePic.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSimpan:
                //Save

                if (id == 0) {
                    if (TextUtils.isEmpty(mNama.getText().toString())) {
                        mNama.setError("Field nama tidak boleh kosong");
                    } else if (TextUtils.isEmpty(mJenis.getText().toString())) {
                        mJenis.setError("Field kategori tidak boleh kosong");
                    } else if (TextUtils.isEmpty(mDitemukan.getText().toString())) {
                        mDitemukan.setError("Field ruang tidak boleh kosong");
                    } else if (TextUtils.isEmpty(mKeterangan.getText().toString())) {
                        mKeterangan.setError("Field keterangan tidak boleh kosong");
                    } else if (TextUtils.isEmpty(mStatus.getText().toString())) {
                        mStatus.setError("Field status tidak boleh kosong");
                    } else if (TextUtils.isEmpty(mTgl_ditemukan.getText().toString())) {
                        mTgl_ditemukan.setError("Field tanggal tidak boleh kosong");
                    } else {

                        postData("insert");
                        btnSimpan.setVisibility(View.VISIBLE);

                        readMode();

                        Intent intent = new Intent(this, TambahBarangActivity.class);
                        startActivity(intent);

                    }

                }
                break;
        }
    }
}
