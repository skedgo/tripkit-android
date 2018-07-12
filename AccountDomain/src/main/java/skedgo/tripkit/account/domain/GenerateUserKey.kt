package skedgo.tripkit.account.domain

import rx.Single
import java.util.*
import javax.inject.Inject

open class GenerateUserKey @Inject constructor() {

  open fun execute(): Single<String> = Single.just(UUID.randomUUID().toString())
}