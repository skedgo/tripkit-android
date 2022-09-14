package com.skedgo.sqlite;

import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;

public final class DatabaseTable {
  private String name;
  private DatabaseField[] fields;
  private String[] customScripts;
  private String[] fieldNames;

  public DatabaseTable(String name, DatabaseField[] fields, String... customScripts) {
    this.name = name;
    this.fields = fields;
    this.customScripts = customScripts;
  }

  public String getName() {
    return name;
  }

  public DatabaseField[] getFields() {
    return fields;
  }

  @Override
  public String toString() {
    return name;
  }

  public String[] getFieldNames() {
    if (fieldNames == null) {
      fieldNames = new String[fields.length];

      for (int i = 0, size = fields.length; i < size; i++) {
        fieldNames[i] = fields[i].getName();
      }
    }

    return fieldNames;
  }

  public String getDropSql() {
    return "DROP TABLE IF EXISTS " + name;
  }

  public String[] getPostCreateSql() {
    return customScripts;
  }

  public String getCreateSql() {
    StringBuilder sqlTextBuilder = new StringBuilder()
        .append("CREATE TABLE ").append(name).append(" ").append("(");

    // Ensure that a comma does not appear on the last iteration
    String comma = "";
    DatabaseField[] fields = getFields();
    for (DatabaseField field : fields) {
      sqlTextBuilder.append(comma);
      comma = ", ";

      sqlTextBuilder.append(field.getName());
      sqlTextBuilder.append(" ");
      sqlTextBuilder.append(field.getType());

      if (field.getConstraint() != null) {
        sqlTextBuilder.append(" ");
        sqlTextBuilder.append(field.getConstraint());
      }
    }

    sqlTextBuilder.append(")");
    return sqlTextBuilder.toString();
  }

  public void create(@NonNull SQLiteDatabase database) {
    database.execSQL(getCreateSql());
    if (customScripts != null) {
      for (String customScript : customScripts) {
        database.execSQL(customScript);
      }
    }
  }
}