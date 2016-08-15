package com.skedgo.android.bookingclient.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.skedgo.android.bookingclient.fragment.AuthWebFragment;

import skedgo.anim.AnimatedTransitionActivity;

public class AuthWebActivity extends AnimatedTransitionActivity {

  public static final String WEB_URL = "web_url";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState == null) {
          Log.e("**", "URL0 " + getIntent().getStringExtra(WEB_URL));
      Uri uri = getIntent().getParcelableExtra(WEB_URL);
      getSupportFragmentManager()
          .beginTransaction()
          .replace(android.R.id.content, AuthWebFragment.newInstance(uri.toString()))
          .commit();
    }
  }
}