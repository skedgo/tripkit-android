package com.skedgo.tripkit.data.connectivity
import android.content.Context
import android.net.ConnectivityManager

class ConnectivityServiceImpl(val context: Context) : ConnectivityService {
  override val isNetworkConnected: Boolean
    get() {
      val connectivityManager: ConnectivityManager =
          context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      return connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true
    }
}
