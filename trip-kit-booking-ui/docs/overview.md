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
If a trip segment has a booking action, you can call then your booking activity.
```
    final Booking booking = segment.getBooking();
    if (!TextUtils.isEmpty(booking.getUrl())) {

    	final Bundle extra = new Bundle();
    	extra.setClassLoader(TripGroup.class.getClassLoader());
    	extra.putParcelable(KEY_TRIP_GROUP, group);

    	Intent intent = new Intent(BookingActivity.ACTION_BOOK)
        		.putExtra(BookingActivity.KEY_URL, booking.getUrl())
        		.putExtra(BookingActivity.KEY_FIRST_SCREEN, true)
        		.putExtra(BookingActivity.KEY_BOOKING_BUNDLE, extra);
	fragment.startActivity(intent);
 
   } else {
      // handle external actions
    }
```
## Handle OAuth callback
As the oauth2 authentication is performed by an external application (web browser) you need to register the intent filter un your app.

### Manifest
```
    <activity
      android:name=“.YourLinkResolverActivity"
      android:enabled="true"
      android:exported="true">      
      <intent-filter>
        <data android:scheme=“your-app-scheme-registered-in-the-exernal-service” />
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
      </intent-filter>     
    </activity>
```

### Callback action
Finally, in `YourLinkResolverActivity`

```
	final Intent intent = getIntent();
    
	Uri uri = intent.getData();
	String host = uri.getHost();
	String urlDecoded = "";
	try {
	   urlDecoded = URLDecoder.decode(uri.toString(), "UTF-8");
	} catch (UnsupportedEncodingException e) {}

	if (intent.getScheme().equals(“your-app-scheme-registered-in-the-exernal-service”) {

	   if (urlDecoded.contains("oauth")) {

        	OAuth2CallbackHandler oAuth2CallbackHandler = BookingActivity.component.getOAuth2CallbackHandler();

        	oAuth2CallbackHandler.handleURL(getActivity(), uri)
            		.timeout(40, TimeUnit.SECONDS, Schedulers.newThread())
            		.observeOn(AndroidSchedulers.mainThread())
            		.subscribeOn(Schedulers.newThread())
            		.subscribe(new Action1<BookingForm>() {
              		@Override public void call(BookingForm form) {

				startActivity(new Intent(this, MyBookingActivity.class)
              					.setAction(BookingActivity.ACTION_BOOK_AFTER_OAUTH)
              					.putExtra(BookingActivity.KEY_FORM, (Parcelable) form));

              			}
            		}, new Action1<Throwable>() {
              			@Override public void call(Throwable throwable) {
        			        // handle error
	              }
 		});
	}

```



