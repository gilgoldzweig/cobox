package com.example.gilgoldzweig.cobox.base

import android.arch.lifecycle.Lifecycle
import android.support.annotation.UiThread

interface BaseContract {

  @UiThread
  interface View

  interface Presenter<V: View> {

    fun attach(view: V, lifecycle: Lifecycle?)

    fun detach()
  }
}
