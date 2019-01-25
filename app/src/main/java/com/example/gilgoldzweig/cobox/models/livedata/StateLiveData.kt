package com.example.gilgoldzweig.cobox.models.livedata

import android.arch.lifecycle.MutableLiveData

class StateLiveData<T> : MutableLiveData<StateData<T>>() {

    private val stateData = StateData<T>()
    /**
     * Use this to put the Data on a LOADING Status
     */
    fun postLoading() {
        postValue(stateData.loading())
    }

    /**
     * Use this to put the Data on a ERROR DataStatus
     * @param throwable the error to be handled
     */
    fun postError(throwable: Throwable) {
        postValue(stateData.error(throwable))
    }

    /**
     * Use this to put the Data on a SUCCESS DataStatus
     * @param data
     */
    fun postSuccess(data: T) {
        postValue(stateData.success(data))
    }

}