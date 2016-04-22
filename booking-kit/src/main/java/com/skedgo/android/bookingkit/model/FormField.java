package com.skedgo.android.bookingkit.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public abstract class FormField implements Parcelable, Serializable {

  public static final int ADDRESS = 5;
  public static final int BOOKINGFORM = 7;
  public static final int DATETIME = 3;
  public static final int LINK = 6;
  public static final int OPTION = 2;
  public static final int STEPPER = 4;
  public static final int STRING = 1;
  public static final int SWITCH = 8;
  public static final Creator<FormField> CREATOR = new Creator<FormField>() {
    @Override
    public FormField createFromParcel(Parcel in) {
      switch (in.readInt()) {
        case STRING:
          return new StringFormField(in);
        case OPTION:
          return new OptionFormField(in);
        case DATETIME:
          return new DateTimeFormField(in);
        case STEPPER:
          return new StepperFormField(in);
        case LINK:
          return new LinkFormField(in);
        case ADDRESS:
          return new AddressFormField(in);
        case BOOKINGFORM:
          return new BookingForm(in);
        case SWITCH:
          return new SwitchFormField(in);
        default:
          return null;
      }
    }

    @Override
    public FormField[] newArray(int size) {
      return new FormField[size];
    }
  };

  @SerializedName("id")
  private String id;
  @SerializedName("title")
  private String title;
  @SerializedName("subtitle")
  private String subtitle;
  @SerializedName("sidetitle")
  private String sidetitle;
  @SerializedName("required")
  private boolean required;
  @SerializedName("readOnly")
  private boolean readOnly;
  @SerializedName("hidden")
  private boolean hidden;
  @SerializedName("type")
  private String type;

  protected FormField(Parcel in) {
    this.id = in.readString();
    this.title = in.readString();
    this.subtitle = in.readString();
    this.sidetitle = in.readString();
    this.required = in.readInt() == 1;
    this.readOnly = in.readInt() == 1;
    this.hidden = in.readInt() == 1;
    this.type = in.readString();
  }

  protected FormField() {

  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(title);
    dest.writeString(subtitle);
    dest.writeString(sidetitle);
    dest.writeInt(required ? 1 : 0);
    dest.writeInt(readOnly ? 1 : 0);
    dest.writeInt(hidden ? 1 : 0);
    dest.writeString(type);
  }

  public abstract Object getValue();

  @Nullable
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Nullable
  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  @Nullable
  public String getSidetitle() {
    return sidetitle;
  }

  public void setSidetitle(String sidetitle) {
    this.sidetitle = sidetitle;
  }

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }
}
