package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import skedgo.tripkit.routing.ServiceColor;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersPurchaseBrand.class)
public abstract class PurchaseBrand {
  public abstract ServiceColor color();
  public abstract String name();
  @Nullable public abstract String remoteIcon();

}