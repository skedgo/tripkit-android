package com.skedgo.tripkit;

import io.reactivex.Single;

interface Cache<TData> {
  Single<TData> getAsync();
  void invalidate();
}