package com.thirdwavelist.coficiando.coreutils.logging

sealed class Event(val eventName: String)
data class ErrorEvent(val priority: ErrorPriority, val componentName: String, val error: Throwable) : Event("Error")
data class BusinessEvent(val name: String, val eventContext: Map<String, Any>) : Event(name)

enum class ErrorPriority {
    /* Experience breaking or seriously unwanted errors e.g. critical network call failed, app initialization error */
    CRITICAL,

    /* User facing errors that are serious, but not critical e.g. secondary network call for optional information */
    HIGH,

    /* Side-effect not affecting the user, but necessary to monitor e.g. analytics failure */
    LOW
}