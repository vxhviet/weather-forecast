package com.example.weatherforecast.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.util.livedata.Event
import com.example.weatherforecast.data.source.Result
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by viet on 1/19/21.
 */
abstract class BaseViewModel() : ViewModel(), CoroutineScope {

    val isLoadingEvent: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData<Event<Boolean>>() }
    val errorLiveData: MutableLiveData<Result.Error> by lazy { MutableLiveData<Result.Error>() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + CoroutineExceptionHandler(handler) + SupervisorJob()

    private val handler: ((CoroutineContext, Throwable) -> Unit) = { coroutineContext, throwable ->
        isLoadingEvent.postValue(Event(false))
        throwable.printStackTrace()
        errorLiveData.postValue(Result.Error(Exception(throwable)))
    }

    val viewModelScope: CoroutineScope by lazy { this }

    override fun onCleared() {
        super.onCleared()

        coroutineContext.cancel()
    }
}