package com.mtz.data.remote.interceptor

import android.content.Context
import com.mtz.data.MockJson
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Invocation
import java.io.IOException


class MockJsonInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class.java)
        val mockJsonAnnotation = invocation?.method()?.getAnnotation(MockJson::class.java)
        return if (mockJsonAnnotation != null) {
            val fileName = mockJsonAnnotation.fileName
            val jsonResponse = readJsonFromFile(fileName)
            val mediaType = "application/json".toMediaTypeOrNull()
            val responseBody = jsonResponse.toResponseBody(mediaType)
            Response.Builder()
                .request(request)
                .protocol(okhttp3.Protocol.HTTP_1_1)
                .code(200) // Mock successful response
                .message("OK")
                .body(responseBody)
                .build()
        } else {
            chain.proceed(request)
        }
    }

    private fun readJsonFromFile(fileName: String): String {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}