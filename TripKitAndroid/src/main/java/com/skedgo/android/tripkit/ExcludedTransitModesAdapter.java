package com.skedgo.android.tripkit;

import androidx.annotation.Nullable;

import java.util.List;

import rx.functions.Func1;

/**
 * @return A list of transit modes that users wish to
 * avoid in results related to public transports.
 */
public interface ExcludedTransitModesAdapter extends Func1<String, List<String>> {
}