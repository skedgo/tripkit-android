package com.skedgo.android.tripkit.booking.viewmodel;

import com.skedgo.android.tripkit.booking.OptionFormField;
import com.skedgo.android.tripkit.booking.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class OptionFieldViewModelTest {
  @Test public void ShouldShowInitValueProperly() {
    OptionFieldViewModel viewModel = OptionFieldViewModel.create(generateOptionFormField());

    assertThat(viewModel.getPrimaryText())
        .describedAs("Should show the Title in optionValue")
        .isEqualTo("Title");
    assertThat(viewModel.getSecondaryText())
        .describedAs("Should show the Value in optionValue")
        .isEqualTo("Value");
  }

  @Test public void ShouldShowSelectedValueProperly() {
    OptionFieldViewModel viewModel = OptionFieldViewModel.create(generateOptionFormField());

    viewModel.select(1);
    assertThat(viewModel.getPrimaryText())
        .describedAs("Should show the Title of optionValue at index 1")
        .isEqualTo("Title 1");
    assertThat(viewModel.getSecondaryText())
        .describedAs("Should show the Value of optionValue at index 1")
        .isEqualTo("Value 1");

    viewModel.select(2);
    assertThat(viewModel.getPrimaryText())
        .describedAs("Should show the Title of optionValue at index 2")
        .isEqualTo("Title 2");
    assertThat(viewModel.getSecondaryText())
        .describedAs("Should show the Value of optionValue at index 2")
        .isEqualTo("Value 2");
  }

  private OptionFormField generateOptionFormField() {
    OptionFormField.OptionValue optionValue = new OptionFormField.OptionValue("Title", "Value");
    List<OptionFormField.OptionValue> allValues = new ArrayList<OptionFormField.OptionValue>();
    allValues.add(optionValue);
    allValues.add(new OptionFormField.OptionValue("Title 1", "Value 1"));
    allValues.add(new OptionFormField.OptionValue("Title 2", "Value 2"));
    allValues.add(new OptionFormField.OptionValue("Title 3", "Value 3"));

    OptionFormField optionFormField = new OptionFormField();
    optionFormField.setValue(optionValue);
    optionFormField.setAllValues(allValues);
    return optionFormField;
  }
}
