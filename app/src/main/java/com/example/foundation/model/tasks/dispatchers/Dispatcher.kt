package com.example.foundation.model.tasks.dispatchers


/**
 * Dispatchers run the specified bloc of code in some way.
 */

interface Dispatcher {

    fun dispatcher(block: () -> Unit)
}