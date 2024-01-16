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

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class MarsApiStatus { LOADING, ERROR, DONE }

// The [ViewModel] that is attached to the [OverviewFragment].
class OverviewViewModel : ViewModel() {

    // live data used to show the data on the screen (for string)
    // This is of type string
    private val _status = MutableLiveData<MarsApiStatus>()
    val status: LiveData<MarsApiStatus>
        get() = _status

    // another live data used to show the data on the screen (for images)
    // This is of type MarsProperty which have four different objects in it including image
    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    // Coroutines ViewModelJob and Coroutine scope are defined (used to do long running operations)
    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    // Call getMarsRealEstateProperties on init block so we can display status immediately
    init {
        getMarsRealEstateProperties()
    }

    // Calls Retrofit Service's methods using MarsApi public object
    // According to failure or success to connect with internet live data value is assigned
    private fun getMarsRealEstateProperties() {
        // using coroutine scope to update our live data
        coroutineScope.launch {
            // list is stored in this getPropertiesDeferred variable
            var getPropertiesDeferred = MarsApi.retrofitService.getProperties()
            // success case (we fetch data correctly)
            try {
                _status.value = MarsApiStatus.LOADING
                // now we can store list in listResult variable
                var listResult = getPropertiesDeferred.await()
                // now we can assign the value to the live data
                if (listResult.size > 0) {
                    _properties.value = listResult
                }
                _status.value = MarsApiStatus.DONE
            // failure case (we can't fetch data correctly)
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
            }
        }
    }
    // And then the assigned value to the live data is displayed on the layout using

    // On cleared function to cancel all the coroutines
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}