package skedgo.tripgo.data.timetables

import androidx.room.Entity

@Entity(
    tableName = "parent_stops_to_children_stops",
    primaryKeys = ["parentStopCode", "childrenStopCode"]
)
class ParentStopEntity(
    val parentStopCode: String,
    val childrenStopCode: String
)