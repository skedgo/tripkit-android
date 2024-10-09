package com.skedgo.tripkit.booking

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import java.util.concurrent.TimeUnit

/**
 * Will serve as a base class for test classes using MockK
 */
open class MockKTest {

    private val immediateScheduler: Scheduler = object : Scheduler() {

        override fun createWorker() = ExecutorScheduler.ExecutorWorker { it.run() }

        // This prevents errors when scheduling a delay
        override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            return super.scheduleDirect(run, 0, unit)
        }

    }

    fun initRx() {
        RxJavaPlugins.setIoSchedulerHandler { immediateScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediateScheduler }
        RxAndroidPlugins.setMainThreadSchedulerHandler { immediateScheduler }
    }

    fun tearDownRx() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

}