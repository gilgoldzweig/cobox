package com.example.gilgoldzweig.cobox.modules.network.retorfit

import kotlinx.coroutines.Deferred
import me.toptas.rssconverter.RssFeed
import retrofit2.http.GET

interface NewsRetrofitService {

    @GET("businessNews")
    fun getBusinessNewsFeed(): Deferred<RssFeed>

    @GET("entertainment")
    fun getEntertainmentNewsFeed(): Deferred<RssFeed>

    @GET("environment")
    fun getEnvironmentNewsFeed(): Deferred<RssFeed>

}
