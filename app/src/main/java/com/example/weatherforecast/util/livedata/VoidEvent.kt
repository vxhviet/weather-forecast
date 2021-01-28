package com.example.weatherforecast.util.livedata

/**
 * Created by viet on 1/28/21.
 */
class VoidEvent {
    private var hasBeenHandled = false

    fun hasBeenHandled(): Boolean = if (hasBeenHandled) {
        true
    } else {
        hasBeenHandled = true
        false
    }
}