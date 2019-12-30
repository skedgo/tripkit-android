package com.skedgo.rxlifecyclecomponents



/**
 * Lifecycle events that can be emitted by Fragments.
 */
enum class FragmentEvent {
    ATTACH,
    CREATE,
    CREATE_VIEW,
    START,
    RESUME,
    PAUSE,
    STOP,
    DESTROY_VIEW,
    DESTROY,
    DETACH
}