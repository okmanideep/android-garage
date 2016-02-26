package com.manidesto.androidgarage.data

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.GsonConverterFactory
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Singleton

/**
 * Network Module.
 * Create only with application context
 */
@Module
class NetworkModule(val context : Context) {
    val CACHE_SIZE : Long = 1024 * 1024 * 10; //10 MB
    val BASE_URL = "https://api.twitter.com/1.1/";
    @Provides
    fun providesLoggingInterceptor() : Interceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        return logger;
    }

    @Provides @Singleton
    fun provideOkHttpClient(logger : Interceptor): OkHttpClient {
        val cache = Cache(context.cacheDir, CACHE_SIZE)
        return OkHttpClient.Builder()
                .addInterceptor(logger)
                .cache(cache)
                .build()
    }

    @Provides
    fun provideGsonConverter() : GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides @Singleton
    fun provideTwitterApi(client : OkHttpClient, gsonConverterFactory: GsonConverterFactory) : TwitterApi {
        Timber.d("Twitter Api provided")
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(TwitterApi::class.java)
    }
}