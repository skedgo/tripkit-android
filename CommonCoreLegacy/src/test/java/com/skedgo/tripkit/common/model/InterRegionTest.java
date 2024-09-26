package com.skedgo.tripkit.common.model;

import com.skedgo.tripkit.common.model.region.Region;
import com.skedgo.tripkit.common.model.region.Regions;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @see <a href="https://redmine.buzzhives.com/projects/buzzhives/wiki/Inter-city_routing">Inter-city routing</a>
 */
@RunWith(AndroidJUnit4.class)
public class InterRegionTest {
    @Test
    public void shouldUnionModesFromDepartureRegionAndArrivalRegion() {
        final Region departureRegion = new Region();
        departureRegion.setName("DepartureRegion");
        departureRegion.setTransportModeIds(new ArrayList<>(Arrays.asList("a", "b", "c")));

        final Region arrivalRegion = new Region();
        arrivalRegion.setName("ArrivalRegion");
        arrivalRegion.setTransportModeIds(new ArrayList<>(Arrays.asList("a", "f", "b", "c")));

        final Regions.InterRegion interRegion = new Regions.InterRegion(departureRegion, arrivalRegion);
        assertThat(interRegion.getTransportModeIds())
            .containsExactly(TransportMode.ID_AIR, "a", "b", "c", "f");
        assertThat(interRegion.getName())
            .isEqualTo(departureRegion.getName() + "_" + arrivalRegion.getName());
    }

    @Test
    public void shouldUnionModesFromArrivalRegion() {
        final Region departureRegion = new Region();
        departureRegion.setName("DepartureRegion");
        departureRegion.setTransportModeIds(null);

        final Region arrivalRegion = new Region();
        arrivalRegion.setName("ArrivalRegion");
        arrivalRegion.setTransportModeIds(new ArrayList<>(Arrays.asList("d", "f", "e")));

        final Regions.InterRegion interRegion = new Regions.InterRegion(departureRegion, arrivalRegion);
        assertThat(interRegion.getTransportModeIds())
            .containsExactly(TransportMode.ID_AIR, "d", "f", "e");
        assertThat(interRegion.getName())
            .isEqualTo(departureRegion.getName() + "_" + arrivalRegion.getName());
    }

    @Test
    public void shouldUnionModesFromDepartureRegion() {
        final Region departureRegion = new Region();
        departureRegion.setName("DepartureRegion");
        departureRegion.setTransportModeIds(new ArrayList<>(Arrays.asList("a", "c", "b")));

        final Region arrivalRegion = new Region();
        arrivalRegion.setName("ArrivalRegion");
        arrivalRegion.setTransportModeIds(null);

        final Regions.InterRegion interRegion = new Regions.InterRegion(departureRegion, arrivalRegion);
        assertThat(interRegion.getTransportModeIds())
            .containsExactly(TransportMode.ID_AIR, "a", "c", "b");
        assertThat(interRegion.getName())
            .isEqualTo(departureRegion.getName() + "_" + arrivalRegion.getName());
    }

    @Test
    public void shouldSelectServerUrlsFromDepartureRegion() {
        final Region departureRegion = new Region();
        final ArrayList<String> departureServerUrls = new ArrayList<>(Arrays.asList(
            "https://sydney-au-nsw-sydney.tripgo.skedgo.com/satapp",
            "https://inflationary-au-nsw-sydney.tripgo.skedgo.com/satapp"
        ));
        departureRegion.setURLs(departureServerUrls);

        final Region arrivalRegion = new Region();
        arrivalRegion.setURLs(new ArrayList<>(Arrays.asList(
            "https://baryogenesis-fr-b-bordeaux.tripgo.skedgo.com/satapp",
            "https://inflationary-fr-b-bordeaux.tripgo.skedgo.com/satapp"
        )));

        final Regions.InterRegion interRegion = new Regions.InterRegion(departureRegion, arrivalRegion);
        assertThat(interRegion.getURLs())
            .containsExactlyElementsOf(departureServerUrls);
    }

    @Test
    public void shouldSelectTimezoneFromDepartureRegion() {
        final Region departureRegion = new Region();
        departureRegion.setTimezone("Europe/Paris");

        final Region arrivalRegion = new Region();
        arrivalRegion.setTimezone("Australia/Sydney");

        final Regions.InterRegion interRegion = new Regions.InterRegion(departureRegion, arrivalRegion);
        assertThat(interRegion.getTimezone())
            .isEqualTo(departureRegion.getTimezone());
    }

    @Test
    public void shouldSelectPolygonFromDepartureRegion() {
        final Region departureRegion = new Region();
        departureRegion.setEncodedPolyline("udbmG`{tGq`cH??s`aJp`cH?");

        final Region arrivalRegion = new Region();
        arrivalRegion.setEncodedPolyline("nwcvE_fno[owyR??mcjRnwyR?");

        final Regions.InterRegion interRegion = new Regions.InterRegion(departureRegion, arrivalRegion);
        assertThat(interRegion.getEncodedPolyline())
            .isEqualTo(departureRegion.getEncodedPolyline());
    }
}