package com.skedgo.tripkit.routing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static com.skedgo.tripkit.routing.VehicleMode.AEROPLANE;
import static com.skedgo.tripkit.routing.VehicleMode.BICYCLE;
import static com.skedgo.tripkit.routing.VehicleMode.BICYCLE_SHARE;
import static com.skedgo.tripkit.routing.VehicleMode.BUS;
import static com.skedgo.tripkit.routing.VehicleMode.CABLECAR;
import static com.skedgo.tripkit.routing.VehicleMode.CAR;
import static com.skedgo.tripkit.routing.VehicleMode.CAR_POOL;
import static com.skedgo.tripkit.routing.VehicleMode.CAR_RIDE_SHARE;
import static com.skedgo.tripkit.routing.VehicleMode.CAR_SHARE;
import static com.skedgo.tripkit.routing.VehicleMode.COACH;
import static com.skedgo.tripkit.routing.VehicleMode.FERRY;
import static com.skedgo.tripkit.routing.VehicleMode.MONORAIL;
import static com.skedgo.tripkit.routing.VehicleMode.MOTORBIKE;
import static com.skedgo.tripkit.routing.VehicleMode.PARKING;
import static com.skedgo.tripkit.routing.VehicleMode.PUBLIC_TRANSPORT;
import static com.skedgo.tripkit.routing.VehicleMode.SHUTTLE_BUS;
import static com.skedgo.tripkit.routing.VehicleMode.SUBWAY;
import static com.skedgo.tripkit.routing.VehicleMode.TAXI;
import static com.skedgo.tripkit.routing.VehicleMode.TRAIN;
import static com.skedgo.tripkit.routing.VehicleMode.TRAIN_INTERCITY;
import static com.skedgo.tripkit.routing.VehicleMode.TRAM;
import static com.skedgo.tripkit.routing.VehicleMode.WALK;
import static com.skedgo.tripkit.routing.VehicleMode.from;

@RunWith(RobolectricTestRunner.class)
public class VehicleModeTest {
    @Test
    public void defineLocalTransportIcons() {
        assertThat(from("aeroplane")).isEqualTo(AEROPLANE);
        assertThat(from("bicycle-share")).isEqualTo(BICYCLE_SHARE);
        assertThat(from("bicycle")).isEqualTo(BICYCLE);
        assertThat(from("bus")).isEqualTo(BUS);
        assertThat(from("cablecar")).isEqualTo(CABLECAR);
        assertThat(from("car-pool")).isEqualTo(CAR_POOL);
        assertThat(from("car-ride-share")).isEqualTo(CAR_RIDE_SHARE);
        assertThat(from("car-share")).isEqualTo(CAR_SHARE);
        assertThat(from("car")).isEqualTo(CAR);
        assertThat(from("coach")).isEqualTo(COACH);
        assertThat(from("ferry")).isEqualTo(FERRY);
        assertThat(from("monorail")).isEqualTo(MONORAIL);
        assertThat(from("motorbike")).isEqualTo(MOTORBIKE);
        assertThat(from("parking")).isEqualTo(PARKING);
        assertThat(from("public-transport")).isEqualTo(PUBLIC_TRANSPORT);
        assertThat(from("shuttlebus")).isEqualTo(SHUTTLE_BUS);
        assertThat(from("subway")).isEqualTo(SUBWAY);
        assertThat(from("taxi")).isEqualTo(TAXI);
        assertThat(from("train-intercity")).isEqualTo(TRAIN_INTERCITY);
        assertThat(from("train")).isEqualTo(TRAIN);
        assertThat(from("tram")).isEqualTo(TRAM);
        assertThat(from("walk")).isEqualTo(WALK);
        assertThat(from("I'm not an enum")).isNull();
    }
}