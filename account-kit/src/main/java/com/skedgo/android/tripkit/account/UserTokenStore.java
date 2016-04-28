package com.skedgo.android.tripkit.account;

import android.support.annotation.Nullable;

import com.skedgo.android.tripkit.Configs;

import rx.functions.Func0;

/**
 * A store to retrieve and persist user token obtained via {@link AccountApi}.
 * This might be supplied to {@link Configs#userTokenProvider()} so that
 * TripKit can send appropriate booking requests.
 */
public interface UserTokenStore extends Func0<String> {
  void put(@Nullable String userToken);
}