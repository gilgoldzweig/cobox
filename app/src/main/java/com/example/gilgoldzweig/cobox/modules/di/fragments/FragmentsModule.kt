package com.example.gilgoldzweig.cobox.modules.di.fragments

import android.support.v4.app.Fragment
import com.example.gilgoldzweig.cobox.fragments.feed.FeedFragment
import com.example.gilgoldzweig.cobox.fragments.home.HomeFragment
import com.example.gilgoldzweig.cobox.models.ui.Tab
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class FragmentsModule {

    @Provides
    fun provideHomeFragment() : HomeFragment =
            HomeFragment()


    @Provides
    fun provideNewsFeedFragment() : FeedFragment =
            FeedFragment()

    @Provides
    fun provideTabs(homeFragment: HomeFragment,
                    feedFragment: FeedFragment): List<Tab> =
            listOf(Tab("home", homeFragment), Tab("news", feedFragment))

}