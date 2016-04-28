package com.skedgo.android.tripkit.booking;

import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Region;

import java.util.List;

import rx.Observable;

public interface AuthService extends AuthApi {
  Observable<List<AuthProvider>> fetchProvidersByRegionAsync(@NonNull Region region);
}