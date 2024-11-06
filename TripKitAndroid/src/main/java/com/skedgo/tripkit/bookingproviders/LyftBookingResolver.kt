package com.skedgo.tripkit.bookingproviders;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import com.skedgo.tripkit.BookingAction;
import com.skedgo.tripkit.ExternalActionParams;
import com.skedgo.tripkit.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

final class LyftBookingResolver implements BookingResolver {
    private static final String LYFT_PACKAGE = "me.lyft.android";
    private final Resources resources;
    private final Function<String, Boolean> isPackageInstalled;

    public LyftBookingResolver(
        @NonNull Resources resources,
        @NonNull Function<String, Boolean> isPackageInstalled) {
        this.resources = resources;
        this.isPackageInstalled = isPackageInstalled;
    }

    @Override
    public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
        final BookingAction.Builder actionBuilder = BookingAction.builder();
        actionBuilder.bookingProvider(LYFT);
        try {
            if (isPackageInstalled.apply(LYFT_PACKAGE)) {
                final BookingAction action = actionBuilder.hasApp(true).data(
                    new Intent(Intent.ACTION_VIEW).setData(Uri.parse("lyft://"))
                ).build();
                return Observable.just(action);
            } else {
                final Intent data = new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://play.google.com/store/apps/details?id=" + LYFT_PACKAGE));
                final BookingAction action = actionBuilder
                    .hasApp(false)
                    .data(data)
                    .build();
                return Observable.just(action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Observable.empty();
        }
    }

    @Nullable
    @Override
    public String getTitleForExternalAction(String externalAction) {
        try {
            return isPackageInstalled.apply(LYFT_PACKAGE)
                ? resources.getString(R.string.open_lyft)
                : resources.getString(R.string.get_lyft);
        } catch (Exception e) {
            e.printStackTrace();
            return resources.getString(R.string.get_lyft);
        }
    }
}