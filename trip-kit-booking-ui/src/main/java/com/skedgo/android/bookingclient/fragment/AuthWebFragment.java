package com.skedgo.android.bookingclient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import rx.functions.Action1;
import skedgo.common.view.ButterKnifeFragment;

@Deprecated
public class AuthWebFragment extends ButterKnifeFragment {

  private WebView webView;
  private Action1<String> onCallback;

  public static AuthWebFragment newInstance(String url, Action1<String> onCallback) {
    final Bundle args = new Bundle();

    args.putString("url", url);

    final AuthWebFragment fragment = new AuthWebFragment();
    fragment.setArguments(args);
    fragment.onCallback = onCallback;
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    if (webView != null) {
      webView.destroy();
    }
    webView = new WebView(getActivity());
    return webView;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    String url = getArguments().getString("url");

    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setLoadWithOverviewMode(true);
    webView.getSettings().setUseWideViewPort(true);
    webView.setWebViewClient(new WebViewClient() {

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (url.startsWith("tripgo://")) {
          onCallback.call(url);
          webView.setVisibility(View.INVISIBLE);
          return false;
        } else {
          view.loadUrl(url);
        }
        return true;
      }

      @Override
      public void onPageFinished(WebView view, final String url) {

      }
    });

    webView.loadUrl(url);
  }

}
