package com.example.gilgoldzweig.cobox.fragments.home

import android.arch.lifecycle.LifecycleOwner
import com.example.gilgoldzweig.cobox.base.BaseContract

interface HomeFragmentContract : BaseContract {

    interface Presenter : BaseContract.Presenter<View>

    interface View : BaseContract.View, LifecycleOwner {

        fun onTimeUpdated(time: String)
        fun onFeedItemTitleUpdated(title: String)
    }
}