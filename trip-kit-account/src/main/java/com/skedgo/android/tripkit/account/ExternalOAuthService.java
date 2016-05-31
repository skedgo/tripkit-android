package com.skedgo.android.tripkit.account;

import rx.Observable;

public interface ExternalOAuthService {

  Observable<AccessToken> getAccessToken(String baseUrl, String clientId, String clientSecret, String code, String grantType);

}
