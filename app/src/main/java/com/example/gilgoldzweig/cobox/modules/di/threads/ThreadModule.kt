package com.example.gilgoldzweig.cobox.modules.di.threads

import com.nyx.tech.models.threads.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class ThreadModule {

  @Provides
  @Singleton
  fun provideCoroutineDispatchers(): CoroutineDispatchers =
    CoroutineDispatchers(
      database = Dispatchers.Default,
      disk =  Dispatchers.IO,
      network = Dispatchers.IO,
      main = Dispatchers.Main
    )
}
