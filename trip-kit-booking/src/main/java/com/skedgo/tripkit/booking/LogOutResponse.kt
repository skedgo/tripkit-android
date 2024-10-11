package com.skedgo.tripkit.booking;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.value.Value;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersLogOutResponse.class)
public abstract class LogOutResponse {
    /**
     * Indicates whether user data was removed or not.
     */
    @Value.Default
    public boolean changed() {
        return false;
    }
}