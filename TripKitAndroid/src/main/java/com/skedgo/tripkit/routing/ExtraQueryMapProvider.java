package com.skedgo.tripkit.routing;

import com.skedgo.tripkit.a2brouting.A2bRoutingApi;

import java.util.Map;

import androidx.annotation.NonNull;

/**
 * A decorator that puts additional query params
 * into the query map that is supplied into {@link A2bRoutingApi}.
 * Note that you should only use this when
 * you really do know what you intend to do.
 */
public interface ExtraQueryMapProvider {
    /**
     * Be careful that some entries of this map
     * may override some default entries of
     * the query map of {@link A2bRoutingApi}.
     */
    @NonNull
    Map<String, Object> call();
}