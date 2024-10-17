package com.skedgo.tripkit;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.skedgo.tripkit.common.model.region.Region;
import com.skedgo.tripkit.common.model.region.RegionsResponse;
import com.skedgo.tripkit.common.model.TransportMode;

import java.util.ArrayList;
import java.util.Collection;

import androidx.annotation.NonNull;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


final class OnSubscribeSaveRegionsResponse implements ObservableOnSubscribe<Void> {
    private final Gson gson = new Gson();
    private final SQLiteDatabase database;
    private final RegionsResponse response;

    OnSubscribeSaveRegionsResponse(
        @NonNull SQLiteDatabase database,
        @NonNull RegionsResponse response) {
        this.database = database;
        this.response = response;
    }

    @Override
    public void subscribe(ObservableEmitter<Void> emitter) throws Exception {
        try {
            database.beginTransaction();

            database.delete(Tables.REGIONS.getName(), null, null);
            database.delete(Tables.TRANSPORT_MODES.getName(), null, null);

            final ArrayList<Region> regions = response.regions;
            if (regions != null) {
                for (Region region : regions) {
                    database.insert(
                        Tables.REGIONS.getName(),
                        null,
                        toRegionValues(region)
                    );
                }
            }

            final Collection<TransportMode> modes = response.getTransportModes();
            if (modes != null) {
                for (TransportMode mode : modes) {
                    database.insert(
                        Tables.TRANSPORT_MODES.getName(),
                        null,
                        toTransportModeValues(mode)
                    );
                }
            }

            database.setTransactionSuccessful();
            emitter.onComplete();
        } catch (Exception e) {
            emitter.onError(e);
        } finally {
            database.endTransaction();
        }
    }

    ContentValues toRegionValues(Region region) {
        final ContentValues values = new ContentValues(1);
        values.put(Tables.FIELD_JSON.name, gson.toJson(region));
        return values;
    }

    ContentValues toTransportModeValues(TransportMode mode) {
        final ContentValues values = new ContentValues(1);
        values.put(Tables.FIELD_JSON.name, gson.toJson(mode));
        return values;
    }

}