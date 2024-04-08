package com.chinhdev.assignment_and103.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chinhdev.assignment_and103.Interface.APIServer;
import com.chinhdev.assignment_and103.Interface.ItemClickListener;
import com.chinhdev.assignment_and103.R;
import com.chinhdev.assignment_and103.adapter.SanPhamAdapter;
import com.chinhdev.assignment_and103.model.SanPhamModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton button;
    Retrofit retrofit;
    APIServer apiServer;
    private String url = "http://10.0.2.2:3000/";
    ArrayList<SanPhamModel> list;

    SanPhamAdapter adapter;
    ImageView imgUp;
    Uri imageUri;
    File file;
    SearchView searchView;
    ImageView btn_asc,btn_des;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recycler);
        navigationView = findViewById(R.id.menu);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_home){
                    startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                }else {
                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
                }
                return false;
            }
        });
        button = findViewById(R.id.btn_add);
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(APIServer.class);
        searchView = findViewById(R.id.search_view);
        btn_asc = findViewById(R.id.image_view1);
        btn_des = findViewById(R.id.image_view2);
        btn_asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ArrayList<SanPhamModel>> call = apiServer.filterAsc();
                call.enqueue(new Callback<ArrayList<SanPhamModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<SanPhamModel>> call, Response<ArrayList<SanPhamModel>> response) {
                        if (response.isSuccessful()) {
                            ArrayList<SanPhamModel> sanPhamList = response.body();
                            if (sanPhamList != null) {
                                adapter.updateData(sanPhamList);
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SanPhamModel>> call, Throwable t) {
                        Toast.makeText(HomeActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btn_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ArrayList<SanPhamModel>> call = apiServer.filterDes();
                call.enqueue(new Callback<ArrayList<SanPhamModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<SanPhamModel>> call, Response<ArrayList<SanPhamModel>> response) {
                        if (response.isSuccessful()) {
                            list = response.body();
                            if (list != null) {
                                adapter.updateData(list);
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SanPhamModel>> call, Throwable t) {

                    }
                });
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        handleCallData();
        button.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View v1 = LayoutInflater.from(this).inflate(R.layout.item_add, null);
            builder.setView(v1);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            EditText edtName = v1.findViewById(R.id.edt_name);
            EditText edtPrice = v1.findViewById(R.id.edt_price);
            EditText edtQuantity = v1.findViewById(R.id.edt_quantity);
            EditText edtInventory = v1.findViewById(R.id.edt_inventory);
            EditText edtMota = v1.findViewById(R.id.edt_moTa);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            imgUp = v1.findViewById(R.id.img_dialog);
            imgUp.setOnClickListener(view1 -> {
                chooseImage();
            });
            Button btnSave = v1.findViewById(R.id.btn_save);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = edtName.getText().toString();
                    String gia = (edtPrice.getText().toString());
                    String sl = (edtQuantity.getText().toString());
                    String tk = (edtInventory.getText().toString());
                    String mota = edtMota.getText().toString();
                    if (name.equals("")||gia.equals("")||sl.equals("")||tk.equals("")||mota.equals("")){
                        Toast.makeText(HomeActivity.this, "Vui lòng không bỏ trống", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int price;
                    try {
                        price = Integer.parseInt(gia);
                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this, "Giá phải là số", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int quantity;
                    try {
                        quantity = Integer.parseInt(sl);
                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this, "số lượng phải là số", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int inventory;
                    try {
                        inventory = Integer.parseInt(tk);
                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this, "Tồn kho phải là số", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SanPhamModel newSanPham = new SanPhamModel();
                    newSanPham.setName(name);
                    newSanPham.setPrice(price);
                    newSanPham.setQuantity(quantity);
                    newSanPham.setInventory(inventory);
                    newSanPham.setDescription(mota);

                    if (imageUri != null) {
                        file = createFileFromUri(imageUri, "image");
                        if (file != null) {
                            uploadSanPham(newSanPham);
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(HomeActivity.this, "Không thể chọn ảnh", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        });
    }
    private void filter(String text) {
        ArrayList<SanPhamModel> filteredList = new ArrayList<>();
        for (SanPhamModel item : list) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }


    private void handleCallData() {
        Call<ArrayList<SanPhamModel>> call = apiServer.getSanPham();
        call.enqueue(new Callback<ArrayList<SanPhamModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SanPhamModel>> call, Response<ArrayList<SanPhamModel>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    adapter = new SanPhamAdapter(HomeActivity.this, list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    recyclerView.setAdapter(adapter);
                    adapter.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void UpdateItem(int position) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            View v1 = LayoutInflater.from(HomeActivity.this).inflate(R.layout.item_add, null);
                            builder.setView(v1);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            EditText edtName = v1.findViewById(R.id.edt_name);
                            EditText edtPrice = v1.findViewById(R.id.edt_price);
                            EditText edtQuantity = v1.findViewById(R.id.edt_quantity);
                            EditText edtInventory = v1.findViewById(R.id.edt_inventory);
                            EditText edtMota = v1.findViewById(R.id.edt_moTa);
                            TextView tv = v1.findViewById(R.id.tv_title);
                            tv.setText("Sửa Sản Phẩm");
                            SanPhamModel selectedProduct = list.get(position);
                            imgUp = v1.findViewById(R.id.img_dialog);
                            imgUp.setOnClickListener(view1 -> {
                                chooseImage();
                            });
                            edtName.setText(selectedProduct.getName());
                            edtPrice.setText(String.valueOf(selectedProduct.getPrice()));
                            edtQuantity.setText(String.valueOf(selectedProduct.getQuantity()));
                            edtInventory.setText(String.valueOf(selectedProduct.getInventory()));
                            edtMota.setText(selectedProduct.getDescription());
                            Glide.with(HomeActivity.this).load(selectedProduct.getImage()).placeholder(R.drawable.image).into(imgUp);
                            Button btn_ok = v1.findViewById(R.id.btn_save);
                            btn_ok.setOnClickListener(v->{
                                String newName = edtName.getText().toString();
                                int newPrice = Integer.parseInt(edtPrice.getText().toString());
                                int newQuantity = Integer.parseInt(edtQuantity.getText().toString());
                                int newInventory = Integer.parseInt(edtInventory.getText().toString());
                                String newDescription = edtMota.getText().toString();

                                selectedProduct.setName(newName);
                                selectedProduct.setPrice(newPrice);
                                selectedProduct.setQuantity(newQuantity);
                                selectedProduct.setInventory(newInventory);
                                selectedProduct.setDescription(newDescription);

                                updateSanPham(selectedProduct);

                                alertDialog.dismiss();
                            });

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SanPhamModel>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void uploadSanPham(SanPhamModel newSanPham) {
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), newSanPham.getName());
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(newSanPham.getPrice()));
        RequestBody quantityBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(newSanPham.getQuantity()));
        RequestBody inventoryBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(newSanPham.getInventory()));
        RequestBody motaBody = RequestBody.create(MediaType.parse("text/plain"), newSanPham.getDescription());

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        Call<Void> call = apiServer.postSanPham(nameBody, motaBody, priceBody, quantityBody, inventoryBody, image);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    handleCallData();
                    Toast.makeText(HomeActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        Glide.with(HomeActivity.this).load(imageUri).into(imgUp);
                    }
                }
            });
    private void updateSanPham(SanPhamModel updatedSanPham) {
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), updatedSanPham.getName());
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(updatedSanPham.getPrice()));
        RequestBody quantityBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(updatedSanPham.getQuantity()));
        RequestBody inventoryBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(updatedSanPham.getInventory()));
        RequestBody motaBody = RequestBody.create(MediaType.parse("text/plain"), updatedSanPham.getDescription());

        // Tạo requestBody cho hình ảnh nếu có
        RequestBody requestFile = null;
        MultipartBody.Part image = null;
        if (imageUri != null) {
            file = createFileFromUri(imageUri, "image");
            if (file != null) {
                requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            } else {
                Toast.makeText(HomeActivity.this, "Không thể chọn ảnh", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Call<Void> call = apiServer.putSanPham(updatedSanPham.get_id(), nameBody, motaBody, priceBody, quantityBody, inventoryBody, image);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    handleCallData();
                    Toast.makeText(HomeActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private File createFileFromUri(Uri path, String name) {
        File _file = new File(HomeActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            Log.d("File created", "createFileFromUri: " + _file);
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getImage.launch(intent);
    }
}
