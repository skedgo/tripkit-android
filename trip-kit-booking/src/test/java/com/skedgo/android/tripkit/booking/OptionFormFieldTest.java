package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class OptionFormFieldTest {

  @Test public void Parcelable() {
    OptionFormField.OptionValue optionValue1 = new OptionFormField.OptionValue("A", "a");
    OptionFormField.OptionValue optionValue2 = new OptionFormField.OptionValue("B", "b");
    List<OptionFormField.OptionValue> list = new ArrayList<>();
    list.add(optionValue1);
    list.add(optionValue2);

    OptionFormField expected = new OptionFormField();
    expected.setValue(optionValue1);
    expected.setAllValues(list);
    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    OptionFormField actual = OptionFormField.CREATOR.createFromParcel(parcel);

    assertThat(actual.getValue().getTitle()).isEqualTo("A");
    assertThat(actual.getValue().getValue()).isEqualTo("a");
    List<OptionFormField.OptionValue> all = actual.getAllValues();
    assertThat(all).hasSize(2);
    assertThat(all.get(0).getTitle()).isEqualTo("A");
    assertThat(all.get(0).getValue()).isEqualTo("a");
    assertThat(all.get(1).getTitle()).isEqualTo("B");
    assertThat(all.get(1).getValue()).isEqualTo("b");
  }
}