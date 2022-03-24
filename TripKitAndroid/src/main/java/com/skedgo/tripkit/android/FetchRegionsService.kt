package com.skedgo.tripkit.android

import android.content.Context
import com.firebase.jobdispatcher.*
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
            val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))
            val myJob = dispatcher.newJobBuilder()
                .setService(FetchRegionsService::class.java)
                .setTag("FetchRegions")
                .setRecurring(false)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.NOW)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build()
            dispatcher.mustSchedule(myJob)
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
