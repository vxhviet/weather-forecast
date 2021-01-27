package com.example.weatherforecast.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.data.source.remote.ErrorResponse
import com.example.weatherforecast.SingleLiveEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by viet on 1/19/21.
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    abstract val repository: BaseRepository

    val isLoadingEvent: SingleLiveEvent<Boolean> by lazy { repository.isLoadingEvent }
    val errorLiveData: MutableLiveData<ErrorResponse> by lazy { repository.errorLiveData }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + CoroutineExceptionHandler(handler) + SupervisorJob()

    private val handler: ((CoroutineContext, Throwable) -> Unit) = { coroutineContext, throwable ->
        isLoadingEvent.postValue(false)
        throwable.printStackTrace()
        errorLiveData.postValue(ErrorResponse(throwable))
    }

    val viewModelScope: CoroutineScope by lazy { this }

    override fun onCleared() {
        super.onCleared()

        coroutineContext.cancel()
    }
}