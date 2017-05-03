package skedgo.tripkit.datetime

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.joda.time.DateTime
import org.junit.Test
import java.util.*

@Suppress("IllegalIdentifier")
class PrintFullDateImplTest {
  private val getLocale: () -> Locale = mock()
  private val printFullDateByJoda: (DateTime, Locale) -> String = mock()
  private val printFullDateByJava: (Date, Locale) -> String = mock()
  private val printFullDate: PrintFullDate by lazy {
    PrintFullDateImpl(getLocale, printFullDateByJoda, printFullDateByJava)
  }

  @Test fun `Should print full date by Java`() {
    val dateTime = DateTime.parse("2017-05-01T00:00")

    whenever(getLocale.invoke()).thenReturn(Locale.US)
    whenever(printFullDateByJoda.invoke(eq(dateTime), eq(Locale.US)))
        .thenThrow(IllegalArgumentException())
    val expectedPrint = "Monday, 1 May 2017"
    whenever(printFullDateByJava.invoke(eq(dateTime.toDate()), eq(Locale.US)))
        .thenReturn(expectedPrint)

    printFullDate.execute(dateTime)
        .test()
        .awaitTerminalEvent()
        .assertNoErrors()
        .assertValue(expectedPrint)
  }

  @Test fun `Should print full date by Joda`() {
    val dateTime = DateTime.parse("2017-05-01T00:00")

    whenever(getLocale.invoke()).thenReturn(Locale.US)
    val expectedPrint = "Monday, 1 May 2017"
    whenever(printFullDateByJoda.invoke(eq(dateTime), eq(Locale.US)))
        .thenReturn(expectedPrint)

    printFullDate.execute(dateTime)
        .test()
        .awaitTerminalEvent()
        .assertNoErrors()
        .assertValue(expectedPrint)
  }

  @Test fun `Should print full date correctly when using default impls`() {
    PrintFullDateImpl(getLocale = { Locale.US })
        .execute(DateTime.parse("2017-05-01T00:00"))
        .test()
        .awaitTerminalEvent()
        .assertNoErrors()
        .assertValue("Monday, May 1, 2017")
  }
}