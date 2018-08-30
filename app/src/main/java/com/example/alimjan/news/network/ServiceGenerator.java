package com.example.alimjan.news.network;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    // Base url of API service.
    private final static String BASE_URL = "https://hn.algolia.com/api/v1/";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                // Request intercepted and added extra query param.
                HttpUrl httpUrl = chain
                        .request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("query", "android").build();

                Request request = chain.request().newBuilder().url(httpUrl).build();

                return chain.proceed(request);
            })
            .build();


    // retrofit instance for creating endpoint interface.
    private static Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();


    /**
     * Create an implementation of the API endpoints defined by the {@code serviceClass} interface.
     *
     * @param serviceClass endpoint definition class instance.
     * @param <T>          This would be type of endpoint interface.
     * @return returns created API endpoint for easy access.
     */
    public static <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
