package skedgo.tripkit.configuration

import io.reactivex.Observable
import javax.inject.Inject

open class GetAppVersion @Inject constructor(
    private val appVersionNameRepository: AppVersionNameRepository
) {
  open fun execute(): Observable<String>
      = appVersionNameRepository.getAppVersionName()
      .onErrorReturn { it::class.java.simpleName }
      .map { "a-$it" }
}
