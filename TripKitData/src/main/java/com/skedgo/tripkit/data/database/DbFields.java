package com.skedgo.tripkit.data.database;

import com.skedgo.sqlite.DatabaseField;
import com.skedgo.tripkit.common.model.Location;

public final class DbFields {
    public static final DatabaseField ID = new DatabaseField("_id", "integer", "primary key");

    /**
     * @see <a href="http://www.tutorialspoint.com/sqlite/sqlite_using_autoincrement.htm">What is it?</a>
     */
    public static final DatabaseField AUTO_ID = new DatabaseField("_id", "INTEGER", "PRIMARY KEY AUTOINCREMENT");
    public static final DatabaseField NAME = new DatabaseField("name", "text", "collate nocase");
    public static final DatabaseField ADDRESS = new DatabaseField("address", "text", "collate nocase");

    public static final DatabaseField PHONE_NUMBER = new DatabaseField("phone", "text");
    public static final DatabaseField URL = new DatabaseField("url", "text");
    public static final DatabaseField RATING_COUNT = new DatabaseField("rating_count", "integer");
    public static final DatabaseField AVERAGE_RATING = new DatabaseField("average_rating", "real");
    public static final DatabaseField RATING_IMAGE_URL = new DatabaseField("rating_image_url", "text");
    public static final DatabaseField SOURCE = new DatabaseField("source", "text");

    public static final DatabaseField LAT = new DatabaseField("lat", "real");
    public static final DatabaseField LON = new DatabaseField("lon", "real");
    public static final DatabaseField EXACT = new DatabaseField("exact", "integer");
    public static final DatabaseField BEARING = new DatabaseField("bearing", "integer");
    public static final DatabaseField FAVOURITE = new DatabaseField("favourite", "integer");
    public static final DatabaseField FAVOURITE_SORT_ORDER_POSITION = new DatabaseField("favourite_sort_order_position", "integer");
    public static final DatabaseField IS_DYNAMIC = new DatabaseField("is_dynamic", "integer");
    public static final DatabaseField LOCATION_TYPE = new DatabaseField("location_type", "integer", "default " + Location.TYPE_UNKNOWN);
    public static final DatabaseField SCHEDULED_STOP_CODE = new DatabaseField("scheduled_stop_code", "text");
    public static final DatabaseField TRIP_SEGMENT_ID = new DatabaseField("trip_segment_id", "integer", "default -1");
    public static final DatabaseField SERVICE_STOP_ID = new DatabaseField("service_stop_id", "integer", "default -1");
    public static final DatabaseField STOP_TYPE = new DatabaseField("stop_type", "text");
    public static final DatabaseField MODE_INFO = new DatabaseField("mode_info", "text");
    public static final DatabaseField CELL_CODE = new DatabaseField("cell_code", "text"); // Aka cell ids.
    public static final DatabaseField DOWNLOAD_TIME = new DatabaseField("download_time", "integer");
    public static final DatabaseField CODE = new DatabaseField("code", "text");
    public static final DatabaseField PARENT_ID = new DatabaseField("parent_id", "text");
    public static final DatabaseField IS_PARENT = new DatabaseField("is_parent", "integer");
    public static final DatabaseField SHORT_NAME = new DatabaseField("short_name", "text");
    public static final DatabaseField SERVICES = new DatabaseField("services", "text");
    public static final DatabaseField SERVICE_SHAPE_ID = new DatabaseField("service_shape_id", "integer", "default -1");
    public static final DatabaseField FREQUENCY = new DatabaseField("frequency", "integer");
    public static final DatabaseField DISPLAY_TRIP_ID = new DatabaseField("display_trip_id", "integer");
    public static final DatabaseField HAS_PUB_TRANS = new DatabaseField("has_pub_trans", "integer");
    public static final DatabaseField HAS_CAR = new DatabaseField("has_car", "integer");
    public static final DatabaseField HAS_TAXI = new DatabaseField("has_taxi", "integer");
    public static final DatabaseField HAS_SHUTTLE = new DatabaseField("has_shuttle", "integer");
    public static final DatabaseField HAS_BICYCLE = new DatabaseField("has_bicycle", "integer");
    public static final DatabaseField HAS_MOTORBIKE = new DatabaseField("has_motorbike", "integer");
    public static final DatabaseField START_TIME = new DatabaseField("start_time", "integer");
    public static final DatabaseField SERVICE_TIME = new DatabaseField("service_time", "integer");
    public static final DatabaseField END_TIME = new DatabaseField("end_time", "integer");
    public static final DatabaseField MODE = new DatabaseField("mode", "text");
    public static final DatabaseField SERVICE_NAME = new DatabaseField("service_name", "text");
    public static final DatabaseField SERVICE_COLOR = new DatabaseField("service_color", "integer");
    public static final DatabaseField SERVICE_NUMBER = new DatabaseField("service_number", "text");
    public static final DatabaseField SERVICE_OPERATOR = new DatabaseField("service_operator", "text");
    public static final DatabaseField STOP_CODE = new DatabaseField("stop_code", "text");
    public static final DatabaseField HASH_CODE = new DatabaseField("hash_code", "text");
    public static final DatabaseField END_STOP_CODE = new DatabaseField("end_stop_code", "text");
    public static final DatabaseField REAL_TIME_STATUS = new DatabaseField("real_time_status", "text");
    public static final DatabaseField FILTER = new DatabaseField("filter", "text");
    public static final DatabaseField DEPARTURE_TIME = new DatabaseField("departure_time", "integer");
    public static final DatabaseField ARRIVAL_TIME = new DatabaseField("arrival_time", "integer");

