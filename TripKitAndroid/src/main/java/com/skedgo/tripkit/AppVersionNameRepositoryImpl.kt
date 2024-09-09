package com.skedgo.tripkit

import android.content.Context
import com.skedgo.tripkit.configuration.AppVersionNameRepository
import io.reactivex.Observable

internal class AppVersionNameRepositoryImpl constructor(
    private val context: Context
) : AppVersionNameRepository {
    override fun getAppVersionName(): Observable<String> = Observable.fromCallable {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName
    }
}
