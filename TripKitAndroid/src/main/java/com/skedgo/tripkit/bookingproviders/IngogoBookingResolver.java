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

final class IngogoBookingResolver implements BookingResolver {
    private static final String INGOGO_PACKAGE = "com.ingogo.passenger";
    private final Resources resources;
    private final Function<String, Boolean> isPackageInstalled;

    public IngogoBookingResolver(
        @NonNull Resources resources,
        @NonNull Function<String, Boolean> isPackageInstalled) {
        this.resources = resources;
        this.isPackageInstalled = isPackageInstalled;
    }

    @Override
    public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
        final BookingAction.Builder actionBuilder = BookingAction.builder();
        actionBuilder.bookingProvider(INGOGO);
        try {
            if (isPackageInstalled.apply(INGOGO_PACKAGE)) {
                final BookingAction action = actionBuilder.hasApp(true).data(
                    new Intent(Intent.ACTION_VIEW).setData(Uri.parse("ingogo://"))
                ).build();
                return Observable.just(action);
            } else {
                final Intent data = new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://play.google.com/store/apps/details?id=" + INGOGO_PACKAGE));
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
        return resources.getString(R.string.ingogo_a_taxi);
    }
}