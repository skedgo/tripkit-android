package skedgo.tripkit.account.backup

import android.app.backup.BackupAgentHelper
import android.app.backup.SharedPreferencesBackupHelper
import skedgo.tripkit.account.data.AccountDataModule

val PREFS_BACKUP_KEY = "account_prefs"

internal class TripGoBackupAgent : BackupAgentHelper() {

  override fun onCreate() {
    val helper = SharedPreferencesBackupHelper(this, AccountDataModule.UserTokenPreferences)
    addHelper(PREFS_BACKUP_KEY, helper)
  }
}