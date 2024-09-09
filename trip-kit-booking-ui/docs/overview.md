# Overview

## `BookingActivity` declaration

### Extend `BookingActivity`

`BookingActivity` is the base application component for booking UI.

```
import com.skedgo.android.bookingclient.activity.BookingActivity;

public class MyBookingActivity extends BookingActivity{

  @Override public void reportProblem() {
    // override to implemente problem report
  }
}
```

### Manifest

You need to extend from it and add some intent filters in your manifest.

```
     <activity
      android:name=“.MyBookingActivity"
      android:exported="false">
      <intent-filter>
        <action android:name="com.skedgo.android.bookingclient.ACTION_BOOK" />
        <category android:name="android.intent.category.DEFAULT" />
      </intent-filter>
      <intent-filter>
        <action android:name="com.skedgo.android.bookingclient.ACTION_BOOK2" />
        <category android:name="android.intent.category.DEFAULT" />
      </intent-filter>
    </activity>
```

## Open `BookingActivity`

If a trip segment has a booking action, and you are NOT using `Quick Booking`, you can call then
your booking activity.

```
    final Booking booking = segment.getBooking();
    if (!TextUtils.isEmpty(booking.getUrl())) {

    	final Bundle extra = new Bundle();
    	extra.setClassLoader(TripGroup.class.getClassLoader());
    	extra.putParcelable(KEY_TRIP_GROUP, group);

    	Intent intent = new Intent(BookingActivity.ACTION_BOOK)
        		.putExtra(BookingActivity.KEY_URL, booking.getUrl())
        		.putExtra(BookingActivity.KEY_FIRST_SCREEN, true);
	fragment.startActivity(intent);

   } else {
      // handle external actions
    }
```

## OAuth

Oauth2 logic has been moved to
Tripkit-account: https://github.com/skedgo/tripkit-android/blob/master/trip-kit-booking/docs/overview.md#oauth
