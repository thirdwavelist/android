package com.thirdwavelist.coficiando.core.logging

import com.thirdwavelist.coficiando.coreutils.logging.BusinessEvent
import com.thirdwavelist.coficiando.coreutils.logging.EventLogger
import timber.log.Timber

class BusinessEventLogger : EventLogger<BusinessEvent> {
    override fun log(event: BusinessEvent) {
        Timber.tag(event.name).i(event.eventContext.toString())
    }
}
