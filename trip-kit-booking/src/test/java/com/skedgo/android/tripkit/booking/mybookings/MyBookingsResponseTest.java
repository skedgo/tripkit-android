package com.skedgo.android.tripkit.booking.mybookings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.skedgo.android.common.model.BookingConfirmation;
import com.skedgo.android.common.model.BookingConfirmationAction;
import com.skedgo.android.common.model.BookingConfirmationPurchase;
import com.skedgo.android.common.model.BookingProvider;
import com.skedgo.android.common.model.BookingSource;
import com.skedgo.android.common.model.PurchaseBrand;
import com.skedgo.android.tripkit.booking.TestRunner;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class MyBookingsResponseTest {
  private Gson gson;

  @Before public void before() {
    GsonBuilder builder = new GsonBuilder();
    gson = builder.create();
  }

  @Test(expected = NullPointerException.class)
  public void bookingsShouldBeNonNull() {
    ImmutableMyBookingsResponse.builder()
        .addBookings((MyBookingsConfirmationResponse) null)
        .build();
  }

  @Test public void bookingConfirmationShouldBeParsedCorrectly() throws IOException {

    String authBookingFormJson = IOUtils.toString(getClass().
        getResourceAsStream("/booking-response.json"), Charset.defaultCharset());

    JsonReader reader = new JsonReader(new StringReader(authBookingFormJson));
    reader.setLenient(true);
    MyBookingsConfirmationResponse bookingsConfirmationResponse = gson.fromJson(reader, MyBookingsConfirmationResponse.class);

    assertThat(bookingsConfirmationResponse.mode()).isEqualTo("pt_pub");
    assertThat(bookingsConfirmationResponse.time()).isEqualTo(1486140077);
    assertThat(bookingsConfirmationResponse.index()).isEqualTo(1);
    assertThat(bookingsConfirmationResponse.trips()).isNotNull();
    assertThat(bookingsConfirmationResponse.trips()).containsExactly("https://beta.tripgo.com/trip/1bcaa750-9ebf-4213-bcf3-91a26264b432");

    BookingConfirmation confirmation = bookingsConfirmationResponse.confirmation();

    assertThat(confirmation).isNotNull();
    assertThat(confirmation.provider()).isNull();
    assertThat(confirmation.status()).isNotNull();

    List<BookingConfirmationAction> actions = confirmation.actions();

    assertThat(actions).isNotNull();
    assertThat(actions.size()).isEqualTo(1);

    BookingConfirmationPurchase purchase = confirmation.purchase();

    assertThat(purchase).isNotNull();
    assertThat(purchase.price()).isEqualTo(5.0f);
    assertThat(purchase.currency()).isEqualTo("EUR");
    assertThat(purchase.productName()).isEqualTo("Weekend ticket");
    assertThat(purchase.productType()).isEqualTo("pt_pub");
    assertThat(purchase.id()).isEqualTo("5894b2ab87d4abdc63f75e8f");
    assertThat(purchase.validFrom()).isEqualTo(1486140073);
    assertThat(purchase.validFor()).isEqualTo(259200);
    assertThat(purchase.timezone()).isEqualTo("Europe/Helsinki");

    PurchaseBrand brand = purchase.brand();

    assertThat(brand).isNotNull();
    assertThat(brand.color()).isNotNull();
    assertThat(brand.name()).isEqualTo("TurkuDev");
    assertThat(brand.remoteIcon()).isEqualTo("foli");

    BookingSource source = purchase.source();

    assertThat(source).isNotNull();

    BookingProvider provider = source.provider();

    assertThat(provider).isNotNull();
    assertThat(provider.color()).isNotNull();
    assertThat(provider.name()).isEqualTo("PayiQ");
    assertThat(provider.phone()).isEqualTo("+358 2 480 842 67");
    assertThat(provider.remoteDarkIcon()).isEqualTo("payiq-logo-white");
    assertThat(provider.remoteIcon()).isEqualTo("payiq-logo-small");
    assertThat(provider.website()).isEqualTo("https://payiq.net/");

  }

}
