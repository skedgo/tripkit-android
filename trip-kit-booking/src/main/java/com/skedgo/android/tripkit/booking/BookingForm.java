package com.skedgo.android.tripkit.booking;

import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.collections4.CollectionUtils;

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

  @Nullable public BookingAction getAction() {
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
    return value;
  }

  public boolean isOAuthForm() {
    return "authForm".equals(getType());
  }

  public boolean isPayIQAuthForm() {
    return "payiq".equals(value);
  }

  @Nullable public String getClientID() {
    return getValueFromField(FormField.CLIENT_ID);
  }

  @Nullable public String getClientSecret() {
    return getValueFromField(FormField.CLIENT_SECRET);
  }

  @Nullable public String getAuthURL() {
    return getValueFromField(FormField.AUTH_URL);
  }

  @Nullable public String getToken() {
    return getValueFromField(FormField.ACCESS_TOKEN);
  }

  @Nullable public int getExpiresIn() {
    return Integer.valueOf(getValueFromField(FormField.EXPIRES_IN));
  }

  @Nullable public String getRefreshToken() {
    return getValueFromField(FormField.REFRESH_TOKEN);
  }

  @Nullable public String getTokenURL() {

    for (FormGroup formGroup : form) {
      for (FormField formField : formGroup.getFields()) {
        String fieldId = formField.getId();
        Object value = formField.getValue();
        if (FormField.TOKEN_URL.equals(fieldId) && value != null && value.toString().endsWith("/token")) {
          return value.toString().substring(0, value.toString().length() - "/token".length());
        }
      }
    }
    return null;
  }

  @Nullable public String getScope() {

    for (FormGroup formGroup : form) {
      for (FormField formField : formGroup.getFields()) {
        if (formField.getId() != null && formField.getValue() != null && formField.getId().equals(FormField.SCOPE)) {
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

        Uri.Builder builder = Uri.parse(authURL)
            .buildUpon()
            .appendQueryParameter("client_id", clientID)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("state", UUID.randomUUID().toString())
            .appendQueryParameter("redirect_uri", getValueFromField(FormField.REDIRECT_URI));

        if (!TextUtils.isEmpty(scope)) {
          builder = builder.appendQueryParameter("scope", scope);
        }

        return builder.build();

      }

    }
    return null;
  }

  public BookingForm setAuthData(ExternalOAuth externalOAuth) {
    for (FormGroup formGroup : form) {
      for (FormField formField : formGroup.getFields()) {
        if (formField.getId() != null && formField.getId().equals(FormField.ACCESS_TOKEN)) {
          // TODO: refactor FormFields using a design pattern
          ((StringFormField) formField).setValue(externalOAuth.token());
        }
        if (formField.getId().equals(FormField.EXPIRES_IN)) {
          ((StringFormField) formField).setValue("" + externalOAuth.expiresIn());
        }
        if (formField.getId().equals(FormField.REFRESH_TOKEN) && externalOAuth.refreshToken() != null) {
          ((StringFormField) formField).setValue("" + externalOAuth.refreshToken());
        }
      }
    }
    return this;
  }

  public String externalAction() {

    if (form != null && !CollectionUtils.isEmpty(form)) {
      for (FormGroup group : form) {
        for (FormField field : group.getFields()) {
          if (field instanceof LinkFormField &&
              (LinkFormField.METHOD_EXTERNAL.equals(((LinkFormField) field).getMethod()))) {
            return ((LinkFormField) field).getValue();
          }
        }
      }
    }

    return null;
  }

  @Nullable private String getValueFromField(@NonNull String fieldName) {
    for (FormGroup formGroup : form) {
      for (FormField formField : formGroup.getFields()) {
        if (fieldName.equals(formField.getId()) && formField.getValue() != null) {
          return formField.getValue().toString();
        }
      }
    }
    return null;
  }

}