package com.skedgo.tripkit.account.data

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import org.amshove.kluent.`should be equal to`
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class UserKeyRepositoryImplTest {
  private val prefs: SharedPreferences by lazy {
    ApplicationProvider.getApplicationContext<Context>().getSharedPreferences("", Context.MODE_PRIVATE)
  }
  private val userKeyRepository: UserKeyRepositoryImpl by lazy {
    UserKeyRepositoryImpl(prefs)
  }

  @Test
  fun `should return saved key`() {
    // Arrange.
    prefs.edit()
        .putString("userUUID", "key")
        .apply()

    // Act.
    val userKey = userKeyRepository.getUserKey()
        .test()
        .values().first()

    // Assert.
    userKey `should be equal to` "key"
  }

//  @Test
//  fun `should return new key`() {
//
//    // Act.
//    val actualUserIdentifier = userKeyRepository.getUserKey()
//        .test()
//        .values().first()
//
//    // Assert.
//    actualUserIdentifier.let { UUID.fromString(it) }
//  }
}
