/*
 * Copyright (c) SkedGo 2013
 */

package com.skedgo.android.bookingclient.activity;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.skedgo.android.bookingclient.R;

import static com.skedgo.android.common.util.LogUtils.makeTag;

public abstract class ProgressEnabledActivity extends AppCompatActivity {
  public static final String TAG = makeTag("ProgressEnabledActivity");
  private static final String KEY_PROGRESS_COUNTER = "progressCounter";

  private int progressCounter = 0;
  private MenuItem progressMenuItem;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
      progressCounter = savedInstanceState.getInt(KEY_PROGRESS_COUNTER, 0);

      if (progressCounter > 0) {
        setSupportProgressBarIndeterminateVisibility(true);
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_progress_indicator, menu);

    progressMenuItem = menu.findItem(R.id.progressMenuItem);
    progressMenuItem.setVisible(progressCounter != 0);
    MenuItemCompat.setActionView(progressMenuItem, R.layout.actionbar_indeterminate_progress);

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public void onStart() {
    super.onStart();

    if (getSupportActionBar() != null) {
      if (progressCounter > 0) {
        setSupportProgressBarIndeterminateVisibility(true);
      } else {
        setSupportProgressBarIndeterminateVisibility(false);
      }
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(KEY_PROGRESS_COUNTER, progressCounter);
  }

  public void showProgressBar() {
    progressCounter++;

    if (getSupportActionBar() != null) {
      setSupportProgressBarIndeterminateVisibility(true);
    }
  }

  public boolean hideProgressBar() {
    progressCounter--;

    if (progressCounter < 0) {
      progressCounter = 0;
    }

    // Check if we are waiting for any other progress-able items.
    if (progressCounter == 0 && getSupportActionBar() != null) {
      setSupportProgressBarIndeterminateVisibility(false);
      return true;
    }

    return false;
  }

  @Override
  public void setSupportProgressBarIndeterminateVisibility(boolean visible) {
    if (progressMenuItem != null) {
      progressMenuItem.setVisible(visible);
    }
  }
}