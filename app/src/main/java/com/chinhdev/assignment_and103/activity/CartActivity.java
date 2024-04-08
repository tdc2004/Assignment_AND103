package com.chinhdev.assignment_and103.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.recyclerViewCartItems);
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(APIServer.class);
        getAllCartItems();
    }
    private void getAllCartItems() {
        Call<ArrayList<CartModel>> call = apiServer.getAllCart();
        call.enqueue(new Callback<ArrayList<CartModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CartModel>> call, Response<ArrayList<CartModel>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    displayCartItems(list);
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
}