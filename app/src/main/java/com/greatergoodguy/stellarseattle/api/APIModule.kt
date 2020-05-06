package com.greatergoodguy.stellarseattle.api

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class APIModule {
    @Provides
    fun provideOKHttpBaseClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    fun provideFourSquareApi(okHttpClient: OkHttpClient): FourSquareAPI {
        return Retrofit.Builder()
            .baseUrl("https://api.foursquare.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(FourSquareAPI::class.java)
    }
}
