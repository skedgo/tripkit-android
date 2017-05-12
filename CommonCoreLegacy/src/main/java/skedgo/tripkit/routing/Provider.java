package skedgo.tripkit.routing;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Provider implements Parcelable {
  public static final Creator<Provider> CREATOR = new Creator<Provider>() {
    @Override public Provider createFromParcel(Parcel source) {
      return new Provider(source);
    }

    @Override public Provider[] newArray(int size) {
      return new Provider[size];
    }
  };

  @SerializedName("name") private String name;

  @Override public int describeContents() {
    return 0;
  }

  public String getName() {
    return name;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {dest.writeString(this.name);}

  public Provider() {}

  protected Provider(Parcel in) {
    this.name = in.readString();
  }
}
