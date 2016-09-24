package com.skedgo.android.bookingclient.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skedgo.android.bookingclient.R;
import com.skedgo.android.tripkit.booking.AddressFormField;
import com.skedgo.android.tripkit.booking.FormGroup;


public class PickupSubHeaderView extends LinearLayout {
    private TextView pickupView;
    private TextView addressView;

    public PickupSubHeaderView(Context context) {
        super(context);
    }

    public PickupSubHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public PickupSubHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PickupSubHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindViewModel(FormGroup pickupFormGroup) {
        pickupView.setText(pickupFormGroup.getTitle());
        AddressFormField addressFormField = (AddressFormField) pickupFormGroup.getFields().get(0);
        addressView.setText(addressFormField.getValue().getName());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        pickupView = (TextView) findViewById(R.id.pickupView);
        addressView = (TextView) findViewById(R.id.addressView);
    }
}
