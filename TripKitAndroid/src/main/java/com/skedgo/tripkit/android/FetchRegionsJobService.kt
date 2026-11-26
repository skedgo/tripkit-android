package com.skedgo.tripkit.android

import android.app.job.JobParameters
import android.app.job.JobService
import com.skedgo.TripKit
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/** Platform-backed region refresh; the existing scheduling entry point remains unchanged. */
internal class FetchRegionsJobService : JobService() {
    private var runningJob: Disposable? = null

    override fun onStartJob(params: JobParameters): Boolean {
        runningJob = Observable.fromCallable { TripKit.getInstance() }
            .refreshRegions()
            .subscribeOn(Schedulers.io())
            .ignoreElements()
            .subscribe(
                { jobFinished(params, false) },
                { jobFinished(params, true) }
            )
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        runningJob?.dispose()
        runningJob = null
        return true
    }
}
