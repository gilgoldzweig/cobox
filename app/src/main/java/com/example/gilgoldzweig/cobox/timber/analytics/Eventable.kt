package com.nyx.tech.timber.analytics

interface Eventable {
    val displayName: String

    override fun toString(): String
}

interface EventableParam : Eventable

interface EventableType : Eventable
