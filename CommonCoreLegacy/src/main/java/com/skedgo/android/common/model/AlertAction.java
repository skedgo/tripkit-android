package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersAlertAction.class)
public abstract class AlertAction implements Parcelable {

  public static final Creator<AlertAction> CREATOR = new Creator<AlertAction>() {
    public AlertAction createFromParcel(Parcel in) {
      final ArrayList<String> excludedStopCodes = in.readArrayList(ArrayList.class.getClassLoader());
      return ImmutableAlertAction.builder()
          .type(in.readString())
          .text(in.readString())
          .excludedStopCodes(excludedStopCodes)
          .build();
    }

    public AlertAction[] newArray(int size) {
      return new AlertAction[size];
    }
  };

  @Nullable public abstract String type();
  @Nullable public abstract String text();
  @Nullable public abstract List<String> excludedStopCodes();


  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel out, int flags) {
    out.writeList(excludedStopCodes());
    out.writeString(type());
    out.writeString(text());
  }
}