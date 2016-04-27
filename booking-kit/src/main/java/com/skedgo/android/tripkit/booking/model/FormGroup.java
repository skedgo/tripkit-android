package com.skedgo.android.tripkit.booking.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FormGroup implements Parcelable {

  public static final Creator<FormGroup> CREATOR = new Creator<FormGroup>() {
    @Override
    public FormGroup createFromParcel(Parcel in) {
      return new FormGroup(in);
    }

    @Override
    public FormGroup[] newArray(int size) {
      return new FormGroup[size];
    }
  };
  @SerializedName("title")
  private String title;
  @SerializedName("footer")
  private String footer;
  @SerializedName("fields")
  private List<FormField> fields;

  private FormGroup(Parcel in) {
    title = in.readString();
    footer = in.readString();
    fields = new ArrayList<>();
    in.readTypedList(fields, FormField.CREATOR);
  }

  public FormGroup() {

  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Nullable
  public String getFooter() {
    return footer;
  }

  public List<FormField> getFields() {
    return fields;
  }

  public void setFields(List<FormField> fields) {
    this.fields = fields;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(title);
    dest.writeString(footer);
    dest.writeTypedList(fields);
  }
}
