package com.example.githubcatalog.data.retrofit

import com.example.githubcatalog.BuildConfig
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class APIConfig {
    companion object{
        fun getApiService(): GithubService {
            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(
                    Interceptor { chain ->
                        val newRequest: Request = chain.request().newBuilder()
                            .addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
                            .build()
                        chain.proceed(newRequest)
                    }
                )
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(GithubService::class.java)
        }
    }
}