package com.skedgo.tripkit.data.database.booking.ticket

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface TicketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: TicketEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTickets(tickets: List<TicketEntity>)

    @Query("SELECT * FROM tickets WHERE id = :id")
    suspend fun getTicketById(id: String): TicketEntity?

    //Included RxJava version for repository/service that still using RxJava
    @Query("SELECT * FROM tickets")
    suspend fun getAllTickets(): List<TicketEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTicketRx(ticket: TicketEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTicketsRx(tickets: List<TicketEntity>): Completable

    @Query("SELECT * FROM tickets WHERE id = :id")
    fun getTicketByIdRx(id: String): Maybe<TicketEntity>

    @Query("SELECT * FROM tickets")
    fun getAllTicketsRx(): Single<List<TicketEntity>>

    @Query("SELECT * FROM tickets WHERE userId = :userId")
    fun getTicketsByUserIdRx(userId: String?): Maybe<List<TicketEntity>>
}