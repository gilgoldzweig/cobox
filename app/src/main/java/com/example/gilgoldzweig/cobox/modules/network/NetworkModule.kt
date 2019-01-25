package com.example.gilgoldzweig.cobox.modules.network

import com.example.gilgoldzweig.cobox.modules.network.retorfit.NewsRetrofitService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import me.toptas.rssconverter.RssConverterFactory
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory =
        RssConverterFactory.create()

    @Provides
    @Singleton
    fun provideCallAdapterFactory(): CallAdapter.Factory =
        CoroutineCallAdapterFactory()


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()


    @Provides
    @Singleton
    fun provideRetrofit(
            converterFactory: Converter.Factory,
            callAdapterFactory: CallAdapter.Factory,
            okHttpClient: OkHttpClient): Retrofit =

        Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()


    @Provides
    @Singleton
    fun provideNewsRetrofitService(retrofit: Retrofit): NewsRetrofitService =
        retrofit.create<NewsRetrofitService>(NewsRetrofitService::class.java)


    companion object {

        private val BASE_URL = "http://feeds.reuters.com/reuters/"
    }
}
