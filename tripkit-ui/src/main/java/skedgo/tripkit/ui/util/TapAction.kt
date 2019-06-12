package skedgo.tripkit.ui.util

import rx.Observable
import rx.subjects.PublishSubject

class TapAction<TSender> internal constructor(
    private val getSender: () -> TSender
) {
  companion object Factory {
    fun <TSender> create(getSender: () -> TSender): TapAction<TSender>
        = TapAction(getSender)
  }

  private val onTap = PublishSubject.create<TSender>()
  val observable: Observable<TSender>
    get() = onTap.asObservable()

  fun perform() = onTap.onNext(getSender())
}