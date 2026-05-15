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
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger
import javax.inject.Inject

class QuickBookingRepository @Inject constructor(
    val quickBookingService: QuickBookingService,
    tripKitDatabase: TripKitDatabase
) {
    companion object {
        private val logger = Logger.getLogger(QuickBookingRepository::class.java.simpleName)
    }

    private val ticketDao = tripKitDatabase.ticketDao()
    private val inFlightTicketsLock = Any()
    @Volatile
    private var inFlightTicketsRequest: Single<List<Ticket>>? = null
    private val ticketRequestCounter = AtomicLong(0)

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

    fun getTicketsRx(userId: String?): Single<List<Ticket>> {
        val caller = resolveTicketsCaller()
        synchronized(inFlightTicketsLock) {
            inFlightTicketsRequest?.let { inFlight ->
                logger.info("getTicketsRx reuse in-flight caller=$caller userId=$userId")
                return inFlight
            }
        }

        val requestId = ticketRequestCounter.incrementAndGet()
        lateinit var sharedRequest: Single<List<Ticket>>
        sharedRequest = quickBookingService.getTickets(true)
            .doOnSubscribe {
                logger.info("getTicketsRx start requestId=$requestId caller=$caller userId=$userId")
            }
            .flatMap { tickets ->
                if (tickets.isNotEmpty()) {
                    // delete first existing tickets
                    ticketDao.deleteUserTicketsRx(userId).andThen(
                        // insertTicketsRx() saves all tickets and returns Completable
                        ticketDao.insertTicketsRx(tickets.map { it.toEntity(userId) })
                            .andThen(Single.just(tickets))
                    )
                } else {
                    ticketDao.getTicketsByUserIdRx(userId)
                        .toSingle()
                        .flatMap { Single.just(it.map { it.toTicket() }) }
                }
            }
            .onErrorResumeNext { e: Throwable ->
                if (e is UnknownHostException || e is IOException || e is SocketTimeoutException) {
                    // Fallback to local data when network call fails
                    ticketDao.getTicketsByUserIdRx(userId)
                        .toSingle()
                        .flatMap { storedTickets -> Single.just(storedTickets.map { it.toTicket() }) }
                } else {
                    Single.error(e)
                }
            }
            .doOnSuccess { tickets ->
                logger.info("getTicketsRx success requestId=$requestId tickets=${tickets.size}")
            }
            .doOnError { e ->
                logger.warning(
                    "getTicketsRx error requestId=$requestId caller=$caller error=${e.message}"
                )
            }
            .doFinally {
                synchronized(inFlightTicketsLock) {
                    if (inFlightTicketsRequest === sharedRequest) {
                        inFlightTicketsRequest = null
                        logger.info("getTicketsRx clear in-flight requestId=$requestId")
                    }
                }
            }
            .cache()

        synchronized(inFlightTicketsLock) {
            inFlightTicketsRequest = sharedRequest
        }
        return sharedRequest
    }

    private fun resolveTicketsCaller(): String {
        val repositoryClassName = QuickBookingRepository::class.java.name
        return Throwable().stackTrace.firstOrNull { element ->
            element.className != repositoryClassName && !element.className.startsWith("io.reactivex")
        }?.let { "${it.className}.${it.methodName}" } ?: "unknown"
    }
}