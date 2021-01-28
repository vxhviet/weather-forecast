package com.example.weatherforecast.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.weatherforecast.util.livedata.Event
import com.example.weatherforecast.util.livedata.VoidEvent

/**
 * Created by viet on 1/28/21.
 */

fun <T> LiveData<out Event<T>>.observeEvent(owner: LifecycleOwner, onEventUnhandled: (T) -> Unit) {
    observe(owner, Observer {
        it?.getContentIfNotHandled()?.let(onEventUnhandled)
    })
}

fun LiveData<out VoidEvent>.observeEvent(owner: LifecycleOwner, onEventUnhandled: () -> Unit) {
    observe(owner, Observer {
        if (!it.hasBeenHandled()) onEventUnhandled()
    })
}