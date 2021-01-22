package com.example.weatherforecast.network

import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response

/**
 * Created by viet on 1/19/21.
 */
open class ErrorResponse() : Exception() {
    var code: Int = ErrorCode.ERR_UNKNOWN
    override var message: String? = null

    constructor(throwable: Throwable) : this() {
        code = ErrorCode.ERR_UNKNOWN
        message = throwable.message ?: ""

        when (throwable) {
            is ErrorResponse -> {
                this.code = throwable.code
                this.message = throwable.message
            }
            is HttpException -> {
                this.code = throwable.code()
                this.message = throwable.message()
            }
        }
    }

    constructor(response: Response<*>) : this() {
        try {
            response.errorBody()?.string()?.let {
                val jsonObj = JSONObject(it)
                this.code = jsonObj.getString("cod").toInt()
                this.message = jsonObj.getString("message")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}