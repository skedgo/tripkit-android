package com.skedgo.android.tripkit;

import rx.Observable;

interface RegionsFetcher {
  Observable<Void> fetchAsync();
}