package com.skedgo.android.tripkit.account;

import com.skedgo.android.tripkit.TripKit;
import com.skedgo.android.tripkit.account.api.AccountApi;
import com.skedgo.android.tripkit.account.api.AccountService;
import com.skedgo.android.tripkit.account.api.ApiModule;
import com.skedgo.android.tripkit.account.api.UserTokenStore;
import com.skedgo.android.tripkit.scope.ExtensionScope;

import dagger.Component;

/**
 * To initialize this, refer to {@link DaggerAccountComponent#builder()}.
 */
@ExtensionScope
@Component(
    dependencies = TripKit.class,
    modules = ApiModule.class
)
public interface AccountComponent {
  AccountApi accountApi();
  AccountService accountService();
  UserTokenStore userTokenStore();
}