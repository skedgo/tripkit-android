package com.skedgo.android.tripkit.booking.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.skedgo.android.common.rx.Var;
import com.skedgo.android.tripkit.booking.DateTimeFormField;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class DateTimeFieldViewModelImpl implements DateTimeViewModel, Parcelable {
  public static final Creator<DateTimeFieldViewModelImpl> CREATOR = new Creator<DateTimeFieldViewModelImpl>() {
    @Override
    public DateTimeFieldViewModelImpl createFromParcel(Parcel in) {
      return new DateTimeFieldViewModelImpl(in);
    }

    @Override
    public DateTimeFieldViewModelImpl[] newArray(int size) {
      return new DateTimeFieldViewModelImpl[size];
    }
  };

  private final Var<DateTimeFieldViewModelImpl> self = Var.create();
  private DateTimeFormField field;
  private Calendar calendar;

  private DateTimeFieldViewModelImpl(@NonNull DateTimeFormField field) {
    this.field = field;
    this.calendar = Calendar.getInstance();
    this.calendar.setTimeInMillis(TimeUnit.SECONDS.toMillis(field.getValue()));
  }

  protected DateTimeFieldViewModelImpl(Parcel in) {
    field = in.readParcelable(DateTimeFormField.class.getClassLoader());
    calendar = (Calendar) in.readSerializable();
  }

  public static DateTimeFieldViewModelImpl create(DateTimeFormField dateTimeFormField) {
    return new DateTimeFieldViewModelImpl(dateTimeFormField);
  }

  public Var<DateTimeFieldViewModelImpl> getSelf() {
    return self;
  }

  public void setTime(int hour, int minute) {
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    calendar.set(Calendar.MINUTE, minute);
    field.setValue(TimeUnit.MILLISECONDS.toSeconds(calendar.getTimeInMillis()));
    self.put(this);
  }

  public void setDate(int year, int month, int day) {
    calendar.set(year, month, day);
    field.setValue(TimeUnit.MILLISECONDS.toSeconds(calendar.getTimeInMillis()));
    self.put(this);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(field, flags);
    dest.writeSerializable(calendar);
  }

  @Override
  public int getYear() {
    return calendar.get(Calendar.YEAR);
  }

  @Override
  public int getMonth() {
    return calendar.get(Calendar.MONTH);
  }

  @Override
  public int getDay() {
    return calendar.get(Calendar.DAY_OF_MONTH);
  }

  @Override
  public int getHour() {
    return calendar.get(Calendar.HOUR_OF_DAY);
  }

  @Override
  public int getMinute() {
    return calendar.get(Calendar.MINUTE);
  }

  @Override
  public String getTitle() {
    return field.getTitle();
  }

  @Override
  public String getDate() {
    final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.US);
    return dateFormat.format(new Date(TimeUnit.SECONDS.toMillis(field.getValue())));
  }

  @Override
  public String getTime() {
    final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
    return dateFormat.format(new Date(TimeUnit.SECONDS.toMillis(field.getValue())));
  }
}