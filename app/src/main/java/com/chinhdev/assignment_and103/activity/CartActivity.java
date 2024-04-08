package com.chinhdev.assignment_and103.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chinhdev.assignment_and103.Interface.APIServer;
import com.chinhdev.assignment_and103.R;
import com.chinhdev.assignment_and103.adapter.CartAdapter;
import com.chinhdev.assignment_and103.model.CartModel;
import com.chinhdev.assignment_and103.model.SanPhamModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CartAdapter adapter;
    ArrayList<CartModel> list;
    Retrofit retrofit;
    APIServer apiServer;
    private String url = "http://10.0.2.2:3000/";
    BottomNavigationView navigationView;
    Button button;
    double totalPrice = 0;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.recyclerViewCartItems);
        button = findViewById(R.id.buttonCheckout);
        textView = findViewById(R.id.textViewTotalPrice);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, LocationActivity.class));
            }
        });
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(APIServer.class);
        navigationView = findViewById(R.id.menu);
        getAllCartItems();
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_home){
                    startActivity(new Intent(CartActivity.this, HomeActivity.class));
                }else {
                    startActivity(new Intent(CartActivity.this, CartActivity.class));
                }
                return false;
            }
        });
    }
    private void getAllCartItems() {
        Call<ArrayList<CartModel>> call = apiServer.getAllCart();
        call.enqueue(new Callback<ArrayList<CartModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CartModel>> call, Response<ArrayList<CartModel>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    displayCartItems(list);
                    getTotalPrice(list);
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CartModel>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }


    private void displayCartItems(ArrayList<CartModel> cartItems) {
        adapter = new CartAdapter(cartItems,CartActivity.this );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void getTotalPrice(ArrayList<CartModel> cartItems) {


        for (CartModel cartItem : cartItems) {
            String productId = cartItem.getProduct_id();
            int quantity = cartItem.getQuantity();

            Call<SanPhamModel> call = apiServer.getSanPham(productId);
            call.enqueue(new Callback<SanPhamModel>() {
                @Override
                public void onResponse(Call<SanPhamModel> call, Response<SanPhamModel> response) {
                    if (response.isSuccessful()) {
                        SanPhamModel product = response.body();
                        double price = product.getPrice();
                        totalPrice += price * quantity;

                        displayTotalPrice(totalPrice);
                    } else {
                        Log.e("Error", response.message());
                    }
                }

                @Override
                public void onFailure(Call<SanPhamModel> call, Throwable t) {
                    Log.e("Error", t.getMessage());
                }
            });
        }
    }

    private void displayTotalPrice(double totalPrice) {
        textView.setText(String.valueOf(totalPrice));
        Log.d("Total Price", String.valueOf(totalPrice));
    }
}