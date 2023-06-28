package com.skedgo.tripkit.data.database;


import com.skedgo.tripkit.common.model.Location;
import com.skedgo.sqlite.DatabaseField;
import com.skedgo.sqlite.DatabaseTable;

public final class DbTables {
    public static final String TABLE_LOCATIONS = "locations";
    public static final DatabaseTable LOCATIONS = new DatabaseTable(
            TABLE_LOCATIONS,
            new DatabaseField[]{
                    DbFields.ID,
                    DbFields.NAME,
                    DbFields.ADDRESS,
                    DbFields.LAT,
                    DbFields.LON,
                    DbFields.EXACT,
                    DbFields.BEARING,
                    DbFields.FAVOURITE,
                    DbFields.IS_DYNAMIC,
                    DbFields.LOCATION_TYPE,
                    DbFields.SCHEDULED_STOP_CODE,
                    DbFields.SERVICE_STOP_ID,
                    DbFields.TRIP_SEGMENT_ID,
                    DbFields.PHONE_NUMBER,
                    DbFields.URL,
                    DbFields.RATING_COUNT,
                    DbFields.AVERAGE_RATING,
                    DbFields.RATING_IMAGE_URL,
                    DbFields.SOURCE,
                    DbFields.FAVOURITE_SORT_ORDER_POSITION,
                    DbFields.HAS_CAR,
                    DbFields.HAS_MOTORBIKE,
                    DbFields.HAS_BICYCLE,
                    DbFields.HAS_PUB_TRANS,
                    DbFields.HAS_TAXI,
                    DbFields.HAS_SHUTTLE,
                    DbFields.LAST_UPDATE_TIME,
                    DbFields.TIMEZONE,
                    DbFields.POPULARITY,
                    DbFields.LOCATION_CLASS,
                    DbFields.W3W,
                    DbFields.W3W_INFO_URL,
            },
            "CREATE UNIQUE INDEX unique_name_lat_lon ON locations (" +
                    DbFields.NAME + ", " +
                    DbFields.LAT + ", " +
                    DbFields.LON + ", " +
                    DbFields.SCHEDULED_STOP_CODE + ", " +
                    DbFields.SERVICE_STOP_ID + ", " +
                    DbFields.TRIP_SEGMENT_ID + ");",
            "CREATE UNIQUE INDEX unique_name_address_loctype_code ON locations (" +
                    DbFields.NAME + "," +
                    DbFields.ADDRESS + "," +
                    DbFields.LOCATION_TYPE + "," +
                    DbFields.SCHEDULED_STOP_CODE + ");",

            "CREATE INDEX index_locations_name ON locations (" + DbFields.NAME + ");",
            "CREATE INDEX index_locations_address ON locations (" + DbFields.ADDRESS + ");",
            "CREATE INDEX index_locations_stop_code ON locations (" + DbFields.SCHEDULED_STOP_CODE + ");",
            "CREATE INDEX index_locations_service_stop_id ON locations (" + DbFields.SERVICE_STOP_ID + ");",
            "CREATE INDEX index_locations_trip_segment_id ON locations (" + DbFields.TRIP_SEGMENT_ID + ");"
    );

    public static final DatabaseTable SCHEDULED_STOPS = new DatabaseTable(
            "scheduled_stops",
            new DatabaseField[]{
                    DbFields.ID,
                    DbFields.FILTER,
                    DbFields.CELL_CODE,
                    DbFields.STOP_TYPE,
                    DbFields.CODE,
                    DbFields.SHORT_NAME,
                    DbFields.SERVICES,
                    DbFields.PARENT_ID,
                    DbFields.IS_PARENT,
                    DbFields.MODE_INFO
            },
            "CREATE UNIQUE INDEX 'unique_code' ON scheduled_stops(" + DbFields.CODE + ");"
    );

    public static final DatabaseTable SCHEDULED_STOP_DOWNLOAD_HISTORY = new DatabaseTable(
            "scheduled_stops_download_history",
            new DatabaseField[]{
                    DbFields.AUTO_ID,
                    DbFields.CELL_CODE,
                    DbFields.DOWNLOAD_TIME,
                    DbFields.HASH_CODE_2
            },
            "CREATE UNIQUE INDEX 'unique_cell_code' ON scheduled_stops_download_history(" + DbFields.CELL_CODE + ");"
    );

