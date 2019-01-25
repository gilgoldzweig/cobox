package com.nyx.tech.timber.utils

/**
 * Created by gilgoldzweig on 05/11/2017.
 */
interface BundlifyType<E: Any> {
    var key: String
    var value: E
}