package com.example.bantuhambamu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bantuhambamu.model.register.Register;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etUsername, etPassword, etName, etEmail;
    Button btnRegister;
    TextView textLogin;
    String Username, Password, Name, Email;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        etName = findViewById(R.id.etRegisterName);
        etEmail = findViewById(R.id.etRegisterEmail);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        textLogin = findViewById(R.id.textLogin);
        textLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        switch (v.getId()) {
            case R.id.btnRegister:
                Username = etUsername.getText().toString().trim();
                Password = etPassword.getText().toString().trim();
                Email = etEmail.getText().toString().trim();
                Name = etName.getText().toString().trim();

                if (Username.isEmpty()) {
                    etUsername.setError("Field ini tidak boleh kosong");
                } else if (Name.isEmpty()) {
                    etName.setError("Field ini tidak boleh kosong");
                }else if (Email.isEmpty() || Email.equals(null) || !Email.matches(emailPattern)){
                    etEmail.setError("Field ini tidak boleh kosong");
                } else if (Password.isEmpty()) {
                    etPassword.setError("Field ini tidak boleh kosong");
                } else {
                    register(Name, Username, Password, Email);
                }
                break;
            case R.id.textLogin:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void register(String username, String password, String name, String email) {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Register> call = apiInterface.registerResponse(username, password, name, email);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                    Toast.makeText(RegistActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegistActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(RegistActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}