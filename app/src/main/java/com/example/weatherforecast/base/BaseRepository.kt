package com.example.weatherforecast.base

import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.data.source.remote.ApiService
import com.example.weatherforecast.data.source.remote.ErrorResponse
import com.example.weatherforecast.SingleLiveEvent

/**
 * Created by viet on 1/21/21.
 */
abstract class BaseRepository {
    val apiService = ApiService.Client.instance
    val isLoadingEvent: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }
    val errorLiveData: MutableLiveData<ErrorResponse> by lazy { MutableLiveData<ErrorResponse>() }
}