    /**
     * This is used to be use to query for services that are in a particular day. Use {@link #START_TIME} instead.
     */
    @Deprecated
    public static final DatabaseField JULIAN_DAY = new DatabaseField("julian_day", "integer");
    public static final DatabaseField SERVICE_TRIP_ID = new DatabaseField("service_trip_id", "text");
    public static final DatabaseField SERVICE_COLOR_RED = new DatabaseField("color_red", "text");
    public static final DatabaseField SERVICE_COLOR_BLUE = new DatabaseField("color_blue", "text");
    public static final DatabaseField SERVICE_COLOR_GREEN = new DatabaseField("color_green", "text");
    public static final DatabaseField WAYPOINT_ENCODING = new DatabaseField("waypoint_encoding", "text");
    public static final DatabaseField TRAVELLED = new DatabaseField("travelled", "integer");
    public static final DatabaseField SEGMENT_ID = new DatabaseField("segment_id", "integer", "default -1");
    public static final DatabaseField SCHEDULED_SERVICE_ID = new DatabaseField("service_id", "integer", "default -1");
    public static final DatabaseField HAS_ALERTS = new DatabaseField("has_alerts", "integer");
    public static final DatabaseField HAS_SERVICE_STOPS = new DatabaseField("has_service_stops", "integer");
    public static final DatabaseField HAS_MULTIPLE_TRIPS = new DatabaseField("has_multiple_trips", "integer");
    public static final DatabaseField SEARCH_STRING = new DatabaseField("search_string", "text", "collate nocase");
    public static final DatabaseField LOCATION_ID = new DatabaseField("location_id", "integer");
    public static final DatabaseField LABEL = new DatabaseField("label", "text");
    public static final DatabaseField LAST_UPDATE_TIME = new DatabaseField("last_update_time", "integer");
    public static final DatabaseField ARRIVAL_TIME_AT_START = new DatabaseField("arrival_time_at_start", "integer");
    public static final DatabaseField ARRIVAL_TIME_AT_END = new DatabaseField("arrival_time_at_end", "integer");
    public static final DatabaseField TITLE = new DatabaseField("title", "text");
    public static final DatabaseField TEXT = new DatabaseField("text", "text");
    public static final DatabaseField PAIR_IDENTIFIER = new DatabaseField("pair_identifier", "text");
    public static final DatabaseField HASH_CODE_2 = new DatabaseField("hash_code", "INTEGER", "DEFAULT 0");
    public static final DatabaseField TIMEZONE = new DatabaseField("timezone", "text");
    public static final DatabaseField POPULARITY = new DatabaseField("popularity", "integer");
    public static final DatabaseField LOCATION_CLASS = new DatabaseField("location_class", "text");
    public static final DatabaseField W3W = new DatabaseField("w3w", "text");
    public static final DatabaseField W3W_INFO_URL = new DatabaseField("w3w_info_url", "text");
    public static final DatabaseField SERVICE_DIRECTION = new DatabaseField("service_direction", "text");
    public static final DatabaseField WHEELCHAIR_ACCESSIBLE = new DatabaseField("wheelchair_accessible", "integer");
    public static final DatabaseField BICYCLE_ACCESSIBLE = new DatabaseField("bicycle_accessible", "integer");
    public static final DatabaseField OCCUPANCY = new DatabaseField("occupancy", "text");
    public static final DatabaseField START_STOP_SHORT_NAME = new DatabaseField("start_stop_short_name", "text");
    public static final DatabaseField START_PLATFORM = new DatabaseField("start_platform", "text");

    private DbFields() {
    }
}
