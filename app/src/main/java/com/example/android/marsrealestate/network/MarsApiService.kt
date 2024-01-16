/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// a variable for root web address of the Mars Server endpoint
private const val BASE_URL = "https://mars.udacity.com/"

// Library called Moshi which parses JSON into Kotlin objects
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Retrofit variable is build using Retrofit.Builder
// Converter factory that allows Retrofit to return the server response in a useful format
// Here we are using Moshi as our converter factory (convert JSON to kotlin objects)
// Also adding CallAdapterFactory so that we can replace the call method with coroutine deferred
// then giving the BASE_URL to the retrofit  and then build it
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

// Explains how retrofit talks to our web server using http requests
interface MarsApiService {
    // When we call the get Properties method, Retrofit appends "___" to the base url
    @GET("realestate")
    // Method which get data from the web server
    // Getting data as a deferred list ( Deferred is a coroutine Job that can directly return a result
    // and it contains await method which causes code to await without blocking coroutine
    fun getProperties():
            Deferred<List<MarsProperty>>
}

// A public object to expose the retrofit service to the rest of the app
object MarsApi {
    val retrofitService : MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}

// Also we need permission from Android to access Internet so, write that in user permission