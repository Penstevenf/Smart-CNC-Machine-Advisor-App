package com.example.winteq.api;

import com.example.winteq.model.user.UserData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

//agar bisa menghit api atau mengirim data seperti di postman
public interface Api_Interface {

    @FormUrlEncoded
    @POST("login.php")
        //pilih login yang di model <login>
    Call<UserData> loginResponse(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
        //pilih login yang di model <login>
    Call<UserData> registerResponse(
            @Field("username") String username,
            @Field("password") String password,
            @Field("fullname") String fullname,
            @Field("email") String email
    );

}