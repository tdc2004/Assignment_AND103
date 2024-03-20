package com.chinhdev.assignment_and103.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chinhdev.assignment_and103.APIServer;
import com.chinhdev.assignment_and103.R;
import com.chinhdev.assignment_and103.adapter.SanPhamAdapter;
import com.chinhdev.assignment_and103.model.SanPhamModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Home_Fragment extends Fragment {
    Toolbar toolbar;
    RecyclerView recyclerView;
    SanPhamAdapter adapter;
    ArrayList<SanPhamModel> list;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar_home);
        recyclerView = view.findViewById(R.id.recycler);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIServer.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIServer apiService = retrofit.create(APIServer.class);
        Call<ArrayList<SanPhamModel>> call = apiService.getSanPham();
        call.enqueue(new Callback<ArrayList<SanPhamModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SanPhamModel>> call, Response<ArrayList<SanPhamModel>> response) {
                if (response.isSuccessful()){
                    list = response.body();
                    adapter = new SanPhamAdapter(getContext(),list);
                    recyclerView.setAdapter(adapter);
                    Log.d("API_Response", "Dữ liệu nhận được từ API: " + list.size());
                }else{
                    Toast.makeText(getContext(), "Hết cứu", Toast.LENGTH_SHORT).show();
                    Log.e( "onResponse: ", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SanPhamModel>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });

        CreateToolbar();


    }
    private void CreateToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Trang chủ");

    }
}