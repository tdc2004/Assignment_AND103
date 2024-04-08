package com.chinhdev.assignment_and103.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chinhdev.assignment_and103.Interface.APIServer;
import com.chinhdev.assignment_and103.R;
import com.chinhdev.assignment_and103.model.CartModel;
import com.chinhdev.assignment_and103.model.SanPhamModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    ArrayList<CartModel> list;
    Context context;
    Retrofit retrofit;
    APIServer apiServer;
    private String url = "http://10.0.2.2:3000/";

    public CartAdapter(ArrayList<CartModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartModel cartModel = list.get(position);
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(APIServer.class);

        Call<SanPhamModel> call = apiServer.getSanPham(cartModel.getProduct_id());
        call.enqueue(new Callback<SanPhamModel>() {
            @Override
            public void onResponse(Call<SanPhamModel> call, Response<SanPhamModel> response) {
                if (response.isSuccessful()) {
                    SanPhamModel sanPham = response.body();
                    Glide.with(context).load(sanPham.getImage()).placeholder(R.drawable.image).into(holder.img_anh);
                    holder.tv_soLuong.setText("Số lượng sản phẩm: "+1);
                    holder.tv_gia.setText("Giá sản phẩm: "+sanPham.getPrice()+"");
                    holder.tv_name.setText("Tên sản phẩm: "+sanPham.getName());
                } else {
                    Log.e("Loi", response.message());
                }
            }

            @Override
            public void onFailure(Call<SanPhamModel> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_anh;
        TextView tv_name,tv_gia,tv_soLuong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_anh = itemView.findViewById(R.id.imageViewProduct);
            tv_name = itemView.findViewById(R.id.textViewProductName);
            tv_gia=itemView.findViewById(R.id.textViewProductPrice);
            tv_soLuong=itemView.findViewById(R.id.textViewProductQuantity);
        }
    }
}
