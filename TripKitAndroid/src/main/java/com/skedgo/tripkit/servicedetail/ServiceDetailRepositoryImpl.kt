package com.skedgo.tripkit.servicedetail

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.skedgo.tripkit.ServiceResponse
import com.skedgo.rxtry.printThrowableStackTrace
import com.skedgo.tripkit.data.regions.RegionService
import io.reactivex.Observable
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import javax.inject.Inject

class ServiceDetailRepositoryImpl @Inject constructor(
    private val serviceDetailApi: ServiceDetailApi,
    private val regionService: RegionService,
    private val gson: Gson
) : ServiceDetailRepository {
    private fun JsonObject.hasNonBlankString(name: String): Boolean =
        has(name) && !get(name).isJsonNull && get(name).asString.isNotBlank()

    private fun validateAndParseServiceResponse(json: JsonObject): ServiceResponse {
        // Some servers return HTTP 200 with an error payload like:
        // {"error":"No service of that code found in provided region.","usererror":false}
        // Treat these as failures so other region servers can win the race.

        val hasShapes = json.has("shapes") && json.get("shapes").isJsonArray
        val hasNonEmptyShapes = hasShapes && json.getAsJsonArray("shapes").size() > 0

        if (!hasNonEmptyShapes && json.hasNonBlankString("error")) {
            val message = json.get("error").asString
            val isUserError = json.has("usererror") && !json.get("usererror").isJsonNull && runCatching {
                json.get("usererror").asBoolean
            }.getOrDefault(false)

            throw RuntimeException(
                if (isUserError) "ServiceDetail error (user): $message" else "ServiceDetail error: $message"
            )
        }

        if (!hasNonEmptyShapes) {
            throw RuntimeException("ServiceDetail error: Missing/empty shapes in response")
        }

        return gson.fromJson(json, ServiceResponse::class.java)
    }

    private fun requestService(
        url: String,
        region: String,
        serviceTripId: String,
        operator: String?,
        startStopCode: String?,
        endStopCode: String?,
        embarkationTimeInSecs: Long,
        encode: Boolean
    ): Observable<ServiceResponse> {
        return serviceDetailApi.getServiceAsync(
            url = url,
            region = region,
            serviceTripId = serviceTripId,
            operator = operator,
            startStopCode = startStopCode,
            endStopCode = endStopCode,
            timeInSecs = embarkationTimeInSecs,
            encode = encode
        )
            .map(::validateAndParseServiceResponse)
    }

    override fun getService(
        baseUrls: List<String>,
        region: String,
        serviceTripId: String,
        operator: String?,
        startStopCode: String?,
        endStopCode: String?,
        embarkationTimeInSecs: Long,
        encode: Boolean
    ): Observable<ServiceResponse> {
        val sources = baseUrls.map { baseUrl ->
            val url = baseUrl.toHttpUrlOrNull()!!
                .newBuilder()
                .addPathSegment("service.json")
                .build()
                .toString()

            requestService(
                url = url,
                region = region,
                serviceTripId = serviceTripId,
                operator = operator,
                startStopCode = startStopCode,
                endStopCode = endStopCode,
                embarkationTimeInSecs = embarkationTimeInSecs,
                encode = encode
            ).onErrorResumeNext { e1: Throwable ->
                requestService(
                    url = url,
                    region = region,
                    serviceTripId = serviceTripId,
                    operator = null,
                    startStopCode = startStopCode,
                    endStopCode = endStopCode,
                    embarkationTimeInSecs = embarkationTimeInSecs,
                    encode = encode
                ).onErrorResumeNext { e2: Throwable ->
                    requestService(
                        url = url,
                        region = region,
                        serviceTripId = serviceTripId,
                        operator = null,
                        startStopCode = null,
                        endStopCode = endStopCode,
                        embarkationTimeInSecs = embarkationTimeInSecs,
                        encode = encode
                    ).onErrorResumeNext { _: Throwable ->
                        Observable.error(e1) // keep original error for diagnostics
                    }
                }
            }.doOnError {
                it.printThrowableStackTrace()
            }
        }

        return Observable.mergeDelayError(sources)
            .firstOrError()
            .toObservable()
    }

    override fun getService(
        region: String,
        serviceTripId: String,
        operator: String?,
        startStopCode: String?,
        endStopCode: String?,
        embarkationTimeInSecs: Long,
        encode: Boolean
    ): Observable<ServiceResponse> {
        return regionService.getRegionByNameAsync(region)
            .flatMap { regionObj ->
                getService(
                    baseUrls = regionObj.getURLs() ?: emptyList(),
                    region = region,
                    serviceTripId = serviceTripId,
                    operator = operator,
                    startStopCode = startStopCode,
                    endStopCode = endStopCode,
                    embarkationTimeInSecs = embarkationTimeInSecs,
                    encode = encode
                )
            }
    }
}
