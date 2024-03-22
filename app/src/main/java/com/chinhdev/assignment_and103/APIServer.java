package com.chinhdev.assignment_and103;

import com.chinhdev.assignment_and103.model.SanPhamModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIServer {
    @GET("/list")
    Call<ArrayList<SanPhamModel>> getSanPham();
    @POST("/list/add")
    Call<Void> postSanPham(@Body SanPhamModel sanPhamModel);
    @PUT("list/{id}")
    Call<Void> putSanPham(@Path("id") String id, @Body SanPhamModel sanPhamModel);
    @DELETE("list/{id}")
    Call<Void> deleteSanPham(@Path("id") String id);
}
