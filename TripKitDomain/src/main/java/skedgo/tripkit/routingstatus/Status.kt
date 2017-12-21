package skedgo.tripkit.routingstatus

sealed class Status {
  class InProgress : Status()
  class Completed : Status()
  class Error(val message: String) : Status()
}
