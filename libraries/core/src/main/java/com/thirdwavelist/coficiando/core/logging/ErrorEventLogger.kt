package com.thirdwavelist.coficiando.core.logging

import com.thirdwavelist.coficiando.coreutils.logging.ErrorEvent
import com.thirdwavelist.coficiando.coreutils.logging.EventLogger
import timber.log.Timber

class ErrorEventLogger : EventLogger<ErrorEvent> {
    override fun log(event: ErrorEvent) {
        Timber.tag(event.componentName).e(event.error, event.eventName)
    }
}