package skedgo.tripkit.logging

interface ErrorLogger {
    /**
     * Just prints the error out for the sake of debugging.
     */
    fun logError(error: Throwable)

    /**
     * If debuggable, it just simply prints the error out.
     * Otherwise, it'll upload that error to Fabric for further investigation.
     * Should use this to catch some errors that we may not be aware of.
     */
    fun trackError(error: Throwable)
}
