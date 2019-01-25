package com.example.gilgoldzweig.cobox.fragments.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import com.example.gilgoldzweig.cobox.R
import com.example.gilgoldzweig.cobox.application.CoboxApplication
import kotlinx.android.synthetic.main.fragment_home.home_fragment_date_txt
import kotlinx.android.synthetic.main.fragment_home.home_fragment_select_news_txt
import javax.inject.Inject

class HomeFragment : Fragment(), HomeFragmentContract.View {

    @Inject
    lateinit var presenter: HomeFragmentContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity!!.application as CoboxApplication)
            .applicationComponent.inject(this)

        //Automatically detach's in onDestroy provided by the lifecycle
        presenter.attach(this, lifecycle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onTimeUpdated(time: String) {
        home_fragment_date_txt.text = time
    }

    override fun onFeedItemTitleUpdated(title: String) {
        home_fragment_select_news_txt.text = title
    }

    companion object {
        const val INJECT_NAMED_ID = "HomeFragment"
    }
}
