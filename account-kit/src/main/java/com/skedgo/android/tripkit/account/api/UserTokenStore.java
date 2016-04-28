package com.skedgo.android.tripkit.account.api;

import android.support.annotation.Nullable;

import rx.functions.Func0;

public interface UserTokenStore extends Func0<String> {
  void put(@Nullable String userToken);
}