package com.skedgo.android.tripkit.account;

import com.skedgo.android.tripkit.TripKit;
import com.skedgo.android.tripkit.scope.ExtensionScope;

import dagger.Component;

/**
 * To initialize this, refer to {@link DaggerAccountComponent#builder()}.
 */
@ExtensionScope
@Component(
    dependencies = TripKit.class,
    modules = AccountModule.class
)
public interface AccountComponent {
  AccountService accountService();
  UserTokenStore userTokenStore();
}