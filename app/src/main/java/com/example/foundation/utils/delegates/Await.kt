package com.example.foundation.utils.delegates

import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty


/**
 * Property delegate which suspend the thread which tries to get value from the  property manager by
 * this delegate if the property contains NULL. Also only first  not-null assignment updates
 * property value, all further assignment are ignored.
 *
 */

class Await<T> {

    private val countDownLatch = CountDownLatch(1)
    private  var value = AtomicReference<T>(null)

    operator fun getValue(thisRef: Any, property: KProperty<*>): T{
        countDownLatch.await()
        return value.get()
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T){
        if(value==null) return
        if(this.value.compareAndSet(null,value)){
            countDownLatch.countDown()
        }
    }

}