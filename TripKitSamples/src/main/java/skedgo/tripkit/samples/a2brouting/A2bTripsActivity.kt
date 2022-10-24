package skedgo.tripkit.samples.a2brouting

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.skedgo.TripKit
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import com.skedgo.rxlifecyclecomponents.RxAppCompatActivity
import com.skedgo.rxlifecyclecomponents.bindToLifecycle
import skedgo.tripkit.samples.R
import skedgo.tripkit.samples.databinding.A2bTripsBinding
import java.util.concurrent.TimeUnit

class A2bTripsActivity : RxAppCompatActivity() {
    val viewModel: A2bTripsViewModel by lazy {
        A2bTripsViewModel(TripKit.getInstance().routeService, applicationContext)
    }
    val binding: A2bTripsBinding by lazy {
        DataBindingUtil.setContentView<A2bTripsBinding>(this, R.layout.a2b_trips)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        viewModel.isRefreshing
                .bindToLifecycle(this)
                .observeOn(mainThread())
                .subscribe { binding.progress.visibility = if(it) View.VISIBLE else View.GONE }

        getSampleTrips()

        viewModel.onTripSelected
                .map { TripDetailsActivity.newIntent(this, it) }
                .bindToLifecycle(this)
                .subscribe { startActivity(it) }

        binding.progressLayout.setOnRefreshListener {
            binding.progressLayout.isRefreshing = false
            getSampleTrips()
        }
    }

    fun getSampleTrips() {
        Observable.just(Unit)
                .delay(500, TimeUnit.MILLISECONDS)
                .flatMap { viewModel.showSampleTrips() }
                .bindToLifecycle(this)
                .subscribe({}, {
                    AlertDialog.Builder(this)
                            .setTitle("Error while routing")
                            .setMessage(it.message)
                            .create()
                            .show()
                })
    }
}
