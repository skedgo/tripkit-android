package skedgo.tripkit.ui.timetable.services

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import skedgo.tripkit.realtime.RealTimeChoreographer
import com.jakewharton.rxrelay.BehaviorRelay
import com.jakewharton.rxrelay.PublishRelay
import com.skedgo.android.common.model.*
import com.skedgo.android.tripkit.RegionService
import me.tatarka.bindingcollectionadapter2.BR
import me.tatarka.bindingcollectionadapter2.ItemBinding
import org.joda.time.DateTimeZone
import rx.Emitter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import skedgo.rxproperty.asObservable
import skedgo.tripkit.realtime.RealtimeAlertRepository
import skedgo.tripkit.time.GetNow
import skedgo.tripkit.timetable.FetchTimetable
import skedgo.tripkit.routing.toSeconds
import skedgo.tripkit.ui.R
import skedgo.tripkit.ui.core.ReactiveViewModel
import skedgo.tripkit.ui.core.isExecuting
import skedgo.tripkit.ui.timetable.services.CreateShareUrl
import skedgo.tripkit.ui.timetable.services.ServiceViewModel
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Provider

class TimetableViewModel @Inject constructor(
    private val context: Context,
    private val fetchTimetable: FetchTimetable,
    private val realTimeChoreographer: RealTimeChoreographer,
    private val serviceViewModelProvider: Provider<ServiceViewModel>,
  //  private val regionService: RegionService,
    private val createShareUrl: CreateShareUrl,
    private val realtimeAlertRepository: RealtimeAlertRepository,
    private val getNow: GetNow
) : ReactiveViewModel() {

  val itemBinding: ItemBinding<ServiceViewModel> = ItemBinding.of<ServiceViewModel>(BR.viewModel, R.layout.timetable_entry2)

  val showLoading = ObservableBoolean(false)
  val services: ObservableField<List<ServiceViewModel>> = ObservableField(emptyList())

  val onAlertClicks: Observable<ArrayList<RealtimeAlert>> = services
      .asObservable()
      .switchMap {
        it.map { it.onAlertsClick.observable }
            .let {
              Observable.merge(it)
            }
      }

  val stop: BehaviorRelay<ScheduledStop> = BehaviorRelay.create<ScheduledStop>()

  val onServiceClick: Observable<Pair<ScheduledStop, TimetableEntry>> = services
      .asObservable()
      .switchMap {
        it.map { it.onItemClick.observable }
            .let {
              Observable.merge(it)
            }
      }.withLatestFrom(stop) { timetable, stop -> stop to timetable }

  private val downloadTimetable: PublishRelay<Long> = PublishRelay.create<Long>()
  val onDateChanged: PublishRelay<Long> = PublishRelay.create<Long>()
//  val resultsUpdated: PublishRelay<Int> = PublishRelay.create<Int>()

  val filter: BehaviorRelay<String> = BehaviorRelay.create<String>("")

  private val loadMore = PublishRelay.create<Unit>()

  val updateRealTime = PublishRelay.create<Pair<List<ServiceViewModel>, Region>>()

  init {
    val regionObservable = stop.flatMap {
     // regionService.getRegionByLocationAsync(it)
      Observable.just(Region())
    }

    Observable.combineLatest(onDateChanged.mergeWith(downloadTimetable), regionObservable, stop)
    { sinceTimeInMillis, region, stop -> Triple(sinceTimeInMillis, region, stop) }
        .switchMap { (sinceTimeInMillis, region, stop) ->
          Observable
              .create<List<TimetableEntry>>({ emitter ->
                val timeInMillis = AtomicLong(sinceTimeInMillis)
                val subscription = loadMore
                    .startWith(Unit)
                    .switchMap {
                      fetchTimetable.execute(stop.getEmbarkationStopCode()!!, region, timeInMillis.get())
                          .isExecuting { showLoading.set(it) }
                    }
                    .subscribe {
                      emitter.onNext(it)
                      timeInMillis.set((it.last().startTimeInSecs + 1) * 1000)
                    }
                emitter.setCancellation { subscription.unsubscribe() }
              }, Emitter.BackpressureMode.BUFFER)
              .startWith(emptyList<TimetableEntry>())
              .scan { a, b -> a + b }
              .map {
                it.map {
                  serviceViewModelProvider.get().apply {
                    this.setService(it,
                        DateTimeZone.forID(region.timezone))
                  }
                }
              }
              .map { it.sortedBy { it.getRealTimeDeparture() } }
              .let {
                Observable.combineLatest(it, filter.asObservable())
                { services, filter ->
                  services.filter { it.service.serviceNumber.contains(filter, ignoreCase = true) }
                }
              }
              .map { it to region }
        }
        .subscribe { (serviceList, region) ->
          services.set(serviceList)
          updateRealTimeInfo(serviceList, region)
        }
        .autoClear()

    updateRealTime.asObservable()
        .switchMap { (servicesVMs, region) ->
          val timetableEntries: List<TimetableEntry> = servicesVMs.map { it.service }
          realTimeChoreographer.getRealTimeResultsFromCleanElements(region, timetableEntries)
              .map { servicesVMs to it }
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { (servicesVMs, vehicles) ->
          vehicles.forEach { vehicle ->
            val serviceViewModel = servicesVMs.find { it.service.serviceTripId == vehicle.serviceTripId }
            serviceViewModel?.let {
              it.service.realtimeVehicle = vehicle
              serviceViewModel.updateService()
            }
          }
          services.set(servicesVMs.sortedBy { it.getRealTimeDeparture() })
        }
        .autoClear()


  }

  fun downloadMoreTimetableAsync() {
    loadMore.call(Unit)
  }

  fun getFirstNowPosition(): Int =
      services.get()?.indexOfFirst {
        it.getRealTimeDeparture() - getNow.execute().toSeconds() >= 0L
      } ?: 0

  private fun updateRealTimeInfo(servicesVMs: List<ServiceViewModel>, region: Region) {
    updateRealTime.call(Pair(servicesVMs, region))
  }

  fun getShareUrl(): Observable<String> =
      stop.flatMap { stop ->
        createShareUrl.execute(stop, services.get()!!.map { it.service })
      }

  fun onAlertForIdAdded(): Observable<RealtimeAlert> =
      stop.flatMap {
        realtimeAlertRepository.onAlertForIdAdded(it.code)
      }

  fun getActionBarTitle(): String {
    val title = with(stop.value) {
      listOf(name, address, code, type?.toString()?.capitalize()).first { it != null }
    }

    return when {
      title.isNullOrEmpty() -> "Unknown Service"
      else -> title
    }
  }

  fun getActionBarSubTitle(): String? {
    val subtitle = with(stop.value) {
      when {
        services.isNullOrEmpty() -> type?.toString()?.capitalize()
        else -> services
      }
    }?.plus(" stop")

    return when {
      subtitle.isNullOrEmpty() -> context.getString(R.string.upcoming_services)
      else -> subtitle
    }
  }

  fun downloadTimetableAsync(sinceTimeInMillis: Long? = null) {
    downloadTimetable.call(sinceTimeInMillis ?: getLast10Min())
  }

  private fun getLast10Min(): Long = getNow.execute().millis - TimeUnit.MINUTES.toMillis(10)

  override fun onCleared() {
    super.onCleared()
    services.get()!!.forEach {
      (it as ReactiveViewModel).onCleared()
    }
  }
}