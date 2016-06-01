package com.skedgo.android.tripkit.booking;

import rx.Observable;

public interface ExternalOAuthService {

  Observable<AccessToken> getAccessToken(BookingForm form, String code, String grantType);

}
