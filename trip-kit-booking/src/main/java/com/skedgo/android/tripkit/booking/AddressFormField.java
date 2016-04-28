package com.skedgo.android.tripkit.booking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AddressFormField extends FormField {

  public static final Creator<AddressFormField> CREATOR = new Creator<AddressFormField>() {
    @Override
    public AddressFormField createFromParcel(Parcel in) {
      in.readInt();
      return new AddressFormField(in);
    }

    @Override
    public AddressFormField[] newArray(int size) {
      return new AddressFormField[size];
    }
  };
  @SerializedName("value")
  private Address value;

  public AddressFormField() {
    super();
  }

  public AddressFormField(Parcel in) {
    super(in);
    this.value = in.readParcelable(Address.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(ADDRESS);
    super.writeToParcel(dest, flags);
    dest.writeParcelable(value, flags);
  }

  @Override
  public Address getValue() {
    return value;
  }

  public void setValue(Address value) {
    this.value = value;
  }

  public static class Address implements Parcelable {

    public static final Creator<Address> CREATOR = new Creator<Address>() {
      @Override
      public Address createFromParcel(Parcel in) {
        return new Address(in);
      }

      @Override
      public Address[] newArray(int size) {
        return new Address[size];
      }
    };
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lng")
    private double longitude;
    @SerializedName("address")
    private String address;
    @SerializedName("name")
    private String name;

    public Address() {

    }

    public Address(Parcel in) {
      this.latitude = in.readDouble();
      this.longitude = in.readDouble();
      this.address = in.readString();
      this.name = in.readString();
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeDouble(latitude);
      dest.writeDouble(longitude);
      dest.writeString(address);
      dest.writeString(name);
    }

    public double getLatitude() {
      return latitude;
    }

    public void setLatitude(double latitude) {
      this.latitude = latitude;
    }

    public double getLongitude() {
      return longitude;
    }

    public void setLongitude(double longitude) {
      this.longitude = longitude;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
