package com.chinhdev.assignment_and103.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.chinhdev.assignment_and103.Interface.APIServer;
import com.chinhdev.assignment_and103.R;
import com.chinhdev.assignment_and103.model.CartModel;
import com.chinhdev.assignment_and103.model.SanPhamModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {
    Retrofit retrofit;
    APIServer apiServer;
    private String url = "http://10.0.2.2:3000/";

    ImageView imageView,image_back;
    TextView textView_name, textView_price, textView_invention, tv_moTa;
    SanPhamModel sanPhamModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        imageView = findViewById(R.id.img_details);
        image_back = findViewById(R.id.img_goBack);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsActivity.this, HomeActivity.class));
            }
        });
        textView_name = findViewById(R.id.tv_name_details);
        textView_price = findViewById(R.id.tv_price_details);
        textView_invention = findViewById(R.id.tv_spice_details);
        tv_moTa = findViewById(R.id.tv_decribe_details);
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(APIServer.class);

        String ma = getIntent().getStringExtra("id");
        getProductDetail(ma);
        Button button = findViewById(R.id.btn_add_cart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartModel cartModel = new CartModel();
                cartModel.setProduct_id(ma);
                cartModel.setQuantity(1);
                addToCart(cartModel);
            }
        });

    }
    private void addToCart(CartModel cartModel) {
        Call<Void> call = apiServer.addToCart(cartModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailsActivity.this, "Sản phẩm đã được thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Error", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void getProductDetail(String productId) {
        Call<SanPhamModel> call = apiServer.getSanPham(productId);
        call.enqueue(new Callback<SanPhamModel>() {
            @Override
            public void onResponse(Call<SanPhamModel> call, Response<SanPhamModel> response) {
                if (response.isSuccessful()) {
                    SanPhamModel sanPham = response.body();
                    displayProductDetail(sanPham);
                } else {
                    Log.e("Loi", response.message());
                }
            }

            @Override
            public void onFailure(Call<SanPhamModel> call, Throwable t) {

            }
        });

    }

    private void displayProductDetail(SanPhamModel sanPham) {
        textView_name.setText(sanPham.getName());
        textView_price.setText(String.valueOf(sanPham.getPrice()));
        textView_invention.setText(sanPham.getInventory() + "");
        tv_moTa.setText(sanPham.getDescription());
        Glide.with(this).load(sanPham.getImage()).placeholder(R.drawable.image).into(imageView);
    }
}