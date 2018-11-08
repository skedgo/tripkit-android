package com.skedgo.android.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;

import org.immutables.value.Value;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersBookingConfirmationPurchase.class)
public abstract class BookingConfirmationPurchase implements Parcelable {

  public static final Creator<BookingConfirmationPurchase> CREATOR = new Creator<BookingConfirmationPurchase>() {
    @Override public BookingConfirmationPurchase createFromParcel(Parcel in) {
      return ImmutableBookingConfirmationPurchase.builder()
          .currency(in.readString())
          .id(in.readString())
          .price(in.readFloat())
          .productName(in.readString())
          .productType(in.readString())
          .timezone(in.readString())
          .brand((PurchaseBrand) in.readParcelable(PurchaseBrand.class.getClassLoader()))
          .source((BookingSource) in.readParcelable(BookingSource.class.getClassLoader()))
          .validFor(in.readLong())
          .validFrom(in.readLong())
          .valid(in.readByte() == 1)
          .build();
    }

    @Override public BookingConfirmationPurchase[] newArray(int size) {
      return new BookingConfirmationPurchase[size];
    }
  };

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(currency());
    dest.writeString(id());
    dest.writeFloat(price());
    dest.writeString(productName());
    dest.writeString(productType());
    dest.writeString(timezone());
    dest.writeParcelable(brand(), flags);
    dest.writeParcelable(source(), flags);
    dest.writeLong(validFor());
    dest.writeLong(validFrom());
    dest.writeByte((byte) (valid() ? 1 : 0));
  }

  @Override public int describeContents() {
    return 0;
  }

  public abstract String currency();
  public abstract String id();
  public abstract float price();
  public abstract String productName();
  public abstract String productType();
  @Nullable public abstract String timezone();
  @Nullable public abstract PurchaseBrand brand();
  @Nullable public abstract BookingSource source();

  @Value.Default public long validFor() {
    return 0;
  }

  @Value.Default public long validFrom() {
    return 0;
  }

  @Value.Default public boolean valid() {
    return false;
  }

}
