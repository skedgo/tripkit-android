package com.skedgo.tripkit;

import io.reactivex.Observable;

interface Cache<TData> {
  Observable<TData> getAsync();
  void invalidate();
}