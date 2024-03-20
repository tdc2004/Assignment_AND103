package com.chinhdev.assignment_and103.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chinhdev.assignment_and103.R;
import com.chinhdev.assignment_and103.model.SanPhamModel;

import java.util.ArrayList;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    Context context;
    ArrayList<SanPhamModel> list;

    public SanPhamAdapter(Context context, ArrayList<SanPhamModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPhamModel sanPhamModel = list.get(position);
        holder.tenTextView.setText("Chinh Dep Yra");
        holder.giaTextView.setText(sanPhamModel.getGia()+"");
        holder.soLuongTextView.setText(sanPhamModel.getSoLuong()+"");
        holder.tonKhoTextView.setText(sanPhamModel.isTonKho()+"");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tenTextView, giaTextView, soLuongTextView, tonKhoTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenTextView = itemView.findViewById(R.id.tvName);
            giaTextView = itemView.findViewById(R.id.tvGia);
            soLuongTextView = itemView.findViewById(R.id.tvSoluong);
            tonKhoTextView = itemView.findViewById(R.id.tvTonkho);
        }
    }
}
