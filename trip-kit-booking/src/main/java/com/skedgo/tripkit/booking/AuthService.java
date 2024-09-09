package com.skedgo.tripkit.booking;

import com.skedgo.tripkit.common.model.Region;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;

/**
 * Extension of {@link AuthApi} that facilitates fetching providers for a region.
 */
public interface AuthService extends AuthApi {
    Observable<List<AuthProvider>> fetchProvidersByRegionAsync(
        @NonNull Region region,
        @Nullable String mode,
        boolean bsb
    );
}