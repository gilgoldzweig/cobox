package com.example.gilgoldzweig.cobox.base

open class BaseViewImpl : BasePresenterContractImpl.ViewImpl

interface BasePresenterContractImpl {
    interface PresenterImpl : BaseContract.Presenter

    interface ViewImpl : BaseContract.View
}

