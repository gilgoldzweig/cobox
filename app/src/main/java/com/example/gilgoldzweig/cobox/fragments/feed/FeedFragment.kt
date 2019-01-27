package com.example.gilgoldzweig.cobox.fragments.feed

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.gilgoldzweig.cobox.R
import com.example.gilgoldzweig.cobox.application.CoboxApplication
import com.example.gilgoldzweig.cobox.fragments.feed.adapters.NewsFeedAdapter
import com.example.gilgoldzweig.cobox.models.news.NewsHolder
import com.example.gilgoldzweig.cobox.models.news.NewsType
import kotlinx.android.synthetic.main.fragment_feed.feed_fragment_business_news_button
import kotlinx.android.synthetic.main.fragment_feed.feed_fragment_news_rcv
import kotlinx.android.synthetic.main.fragment_feed.feed_fragment_news_refresh_layout
import kotlinx.android.synthetic.main.fragment_feed.feed_fragment_other_news_button
import kotlinx.android.synthetic.main.fragment_feed.feed_fragment_root
import me.toptas.rssconverter.RssFeed
import me.toptas.rssconverter.RssItem
import javax.inject.Inject

class FeedFragment : Fragment(), FeedFragmentContract.View {

    @Inject
    lateinit var presenter: FeedFragmentContract.Presenter

    private lateinit var newsFeedAdapter: NewsFeedAdapter

    private var newsHolder = NewsHolder()

    private var newsShowedFirstTime = false

    private var selectedButtonBackgroundColor: Int = -1
    private var selectedButtonTextColor: Int = -1
    private var buttonBackgroundColor: Int = -1
    private var buttonTextColor: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity!!.application as CoboxApplication)
                .applicationComponent.inject(this)

        val context = requireContext()

        newsFeedAdapter = NewsFeedAdapter(context, onItemClicked = presenter::selectFeedItem)

        selectedButtonBackgroundColor = ContextCompat.getColor(context, R.color.primary)
        buttonBackgroundColor = ContextCompat.getColor(context, R.color.primary_dark)

        selectedButtonTextColor = ContextCompat.getColor(context, R.color.white)
        buttonTextColor = ContextCompat.getColor(context, R.color.secondary_text)

        //Automatically detach's in onDestroy provided by the lifecycle
        presenter.attach(this, lifecycle)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_feed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Creating new linear layout every time because of
        //https://stackoverflow.com/a/32572653/5362029
        feed_fragment_news_rcv.layoutManager = LinearLayoutManager(requireContext())
        feed_fragment_news_rcv.adapter = newsFeedAdapter

        feed_fragment_business_news_button.setOnClickListener {
            if (newsHolder.showingBusinessNewsFeed) return@setOnClickListener

            toggleButtonColors(feed_fragment_business_news_button,
                               feed_fragment_other_news_button)

            newsHolder.showingBusinessNewsFeed = true
            newsFeedAdapter.updateNews(newsHolder)
        }

        feed_fragment_other_news_button.setOnClickListener {
            if (!newsHolder.showingBusinessNewsFeed) return@setOnClickListener

            toggleButtonColors(feed_fragment_other_news_button,
                               feed_fragment_business_news_button)

            newsHolder.showingBusinessNewsFeed = false
            newsFeedAdapter.updateNews(newsHolder)
        }

    }

    private fun toggleButtonColors(selected: Button, unselected: Button) {

        selected.backgroundTintList =
                ColorStateList.valueOf(selectedButtonBackgroundColor)

        unselected.backgroundTintList =
                ColorStateList.valueOf(buttonBackgroundColor)

        selected.setTextColor(selectedButtonTextColor)
        unselected.setTextColor(buttonTextColor)

    }

    override fun onLoadingStarted() {
        feed_fragment_news_refresh_layout.isRefreshing = true
    }

    override fun onBusinessFeedReceived(newsFeed: RssFeed) {
        newsHolder.businessNews = newsFeed.items ?: return

        if (newsHolder.showingBusinessNewsFeed) {
            if (newsShowedFirstTime) {
                presenter.calculateDiff(newsFeedAdapter.news, newsHolder)
            } else {
                newsFeedAdapter.updateNews(newsHolder)
                newsShowedFirstTime = false
                feed_fragment_news_refresh_layout.isRefreshing = false
            }
        }
    }

    override fun onEntertainmentFeedReceived(newsFeed: RssFeed) {
        newsHolder.entertainmentNews = newsFeed.items ?: return

        if (!newsHolder.showingBusinessNewsFeed) {
            presenter.calculateDiff(newsFeedAdapter.news, newsHolder)
        }

    }

    override fun onEnvironmentFeedReceived(newsFeed: RssFeed) {
        newsHolder.environmentNews = newsFeed.items ?: return

        if (!newsHolder.showingBusinessNewsFeed) {
            presenter.calculateDiff(newsFeedAdapter.news, newsHolder)
        }
    }

    override fun onFeedRequestFailed(newsType: NewsType) {
        Snackbar.make(feed_fragment_root, "Failed to fetch ${newsType.name} news", Snackbar.LENGTH_LONG)
                .setAction("Try again") {
                    when (newsType) {

                        NewsType.BUSINESS ->
                            presenter.fetchBusinessFeed()

                        NewsType.ENTERTAINMENT ->
                            presenter.fetchEntertainmentFeed()

                        NewsType.ENVIRONMENT ->
                            presenter.fetchEnvironmentFeed()

                    }
                }
                .show()
    }

    override fun onDiffCalculated(diffResult: DiffUtil.DiffResult) {
        newsFeedAdapter.updateNews(newsHolder, diffResult)
        feed_fragment_news_refresh_layout.isRefreshing = false
    }

    override fun onFeedItemClicked(feedItem: RssItem) {
        //If we want to perform any other action than update the other fragment
    }

    override fun onDestroyView() {
        //https://stackoverflow.com/questions/35520946/leak-canary-recyclerview-leaking-madapter
        feed_fragment_news_rcv.layoutManager = null
        feed_fragment_news_rcv.adapter = null
        super.onDestroyView()
    }
}