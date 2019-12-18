package com.skedgo.android.tripkit.booking;

import io.reactivex.Observable;

public interface ExternalOAuthService {

  Observable<ExternalOAuth> getAccessToken(BookingForm form, String code, String grantType, String callback);

}
