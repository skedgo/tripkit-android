package com.skedgo.android.tripkit.booking.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.skedgo.android.tripkit.booking.AddressFormField;
import com.skedgo.android.tripkit.booking.BookingAction;
import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.DateTimeFormField;
import com.skedgo.android.tripkit.booking.FormField;
import com.skedgo.android.tripkit.booking.FormGroup;
import com.skedgo.android.tripkit.booking.LinkFormField;
import com.skedgo.android.tripkit.booking.OptionFormField;
import com.skedgo.android.tripkit.booking.PasswordFormField;
import com.skedgo.android.tripkit.booking.StepperFormField;
import com.skedgo.android.tripkit.booking.StringFormField;
import com.skedgo.android.tripkit.booking.SwitchFormField;
import com.skedgo.android.tripkit.booking.ui.R;
import com.skedgo.android.tripkit.booking.ui.activity.BookingActivity;
import com.skedgo.android.tripkit.booking.ui.view.DateTimeFieldView;
import com.skedgo.android.tripkit.booking.ui.view.OptionFieldView;
import com.skedgo.android.tripkit.booking.ui.view.PasswordFieldView;
import com.skedgo.android.tripkit.booking.ui.view.PickupSubHeaderView;
import com.skedgo.android.tripkit.booking.ui.view.StepperView;
import com.skedgo.android.tripkit.booking.ui.view.StringFieldView;
import com.skedgo.android.tripkit.booking.ui.view.SwitchView;
import com.skedgo.android.tripkit.booking.ui.view.TwoLineFieldView;
import com.skedgo.android.tripkit.booking.ui.view.util.ViewUtils;
import com.skedgo.android.tripkit.booking.ui.viewmodel.StepperFieldViewModel;
import com.skedgo.android.tripkit.booking.viewmodel.DateTimeFieldViewModelImpl;
import com.skedgo.android.tripkit.booking.viewmodel.OptionFieldViewModel;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;
import skedgo.common.view.ButterKnifeFragment;

import static com.skedgo.android.tripkit.booking.ui.activity.BookingActivity.KEY_BOOKING_FORM;

public class BookingFormFragment extends ButterKnifeFragment {

  // TODO: REFACTOR THIS CLASS!!!!

  @Inject Bus bus;
  @Inject Picasso picasso;
  @Inject Gson gson;
  Button actionButton;
  ViewGroup formItemsView;
  LayoutInflater inflater;
  private BookingFormFragmentListener listener;

  public static BookingFormFragment newInstance(BookingForm form) {
    final Bundle args = new Bundle();
    args.putParcelable(KEY_BOOKING_FORM, form);

    final BookingFormFragment fragment = new BookingFormFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    inflater = LayoutInflater.from(activity);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentLayout(R.layout.fragment_booking_form);
    if (getActivity() instanceof BookingActivity) {
      ((BookingActivity) getActivity()).getBookingClientComponent().inject(this);
      listener = ((BookingActivity) getActivity());
    }
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    actionButton = (Button) view.findViewById(R.id.actionButton);
    formItemsView = (ViewGroup) view.findViewById(R.id.formItemsView);

    final BookingForm form = getArguments().getParcelable(KEY_BOOKING_FORM);
    showBookingTitle(form);
    showBookingForm(form);

  }

  private void showBookingTitle(BookingForm form) {
    listener.updateTitleEvent(form);

  }

