package com.chinhdev.assignment_and103.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.chinhdev.assignment_and103.Interface.APIServer;
import com.chinhdev.assignment_and103.R;
import com.chinhdev.assignment_and103.model.UserModel;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    TextView tv;
    TextInputEditText edt_email, edt_pass;
    Button btn_login;
    private String url = "http://10.0.2.2:3000/";
    Retrofit retrofit;
    APIServer apiServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        tv = findViewById(R.id.tv_signUp);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        edt_email = findViewById(R.id.ed_email_login);
        edt_pass = findViewById(R.id.ed_password_login);
        btn_login =findViewById(R.id.bt_login);
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(APIServer.class);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString().trim();
                String pass = edt_pass.getText().toString().trim();
                UserModel userModel = new UserModel();
                userModel.setEmail(email);
                userModel.setPassword(pass);
                Call<Void> call = apiServer.loginUser(userModel);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            Log.e("Loi",response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Login",t.getMessage());
                    }
                });
            }
        });
    }
}