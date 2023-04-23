package com.example.foundation.utils


/**
 * Represents "side effect"
 * Used in [LiveData] as a wrapper fo events
 */
class Event<T>(
    private val value: T
) {
    private var handled: Boolean = false

    fun getValue(): T?{
        if(handled) return null
        handled = true
        return value
    }
}