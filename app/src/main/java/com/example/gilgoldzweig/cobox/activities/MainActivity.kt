package com.example.gilgoldzweig.cobox.activities

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import com.example.gilgoldzweig.cobox.R
import com.example.gilgoldzweig.cobox.activities.adapters.ViewPagerTabsAdapter
import com.example.gilgoldzweig.cobox.application.CoboxApplication
import com.example.gilgoldzweig.cobox.models.ui.Tab
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: PagerAdapter

    @Inject
    lateinit var tabs: List<Tab>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as CoboxApplication).fragmentsComponent.inject(this)

        setSupportActionBar(main_activity_toolbar)

        pagerAdapter = ViewPagerTabsAdapter(tabs, supportFragmentManager)

        main_activity_view_pager.adapter = pagerAdapter

        main_activity_tab_layout.setupWithViewPager(main_activity_view_pager)
    }
}