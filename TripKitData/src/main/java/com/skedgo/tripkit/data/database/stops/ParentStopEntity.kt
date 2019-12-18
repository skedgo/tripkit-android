package skedgo.tripgo.data.timetables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parent_stops_to_children_stops",
    primaryKeys = ["parentStopCode", "childrenStopCode"])
class ParentStopEntity(
    val parentStopCode: String,
    val childrenStopCode: String
)