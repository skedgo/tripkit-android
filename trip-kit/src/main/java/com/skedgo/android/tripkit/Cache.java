package com.skedgo.android.tripkit;

import rx.Observable;

interface Cache<TData> {
  Observable<TData> getAsync();
  void invalidate();
}