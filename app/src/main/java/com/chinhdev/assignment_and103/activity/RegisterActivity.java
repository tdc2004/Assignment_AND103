package com.chinhdev.assignment_and103.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.chinhdev.assignment_and103.Interface.APIServer;
import com.chinhdev.assignment_and103.R;
import com.chinhdev.assignment_and103.model.UserModel;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    ImageView imageView;
    TextInputEditText edt_email, edt_user, edt_pass,ed_respass;
    Retrofit retrofit;
    APIServer apiServer;
    private String url = "http://10.0.2.2:3000/";
    Button btn_res;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imageView = findViewById(R.id.img_avatar);
        edt_email = findViewById(R.id.ed_email_regis);
        edt_pass = findViewById(R.id.ed_password_regis);
        ed_respass = findViewById(R.id.ed_repassword_regis);
        edt_user = findViewById(R.id.ed_username_regis);
        btn_res = findViewById(R.id.bt_register);
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(APIServer.class);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btn_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString();
                String username = edt_user.getText().toString();
                String password = edt_pass.getText().toString();
                String repass = ed_respass.getText().toString();
                if (email.equals("")||username.equals("")||password.equals("")){
                    Toast.makeText(RegisterActivity.this, "Vui long khong bo trong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!repass.equals(password)){
                    Toast.makeText(RegisterActivity.this, "Mat khau khong trung", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (file != null) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part imageFile = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);

                    RequestBody usernameRequestBody = RequestBody.create(MediaType.parse("text/plain"), username);
                    RequestBody emailRequestBody = RequestBody.create(MediaType.parse("text/plain"), email);
                    RequestBody passwordRequestBody = RequestBody.create(MediaType.parse("text/plain"), password);

                    Call<UserModel> call = apiServer.register(usernameRequestBody,passwordRequestBody,emailRequestBody,imageFile);
                    call.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            Log.e("Respone", String.valueOf(response.body()));
                            if (response.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                            } else {
                                Toast.makeText(RegisterActivity.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                                Log.e("Loi", String.valueOf(response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            Log.e("Loi server",t.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Vui lòng chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        file = createFileFormUri(imageUri,"avatar");
                        if (file != null) {
                            Glide.with(RegisterActivity.this).load(imageUri).into(imageView);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Không thể chọn ảnh", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    private File createFileFormUri (Uri path, String name) {
        File _file = new File(RegisterActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = RegisterActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) >0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            Log.d("123123", "createFileFormUri: " +_file);
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
