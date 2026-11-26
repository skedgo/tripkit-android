package com.skedgo.tripkit.android

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class FetchRegionsSchedulingTest {
    @Test
    fun `region refresh schedules one replaceable network job without Google Play dispatcher`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val scheduler = context.getSystemService(JobScheduler::class.java)
        scheduler.cancelAll()

        repeat(2) {
            FetchRegionsService.scheduleAsync(context).test().assertComplete().assertNoErrors()
        }

        val jobs = scheduler.allPendingJobs.filter {
            it.service.className == "com.skedgo.tripkit.android.FetchRegionsJobService"
        }
        assertEquals(1, jobs.size)
        assertEquals(JobInfo.NETWORK_TYPE_ANY, jobs.single().networkType)
        assertEquals(false, jobs.single().isPersisted)
    }
}
