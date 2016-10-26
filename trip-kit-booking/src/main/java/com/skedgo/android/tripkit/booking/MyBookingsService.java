package com.skedgo.android.tripkit.booking;

import java.util.List;

import rx.Observable;

public interface MyBookingsService {

  Observable<List<String>> getMyBookingsAsync();
}