  private void showBookingForm(final BookingForm form) {

    final BookingAction action = form.getAction();

    if (action != null) {
      actionButton.setEnabled(action.isEnable());
      actionButton.setText(action.getTitle());
      actionButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          bus.post(new PerformActionEvent(form));
        }
      });
    } else {
      actionButton.setVisibility(View.GONE);
    }

    final List<FormGroup> formGroups = form.getForm();
    if (CollectionUtils.isEmpty(formGroups)) {
      return;
    }

    for (FormGroup formGroup : formGroups) {
      if (formGroup.getTitle() != null && formGroup.getTitle().equals("Pick up")) {
        PickupSubHeaderView pickupSubHeaderView = (PickupSubHeaderView) inflater
            .inflate(R.layout.view_subheader_pick_up, formItemsView, false);
        pickupSubHeaderView.bindViewModel(formGroup);
        formItemsView.addView(pickupSubHeaderView);
        continue;
      }

      final TextView itemTitleView = (TextView) inflater
          .inflate(R.layout.view_list_subheader, formItemsView, false);
      ViewUtils.setText(itemTitleView, formGroup.getTitle());
      if (formGroup.getTitle() == null) {
        formItemsView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.v4_booking_margin_top), 0, 0);
      } else {
        formItemsView.addView(itemTitleView);
      }

      final List<FormField> formFields = formGroup.getFields();
      for (FormField field : formFields) {
        if (field instanceof BookingForm) {
          final BookingForm childBookingForm = (BookingForm) field;
          final TwoLineFieldView childBookingFormView =
              (TwoLineFieldView) inflater.inflate(R.layout.view_field_two_line, formItemsView, false);
          childBookingFormView.setPrimaryText(childBookingForm.getTitle());
          childBookingFormView.setSecondaryText(childBookingForm.getSubtitle());
          ImageView imageView = childBookingFormView.getImageView();
          picasso.load(childBookingForm.getImageUrl()).into(imageView);

          childBookingFormView.enableDivider();
          childBookingFormView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showChildBookingForm(childBookingForm);
            }
          });
          formItemsView.addView(childBookingFormView);
        } else if (field instanceof LinkFormField) {
          final LinkFormField linkField = (LinkFormField) field;
          final LinearLayout linkFieldView = (LinearLayout) inflater
              .inflate(R.layout.view_field_link, formItemsView, false);
          TextView linkView = (TextView) linkFieldView.findViewById(R.id.linkView);
          linkView.setText(linkField.getTitle());
          linkFieldView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              bus.post(new LinkFormFieldClickedEvent(linkField));
            }
          });
          formItemsView.addView(linkFieldView);
        } else if (field instanceof PasswordFormField) {
          final PasswordFormField passField = (PasswordFormField) field;
          final PasswordFieldView passFieldView =
              (PasswordFieldView) inflater.inflate(R.layout.view_field_password, formItemsView, false);
          passFieldView.bindViewModel(passField);
          formItemsView.addView(passFieldView);
        } else if (field instanceof StringFormField) {
          final StringFormField stringField = (StringFormField) field;
          final StringFieldView stringFieldView =
              (StringFieldView) inflater.inflate(R.layout.view_field_string, formItemsView, false);
          stringFieldView.bindViewModel(stringField);
          formItemsView.addView(stringFieldView);
        } else if (field instanceof DateTimeFormField) {
          final DateTimeFormField dateTimeField = (DateTimeFormField) field;
          final DateTimeFieldViewModelImpl viewModel = DateTimeFieldViewModelImpl.create(dateTimeField);
          final DateTimeFieldView dateTimeFieldView =
              (DateTimeFieldView) inflater.inflate(R.layout.view_field_date_time, formItemsView, false);
          dateTimeFieldView.bind(viewModel, getChildFragmentManager());
          viewModel.getSelf()
              .observe()
              .takeUntil(lifecycle().onDestroyView())
              .subscribe(new Action1<DateTimeFieldViewModelImpl>() {
                @Override
                public void call(DateTimeFieldViewModelImpl viewModel) {
                  dateTimeFieldView.setDateString(viewModel.getDate());
                  dateTimeFieldView.setTimeString(viewModel.getTime());
                }
              })
          ;
          formItemsView.addView(dateTimeFieldView);
        } else if (field instanceof AddressFormField) {
          final AddressFormField addressField = (AddressFormField) field;
          final TwoLineFieldView addressFieldView =
              (TwoLineFieldView) inflater.inflate(R.layout.view_field_two_line, formItemsView, false);
          final AddressFormField.Address address = addressField.getValue();
          if (address != null) {
            addressFieldView.setPrimaryText(addressField.getValue().getName());
          }
          formItemsView.addView(addressFieldView);
        } else if (field instanceof OptionFormField) {
          final OptionFieldView optionFieldView =
              (OptionFieldView) inflater.inflate(R.layout.view_field_option, formItemsView, false);
          optionFieldView.bindViewModel(
              OptionFieldViewModel.create((OptionFormField) field),
              lifecycle().onDestroyView()
          );
          formItemsView.addView(optionFieldView);
        } else if (field instanceof StepperFormField) {
          final StepperView stepperFieldView =
              (StepperView) inflater.inflate(R.layout.view_field_stepper, formItemsView, false);
          stepperFieldView.bindViewModel(new StepperFieldViewModel((StepperFormField) field));
          formItemsView.addView(stepperFieldView);
        } else if (field instanceof SwitchFormField) {
          final SwitchView switchView =
              (SwitchView) inflater.inflate(R.layout.view_field_switch, formItemsView, false);
          switchView.bindViewModel((SwitchFormField) field);
          formItemsView.addView(switchView);
        }
      }

      showFooter(formGroup.getFooter());
    }
  }

  private void showFooter(String footer) {
    if (footer != null) {
      TextView footerView = (TextView) inflater
          .inflate(R.layout.view_form_group_footer, formItemsView, false);
      footerView.setText(footer);
      formItemsView.addView(footerView);
    }
  }

  private void showChildBookingForm(@NonNull BookingForm childBookingForm) {
    final Fragment fragment = BookingFormFragment.newInstance(childBookingForm);
    getFragmentManager()
        .beginTransaction()
        .add(android.R.id.content, fragment)
        .addToBackStack(null)
        .commit();
  }

  public interface BookingFormFragmentListener {
    void updateTitleEvent(BookingForm form);
  }

  public static class PerformActionEvent {
    public final BookingForm form;

    public PerformActionEvent(BookingForm form) {
      this.form = form;
    }
  }

  public static class LinkFormFieldClickedEvent {
    public final LinkFormField linkField;

    public LinkFormFieldClickedEvent(LinkFormField linkField) {
      this.linkField = linkField;
    }
  }
}