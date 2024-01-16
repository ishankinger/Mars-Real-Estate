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

import com.squareup.moshi.Json

// Now using Moshi we want to convert the JSON to Kotlin objects
// So we make a data class with it's objects having same name as used in the web Server
data class MarsProperty(
    val id: String,
    // Here we don't want to use img_src as our object name but it is used in the web server so
    // we can use this annotation to tell android that for this "___" name we are using different name
    @Json(name = "img_src")
    val imgSrcUrl: String,
    val type: String,
    val price: Double)
