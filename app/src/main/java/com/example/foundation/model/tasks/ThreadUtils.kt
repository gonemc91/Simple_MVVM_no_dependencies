package com.example.foundation.model.tasks


/**
 * Common methods for working with thread.
 *
 */

interface ThreadUtils {

    /**
     *Suspend the current thread for the specified amount of time
     */


    fun sleep(millis: Long)


    class Default: ThreadUtils {
        override fun sleep(millis: Long) {
            Thread.sleep(millis)
        }
    }


}