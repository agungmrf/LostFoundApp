package com.example.bantuhambamu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class DetailBarangActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mNama, mKeterangan, mTgl_ditemukan, mStatus, mJenis, mDitemukan;
    ImageView mPicture;
    FloatingActionButton mFabChoosePic;
    Button btnSimpan, btnEdit, btnDelete;

    Calendar myCalendar = Calendar.getInstance();

    String nama, jenis, ditemukan, keterangan, status, picture, tgl_ditemukan;
    private int id;

    Bitmap bitmap;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btnSimpan = findViewById(R.id.btnSimpan);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);

        btnSimpan.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        btnSimpan.setVisibility(View.INVISIBLE);

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
                new DatePickerDialog(DetailBarangActivity.this, date, myCalendar
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

            Glide.with(DetailBarangActivity.this)
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
        String myFormat = "dd MMMM yyyy";
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

                Log.i(DetailBarangActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")) {
                    finish();
                } else {
                    Toast.makeText(DetailBarangActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Barang> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailBarangActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateData(final String key, final int id) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
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

        Call<Barang> call = apiInterface.updateBarang(key, id, nama, jenis, ditemukan, keterangan, status, tgl_ditemukan, picture);

        call.enqueue(new Callback<Barang>() {
            @Override
            public void onResponse(Call<Barang> call, Response<Barang> response) {

                progressDialog.dismiss();

                Log.i(DetailBarangActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")) {
                    Toast.makeText(DetailBarangActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailBarangActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Barang> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailBarangActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteData(final String key, final int id, final String pic) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();

        readMode();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Barang> call = apiInterface.deleteBarang(key, id, pic);

        call.enqueue(new Callback<Barang>() {
            @Override
            public void onResponse(Call<Barang> call, Response<Barang> response) {

                progressDialog.dismiss();

                Log.i(DetailBarangActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")) {
                    Toast.makeText(DetailBarangActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DetailBarangActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Barang> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailBarangActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void readMode() {

        mNama.setFocusableInTouchMode(false);
        mJenis.setFocusableInTouchMode(false);
        mDitemukan.setFocusableInTouchMode(false);
        mKeterangan.setFocusableInTouchMode(false);
        mStatus.setFocusableInTouchMode(false);

        mNama.setFocusable(false);
        mJenis.setFocusable(false);
        mDitemukan.setFocusable(false);
        mKeterangan.setFocusable(false);
        mStatus.setFocusable(false);

        mTgl_ditemukan.setEnabled(false);

        mFabChoosePic.setVisibility(View.INVISIBLE);

    }

    private void editMode() {

        mNama.setFocusableInTouchMode(true);
        mJenis.setFocusableInTouchMode(true);
        mDitemukan.setFocusableInTouchMode(true);
        mKeterangan.setFocusableInTouchMode(true);
        mStatus.setFocusableInTouchMode(true);

        mTgl_ditemukan.setEnabled(true);

        mFabChoosePic.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEdit:
                AlertDialog.Builder dialog = new AlertDialog.Builder(DetailBarangActivity.this);
                dialog.setMessage("Yakin ingin mengupdate barang hilang?");
                dialog.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        editMode();

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(mNama, InputMethodManager.SHOW_IMPLICIT);

                        btnEdit.setVisibility(View.INVISIBLE);
                        btnDelete.setVisibility(View.INVISIBLE);
                        btnSimpan.setVisibility(View.VISIBLE);
                    }
                });

                dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.btnSimpan:
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
                        btnEdit.setVisibility(View.INVISIBLE);
                        btnSimpan.setVisibility(View.VISIBLE);
                        readMode();
                    }
                } else {
                    updateData("update", id);
                    btnEdit.setVisibility(View.INVISIBLE);
                    btnSimpan.setVisibility(View.VISIBLE);
                    readMode();
                }
                break;
            case R.id.btnDelete:
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(DetailBarangActivity.this);
                dialog1.setMessage("Yakin ingin menghapus barang hilang ini?");
                dialog1.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteData("delete", id, picture);
                    }
                });

                dialog1.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog1.show();
                break;
        }
    }
}
