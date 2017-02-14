package com.skedgo.android.tripkit.booking;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skedgo.android.common.model.Region;

import java.util.List;

import rx.Observable;

/**
 * Extension of {@link AuthApi} that facilitates fetching providers for a region.
 */
public interface AuthService extends AuthApi {
  Observable<List<AuthProvider>> fetchProvidersByRegionAsync(@NonNull Region region,
                                                             @Nullable String mode,
                                                             boolean bsb);
}