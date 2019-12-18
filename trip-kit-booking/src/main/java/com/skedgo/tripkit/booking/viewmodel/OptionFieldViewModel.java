package com.skedgo.tripkit.booking.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.skedgo.tripkit.common.rx.Var;
import com.skedgo.tripkit.booking.OptionFormField;

import io.reactivex.Flowable;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class OptionFieldViewModel implements TwoLineViewModel {
  private final OptionFormField optionField;
  private Var<OptionFieldViewModel> onValueSelected = Var.create();
  private int selectedIndex;

  protected OptionFieldViewModel(@NonNull OptionFormField optionField) {
    this.optionField = optionField;
    this.onValueSelected.put(this);
    this.selectedIndex = -1;
  }

  public static OptionFieldViewModel create(@NonNull OptionFormField optionField) {
    return new OptionFieldViewModel(optionField);
  }

  public Flowable<OptionFieldViewModel> onValueSelected() {
    return onValueSelected.observe();
  }

  public List<OptionFormField.OptionValue> getAllValues() {
    return optionField.getAllValues();
  }

  public int getSelectedIndex() {
    if (selectedIndex == -1) {
      selectedIndex = findSelectedIndex();
    }
    return selectedIndex;
  }

  public void select(int valueIndex) {
    selectedIndex = valueIndex;
    final List<OptionFormField.OptionValue> allValues = optionField.getAllValues();
    if (CollectionUtils.isEmpty(allValues)
        && (valueIndex < 0 || valueIndex >= allValues.size())) {
      return;
    }

    final OptionFormField.OptionValue newSelectedValue = allValues.get(valueIndex);
    optionField.setValue(newSelectedValue);
    onValueSelected.put(this);
  }

  @Override
  public String getPrimaryText() {
    final OptionFormField.OptionValue selectedValue = optionField.getValue();
    return selectedValue != null ? selectedValue.getTitle() : null;
  }

  @Nullable
  @Override
  public String getSecondaryText() {
    final OptionFormField.OptionValue selectedValue = optionField.getValue();
    return selectedValue != null ? selectedValue.getValue() : null;
  }

  private int findSelectedIndex() {
    final OptionFormField.OptionValue selectedValue = optionField.getValue();
    final List<OptionFormField.OptionValue> allValues = optionField.getAllValues();
    if (selectedValue != null && CollectionUtils.isNotEmpty(allValues)) {
      for (int i = 0; i < allValues.size(); i++) {
        if (TextUtils.equals(allValues.get(i).getValue(), selectedValue.getValue())) {
          return i;
        }
      }
    }
    return 0;
  }
}