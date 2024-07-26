package com.skedgo.tripkit.account.data;

import com.google.gson.annotations.JsonAdapter;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersLogInResponse.class)
public abstract class LogInResponse {
    @Nullable
    public abstract String userToken();

    @Nullable
    public abstract String userID();

    @Nullable
    public abstract Boolean changed();

    @Nullable
    public abstract Boolean newUser();
}
