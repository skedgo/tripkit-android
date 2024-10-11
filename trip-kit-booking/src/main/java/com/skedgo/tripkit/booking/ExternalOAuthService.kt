package com.skedgo.tripkit.booking;

import io.reactivex.Observable;

public interface ExternalOAuthService {

    Observable<ExternalOAuth> getAccessToken(BookingForm form, String code, String grantType, String callback);

}
