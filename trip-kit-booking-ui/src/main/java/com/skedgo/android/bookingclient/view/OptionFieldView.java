package com.skedgo.android.bookingclient.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skedgo.android.bookingclient.R;
import com.skedgo.android.bookingclient.view.adapter.PrimitiveListAdapter;
import com.skedgo.android.tripkit.booking.OptionFormField;
import com.skedgo.android.tripkit.booking.viewmodel.OptionFieldViewModel;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class OptionFieldView extends LinearLayout {
    private TextView titleView;
    private TextView valueView;

    public OptionFieldView(Context context) {
        super(context);
    }

    public OptionFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public OptionFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OptionFieldView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindViewModel(@NonNull final OptionFieldViewModel viewModel,
                              @NonNull Observable<?> stopBindingSignal) {
        viewModel.onValueSelected()
                .takeUntil(stopBindingSignal)
                .subscribe(new Action1<OptionFieldViewModel>() {
                    @Override
                    public void call(OptionFieldViewModel self) {
                        titleView.setText(self.getPrimaryText());
                        valueView.setText(self.getSecondaryText());
                    }
                });

        if (isNotEmpty(viewModel.getAllValues())) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showValuePickerView(viewModel);
                }
            });
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        titleView = (TextView) findViewById(R.id.titleView);
        valueView = (TextView) findViewById(R.id.valueView);
    }

    void showValuePickerView(@NonNull final OptionFieldViewModel viewModel) {
        final AlertDialog valuePickerView = new AlertDialog.Builder(getContext())
                .setTitle(viewModel.getPrimaryText())
                .setSingleChoiceItems(
                        new ValuesAdapter(viewModel.getAllValues(), viewModel.getSelectedIndex()),
                        viewModel.getSelectedIndex(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.select(which);
                                dialog.dismiss();
                            }
                        }
                )
                .create();
        valuePickerView.show();
    }

    static class ValuesAdapter extends PrimitiveListAdapter<OptionFormField.OptionValue> {
        private final int selectedPosition;

        protected ValuesAdapter(List<OptionFormField.OptionValue> values, int selectedPosition) {
            super(values);
            this.selectedPosition = selectedPosition;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final OptionFormField.OptionValue value = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_field_option_item, parent, false);
            }

            final TextView titleView = ViewHolder.get(convertView, R.id.titleView);
            titleView.setText(value.getTitle());

            final TextView valueView = ViewHolder.get(convertView, R.id.valueView);
            valueView.setText(value.getValue());

            final ImageView checkView = ViewHolder.get(convertView, R.id.checkView);
            if (position == selectedPosition) {
                checkView.setVisibility(VISIBLE);
            } else {
                checkView.setVisibility(INVISIBLE);
            }

            return convertView;
        }
    }
}