package com.skedgo.tripkit.bookingproviders;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.skedgo.tripkit.BookingAction;
import com.skedgo.tripkit.ExternalActionParams;
import com.skedgo.tripkit.common.model.Location;
import com.skedgo.tripkit.geocoding.ReverseGeocodable;
import com.skedgo.tripkit.routing.TripSegment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import androidx.test.core.app.ApplicationProvider;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static com.skedgo.tripkit.bookingproviders.BookingResolver.FLITWAYS;
import static com.skedgo.tripkit.bookingproviders.BookingResolver.GOCATCH;
import static com.skedgo.tripkit.bookingproviders.BookingResolver.INGOGO;
import static com.skedgo.tripkit.bookingproviders.BookingResolver.LYFT;
import static com.skedgo.tripkit.bookingproviders.BookingResolver.OTHERS;
import static com.skedgo.tripkit.bookingproviders.BookingResolver.SMS;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("WrongConstant")
@RunWith(RobolectricTestRunner.class)
public class BookingResolverImplTest {
    private static final Comparator<BookingAction> BOOKING_ACTION_COMPARATOR =
        new Comparator<BookingAction>() {
            @Override
            public int compare(BookingAction lhs, BookingAction rhs) {
                final Intent lhsData = lhs.data();
                final Intent rhsData = rhs.data();
                return lhs.hasApp() == rhs.hasApp() &&
                    lhs.bookingProvider() == rhs.bookingProvider() &&
                    lhsData.getAction().equals(rhsData.getAction()) &&
                    lhsData.getData().equals(rhsData.getData())
                    ? 0 : 1;
            }
        };
    @Mock
    PackageManager packageManager;
    @Mock
    ReverseGeocodable geocoderFactory;
    private BookingResolverImpl bookingResolver;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        bookingResolver = new BookingResolverImpl(
            ApplicationProvider.getApplicationContext().getResources(),
            packageManager,
            geocoderFactory
        );
    }

    @Test
    public void hasLyftApp() throws PackageManager.NameNotFoundException {
        when(packageManager.getPackageInfo(
            eq("me.lyft.android"),
            eq(PackageManager.GET_ACTIVITIES)
        )).thenReturn(new PackageInfo());

        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("lyft")
                .segment(mock(TripSegment.class))
                .build()
        ).test();

        final BookingAction action = BookingAction.builder()
            .bookingProvider(LYFT)
            .hasApp(true)
            .data(new Intent(Intent.ACTION_VIEW, Uri.parse("lyft://")))
            .build();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        final List<BookingAction> events = subscriber.values();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
            .usingComparator(BOOKING_ACTION_COMPARATOR)
            .isEqualTo(action);
    }

    @Test
    public void hasNoLyftApp() throws PackageManager.NameNotFoundException {
        when(packageManager.getPackageInfo(
            eq("me.lyft.android"),
            eq(PackageManager.GET_ACTIVITIES)
        )).thenThrow(new PackageManager.NameNotFoundException());

        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("lyft")
                .segment(mock(TripSegment.class))
                .build()
        ).test();

        final Intent data = new Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse("https://play.google.com/store/apps/details?id=me.lyft.android"));
        final BookingAction action = BookingAction.builder()
            .bookingProvider(LYFT)
            .hasApp(false)
            .data(data)
            .build();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        final List<BookingAction> events = subscriber.values();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
            .usingComparator(BOOKING_ACTION_COMPARATOR)
            .isEqualTo(action);
    }

    @Test
    public void hasNoFlitwaysApp_noPartnerKey() {
        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("flitways")
                .segment(mock(TripSegment.class))
                .build()
        ).test();

        final BookingAction action = BookingAction.builder()
            .bookingProvider(FLITWAYS)
            .hasApp(false)
            .data(new Intent(Intent.ACTION_VIEW, Uri.parse("https://flitways.com")))
            .build();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        final List<BookingAction> events = subscriber.values();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
            .usingComparator(BOOKING_ACTION_COMPARATOR)
            .isEqualTo(action);
    }

    @Test
    public void hasNoFlitwaysApp_hasPartnerKey() {
        when(geocoderFactory.getAddress(anyDouble(), anyDouble()))
            .thenAnswer(new Answer<Observable<String>>() {
                @Override
                public Observable<String> answer(InvocationOnMock invocation) {
                    final double lat = invocation.getArgument(0);
                    if (lat == 1.0) {
                        return Observable.just("A");
                    } else {
                        return Observable.just("B");
                    }
                }
            });

        final Calendar time = Calendar.getInstance(TimeZone.getTimeZone("Australia/Sydney"), Locale.US);
        time.set(2014, Calendar.FEBRUARY, 14, 11, 10);

        final TripSegment segment = mock(TripSegment.class);
        when(segment.getFrom()).thenReturn(new Location(1.0, 2.0));
        when(segment.getTo()).thenReturn(new Location(3.0, 4.0));
        when(segment.getTimeZone()).thenReturn("Australia/Sydney");
        when(segment.getStartTimeInSecs()).thenReturn(TimeUnit.MILLISECONDS.toSeconds(time.getTimeInMillis()));

        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("flitways")
                .segment(segment)
                .flitWaysPartnerKey("25251325")
                .build()
        ).test();

        final String url = "https://flitways.com/api/link?trip_date=02%2F14%2F2014%2011%3A10%20AM&key=25251325&pickup=A&destination=B";
        final BookingAction action = BookingAction.builder()
            .bookingProvider(FLITWAYS)
            .hasApp(false)
            .data(new Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            .build();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        final List<BookingAction> events = subscriber.values();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
            .usingComparator(BOOKING_ACTION_COMPARATOR)
            .isEqualTo(action);
    }

    @Test
    public void hasNoApp() {
        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("https://github.com/")
                .segment(mock(TripSegment.class))
                .build()
        ).test();

        final BookingAction action = BookingAction.builder()
            .bookingProvider(OTHERS)
            .hasApp(false)
            .data(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/")))
            .build();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        final List<BookingAction> events = subscriber.values();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
            .usingComparator(BOOKING_ACTION_COMPARATOR)
            .isEqualTo(action);
    }

    @Test
    public void hasNoGoCatchApp() throws PackageManager.NameNotFoundException {
        when(packageManager.getPackageInfo(
            eq("com.gocatchapp.goCatch"),
            eq(PackageManager.GET_ACTIVITIES)
        )).thenThrow(new PackageManager.NameNotFoundException());

        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("gocatch")
                .segment(mock(TripSegment.class))
                .build()
        ).test();

        final Intent data = new Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse("https://play.google.com/store/apps/details?id=com.gocatchapp.goCatch"));
        final BookingAction action = BookingAction.builder()
            .bookingProvider(GOCATCH)
            .hasApp(false)
            .data(data)
            .build();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        final List<BookingAction> events = subscriber.values();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
            .usingComparator(BOOKING_ACTION_COMPARATOR)
            .isEqualTo(action);
    }

    @Test
    public void hasGoCatchApp() throws PackageManager.NameNotFoundException {
        when(geocoderFactory.getAddress(anyDouble(), anyDouble()))
            .thenAnswer(new Answer<Observable<String>>() {
                @Override
                public Observable<String> answer(InvocationOnMock invocation) {
                    final double lat = invocation.getArgument(0);
                    if (lat == 1.0) {
                        return Observable.just("A");
                    } else {
                        return Observable.just("B");
                    }
                }
            });

        when(packageManager.getPackageInfo(
            eq("com.gocatchapp.goCatch"),
            eq(PackageManager.GET_ACTIVITIES)
        )).thenReturn(new PackageInfo());

        final TripSegment segment = mock(TripSegment.class);
        when(segment.getFrom()).thenReturn(new Location(1.0, 2.0));
        when(segment.getTo()).thenReturn(new Location(3.0, 4.0));
        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("gocatch")
                .segment(segment)
                .build()
        ).test();

        final BookingAction action = BookingAction.builder()
            .bookingProvider(GOCATCH)
            .hasApp(true)
            .data(new Intent(Intent.ACTION_VIEW, Uri.parse("gocatch://referral?code=tripgo&destination=B&pickup=&lat=1.0&lng=2.0")))
            .build();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        final List<BookingAction> events = subscriber.values();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
            .usingComparator(BOOKING_ACTION_COMPARATOR)
            .isEqualTo(action);
    }

    @Test
    public void hasIngogoApp() throws PackageManager.NameNotFoundException {
        when(packageManager.getPackageInfo(
            eq("com.ingogo.passenger"),
            eq(PackageManager.GET_ACTIVITIES)
        )).thenReturn(new PackageInfo());

        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("ingogo")
                .segment(mock(TripSegment.class))
                .build()
        ).test();

        final BookingAction action = BookingAction.builder()
            .bookingProvider(INGOGO)
            .hasApp(true)
            .data(new Intent(Intent.ACTION_VIEW, Uri.parse("ingogo://")))
            .build();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        final List<BookingAction> events = subscriber.values();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
            .usingComparator(BOOKING_ACTION_COMPARATOR)
            .isEqualTo(action);
    }

    @Test
    public void hasNoIngogoApp() throws PackageManager.NameNotFoundException {
        when(packageManager.getPackageInfo(
            eq("com.ingogo.passenger"),
            eq(PackageManager.GET_ACTIVITIES)
        )).thenThrow(new PackageManager.NameNotFoundException());

        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("ingogo")
                .segment(mock(TripSegment.class))
                .build()
        ).test();

        final BookingAction action = BookingAction.builder()
            .bookingProvider(INGOGO)
            .hasApp(false)
            .data(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ingogo.passenger")))
            .build();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        final List<BookingAction> events = subscriber.values();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
            .usingComparator(BOOKING_ACTION_COMPARATOR)
            .isEqualTo(action);
    }

    @Test
    public void handleSMS() {
        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("sms:12345")
                .segment(mock(TripSegment.class))
                .build()
        ).test();

        String body = null;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:12345"));
        intent.putExtra("sms_body", body).putExtra(Intent.EXTRA_TEXT, body);

        final BookingAction action = BookingAction.builder()
            .bookingProvider(SMS)
            .hasApp(false)
            .data(intent)
            .build();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        final List<BookingAction> events = subscriber.values();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
            .usingComparator(BOOKING_ACTION_COMPARATOR)
            .isEqualTo(action);
    }

    @Test
    public void handleSMSWithBody() {
        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("sms:12345?Body goes here")
                .segment(mock(TripSegment.class))
                .build()
        ).test();

        String body = "Body goes here";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:12345"));
        intent.putExtra("sms_body", body).putExtra(Intent.EXTRA_TEXT, body);

        final BookingAction action = BookingAction.builder()
            .bookingProvider(SMS)
            .hasApp(false)
            .data(intent)
            .build();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        final List<BookingAction> events = subscriber.values();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
            .usingComparator(BOOKING_ACTION_COMPARATOR)
            .isEqualTo(action);
    }

    @Test
    public void handleTel() {
        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("tel:12345")
                .segment(mock(TripSegment.class))
                .build()
        ).test();

        final BookingAction action = BookingAction.builder()
            .bookingProvider(OTHERS)
            .hasApp(false)
            .data(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:12345")))
            .build();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        final List<BookingAction> events = subscriber.values();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
            .usingComparator(BOOKING_ACTION_COMPARATOR)
            .isEqualTo(action);
    }

    @Test
    public void handleTelWithName() {
        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("tel:12345?name=user")
                .segment(mock(TripSegment.class))
                .build()
        ).test();

        final BookingAction action = BookingAction.builder()
            .bookingProvider(OTHERS)
            .hasApp(false)
            .data(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:12345")))
            .build();
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        final List<BookingAction> events = subscriber.values();
        assertThat(events).hasSize(1);
        assertThat(events.get(0))
            .usingComparator(BOOKING_ACTION_COMPARATOR)
            .isEqualTo(action);
    }

    @Test
    public void strangeExternalAction() {
        final TestObserver<BookingAction> subscriber = bookingResolver.performExternalActionAsync(
            ExternalActionParams.builder()
                .action("Some strange action")
                .segment(mock(TripSegment.class))
                .build()
        ).test();
        subscriber.assertError(UnsupportedOperationException.class);
    }
}