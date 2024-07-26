package com.skedgo.tripkit.datetime

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.data.datetime.PrintFullDateImpl
import org.joda.time.DateTime
import org.junit.Test
import java.util.Date
import java.util.Locale

@Suppress("IllegalIdentifier")
class PrintFullDateImplTest {
    private val getLocale: () -> Locale = mock()
    private val printFullDateByJoda: (DateTime, Locale) -> String = mock()
    private val printFullDateByJava: (Date, Locale) -> String = mock()
    private val printFullDate: PrintFullDate by lazy {
        PrintFullDateImpl(getLocale, printFullDateByJoda, printFullDateByJava)
    }

    @Test
    fun `Should print full date by Java`() {
        val dateTime = DateTime.parse("2017-05-01T00:00")

        whenever(getLocale.invoke()).thenReturn(Locale.US)
        whenever(printFullDateByJoda.invoke(eq(dateTime), eq(Locale.US)))
            .thenThrow(IllegalArgumentException())
        val expectedPrint = "Monday, 1 May 2017"
        whenever(printFullDateByJava.invoke(eq(dateTime.toDate()), eq(Locale.US)))
            .thenReturn(expectedPrint)

        val subscriber = printFullDate.execute(dateTime)
            .test()
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors()
        subscriber.assertValue(expectedPrint)
    }

    @Test
    fun `Should print full date by Joda`() {
        val dateTime = DateTime.parse("2017-05-01T00:00")

        whenever(getLocale.invoke()).thenReturn(Locale.US)
        val expectedPrint = "Monday, 1 May 2017"
        whenever(printFullDateByJoda.invoke(eq(dateTime), eq(Locale.US)))
            .thenReturn(expectedPrint)

        val subscriber = printFullDate.execute(dateTime)
            .test()
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors()
        subscriber.assertValue(expectedPrint)
    }

    @Test
    fun `Should print full date correctly when using default impls`() {
        val subscriber = PrintFullDateImpl(getLocale = { Locale.US })
            .execute(DateTime.parse("2017-05-01T00:00"))
            .test()
        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertValue("Monday, May 1, 2017")
    }
}