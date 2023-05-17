package com.example.foundation.model.dispatchers


/**
 * Dispatchers run the specified bloc of code in some way.
 */

interface Dispatcher {

    fun dispatch(block: () -> Unit)
}