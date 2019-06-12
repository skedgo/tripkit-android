package skedgo.tripkit.ui.core

import com.jakewharton.rxrelay.PublishRelay
import com.skedgo.android.common.model.ScheduledStop
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * A placeholder for upcoming adoption of the Architecture Components library.
 *
 * When we adopt the Architecture Components library,
 * this class will extend this [ViewModel](https://developer.android.com/reference/android/arch/lifecycle/ViewModel.html).
 */
abstract class ReactiveViewModel {
  private val clearRelay = PublishRelay.create<Unit>()
  private val compositeSubscription: CompositeSubscription by lazy {
    CompositeSubscription()
  }
  fun <T> Observable<T>.autoClear(): Observable<T> = this.takeUntil(clearRelay)

  fun Subscription.autoClear() = compositeSubscription.add(this)

  /**
   * This method will be called when this ViewModel is no longer used and will be destroyed.
   */
  open fun onCleared() {
    clearRelay.call(Unit)
    compositeSubscription.clear()
  }
}
