package com.skedgo.tripkit;


import com.skedgo.sqlite.DatabaseField;
import com.skedgo.sqlite.DatabaseTable;

final class Tables {
  public static final DatabaseField FIELD_JSON = new DatabaseField("json", "TEXT");
  public static final DatabaseTable TRANSPORT_MODES = new DatabaseTable(
      "transport_modes",
      new DatabaseField[] {FIELD_JSON}
  );
  public static final DatabaseTable REGIONS = new DatabaseTable(
      "regions",
      new DatabaseField[] {FIELD_JSON}
  );

  private Tables() {}
}