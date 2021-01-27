package com.example.weatherforecast.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.SingleLiveEvent
import com.example.weatherforecast.data.source.Result
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

/**
 * Created by viet on 1/19/21.
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    val isLoadingEvent: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }
    val errorLiveData: MutableLiveData<Result.Error> by lazy { MutableLiveData<Result.Error>() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + CoroutineExceptionHandler(handler) + SupervisorJob()

    private val handler: ((CoroutineContext, Throwable) -> Unit) = { coroutineContext, throwable ->
        isLoadingEvent.postValue(false)
        throwable.printStackTrace()
        errorLiveData.postValue(Result.Error(Exception(throwable)))
    }

    val viewModelScope: CoroutineScope by lazy { this }

    override fun onCleared() {
        super.onCleared()

        coroutineContext.cancel()
    }
}