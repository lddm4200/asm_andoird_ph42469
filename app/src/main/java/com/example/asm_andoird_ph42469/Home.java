package com.example.asm_andoird_ph42469;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.asm_andoird_ph42469.adapter.SanPhamAdapter;
import com.example.asm_andoird_ph42469.Modal.SanPham;
import com.example.asm_andoird_ph42469.services.ApiService;
import com.example.asm_andoird_ph42469.Modal.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class Home extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 10;
    List<SanPham> list = new ArrayList<>();
    FloatingActionButton fltadd;
    ImageView imagePiker;
    private File file;

    public RecyclerView rcvSV ;
    public SanPhamAdapter adapter;

    Button btnUp, btnDown, btnSearch;
    EditText txtsearch;


    private File createFileFormUri (Uri path, String name) {
        File _file = new File(Home.this.getCacheDir(), name + ".png");
        try {
            InputStream in = Home.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) >0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            Log.d("123123", "createFileFormUri: " +_file);
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fltadd = findViewById(R.id.fltadd);
        rcvSV = findViewById(R.id.rcvSV);

        loadData();
        getView();

        fltadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(Home.this,new SanPham(),1,list);
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortStudent(-1);
            }
        });
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortStudent(1);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = txtsearch.getText().toString().trim();
                searchStudent(key);
            }
        });
    }

    private void getView(){
        btnDown = findViewById(R.id.btnDown);
        btnUp = findViewById(R.id.btnUp);
        btnSearch = findViewById(R.id.btnSearch);
        txtsearch = findViewById(R.id.txtsearch);
    }


    public void showDialog (Context context, SanPham sanPham, Integer type, List<SanPham> list){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater= ((Activity) context).getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_add_sanpham,null);
        builder.setView(view);
        Dialog dialog=builder.create();
        dialog.show();

        EditText edtMaSV = view.findViewById(R.id.edtMaSV);
        EditText edtNameSV = view.findViewById(R.id.edtNameSV);
        EditText edtDiemTB = view.findViewById(R.id.edtDiemTB);
        imagePiker = view.findViewById(R.id.imgAvatarSV);
        Button btnChonAnh =view.findViewById(R.id.btnChonAnh);
        Button btnSave =view.findViewById(R.id.btnSave);

        if (type == 0){
            edtMaSV.setText(sanPham.getTen());
            edtNameSV.setText(sanPham.getGia()+"");
            edtDiemTB.setText(sanPham.getSoLuong()+"");
            Glide.with(view).load(sanPham.getImage()).into(imagePiker);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String masv = edtMaSV.getText().toString().trim();
                String name = edtNameSV.getText().toString();
                String diemTB = edtDiemTB.getText().toString();
                if (masv.isEmpty() || name.isEmpty()|| diemTB.isEmpty()){
                    Toast.makeText(context, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                }else if(!iSo(name)) {

                    Toast.makeText(context, "Giá phải là số", Toast.LENGTH_SHORT).show();
                }else if(!iSo(diemTB)) {

                    Toast.makeText(context, "So luong phải là số", Toast.LENGTH_SHORT).show();
                }
                    else {



                        RequestBody rTen = RequestBody.create(MediaType.parse("multipart/form-data"), masv);
                        RequestBody rGia = RequestBody.create(MediaType.parse("multipart/form-data"), name);
                        RequestBody rSoLuong = RequestBody.create(MediaType.parse("multipart/form-data"), diemTB);
                        MultipartBody.Part muPart;
                        if (file != null){
                            RequestBody rAvatar = RequestBody.create(MediaType.parse("image/*"),file);
                            muPart = MultipartBody.Part.createFormData("image",file.getName(),rAvatar);
                        }else {
                            muPart = null;
                        }

                        Call<Response<SanPham>> call = ApiService.apiService.addStudent(rTen,rGia,rSoLuong,muPart);
                        if(type == 0){
                            call = ApiService.apiService.updateStudent(sanPham.get_id(),rTen,rGia,rSoLuong,muPart);
                        }
                        call.enqueue(new Callback<Response<SanPham>>() {
                            @Override
                            public void onResponse(Call<Response<SanPham>> call, retrofit2.Response<Response<SanPham>> response) {
                                if (response.isSuccessful()){
                                    if(response.body().getStatus() == 200){
                                        Toast.makeText(Home.this,"Success", Toast.LENGTH_SHORT).show();
                                        loadData();
                                        dialog.dismiss();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Response<SanPham>> call, Throwable t) {
                                Toast.makeText(Home.this, "Fail"+t, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }

            }
        });

        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }



    public void loadData (){

        Call<Response<List<SanPham>>> call = ApiService.apiService.getData();

        call.enqueue(new Callback<Response<List<SanPham>>>() {
            @Override
            public void onResponse(Call<Response<List<SanPham>>> call, retrofit2.Response<Response<List<SanPham>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 200){
                        List<SanPham> ds = response.body().getData();
                        Log.d("List","a : "+response.body().getData());
                        getData(ds);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<List<SanPham>>> call, Throwable t) {
                Log.d("List","a : "+ t);
            }
        });
    }

    public void getData (List<SanPham> list){
        adapter = new SanPhamAdapter(Home.this, list);
        rcvSV.setLayoutManager(new LinearLayoutManager(Home.this));
        rcvSV.setAdapter(adapter);
    }

    public void searchStudent(String key){
        Call<Response<List<SanPham>>> call = ApiService.apiService.searchStudent(key);
        call.enqueue(new Callback<Response<List<SanPham>>>() {
            @Override
            public void onResponse(Call<Response<List<SanPham>>> call, retrofit2.Response<Response<List<SanPham>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 200){
                        List<SanPham> ds = response.body().getData();
                        getData(ds);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<List<SanPham>>> call, Throwable t) {
                Log.d("Search","Lỗi : "+ t);
            }
        });
    }

    public void sortStudent(Integer type){
        Call<Response<List<SanPham>>> call = ApiService.apiService.sortStudent(type);
        call.enqueue(new Callback<Response<List<SanPham>>>() {
            @Override
            public void onResponse(Call<Response<List<SanPham>>> call, retrofit2.Response<Response<List<SanPham>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 200){
                        List<SanPham> ds = response.body().getData();
                        getData(ds);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<List<SanPham>>> call, Throwable t) {

            }
        });
    }

    private void chooseImage() {
        Log.d("123123", "chooseAvatar: " +123123);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
    }
    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        Uri imageUri = data.getData();

                        Log.d("RegisterActivity", imageUri.toString());

                        Log.d("123123", "onActivityResult: "+data);

                        file = createFileFormUri(imageUri, "image");

                        Glide.with(imagePiker)
                                .load(imageUri)
                                .centerCrop()
                                .into(imagePiker);

                    }
                }
            });
    public boolean iSo ( String so){
        return so.matches("\\d+");
    }
}