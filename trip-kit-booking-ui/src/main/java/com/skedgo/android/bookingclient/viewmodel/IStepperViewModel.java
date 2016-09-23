package com.skedgo.android.bookingclient.viewmodel;

public interface IStepperViewModel {
  String getTitle();
  int getValue();
  String getSubtitle();
  int getMinValue();
  int getMaxValue();
  void decrement();
  void increment();
}