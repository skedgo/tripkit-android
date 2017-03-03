package com.skedgo.android.tripkit.account;

import android.support.annotation.Nullable;

public interface UserTokenRepository {
  String getUserToken();
  void putUserToken(@Nullable String userToken);
}
