package com.skedgo.tripkit.bookingproviders;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import com.skedgo.tripkit.BookingAction;
import com.skedgo.tripkit.ExternalActionParams;
import com.skedgo.tripkit.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import androidx.annotation.Nullable;
import io.reactivex.Observable;

final class TelBookingResolver implements BookingResolver {
    private final Resources resources;

    public TelBookingResolver(Resources resources) {
        this.resources = resources;
    }

    @Override
    public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
        String telAction = params.action();
        if (telAction.contains("?name=")) {
            telAction = telAction.substring(0, telAction.indexOf("?name="));
        }
        final BookingAction action = BookingAction.builder()
            .bookingProvider(OTHERS)
            .hasApp(false)
            .data(new Intent(Intent.ACTION_VIEW, Uri.parse(telAction)))
            .build();
        return Observable.just(action);
    }

    @Nullable
    @Override
    public String getTitleForExternalAction(String externalAction) {
        if (externalAction.contains("name=")) {
            String name = externalAction.substring(externalAction.indexOf("name=") + "name=".length());
            try {
                name = URLDecoder.decode(name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
            return resources.getString(R.string.call__pattern, name);
        } else {
            return resources.getString(R.string.call);
        }
    }
}