    /**
     * Represents {@link TimetableEntry}
     */
    public static final DatabaseTable SCHEDULED_SERVICES = new DatabaseTable(
            "services",
            new DatabaseField[]{
                    DbFields.ID,

                    // The value of this column is formatted as '[startStopCode]#[endStopCode]'.
                    // [startStopCode] and [endStopCode] are respectively placeholders for
                    // A (stopCode) and B (stopCode) from an A2B timetable.
                    DbFields.PAIR_IDENTIFIER,
                    DbFields.STOP_CODE,
                    DbFields.END_STOP_CODE,
                    DbFields.MODE,
                    DbFields.START_TIME,
                    DbFields.END_TIME,
                    DbFields.JULIAN_DAY,
                    DbFields.FREQUENCY,
                    DbFields.SERVICE_NUMBER,
                    DbFields.SERVICE_NAME,
                    DbFields.SERVICE_TRIP_ID,
                    DbFields.SERVICE_COLOR_RED,
                    DbFields.SERVICE_COLOR_BLUE,
                    DbFields.SERVICE_COLOR_GREEN,
                    DbFields.SERVICE_OPERATOR,
                    DbFields.REAL_TIME_STATUS,
                    DbFields.FAVOURITE, // 'Favourite' in this table means 'added to reminder list', inherited from 'history'
                    DbFields.HAS_ALERTS,
                    DbFields.SEARCH_STRING,
                    DbFields.SERVICE_TIME,
                    DbFields.MODE_INFO,
                    DbFields.SERVICE_DIRECTION,
                    DbFields.WHEELCHAIR_ACCESSIBLE,
                    DbFields.START_STOP_SHORT_NAME,
                    DbFields.START_PLATFORM
            },
            "CREATE UNIQUE INDEX 'unique_service_at_stop_and_time' ON services (" +
                    DbFields.SERVICE_NUMBER + ", " +
                    DbFields.STOP_CODE + ", " +
                    DbFields.START_TIME + ");",

            "CREATE UNIQUE INDEX 'unique_service_and_time' ON services (" +
                    DbFields.SERVICE_TRIP_ID + ", " +
                    DbFields.STOP_CODE + ", " +
                    DbFields.JULIAN_DAY + ");",

            "CREATE INDEX 'index_services_service_trip_id' ON services(" + DbFields.SERVICE_TRIP_ID + ");",

            "CREATE TRIGGER delete_old_locations AFTER DELETE ON services BEGIN " +
                    "DELETE FROM locations " +
                    "WHERE " + DbFields.SCHEDULED_STOP_CODE + " IS NOT NULL AND " +
                    DbFields.SCHEDULED_STOP_CODE + " = old.stop_code AND " +
                    DbFields.LOCATION_TYPE + "=" + Location.TYPE_SERVICE_STOP +
                    ";" +
                    "END;"
    );

    public static final DatabaseTable SERVICE_STOPS = new DatabaseTable(
            "service_stops",
            new DatabaseField[]{
                    DbFields.ID,
                    DbFields.STOP_TYPE,
                    DbFields.SERVICE_SHAPE_ID,
                    DbFields.STOP_CODE,
                    DbFields.DEPARTURE_TIME,
                    DbFields.ARRIVAL_TIME,
                    DbFields.JULIAN_DAY,
                    DbFields.WHEELCHAIR_ACCESSIBLE
            },
            "CREATE UNIQUE INDEX 'unique_stop_per_service_per_day' ON service_stops (" +
                    DbFields.STOP_CODE + ", " +
                    DbFields.DEPARTURE_TIME + ")",
            "CREATE INDEX 'index_service_stops_service_shape_id' ON service_stops(" + DbFields.SERVICE_SHAPE_ID + ");"
    );

    public static final DatabaseTable REAL_TIME_UPDATES = new DatabaseTable(
            "real_time_updates",
            new DatabaseField[]{
                    DbFields.ID,
                    DbFields.LOCATION_ID,
                    DbFields.LABEL,
                    DbFields.LAST_UPDATE_TIME,
                    DbFields.SERVICE_TRIP_ID,
                    DbFields.END_STOP_CODE,
                    DbFields.STOP_CODE,
                    DbFields.ARRIVAL_TIME_AT_START,
                    DbFields.ARRIVAL_TIME_AT_END,
                    DbFields.OCCUPANCY
            },

            "CREATE UNIQUE INDEX 'unique_real_time_updates' ON real_time_updates (" +
                    DbFields.SERVICE_TRIP_ID + ", " + DbFields.STOP_CODE + ")"
    );

