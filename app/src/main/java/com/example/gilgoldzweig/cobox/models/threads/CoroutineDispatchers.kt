package com.nyx.tech.models.threads

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class CoroutineDispatchers(
		val database: CoroutineDispatcher = Dispatchers.Default,
		val disk: CoroutineDispatcher = Dispatchers.Default,
		val network: CoroutineDispatcher = Dispatchers.Default,
		val main: CoroutineDispatcher = Dispatchers.Default,
		val default: CoroutineDispatcher = Dispatchers.Default
)