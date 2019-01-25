package com.example.gilgoldzweig.cobox.fragments.feed

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.v7.util.DiffUtil
import com.example.gilgoldzweig.cobox.base.BasePresenter
import com.example.gilgoldzweig.cobox.fragments.feed.adapters.diffutil.NewsDiffCallback
import com.example.gilgoldzweig.cobox.models.exceptions.NewsFeedFetchException
import com.example.gilgoldzweig.cobox.models.livedata.StateData
import com.example.gilgoldzweig.cobox.models.livedata.StateData.DataStatus
import com.example.gilgoldzweig.cobox.models.livedata.StateLiveData
import com.example.gilgoldzweig.cobox.models.news.*
import com.example.gilgoldzweig.cobox.modules.network.retorfit.NewsRetrofitService
import com.example.gilgoldzweig.cobox.timber.Timber
import com.nyx.tech.models.threads.CoroutineDispatchers
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.toptas.rssconverter.RssFeed
import me.toptas.rssconverter.RssItem
import java.util.*
import kotlin.concurrent.fixedRateTimer
import com.example.gilgoldzweig.cobox.fragments.feed.FeedFragmentContract as Contract

/**
 * The presenter for the feed fragment
 * all the modules are requested in the constructor by dagger.
 * This way makes it so much easier to add Unit test and replace dependencies with mocks
 */
class FeedFragmentPresenter(
        override var dispatchers: CoroutineDispatchers,
        var newsRetrofitService: NewsRetrofitService,
        var selectedItemLiveData: MutableLiveData<RssItem>) :
    BasePresenter<Contract.View>(),
    Contract.Presenter,
    Observer<StateData<NewsFeed>> {

    private val stateLiveData = StateLiveData<NewsFeed>()
    private var timer: Timer? = null
    private var isTimerRunning = false


    override fun attach(view: Contract.View, lifecycle: Lifecycle?) {
        super.attach(view, lifecycle)
        if (lifecycle != null) {
            stateLiveData.observe(view, this)
            isTimerRunning = true
            setupTimerTask()
        }
    }

    override fun detach() {
        stateLiveData.removeObserver(this)
        timer?.purge()
        timer = null
        super.detach()
    }

    /**
     * The state live data observer
     * I'm using live data so that even if the user exit the screen and comes back, he will receive the data
     */
    override fun onChanged(state: StateData<NewsFeed>?) {
        if (state == null) return

        when (state.status) {

            DataStatus.LOADING -> {
                Timber.w("Loading started") //In real use this would represent an analytics events
                view.onLoadingStarted()
            }

            DataStatus.SUCCESS -> {
                val newsFeed = state.data ?: return

                when (newsFeed) {
                    is BusinessNewsFeed ->
                        view.onBusinessFeedReceived(newsFeed.newsFeed)

                    is EntertainmentNewsFeed ->
                        view.onEntertainmentFeedReceived(newsFeed.newsFeed)

                    is EnvironmentNewsFeed ->
                        view.onEnvironmentFeedReceived(newsFeed.newsFeed)
                }
            }

            DataStatus.ERROR -> {
                val newsFeedFetchException = state.error as? NewsFeedFetchException ?: return

                //In real use this also updates firebase and Crashlytics
                Timber.crash(newsFeedFetchException,
                             "Failed fetching ${newsFeedFetchException.newsType.name}")

                view.onFeedRequestFailed(newsFeedFetchException.newsType)
            }

            DataStatus.CREATED -> Unit
        }
    }

    override fun onStart() {
        super.onStart()
        if (!isTimerRunning) {
            setupTimerTask()
        }
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
        timer = null
        isTimerRunning = false
    }


    /**
     * Fetches a news feed
     * and updates the state live data for error and success states
     *
     * @receiver rssFeed wrapped in a deferred so we can use [Deferred#await] and wait for the result
     * @return the result of fetch using the [stateLiveData]
     */
    fun Deferred<RssFeed>.fetchFeed(newsType: NewsType) {
        launch(networkContext) {

            val newsFeed: RssFeed

            try {
                newsFeed = await()
            } catch (e: Exception) {
                withContext(uiContext) {
                    stateLiveData.postError(NewsFeedFetchException(newsType, e))
                }
                return@launch
            }

            withContext(uiContext) {
                stateLiveData.postSuccess(
                    when (newsType) {

                        NewsType.BUSINESS ->
                            BusinessNewsFeed(newsFeed)

                        NewsType.ENTERTAINMENT ->
                            EntertainmentNewsFeed(newsFeed)

                        NewsType.ENVIRONMENT ->
                            EnvironmentNewsFeed(newsFeed)
                    })
            }
        }
    }

    private fun setupTimerTask() {
        if (timer == null) {
            timer = fixedRateTimer(period = 5000) {
                fetchAllFeeds()
            }
        }
    }

    override fun fetchBusinessFeed() =
        newsRetrofitService
            .getBusinessNewsFeed()
            .fetchFeed(NewsType.BUSINESS)

    override fun fetchEntertainmentFeed() =
        newsRetrofitService
            .getEntertainmentNewsFeed()
            .fetchFeed(NewsType.ENTERTAINMENT)

    override fun fetchEnvironmentFeed() =
        newsRetrofitService
            .getEnvironmentNewsFeed()
            .fetchFeed(NewsType.ENVIRONMENT)

    override fun calculateDiff(currentNews: NewsHolder, newNews: NewsHolder) {
        launch {
            val diff = DiffUtil.calculateDiff(NewsDiffCallback(currentNews, newNews))
            updateUi {
                onDiffCalculated(diff)
            }
        }

    }


    override fun fetchAllFeeds() {
        stateLiveData.postLoading()
        fetchBusinessFeed()
        fetchEntertainmentFeed()
        fetchEnvironmentFeed()
    }

    override fun selectFeedItem(feedItem: RssItem) {
        selectedItemLiveData.postValue(feedItem)
    }

}