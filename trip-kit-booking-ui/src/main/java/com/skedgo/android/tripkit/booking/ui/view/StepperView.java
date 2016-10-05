package com.skedgo.android.tripkit.booking.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.skedgo.android.tripkit.booking.ui.R;
import com.skedgo.android.tripkit.booking.ui.view.util.ViewUtils;
import com.skedgo.android.tripkit.booking.ui.viewmodel.IStepperViewModel;

public class StepperView extends LinearLayout {
    private TextView titleView;
    private TextView subtitleView;
    private TextView valueView;
    private View incrementButton;
    private View decrementButton;

    public StepperView(Context context) {
        super(context);
    }

    public StepperView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public StepperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StepperView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindViewModel(@NonNull final IStepperViewModel viewModel) {
        titleView.setText(viewModel.getTitle());
        ViewUtils.setText(subtitleView, viewModel.getSubtitle());
        valueView.setText(String.valueOf(viewModel.getValue()));
        updateButtonsState(viewModel);

        incrementButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.increment();
                updateButtonsState(viewModel);
            }
        });
        decrementButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.decrement();
                updateButtonsState(viewModel);
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleView = (TextView) findViewById(R.id.titleView);
        subtitleView = (TextView) findViewById(R.id.subtitleView);
        valueView = (TextView) findViewById(R.id.valueView);
        incrementButton = findViewById(R.id.incrementButton);
        decrementButton = findViewById(R.id.decrementButton);
    }

    private void updateButtonsState(IStepperViewModel viewModel) {
        valueView.setText(String.valueOf(viewModel.getValue()));
        if (viewModel.getValue() == viewModel.getMinValue()) {
            ViewHelper.setAlpha(decrementButton, 0.5f);
        } else {
            ViewHelper.setAlpha(decrementButton, 1f);
        }
        if (viewModel.getValue() == viewModel.getMaxValue()) {
            ViewHelper.setAlpha(incrementButton, 0.5f);
        } else {
            ViewHelper.setAlpha(incrementButton, 1f);
        }
    }
}