package com.skedgo.android.tripkit.booking;

import rx.Observable;
import rx.Single;

public interface ExternalOAuthStore {

  Observable<ExternalOAuth> updateExternalOauth(ExternalOAuth externalOAuth);
  Single<ExternalOAuth> getExternalOauth(String authId);

}
