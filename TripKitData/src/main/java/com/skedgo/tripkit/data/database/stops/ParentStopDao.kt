package skedgo.tripgo.data.timetables

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable

@Dao
interface ParentStopDao {

    @Query("SELECT * FROM parent_stops_to_children_stops WHERE parentStopCode == :parentStopCode")
    fun getChildrenStops(parentStopCode: String): Observable<List<ParentStopEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entities: List<ParentStopEntity>)

}