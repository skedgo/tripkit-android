package skedgo.tripkit.routing;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Source implements Parcelable {


  @SerializedName("disclaimer") private String disclaimer;
  @SerializedName("provider") private Provider provider;

  public String getDisclaimer() {
    return disclaimer;
  }

  public Provider getProvider() {
    return provider;
  }

  @Override public int describeContents() { return 0; }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.disclaimer);
    dest.writeParcelable(this.provider, flags);
  }

  public Source() {}

  protected Source(Parcel in) {
    this.disclaimer = in.readString();
    this.provider = in.readParcelable(Provider.class.getClassLoader());
  }

  public static final Creator<Source> CREATOR = new Creator<Source>() {
    @Override public Source createFromParcel(Parcel source) {return new Source(source);}

    @Override public Source[] newArray(int size) {return new Source[size];}
  };
}