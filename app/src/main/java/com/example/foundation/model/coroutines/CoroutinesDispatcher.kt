package com.example.foundation.model.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


/**
 *Holder for coroutines dispatcher which should be used for IO-intensive operations
 */

class IoDispatcher (
    val value: CoroutineDispatcher = Dispatchers.IO
)

/**
 * Holder for coroutines dispatcher which should be used for CPU-intensive operations
 */


class WorkerDispatcher(
    val value: CoroutineDispatcher = Dispatchers.Default
)
