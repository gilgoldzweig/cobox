package com.example.gilgoldzweig.cobox.fragments.home

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.example.gilgoldzweig.cobox.base.BasePresenter
import com.example.gilgoldzweig.cobox.fragments.home.HomeFragmentContract as Contract
import com.nyx.tech.models.threads.CoroutineDispatchers
import me.toptas.rssconverter.RssItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer

/**
 * The presenter for the home fragment
 * all the modules are requested in the constructor by dagger.
 * This way makes it so much easier to add Unit test and replace dependencies with mocks
 */
class HomeFragmentPresenter(
        override var dispatchers: CoroutineDispatchers,
        var selectedItemLiveData: MutableLiveData<RssItem>,
        var simpleDateFormat: SimpleDateFormat) : BasePresenter<Contract.View>(),
    Contract.Presenter,
    Observer<RssItem> {

    private var timer: Timer? = null
    private var isTimerRunning = false
    private var currentTime: Long = System.currentTimeMillis()
    private val currentTimeLiveData = MutableLiveData<String>()

    //I'm setting this as a parameter because I can't implement another one and I want to be able to dismiss it
    private var currentTimeUpdatedObserver = Observer<String> {
        it?.let(view::onTimeUpdated)
    }

    private val calendar = Calendar.getInstance(TimeZone.getDefault())

    override fun attach(view: Contract.View, lifecycle: Lifecycle?) {
        super.attach(view, lifecycle)
        if (lifecycle != null) {
            selectedItemLiveData.observe(view, this)
            currentTimeLiveData.observe(view, currentTimeUpdatedObserver)
        }
    }

    override fun detach() {
        selectedItemLiveData.removeObserver(this)
        currentTimeLiveData.removeObserver(currentTimeUpdatedObserver)
        super.detach()
    }

    override fun onStart() {
        super.onStart()
        if (!isTimerRunning) {
            currentTime = System.currentTimeMillis()
            setupTimerTask()
        }
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
        timer = null
        isTimerRunning = false
    }

    private fun setupTimerTask() {
        if (timer == null) {
            timer = fixedRateTimer(period = 1000) {
                currentTime += 1000
                calendar.timeInMillis = currentTime
                currentTimeLiveData.postValue(simpleDateFormat.format(calendar.time))
            }
        }
    }

    /**
     * Receive the selected item updates sent from [com.example.gilgoldzweig.cobox.fragments.feed.FeedFragmentPresenter]
     */
    override fun onChanged(feedItem: RssItem?) {
        if (feedItem == null) return
        view.onFeedItemTitleUpdated(feedItem.title ?: "No title")
    }
}