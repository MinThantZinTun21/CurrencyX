package com.mtz.currencyex.repository

import com.mtz.currencyex.fn.Failure
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

interface BaseRepository {
    fun handleException(e: Throwable): Failure {
        return when (e) {
            is IOException -> Failure.Network(e.message.toString())
            is InterruptedException -> Failure.Network(e.message.toString())
            is JSONException -> Failure.WrongFormat(e.message.toString())
            is HttpException -> Failure.Http(extractException(e))
            else -> Failure.Unknown((e.message.toString()))
        }
    }

    fun extractException(e: Throwable): String {
        return when (e) {
            is HttpException -> {
                var message = ""
                val response = e.response()?.errorBody()?.string()
                response?.let {
                    message = JSONObject(response).getString("message")
                }
                message
            }

            else -> "Something went wrong"
        }
    }


}