package com.chinhdev.assignment_and103;

import com.chinhdev.assignment_and103.model.SanPhamModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIServer {
    String DOMAIN = "http://192.168.0.102/3000/";
    @GET("/api/list")
    Call<ArrayList<SanPhamModel>> getSanPham();
}
