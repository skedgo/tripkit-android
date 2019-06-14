package skedgo.tripkit.ui.core;

import android.view.View;
import android.widget.ImageView;

import com.skedgo.android.common.util.TransportModeUtils;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.databinding.ObservableBoolean;
import skedgo.tripkit.routing.ModeInfo;
import skedgo.tripkit.routing.VehicleMode;
import skedgo.tripkit.ui.R;
import skedgo.tripkit.ui.module.TripkitUiComponent;

public final class Converters {
  private Converters() {}

  /**
   * Binds a boolean into {@link View#setVisibility(int)}.
   * Sample: `android:visibility="@{viewModel.isBusy}`
   * `isBusy` can be an {@link ObservableBoolean}.
   */
  @BindingConversion
  public static int convertBooleanToViewVisibility(boolean value) {
    return value ? View.VISIBLE : View.GONE;
  }

  @BindingAdapter("android:src")
  public static void setModeInfo(TripkitUiComponent component, ImageView view, @Nullable ModeInfo modeInfo) {
    if (component == null) {
      return;
    }

    if (modeInfo == null) {
      component.picasso()
          .load(R.drawable.ic_public_transport)
          .into(view);
      return;
    }

    final VehicleMode mode = modeInfo.getModeCompat();
    int placeHolder = mode != null ? mode.getIconRes() : R.drawable.ic_public_transport;
    final String url = TransportModeUtils.getIconUrlForModeInfo(view.getResources(), modeInfo);
    component.picasso()
        .load(url)
        .placeholder(placeHolder)
        .error(placeHolder)
        .into(view);
  }
}