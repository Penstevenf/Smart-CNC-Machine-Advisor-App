package com.example.winteq.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//tempat coding retrofit dan base url
public class ApiClient {
//    private static final String BASE_URL = "http://192.168.100.12/winteq/";
    private static final String BASE_URL = "http://192.168.0.124/winteq/";

    private static Retrofit retrofit;

    //sesuaikan nama get sesuai dengan nama class
    //cara membuat retrofit
    public static Retrofit getClient() {

        if(retrofit == null)
        {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
