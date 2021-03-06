package com.example.gilgoldzweig.cobox.base

open class BaseViewImpl : BasePresenterContractImpl.ViewImpl

interface BasePresenterContractImpl {
    interface PresenterImpl : BaseContract.Presenter<ViewImpl>

    interface ViewImpl : BaseContract.View
}

