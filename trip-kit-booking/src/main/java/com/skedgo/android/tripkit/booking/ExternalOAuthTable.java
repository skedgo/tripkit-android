package com.skedgo.android.tripkit.booking;

import skedgo.sqlite.DatabaseField;
import skedgo.sqlite.DatabaseTable;
import skedgo.sqlite.UniqueIndices;

public class ExternalOAuthTable {

  public static final DatabaseField ID = new DatabaseField("_id", "INTEGER", "PRIMARY KEY AUTOINCREMENT");
  public static final DatabaseField AUTH_SERVICE_ID = new DatabaseField("auth_service_id", "TEXT", "COLLATE NOCASE");
  public static final DatabaseField TOKEN = new DatabaseField("token", "TEXT", "COLLATE NOCASE");
  public static final DatabaseField EXPIRES_IN = new DatabaseField("expires_in", "REAL");

  public static final DatabaseTable EXTERNAL_AUTHS = new DatabaseTable(
      "external_auths",
      new DatabaseField[] {ID, AUTH_SERVICE_ID, TOKEN, EXPIRES_IN},
      UniqueIndices.of("external_auths", AUTH_SERVICE_ID));

  private ExternalOAuthTable() {}
}
