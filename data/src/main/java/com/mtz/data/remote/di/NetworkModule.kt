package com.mtz.data.remote.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mtz.data.remote.ApiService
import com.mtz.data.remote.interceptor.MockJsonInterceptor
import com.mtz.data.remote.repository.ExchageRateRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context, loggingInterceptor: HttpLoggingInterceptor,
        mockJsonInterceptor: MockJsonInterceptor
    ): OkHttpClient {
        val cacheSize = (30 * 1024 * 1024).toLong() // 30 MB
        val httpCacheDirectory = File(context.cacheDir, "http-cache")
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())
        return OkHttpClient.Builder().cache(cache)
            .addInterceptor(mockJsonInterceptor)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(context)) {
                    val cacheControl = CacheControl.Builder()
                        .maxAge(30, TimeUnit.MINUTES) // Cache the response for 30 minutes
                        .build()
                    request.newBuilder().header("Cache-Control", cacheControl.toString()).build()
                } else request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 30).build()
                chain.proceed(request)
            }.addInterceptor(loggingInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideMockJsonInterceptor(@ApplicationContext context: Context): MockJsonInterceptor {
        return MockJsonInterceptor(context)
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient, gson: Gson): ApiService {
        return Retrofit.Builder().baseUrl("http://api.currencylayer.com/").client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
            .create(ApiService::class.java)
    }

    private fun hasNetwork(@ApplicationContext context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }


    @Provides
    @Singleton
    fun provideExchangeRateRepository(apiService: ApiService): ExchageRateRepositoryImpl {
        return ExchageRateRepositoryImpl(apiService)
    }


}