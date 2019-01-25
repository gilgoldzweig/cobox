package com.example.gilgoldzweig.cobox.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit

class BasePresenterTest {

	@get:Rule
	val rule = MockitoJUnit.rule()

	@Mock
	private lateinit var baseViewImpl: BaseViewImpl

	@Mock
	private lateinit var lifecycleOwner: LifecycleOwner

	private lateinit var lifecycleRegistry: LifecycleRegistry

	private lateinit var basePresenter: BasePresenterImpl

	@Before
	fun setUp() {
		basePresenter = BasePresenterImpl()

		lifecycleRegistry = LifecycleRegistry(lifecycleOwner)

		lifecycleRegistry.markState(Lifecycle.State.CREATED)

		basePresenter.attach(baseViewImpl)
	}

	@Test
	fun testDetach() {

		assertFalse(basePresenter.job.isCancelled)

		basePresenter.detach()

		assertTrue(basePresenter.job.isCancelled)
	}

	@Test
	fun testLifecycleBind() {
		basePresenter.attach(baseViewImpl, lifecycleRegistry)

		assertNotNull(basePresenter.lifecycle)

		assert(lifecycleRegistry.observerCount == 1)
	}

	@Test
	fun testLifecycleDetachCalled() {
		basePresenter.attach(baseViewImpl, lifecycleRegistry)

		lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)

		assert(lifecycleRegistry.observerCount == 0)

		assertNull(basePresenter.lifecycle)
	}
}