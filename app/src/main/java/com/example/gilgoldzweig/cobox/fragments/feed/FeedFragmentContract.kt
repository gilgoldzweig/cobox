package com.example.gilgoldzweig.cobox.fragments.feed

import android.arch.lifecycle.LifecycleOwner
import android.support.annotation.UiThread
import android.support.v7.util.DiffUtil
import com.example.gilgoldzweig.cobox.base.BaseContract
import com.example.gilgoldzweig.cobox.models.news.NewsHolder
import com.example.gilgoldzweig.cobox.models.news.NewsType
import me.toptas.rssconverter.RssFeed
import me.toptas.rssconverter.RssItem

interface FeedFragmentContract : BaseContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun fetchBusinessFeed()
        fun fetchEntertainmentFeed()
        fun fetchEnvironmentFeed()
        fun fetchAllFeeds()

        fun calculateDiff(currentNews: NewsHolder, newNews: NewsHolder)

        fun selectFeedItem(feedItem: RssItem)
    }

    @UiThread
    interface View : BaseContract.View, LifecycleOwner {

        fun onLoadingStarted()

        fun onBusinessFeedReceived(newsFeed: RssFeed)
        fun onEntertainmentFeedReceived(newsFeed: RssFeed)
        fun onEnvironmentFeedReceived(newsFeed: RssFeed)

        fun onDiffCalculated(diffResult: DiffUtil.DiffResult)
        /**
         * @param newsType the NewsType of the failed fetch request
         * the exception is already handled by the presenter
         */
        fun onFeedRequestFailed(newsType: NewsType)

        fun onFeedItemClicked(feedItem: RssItem)

    }

}
