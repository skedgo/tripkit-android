package com.skedgo.tripkit.booking.quickbooking

import com.skedgo.tripkit.booking.quickbooking.Ticket.Companion.toEntity
import com.skedgo.tripkit.booking.quickbooking.Ticket.Companion.toTicket
import com.skedgo.tripkit.data.database.TripKitDatabase
import com.skedgo.tripkit.utils.async.Result
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class QuickBookingRepository @Inject constructor(
    val quickBookingService: QuickBookingService,
    tripKitDatabase: TripKitDatabase
) {
    private val ticketDao = tripKitDatabase.ticketDao()
    suspend fun getTickets(): Flow<Result<List<Ticket>>> = flow {
        emit(Result.loading())

        try {
            val ticketsFromApi = quickBookingService.getTicketsAsync(true)
            val ticketEntities = ticketsFromApi.map { ticket ->
                ticket.toEntity()
            }
            ticketDao.insertTickets(ticketEntities)
            emit(Result.success(ticketsFromApi))
        } catch (e: Exception) {
            // If the API call fails, fetch tickets from the local database
            val tickets = ticketDao.getAllTickets().map { entity -> entity.toTicket() }
            if (tickets.isNotEmpty()) {
                emit(Result.success(tickets))
            } else {
                throw e
            }
        }
    }.onStart {
        // Emit loading state at the start
        emit(Result.loading())
    }.catch { e ->
        e.printStackTrace()
        // Emit error state in case of exceptions
        emit(Result.error(e.message ?: "Unknown error"))
    }.flowOn(Dispatchers.IO)

    fun getTicketsRx(): Single<List<Ticket>> {
        return quickBookingService.getTickets(true)
            .flatMap { tickets ->
                if (tickets.isNotEmpty()) {
                    // insertTicketsRx() saves all tickets and returns Completable
                    ticketDao.insertTicketsRx(tickets.map { it.toEntity() })
                        .andThen(Single.just(tickets))
                } else {
                    Single.just(tickets)
                }
            }
    }
}