    public static final DatabaseTable SERVICE_ALERTS = new DatabaseTable(
            "service_alerts",
            new DatabaseField[]{
                    DbFields.ID,
                    DbFields.SEGMENT_ID,
                    DbFields.SCHEDULED_SERVICE_ID,
                    DbFields.TITLE,
                    DbFields.TEXT,
                    DbFields.SERVICE_TRIP_ID,
                    DbFields.STOP_CODE,
                    DbFields.HASH_CODE
            },

            "CREATE UNIQUE INDEX 'unique_stop_code_service_alerts' ON service_alerts (" +
                    DbFields.STOP_CODE + ", " +
                    DbFields.TEXT + ", " +
                    DbFields.TITLE + ")",

            "CREATE UNIQUE INDEX 'unique_segment_id_service_alerts' ON service_alerts (" +
                    DbFields.SEGMENT_ID + ", " +
                    DbFields.TEXT + ", " +
                    DbFields.TITLE + ")",

            "CREATE UNIQUE INDEX 'unique_scheduled_service_id_service_alerts' ON service_alerts (" +
                    DbFields.SCHEDULED_SERVICE_ID + ", " +
                    DbFields.TEXT + ", " +
                    DbFields.TITLE + ")",

            "CREATE UNIQUE INDEX 'unique_service_trip_id_service_alerts' ON service_alerts (" +
                    DbFields.SERVICE_TRIP_ID + ", " +
                    DbFields.TEXT + ", " +
                    DbFields.TITLE + ")",

            "CREATE TRIGGER insert_has_alerts AFTER INSERT ON service_alerts BEGIN " +
                    "UPDATE services " +
                    "SET " + DbFields.HAS_ALERTS + " = " +
                    "(SELECT COUNT(_id) " +
                    "FROM service_alerts " +
                    "WHERE " + DbFields.SCHEDULED_SERVICE_ID + " = new." + DbFields.SCHEDULED_SERVICE_ID +
                    " GROUP BY " + DbFields.SCHEDULED_SERVICE_ID + ") " +
                    "WHERE _id = new." + DbFields.SCHEDULED_SERVICE_ID + "; " +
                    "END;",

            "CREATE TRIGGER update_has_alerts AFTER UPDATE ON service_alerts BEGIN " +
                    "UPDATE services " +
                    "SET " + DbFields.HAS_ALERTS + " = " +
                    "(SELECT COUNT(_id) " +
                    "FROM service_alerts " +
                    "WHERE " + DbFields.SCHEDULED_SERVICE_ID + " = new." + DbFields.SCHEDULED_SERVICE_ID +
                    " GROUP BY " + DbFields.SCHEDULED_SERVICE_ID + ") " +
                    "WHERE _id = new." + DbFields.SCHEDULED_SERVICE_ID + "; " +
                    "END;",

            "CREATE TRIGGER delete_has_alerts AFTER DELETE ON service_alerts BEGIN " +
                    "UPDATE services " +
                    "SET " + DbFields.HAS_ALERTS + " = " +
                    "(SELECT COUNT(_id) " +
                    "FROM service_alerts " +
                    " WHERE " + DbFields.SCHEDULED_SERVICE_ID + " = old." + DbFields.SCHEDULED_SERVICE_ID +
                    " GROUP BY " + DbFields.SCHEDULED_SERVICE_ID + ") " +
                    "WHERE _id = old." + DbFields.SCHEDULED_SERVICE_ID + "; " +
                    "END;"
    );

    public static final DatabaseTable SEGMENT_SHAPES = new DatabaseTable(
            "segment_shapes",
            new DatabaseField[]{
                    DbFields.ID,
                    DbFields.SEGMENT_ID,
                    DbFields.SERVICE_TRIP_ID,
                    DbFields.TRAVELLED,
                    DbFields.WAYPOINT_ENCODING,
                    DbFields.SERVICE_COLOR,
                    DbFields.HAS_SERVICE_STOPS,
            },

            "CREATE INDEX 'index_segment_shapes_segment_id' ON segment_shapes(" + DbFields.SEGMENT_ID + ");",
            "CREATE INDEX 'index_segment_shapes_service_trip_id' ON segment_shapes(" + DbFields.SERVICE_TRIP_ID + ");"
    );

    public static final DatabaseTable PRIVATE_VEHICLES = new DatabaseTable(
            "private_vehicles",
            new DatabaseField[]{
                    DbFields.AUTO_ID,
                    DbFields.LOCATION_ID,
                    DbFields.MODE,
                    DbFields.NAME
            },

            "CREATE INDEX 'index_private_vehicles_location_id' ON private_vehicles(" + DbFields.LOCATION_ID + ");"
    );

    private DbTables() {
    }
}