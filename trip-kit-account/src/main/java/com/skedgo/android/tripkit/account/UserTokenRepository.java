package com.skedgo.android.tripkit.account;

import android.support.annotation.Nullable;

public abstract class UserTokenRepository {
  abstract public String getUserToken();
  abstract void putUserToken(@Nullable String userToken);
}
