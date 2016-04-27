package com.skedgo.android.tripkit.account;

import com.skedgo.android.tripkit.TripKit;
import com.skedgo.android.tripkit.account.api.AccountApi;
import com.skedgo.android.tripkit.scope.ExtensionScope;

import dagger.Component;

@ExtensionScope
@Component(
    dependencies = TripKit.class,
    modules = AccountModule.class
)
public interface AccountComponent {
  AccountApi accountApi();
}