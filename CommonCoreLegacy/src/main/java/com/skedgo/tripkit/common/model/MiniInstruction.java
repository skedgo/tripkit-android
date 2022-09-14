package com.skedgo.tripkit.common.model;

import androidx.annotation.Nullable;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import org.immutables.gson.Gson;
import org.immutables.value.Value;


@Gson.TypeAdapters
@Value.Immutable
@Value.Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersMiniInstruction.class)
public abstract class MiniInstruction {
    @SerializedName("description") @Nullable public abstract String getDescription();
    @SerializedName("instruction") @Nullable public abstract String getInstruction();
    @SerializedName("mainValue") @Nullable public abstract String getMainValue();
}