package skedgo.tripkit.android

import android.content.Context
import com.google.android.gms.gcm.*
import rx.Observable
import java.util.concurrent.TimeUnit

class FetchRegionsService : GcmTaskService() {
  override fun onRunTask(taskParams: TaskParams): Int =
      Observable
          .fromCallable {
            // Pulling this into fromCallable() is actually a trick to deal with an issue
            // when this getInstance() was called before we initialize TripKit after
            // the super.onCreate() in the sub Application class.
            // So when it happens, we just let it throw error, and thus
            // the task is rescheduled after TripKit initialization.
            TripKit.getInstance()
          }
          .refreshRegions()

  companion object {
    fun scheduleAsync(context: Context): Observable<Void> =
        Observable.create { subscriber ->
          val task = OneoffTask.Builder()
              .setService(FetchRegionsService::class.java)
              .setTag("FetchRegions")
              .setExecutionWindow(0, TimeUnit.MINUTES.toSeconds(1))
              .setPersisted(true)
              .setUpdateCurrent(true)
              .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
              .setRequiresCharging(true)
              .build()
          GcmNetworkManager.getInstance(context.applicationContext)
              .schedule(task)
          subscriber.onCompleted()
        }
  }
}

internal fun Observable<TripKit>.refreshRegions() = this
    .flatMap {
      it.regionService
          .refreshAsync()
          .toObservable<Unit>()
    }
    .map { GcmNetworkManager.RESULT_SUCCESS }
    .onErrorReturn { GcmNetworkManager.RESULT_RESCHEDULE }
    // To avoid NoSuchElementException when using first().
    .defaultIfEmpty(GcmNetworkManager.RESULT_SUCCESS)
    .toBlocking()
    .first()
