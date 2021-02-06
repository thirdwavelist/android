package com.thirdwavelist.coficiando.coreutils.logging

interface EventLogger<T> where T : Event {
    fun log(event: T)
}