package com.example.gilgoldzweig.cobox.modules.di

import com.example.gilgoldzweig.cobox.fragments.feed.FeedFragment
import com.example.gilgoldzweig.cobox.fragments.home.HomeFragment
import com.example.gilgoldzweig.cobox.modules.di.presenters.PresentersModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [PresentersModule::class])
@Singleton
interface ApplicationComponent {

  /**
   * Used to inject the presenter to the feed fragment
   */
  fun inject(feedFragment: FeedFragment)

  /**
   * Used to inject the presenter to the home fragment
   */
  fun inject(homeFragment: HomeFragment)

}
