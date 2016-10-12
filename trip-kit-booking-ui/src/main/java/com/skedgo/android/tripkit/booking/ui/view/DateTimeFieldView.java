package com.skedgo.android.tripkit.booking.ui.view;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.skedgo.android.tripkit.booking.ui.R;
import com.skedgo.android.tripkit.booking.viewmodel.DateTimeFieldViewModelImpl;

public class DateTimeFieldView extends LinearLayout {
    private TextView titleView;
    private TextView dateView;
    private TextView timeView;

    public DateTimeFieldView(Context context) {
        super(context);
    }

    public DateTimeFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public DateTimeFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateTimeFieldView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bind(final DateTimeFieldViewModelImpl viewModel, final FragmentManager manager) {
        titleView.setText(viewModel.getTitle());
        dateView.setText(viewModel.getDate());
        timeView.setText(viewModel.getTime());
        dateView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment fragment = TimePickerFragment.newInstance(viewModel, true);
                fragment.show(manager, null);
            }
        });
        timeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment fragment = TimePickerFragment.newInstance(viewModel, false);
                fragment.show(manager, null);
            }
        });
    }

    public void setDateString(String date) {
        dateView.setText(date);
    }

    public void setTimeString(String time) {
        timeView.setText(time);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        titleView = (TextView) findViewById(R.id.titleView);
        dateView = (TextView) findViewById(R.id.dateView);
        timeView = (TextView) findViewById(R.id.timeView);
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener,
            DatePickerDialog.OnDateSetListener {

        private static final String KEY_PICK_DATE = "pickDate";
        public static String KEY_DATE_FORM_VIEW_MODEL = "time";

        private DateTimeFieldViewModelImpl viewModel;

        public static TimePickerFragment newInstance(DateTimeFieldViewModelImpl viewModel, boolean pickDate) {
            Bundle args = new Bundle();
            args.putParcelable(KEY_DATE_FORM_VIEW_MODEL, viewModel);
            args.putBoolean(KEY_PICK_DATE, pickDate);
            TimePickerFragment fragment = new TimePickerFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            viewModel = getArguments().getParcelable(KEY_DATE_FORM_VIEW_MODEL);
            boolean pickDate = getArguments().getBoolean(KEY_PICK_DATE);
            if (pickDate) {
                return new DatePickerDialog(getActivity(), this,
                        viewModel.getYear(), viewModel.getMonth(), viewModel.getDay());
            } else {
                return new TimePickerDialog(getActivity(), this,
                        viewModel.getHour(), viewModel.getMinute(), true);
            }
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            viewModel.setTime(hourOfDay, minute);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            viewModel.setDate(year, monthOfYear, dayOfMonth);
        }
    }
}

