package skedgo.tripkit.ui.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import skedgo.tripkit.ui.R;

public class SearchInputView extends RelativeLayout {
  private EditText mQueryField;
  private ImageButton mClearButton;
  private OnQueryTextListener mOnQueryTextListener;
  private ProgressBar mQueryProgressBar;
  private PublishSubject<Editable> onQueryTextChangeEventThrottle = PublishSubject.create();
  private TextWatcher mTextWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
      onQueryTextChangeEventThrottle.onNext(s);
    }
  };
  private Subscription onQueryTextChangeEventSubscription;

  public SearchInputView(Context context) {
    super(context);
    initLayout(context);
  }

  public SearchInputView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initLayout(context);
  }

  public SearchInputView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initLayout(context);
  }

  public void hideQueryProgressBar() {
    if (mQueryProgressBar.getVisibility() != GONE) {
      mQueryProgressBar.setVisibility(GONE);
    }
  }

  public void showQueryProgressBar() {
    if (mQueryProgressBar.getVisibility() != VISIBLE) {
      mQueryProgressBar.setVisibility(VISIBLE);
    }
  }

  public void setOnQueryTextListener(OnQueryTextListener onQueryTextListener) {
    mOnQueryTextListener = onQueryTextListener;
  }

  public EditText getQueryField() {
    return mQueryField;
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();

    // To avoid https://redmine.buzzhives.com/issues/4296.
    if (onQueryTextChangeEventSubscription != null) {
      onQueryTextChangeEventSubscription.unsubscribe();
    }
  }

  private void initLayout(Context context) {
    // See http://reactivex.io/documentation/operators/debounce.html.
    // TODO: 500ms is a proper delay?
    onQueryTextChangeEventSubscription = onQueryTextChangeEventThrottle
        .debounce(500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Editable>() {
          @Override
          public void call(Editable newText) {
            onQueryTextChange(newText);
          }
        });

    LayoutInflater.from(context).inflate(R.layout.v4_custom_view_search_input, this, true);
    initViews();
    enableLayoutTransition();
  }

  private void enableLayoutTransition() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      LayoutTransition layoutTransition = new LayoutTransition();
      layoutTransition.enableTransitionType(LayoutTransition.APPEARING);
      setLayoutTransition(layoutTransition);
    }
  }

  private void initViews() {
    initQueryField();
    initClearButton();

    mQueryProgressBar = (ProgressBar) findViewById(R.id.v4_query_progress_bar);
  }

  private void initClearButton() {
    mClearButton = (ImageButton) findViewById(R.id.v4_btn_clear);
    mClearButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mQueryField.setText(null);
      }
    });
  }

  private void initQueryField() {
    mQueryField = (EditText) findViewById(R.id.v4_query_field);
    mQueryField.addTextChangedListener(mTextWatcher);
  }

  private void onQueryTextChange(CharSequence newText) {
    if (mOnQueryTextListener != null) {
      mOnQueryTextListener.onQueryTextChange(newText);
    }

    adjustClearButtonVisibility(newText);
  }

  private void adjustClearButtonVisibility(CharSequence newQueryText) {
    if (!TextUtils.isEmpty(newQueryText)) {
      if (mClearButton.getVisibility() != VISIBLE) {
        mClearButton.setVisibility(VISIBLE);
      }
    } else {
      if (mClearButton.getVisibility() != GONE) {
        mClearButton.setVisibility(GONE);
      }
    }
  }

  public interface OnQueryTextListener {
    void onQueryTextChange(CharSequence newText);
  }
}