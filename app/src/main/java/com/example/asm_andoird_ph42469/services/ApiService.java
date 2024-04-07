package com.example.asm_andoird_ph42469.services;

import com.example.asm_andoird_ph42469.Modal.SanPham;
import com.example.asm_andoird_ph42469.Modal.Response;
import com.example.asm_andoird_ph42469.Modal.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    String DOMAIN = "http://192.168.1.4:3000/api/";

    ApiService apiService  = new Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);
    @GET("sanphams")
    Call<Response<List<SanPham>>> getData();

    @Multipart
    @POST("sanphams/add")
    Call<Response<SanPham>> addStudent(
            @Part("ten") RequestBody ten,
            @Part("gia") RequestBody gia,
            @Part("soLuong") RequestBody soLuong,
            @Part MultipartBody.Part image);

    @DELETE("sanphams/delete/{id}")
    Call<Response<SanPham>> deleteStudent(@Path("id") String idStudent);

    @Multipart
    @PUT("sanphams/update/{id}")
    Call<Response<SanPham>> updateStudent(
            @Path("id") String idStudent,
            @Part("ten") RequestBody ten,
            @Part("gia") RequestBody gia,
            @Part("soLuong") RequestBody soLuong,
            @Part MultipartBody.Part image);

    @Multipart
    @POST("register")
    Call<Response<User>> registerUser(@Part("username") RequestBody username,
                            @Part("password") RequestBody password,
                            @Part("email") RequestBody email,
                            @Part("fullname") RequestBody fullname,
                            @Part MultipartBody.Part avatar );

    @POST("login")
    Call<Response<User>> loginUser(@Body User user);

    @GET("search")
    Call<Response<List<SanPham>>> searchStudent(@Query("key") String key);

    @GET("sort")
    Call<Response<List<SanPham>>> sortStudent(@Query("type") Integer type);
}
