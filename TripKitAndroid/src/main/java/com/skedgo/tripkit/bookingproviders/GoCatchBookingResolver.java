package com.skedgo.tripkit.bookingproviders;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import com.skedgo.tripkit.BookingAction;
import com.skedgo.tripkit.ExternalActionParams;
import com.skedgo.tripkit.R;
import com.skedgo.tripkit.common.model.location.Location;
import com.skedgo.tripkit.geocoding.ReverseGeocodable;
import com.skedgo.tripkit.routing.TripSegment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

final class GoCatchBookingResolver implements BookingResolver {
    private static final String GOCATCH_PACKAGE = "com.gocatchapp.goCatch";
    private static final String GOCATCH_CODE = "tripgo";

    private final Resources resources;
    private final Function<String, Boolean> isPackageInstalled;
    private final ReverseGeocodable geocoderFactory;

    GoCatchBookingResolver(
        @NonNull Resources resources,
        @NonNull Function<String, Boolean> isPackageInstalled,
        @NonNull ReverseGeocodable geocoderFactory) {
        this.resources = resources;
        this.isPackageInstalled = isPackageInstalled;
        this.geocoderFactory = geocoderFactory;
    }

    @Override
    public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
        try {
            if (isPackageInstalled.apply(GOCATCH_PACKAGE)) {
                final TripSegment segment = params.segment();
                final Location departure = segment.getFrom();
                final Location arrival = segment.getTo();
                return geocoderFactory.getAddress(arrival.getLat(), arrival.getLon())
                    .map(new Function<String, BookingAction>() {
                        @Override
                        public BookingAction apply(String arrivalAddress) {
                            final Uri uri = Uri.parse("gocatch://referral")
                                .buildUpon()
                                .appendQueryParameter("code", GOCATCH_CODE)
                                .appendQueryParameter("destination", arrivalAddress)
                                .appendQueryParameter("pickup", "")
                                .appendQueryParameter("lat", String.valueOf(departure.getLat()))
                                .appendQueryParameter("lng", String.valueOf(departure.getLon()))
                                .build();
                            return BookingAction.builder()
                                .bookingProvider(GOCATCH)
                                .hasApp(true)
                                .data(new Intent(Intent.ACTION_VIEW).setData(uri))
                                .build();
                        }
                    });
            } else {
                final Intent data = new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://play.google.com/store/apps/details?id=" + GOCATCH_PACKAGE));
                final BookingAction action = BookingAction.builder()
                    .bookingProvider(GOCATCH)
                    .hasApp(false)
                    .data(data)
                    .build();
                return Observable.just(action);
            }
        } catch (Exception e) {
            return Observable.empty();
        }
    }

    @Nullable
    @Override
    public String getTitleForExternalAction(String externalAction) {
        return resources.getString(R.string.gocatch_a_taxi);
    }
}