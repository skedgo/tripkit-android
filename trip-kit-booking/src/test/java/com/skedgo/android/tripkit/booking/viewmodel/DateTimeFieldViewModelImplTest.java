package com.skedgo.android.tripkit.booking.viewmodel;

import android.os.Parcel;

import com.skedgo.android.tripkit.booking.BuildConfig;
import com.skedgo.android.tripkit.booking.DateTimeFormField;
import com.skedgo.android.tripkit.booking.TestRunner;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class DateTimeFieldViewModelImplTest {
  /**
   * FIXME: Temporarily ignore as it's flaky by the use of {@link Calendar#getInstance()}.
   */
  @Ignore
  @Test
  public void shouldReflectSomeValuesFromField() {
    final DateTimeFormField field = new DateTimeFormField();
    field.setValue(1234567890);
    field.setTitle("Time");
    final DateTimeFieldViewModelImpl viewModel = DateTimeFieldViewModelImpl.create(field);

    assertThat(viewModel.getTitle())
        .describedAs("Should reflect title from field")
        .isEqualTo("Time");
    assertThat(viewModel.getDate())
        .describedAs("Should format date from field")
        .isEqualTo("Sat, 14 Feb 2009");
    assertThat(viewModel.getTime())
        .describedAs("Should format time from field")
        .isEqualTo("06:31");

    assertThat(viewModel.getDay()).isEqualTo(14);
    assertThat(viewModel.getMonth()).isEqualTo(1);
    assertThat(viewModel.getYear()).isEqualTo(2009);
    assertThat(viewModel.getHour()).isEqualTo(6);
    assertThat(viewModel.getMinute()).isEqualTo(31);
  }

  /**
   * FIXME: Temporarily ignore as it's flaky by the use of {@link Calendar#getInstance()}.
   */
  @Ignore
  @Test
  public void shouldDisplayHourIn24hFormat() {
    final DateTimeFormField field = new DateTimeFormField();
    field.setValue(1445235929);
    field.setTitle("Time");

    final DateTimeFieldViewModelImpl viewModel = DateTimeFieldViewModelImpl.create(field);
    assertThat(viewModel.getHour()).isEqualTo(13);
  }

  @Test
  public void shouldBeParcelledProperly() {
    final DateTimeFormField field = new DateTimeFormField();
    field.setValue(1234567890);
    field.setTitle("Time");
    final DateTimeFieldViewModelImpl expected = DateTimeFieldViewModelImpl.create(field);

    final Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);

    final DateTimeFieldViewModelImpl actual = DateTimeFieldViewModelImpl.CREATOR.createFromParcel(parcel);
    assertThat(actual.getTitle())
        .describedAs("Title should be parcelled properly")
        .isEqualTo(expected.getTitle());
    assertThat(actual.getDate())
        .describedAs("Date should parcelled properly")
        .isEqualTo(expected.getDate());
    assertThat(actual.getTime())
        .describedAs("Time should parcelled properly")
        .isEqualTo(expected.getTime());
  }

  @Test
  public void shouldShowSelectedValueProperly() {
    final DateTimeFormField field = new DateTimeFormField();
    field.setValue(1234567890);
    field.setTitle("Time");

    final DateTimeFieldViewModelImpl viewModel = DateTimeFieldViewModelImpl.create(field);
    viewModel.setTime(23, 59);
    viewModel.setDate(2015, 10, 20);

    assertThat(viewModel.getDate())
        .describedAs("Should reflect new date selected")
        .isEqualTo("Fri, 20 Nov 2015");
    assertThat(viewModel.getTime())
        .describedAs("Should reflect new time selected")
        .isEqualTo("23:59");
  }
}