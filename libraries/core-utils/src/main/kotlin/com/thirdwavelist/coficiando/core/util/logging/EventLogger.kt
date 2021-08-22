package com.thirdwavelist.coficiando.core.util.logging

interface EventLogger<T> where T : Event {
    fun log(event: T)
}