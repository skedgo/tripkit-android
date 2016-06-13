package com.skedgo.android.bookingclient.viewmodel;

import rx.Observable;

/**
 * @see <a href="https://github.com/reactiveui/ReactiveUI/blob/master/docs/basics/reactive-command.md">ReactiveCommand</a>
 */
public interface Command<TParam, TResult> {
  Observable<TResult> executeAsync(TParam param);
}