package com.skedgo.android.tripkit.booking.ui.viewmodel;

import android.support.annotation.NonNull;

import com.skedgo.android.tripkit.booking.StepperFormField;

public final class StepperFieldViewModel implements IStepperViewModel {
  private final StepperFormField stepperField;

  public StepperFieldViewModel(@NonNull StepperFormField stepperField) {
    this.stepperField = stepperField;
  }

  @Override
  public String getTitle() {
    return stepperField.getTitle();
  }

  @Override
  public int getValue() {
    return stepperField.getValue();
  }

  @Override
  public String getSubtitle() {
    return stepperField.getSubtitle();
  }

  @Override
  public int getMinValue() {
    return stepperField.getMinValue();
  }

  @Override
  public int getMaxValue() {
    return stepperField.getMaxValue();
  }

  @Override
  public void decrement() {
    if (stepperField.getValue() > stepperField.getMinValue()) {
      stepperField.setValue(stepperField.getValue() - 1);
    }
  }

  @Override
  public void increment() {
    if (stepperField.getValue() < stepperField.getMaxValue()) {
      stepperField.setValue(stepperField.getValue() + 1);
    }
  }
}