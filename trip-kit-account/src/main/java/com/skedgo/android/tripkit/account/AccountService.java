package com.skedgo.android.tripkit.account;

import android.support.annotation.Nullable;

/**
 * Extension of {@link AccountApi} that provides user token persistence.
 */
public interface AccountService extends AccountApi {
  /**
   * @return True if users already logged in. Otherwise, false.
   */
  boolean hasUser();

  /**
   * @return If {@link #hasUser()} returns true,
   * this returns the username that users used
   * to log in or to sign up before.
   */
  @Nullable String username();
}