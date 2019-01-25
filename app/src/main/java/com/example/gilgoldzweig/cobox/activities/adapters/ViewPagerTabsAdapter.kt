package com.example.gilgoldzweig.cobox.activities.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.gilgoldzweig.cobox.models.ui.Tab

class ViewPagerTabsAdapter(var tabs: List<Tab>, fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment =
            tabs[position].fragment

    override fun getPageTitle(position: Int): CharSequence? =
            tabs[position].title

    override fun getCount(): Int =
            tabs.size
}