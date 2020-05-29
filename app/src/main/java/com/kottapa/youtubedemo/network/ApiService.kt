package com.kottapa.youtubedemo.network

/*
Backend API service
Uses Retrofit and Moshi
 */


import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import kotlinx.coroutines.Deferred
import retrofit2.http.Query


//test urls
private const val BASE_URL = " https://android-kotlin-fun-mars-server.appspot.com/"
//private const val BASE_URL = "http://www.splashbase.co/api/v1/images/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getItems] method
 */
interface ApiService {
    /**
     * Returns a Coroutine [Deferred] [List] of [Item] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("realestate")
    fun getItems():
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
    //Deferred - wait without blocking
            Deferred<List<Item>>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 * Singleton object
 */
object ItemApi {
    val retrofitService : ApiService by lazy { retrofit.create(ApiService::class.java) }
}
