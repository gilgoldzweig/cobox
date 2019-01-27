package com.example.gilgoldzweig.cobox.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.nyx.tech.models.threads.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


abstract class BasePresenter<V : BaseContract.View> :
    CoroutineScope, LifecycleObserver, BaseContract.Presenter<V> {

    var job: Job = Job()

    var lifecycle: Lifecycle? = null

    lateinit var view: V

    abstract var dispatchers: CoroutineDispatchers

    override val coroutineContext: CoroutineContext
        get() = dispatchers.default + job

    val networkContext: CoroutineContext
        get() = dispatchers.network + job

    val uiContext: CoroutineContext
        get() = dispatchers.main + job

    val databaseContext: CoroutineContext
        get() = dispatchers.database + job


    /**
     * attach the view to the presenter
     * creates a new job if the old one was cancelled
     *
     * @param view, view to bind
     * @param lifecycle a lifecycle we can bind
     */
    override fun attach(view: V, lifecycle: Lifecycle?) {
        if (job.isCancelled) {
            job = Job()
        }
        this.view = view

        if (lifecycle != null) {
            bindToLifecycle(lifecycle)
        }
    }

    fun updateUi(action: V.() -> Unit) {
        if (!job.isCancelled &&
            lifecycle?.currentState?.isAtLeast(Lifecycle.State.STARTED) != false) {

            launch(uiContext) {
                view.action()
            }
        }
    }

    fun bindToLifecycle(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
        lifecycle.addObserver(this)
    }

    override fun detach() {
        job.cancel()
        lifecycle?.removeObserver(this)
        lifecycle = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        detach()
    }
}