package com.skedgo.android.tripkit.booking.model;

import android.os.Parcel;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BookingForm extends FormField {

  public static final Creator<BookingForm> CREATOR = new Creator<BookingForm>() {
    @Override
    public BookingForm createFromParcel(Parcel in) {
      in.readInt();
      return new BookingForm(in);
    }

    @Override
    public BookingForm[] newArray(int size) {
      return new BookingForm[size];
    }
  };
  @SerializedName("action")
  private BookingAction action;
  @SerializedName("form")
  private List<FormGroup> form;
  @SerializedName("value")
  private String value;
  @SerializedName("refreshURLForSourceObject")
  private String refreshURLForSourceObject;
  @SerializedName("image")
  private String imageUrl;

  public BookingForm(Parcel in) {
    super(in);
    this.action = in.readParcelable(BookingAction.class.getClassLoader());
    this.form = new ArrayList<>();
    in.readTypedList(this.form, FormGroup.CREATOR);
    this.value = in.readString();
    this.refreshURLForSourceObject = in.readString();
    this.imageUrl = in.readString();
  }

  public BookingForm() {
    super();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(BOOKINGFORM);
    super.writeToParcel(dest, flags);
    dest.writeParcelable(action, flags);
    dest.writeTypedList(form);
    dest.writeString(value);
    dest.writeString(refreshURLForSourceObject);
    dest.writeString(imageUrl);
  }

  public BookingAction getAction() {
    return action;
  }

  public void setAction(BookingAction action) {
    this.action = action;
  }

  public String getRefreshURLForSourceObject() {
    return refreshURLForSourceObject;
  }

  public void setRefreshURLForSourceObject(String refreshURLForSourceObject) {
    this.refreshURLForSourceObject = refreshURLForSourceObject;
  }

  public List<FormGroup> getForm() {
    return form;
  }

  public void setForm(List<FormGroup> form) {
    this.form = form;
  }

  @Nullable
  public String getImageUrl() {
    return imageUrl;
  }

  @Override
  public Object getValue() {
    return getTitle();
  }
}