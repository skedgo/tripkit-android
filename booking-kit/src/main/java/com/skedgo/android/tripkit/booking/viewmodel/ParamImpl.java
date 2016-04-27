package com.skedgo.android.tripkit.booking.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.skedgo.android.tripkit.booking.model.BookingAction;
import com.skedgo.android.tripkit.booking.model.InputForm;
import com.skedgo.android.tripkit.booking.model.LinkFormField;

public final class ParamImpl implements BookingViewModel.Param {
  public static final Parcelable.Creator<ParamImpl> CREATOR = new Parcelable.Creator<ParamImpl>() {
    public ParamImpl createFromParcel(Parcel in) {
      return new ParamImpl(in);
    }

    public ParamImpl[] newArray(int size) {
      return new ParamImpl[size];
    }
  };

  private final String url;
  private final String hudText;
  private final InputForm postBody;
  private final String method;

  private ParamImpl(String url, String method, String hudText, InputForm postBody) {
    this.url = url;
    this.hudText = hudText;
    this.postBody = postBody;
    this.method = method;
  }

  public ParamImpl(Parcel in) {
    url = in.readString();
    hudText = in.readString();
    method = in.readString();
    postBody = in.readParcelable(InputForm.class.getClassLoader());
  }

  public static ParamImpl create(String url) {
    return new ParamImpl(url, LinkFormField.METHOD_GET, null, null);
  }

  public static ParamImpl create(BookingAction bookingAction, InputForm postBody) {
    return new ParamImpl(bookingAction.getUrl(), LinkFormField.METHOD_POST, bookingAction.getHudText(), postBody);
  }

  public static ParamImpl create(LinkFormField linkFormField) {
    final InputForm postBody = linkFormField.getMethod().equals(LinkFormField.METHOD_POST) ? new InputForm() : null;
    return new ParamImpl(linkFormField.getValue(), linkFormField.getMethod(), null, postBody);
  }

  @Override
  public String getMethod() {
    return method;
  }

  @Override
  public String getUrl() {
    return url;
  }

  @Override
  public String getHudText() {
    return hudText;
  }

  @Override
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