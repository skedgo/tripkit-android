package com.skedgo.android.tripkit.booking;

import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

  @SerializedName("usererror")
  private boolean hasUserError;
  @SerializedName("error")
  private String errorMessage;

  public BookingForm(Parcel in) {
    super(in);
    this.action = in.readParcelable(BookingAction.class.getClassLoader());
    this.form = new ArrayList<>();
    in.readTypedList(this.form, FormGroup.CREATOR);
    this.value = in.readString();
    this.refreshURLForSourceObject = in.readString();
    this.imageUrl = in.readString();
    this.hasUserError = in.readInt() == 1;
    this.errorMessage = in.readString();
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
    dest.writeInt(hasUserError ? 1 : 0);
    dest.writeString(errorMessage);
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

  public boolean isOAuthForm() {
    return getType() != null && getType().equals("authForm");
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public boolean hasUserError() {
    return hasUserError;
  }

  public void setHasUserError(boolean hasUserError) {
    this.hasUserError = hasUserError;
  }

  @Nullable public String getClientID() {

    for (FormGroup formGroup : form) {
      for (FormField formField : formGroup.getFields()) {
        if (formField.getId().equals("clientID")) {
          return formField.getValue().toString();
        }
      }
    }
    return null;
  }

  @Nullable public String getClientSecret() {

    for (FormGroup formGroup : form) {
      for (FormField formField : formGroup.getFields()) {
        if (formField.getId().equals("clientSecret")) {
          return formField.getValue().toString();
        }
      }
    }
    return null;
  }

  @Nullable public String getAuthURL() {

    for (FormGroup formGroup : form) {
      for (FormField formField : formGroup.getFields()) {
        if (formField.getId().equals("authURL")) {
          return formField.getValue().toString();
        }
      }
    }
    return null;
  }

  @Nullable public String getTokenURL() {

    for (FormGroup formGroup : form) {
      for (FormField formField : formGroup.getFields()) {
        String fieldId = formField.getId();
        Object value = formField.getValue();
        if (fieldId.equals("tokenURL") && value != null && value.toString().endsWith("/token")) {
          return value.toString().substring(0, value.toString().length() - "/token".length());
        }
      }
    }
    return null;
  }

  @Nullable public String getScope() {

    for (FormGroup formGroup : form) {
      for (FormField formField : formGroup.getFields()) {
        if (formField.getId().equals("scope")) {
          return formField.getValue().toString();
        }
      }
    }
    return null;
  }

  /**
   * getOAuthLink: get first (unique?) oauth link
   * TODO: is it possible to have multiple authentication links?
   */
  @Nullable public Uri getOAuthLink() {
    if (isOAuthForm()) {

      String authURL = getAuthURL();
      String clientID = getClientID();
      String scope = getScope();

      if (authURL != null && clientID != null && scope != null) {

        return Uri.parse(authURL)
            .buildUpon()
            .appendQueryParameter("client_id", clientID)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", scope)
            // TODO: app config
            .appendQueryParameter("redirect_uri", "go-la://oauth-callback")
            .appendQueryParameter("state", UUID.randomUUID().toString())
            .build();

      }

    }
    return null;
  }

  public void setAuthData(AccessToken accessToken) {
    for (FormGroup formGroup : form) {
      for (FormField formField : formGroup.getFields()) {
        if (formField.getId().equals("access_token")) {
          // TODO: refactor FormFields using a design pattern
          ((StringFormField)formField).setValue(accessToken.getAccessToken());
        }
        if (formField.getId().equals("expires_in")) {
          ((StringFormField)formField).setValue("" + accessToken.getExpiresIn());
        }
      }
    }
  }
}