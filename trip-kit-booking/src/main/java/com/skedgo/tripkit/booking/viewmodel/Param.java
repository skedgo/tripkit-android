package com.skedgo.tripkit.booking.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.skedgo.tripkit.booking.BookingAction;
import com.skedgo.tripkit.booking.BookingForm;
import com.skedgo.tripkit.booking.InputForm;
import com.skedgo.tripkit.booking.LinkFormField;

public class Param implements Parcelable {
  public static final Parcelable.Creator<Param> CREATOR = new Parcelable.Creator<Param>() {
    public Param createFromParcel(Parcel in) {
      return new Param(in);
    }

    public Param[] newArray(int size) {
      return new Param[size];
    }
  };

  private final String url;
  private final String hudText;
  private final InputForm postBody;
  private final String method;

  private Param(String url, String method, String hudText, InputForm postBody) {
    this.url = url;
    this.hudText = hudText;
    this.postBody = postBody;
    this.method = method;
  }

  public Param(Parcel in) {
    url = in.readString();
    hudText = in.readString();
    method = in.readString();
    postBody = in.readParcelable(InputForm.class.getClassLoader());
  }

  public static Param create(String url) {
    return new Param(url, LinkFormField.METHOD_GET, null, null);
  }

  public static Param create(BookingAction bookingAction, InputForm postBody) {
    return new Param(bookingAction.getUrl(), LinkFormField.METHOD_POST, bookingAction.getHudText(), postBody);
  }

  public static Param create(LinkFormField linkFormField) {
    final InputForm postBody = linkFormField.getMethod().equals(LinkFormField.METHOD_POST) ? new InputForm() : null;
    return new Param(linkFormField.getValue(), linkFormField.getMethod(), null, postBody);
  }

  public static Param create(BookingForm form) {
    final InputForm postBody = InputForm.from(form.getForm());
    return new Param(form.getAction().getUrl(), LinkFormField.METHOD_POST, null, postBody);
  }

  public String getMethod() {
    return method;
  }

  public String getUrl() {
    return url;
  }

  public String getHudText() {
    return hudText;
  }

  public InputForm postBody() {
    return postBody;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int i) {
    dest.writeString(url);
    dest.writeString(hudText);
    dest.writeString(method);
    dest.writeParcelable(postBody, i);
  }
}