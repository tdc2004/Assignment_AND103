package com.chinhdev.assignment_and103.Interface;

import com.chinhdev.assignment_and103.model.CartModel;
import com.chinhdev.assignment_and103.model.SanPhamModel;
import com.chinhdev.assignment_and103.model.UserModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIServer {
    @GET("/list")
    Call<ArrayList<SanPhamModel>> getSanPham();
    @Multipart
    @POST("/list/add")
    Call<Void> postSanPham(
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("quantity") RequestBody quantity,
            @Part("inventory") RequestBody inventory,
            @Part MultipartBody.Part image
    );
    @Multipart
    @PUT("/list/{id}")
    Call<Void> putSanPham(
            @Path("id") String id,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("quantity") RequestBody quantity,
            @Part("inventory") RequestBody inventory,
            @Part MultipartBody.Part image
    );


    @DELETE("list/{id}")
    Call<Void> deleteSanPham(@Path("id") String id);
    @Multipart
    @POST("register")
    Call<UserModel> register(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part avatar

    );

    @POST("/login")
    Call<Void> loginUser(@Body UserModel userModel);

    @GET("/list/sort/des")
    Call<ArrayList<SanPhamModel>> filterDes();
    @GET("/list/sort/asc")
    Call<ArrayList<SanPhamModel>> filterAsc();

    @GET("/list/{id}")
    Call<SanPhamModel> getSanPham(@Path("id") String id);
    @POST("/addCart")
    Call<Void> addToCart(@Body CartModel cartModel);
    @GET("/listCart")
    Call<ArrayList<CartModel>> getAllCart();
}
