package com.skedgo.tripkit;

import android.content.Intent;

import com.skedgo.tripkit.bookingproviders.BookingProvider;

import org.immutables.value.Value;

import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;
import static org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE;

@Value.Immutable
@Value.Style(visibility = PRIVATE, builderVisibility = PACKAGE)
public abstract class BookingAction {
    public static Builder builder() {
        return new BookingActionBuilder();
    }

    @BookingProvider
    public abstract int bookingProvider();

    public abstract boolean hasApp();

    public abstract Intent data();

    public interface Builder {
        Builder bookingProvider(@BookingProvider int bookingProvider);

        Builder hasApp(boolean hasApp);

        Builder data(Intent data);

        BookingAction build();
    }
}