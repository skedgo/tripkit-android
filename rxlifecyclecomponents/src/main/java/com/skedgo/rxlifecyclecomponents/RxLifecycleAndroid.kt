package com.skedgo.rxlifecyclecomponents



import androidx.annotation.CheckResult
import androidx.annotation.NonNull
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.OutsideLifecycleException
import com.trello.rxlifecycle3.RxLifecycle.bind
import io.reactivex.Observable
import io.reactivex.functions.Function

object RxLifecycleAndroid {
    /**
     * Binds the given source to an Activity lifecycle.
     *
     * Use with [Observable.compose]:
     * `source.compose(RxLifecycleAndroid.bindActivity(lifecycle)).subscribe()`
     *
     * This helper automatically determines (based on the lifecycle sequence itself) when the source
     * should stop emitting items. In the case that the lifecycle sequence is in the
     * creation phase (CREATE, START, etc) it will choose the equivalent destructive phase (DESTROY,
     * STOP, etc). If used in the destructive phase, the notifications will cease at the next event;
     * for example, if used in PAUSE, it will unsubscribe in STOP.
     *
     * Due to the differences between the Activity and Fragment lifecycles, this method should only
     * be used for an Activity lifecycle.
     *
     * @param lifecycle the lifecycle sequence of an Activity
     * @return a reusable [Observable.Transformer] that unsubscribes the source during the Activity lifecycle
     */
    @NonNull
    @CheckResult
    fun <T> bindActivity(@NonNull lifecycle: Observable<ActivityEvent>): LifecycleTransformer<T> {
        return bind(lifecycle, ACTIVITY_LIFECYCLE)
    }

    /**
     * Binds the given source to a Fragment lifecycle.
     *
     * Use with [Observable.compose]:
     * `source.compose(RxLifecycleAndroid.bindFragment(lifecycle)).subscribe()`
     *
     * This helper automatically determines (based on the lifecycle sequence itself) when the source
     * should stop emitting items. In the case that the lifecycle sequence is in the
     * creation phase (CREATE, START, etc) it will choose the equivalent destructive phase (DESTROY,
     * STOP, etc). If used in the destructive phase, the notifications will cease at the next event;
     * for example, if used in PAUSE, it will unsubscribe in STOP.
     *
     * Due to the differences between the Activity and Fragment lifecycles, this method should only
     * be used for a Fragment lifecycle.
     *
     * @param lifecycle the lifecycle sequence of a Fragment
     * @return a reusable [Observable.Transformer] that unsubscribes the source during the Fragment lifecycle
     */
    @CheckResult
    fun <T> bindFragment(lifecycle: Observable<FragmentEvent>): LifecycleTransformer<T> {
        return bind(lifecycle, FRAGMENT_LIFECYCLE)
    }

    // Figures out which corresponding next lifecycle event in which to unsubscribe, for Activities
    private val ACTIVITY_LIFECYCLE = Function<ActivityEvent, ActivityEvent> { lastEvent ->
        when (lastEvent) {
            ActivityEvent.CREATE -> ActivityEvent.DESTROY
            ActivityEvent.START -> ActivityEvent.STOP
            ActivityEvent.RESUME -> ActivityEvent.PAUSE
            ActivityEvent.PAUSE -> ActivityEvent.STOP
            ActivityEvent.STOP -> ActivityEvent.DESTROY
            ActivityEvent.DESTROY -> throw OutsideLifecycleException("Cannot bind to Activity lifecycle when outside of it.")
            else -> throw UnsupportedOperationException("Binding to $lastEvent not yet implemented")
        }
    }

    // Figures out which corresponding next lifecycle event in which to unsubscribe, for Fragments
    private val FRAGMENT_LIFECYCLE = Function<FragmentEvent, FragmentEvent> { lastEvent ->
        when (lastEvent) {
            FragmentEvent.ATTACH -> FragmentEvent.DETACH
            FragmentEvent.CREATE -> FragmentEvent.DESTROY
            FragmentEvent.CREATE_VIEW -> FragmentEvent.DESTROY_VIEW
            FragmentEvent.START -> FragmentEvent.STOP
            FragmentEvent.RESUME -> FragmentEvent.PAUSE
            FragmentEvent.PAUSE -> FragmentEvent.STOP
            FragmentEvent.STOP -> FragmentEvent.DESTROY_VIEW
            FragmentEvent.DESTROY_VIEW -> FragmentEvent.DESTROY
            FragmentEvent.DESTROY -> FragmentEvent.DETACH
            FragmentEvent.DETACH -> throw OutsideLifecycleException("Cannot bind to Fragment lifecycle when outside of it.")
            else -> throw UnsupportedOperationException("Binding to $lastEvent not yet implemented")
        }
    }
}