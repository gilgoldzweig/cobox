package com.example.gilgoldzweig.cobox.base

import com.nyx.tech.models.threads.CoroutineDispatchers
import com.example.gilgoldzweig.cobox.base.BasePresenterContractImpl as Base

open class BasePresenterImpl : BasePresenter<Base.ViewImpl>(), Base.PresenterImpl {

    override var dispatchers: CoroutineDispatchers = CoroutineDispatchers()
}
