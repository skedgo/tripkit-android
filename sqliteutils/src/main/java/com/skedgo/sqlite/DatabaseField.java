package com.skedgo.sqlite;

public final class DatabaseField {
  private String name;
  private String type;
  private String constraint;

  public DatabaseField(String name, String type) {
    this(name, type, null);
  }

  public DatabaseField(String name, String type, String constraint) {
    this.name = name;
    this.type = type;
    this.constraint = constraint;
  }

  @Override
  public String toString() {
    return name;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getConstraint() {
    return constraint;
  }
}