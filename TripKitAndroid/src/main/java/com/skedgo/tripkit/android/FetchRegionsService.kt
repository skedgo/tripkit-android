package com.skedgo.tripkit.android

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.skedgo.TripKit
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class FetchRegionsService : JobService() {
    var runningJob: Disposable? = null

    override fun onStopJob(job: JobParameters?): Boolean {
        if (runningJob != null && runningJob!!.isDisposed.not()) {
            runningJob?.dispose()
            return true
        } else {
            return false
        }
    }

    override fun onStartJob(job: JobParameters): Boolean {
        runningJob = Observable
            .fromCallable {
                // Pulling this into fromCallable() is actually a trick to deal with an issue
                // when this getInstance() was called before we initialize TripKit after
                // the super.onCreate() in the sub Application class.
                // So when it happens, we just let it throw error, and thus
                // the task is rescheduled after TripKit initialization.
                TripKit.getInstance()
            }
            .refreshRegions()
            .subscribeOn(Schedulers.io())
            .ignoreElements()
            .subscribe(
                { jobFinished(job, false) },
                { jobFinished(job, true) })
        return true
    }


    companion object {
        fun scheduleAsync(context: Context): Observable<Void> {
            return Completable
                .fromAction {
                    // Firebase's GooglePlayDriver creates an unflagged PendingIntent on
                    // Android 12+. Keep this API, but schedule via the platform instead.
                    val scheduler = context.getSystemService(JobScheduler::class.java)
                    val job = JobInfo.Builder(
                        0x54524B,
                        ComponentName(context, FetchRegionsJobService::class.java)
                    )
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setBackoffCriteria(30_000L, JobInfo.BACKOFF_POLICY_EXPONENTIAL)
                        .build()
                    check(scheduler.schedule(job) == JobScheduler.RESULT_SUCCESS) {
                        "Unable to schedule TripKit region refresh"
                    }
                }
                .toObservable<Void>()
        }
    }
}

fun Observable<TripKit>.refreshRegions(): Observable<Unit> = this
    .flatMap {
        it.regionService
            .refreshAsync()
            .toObservable<Unit>()
    }
