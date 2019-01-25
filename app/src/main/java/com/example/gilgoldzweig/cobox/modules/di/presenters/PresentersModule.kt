package com.example.gilgoldzweig.cobox.modules.di.presenters

import android.arch.lifecycle.MutableLiveData
import com.example.gilgoldzweig.cobox.fragments.feed.FeedFragmentContract
import com.example.gilgoldzweig.cobox.fragments.feed.FeedFragmentPresenter
import com.example.gilgoldzweig.cobox.fragments.home.HomeFragmentContract
import com.example.gilgoldzweig.cobox.fragments.home.HomeFragmentPresenter
import com.example.gilgoldzweig.cobox.modules.di.threads.ThreadModule
import com.example.gilgoldzweig.cobox.modules.network.NetworkModule
import com.example.gilgoldzweig.cobox.modules.network.retorfit.NewsRetrofitService
import com.nyx.tech.models.threads.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import me.toptas.rssconverter.RssItem
import java.text.SimpleDateFormat
import javax.inject.Singleton

@Module(includes = [ThreadModule::class, NetworkModule::class])
class PresentersModule {


    @Provides
    @Singleton
    fun provideSelectedFeedItemLiveData(): MutableLiveData<RssItem> =
        MutableLiveData()

    @Provides
    @Singleton
    fun provideFeedPresenter(
            dispatchers: CoroutineDispatchers,
            newsRetrofitService: NewsRetrofitService,
            selectedItemLiveData: MutableLiveData<RssItem>): FeedFragmentContract.Presenter =
        FeedFragmentPresenter(dispatchers, newsRetrofitService, selectedItemLiveData)


    @Provides
    @Singleton
    fun provideSimpleDateTimeFormat(): SimpleDateFormat =
        SimpleDateFormat("dd/MM/YYYY HH:mm:ss")


    @Provides
    @Singleton
    fun provideHomePresenter(
            dispatchers: CoroutineDispatchers,
            selectedItemLiveData: MutableLiveData<RssItem>,
            simpleDateFormat: SimpleDateFormat): HomeFragmentContract.Presenter =
        HomeFragmentPresenter(dispatchers, selectedItemLiveData, simpleDateFormat)


}
