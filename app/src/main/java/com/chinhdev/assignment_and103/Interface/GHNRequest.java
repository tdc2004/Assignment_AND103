package com.chinhdev.assignment_and103.Interface;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GHNRequest {
    public final static String SHOPID = "191536";
    public final static String TokenGHN = "cb5c70ec-ed42-11ee-b1d4-92b443b7a897";
    private GHNService ghnRequestInterface;

    public GHNRequest(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("ShopId", SHOPID)
                        .addHeader("Token", TokenGHN)
                        .build();
                return chain.proceed(request);
            }
        });

        ghnRequestInterface = new Retrofit.Builder()
                .baseUrl(GHNService.GHN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build().create(GHNService.class);
    }

    public GHNService callAPI(){
        return ghnRequestInterface;
    }
}
