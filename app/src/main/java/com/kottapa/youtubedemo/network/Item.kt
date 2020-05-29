package com.kottapa.youtubedemo.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * This data class defines a single item which includes an ID, the image URL and other info
 * The names of this data class are used by Moshi to match the names of values in JSON.
 */


@Parcelize
data class Item (
    val id: String,
    // used to map img_src from the JSON to imgSrcUrl in our class
    @Json(name = "img_src") val imgSrcUrl: String,
    val type: String
): Parcelable



/*
//http://www.splashbase.co/api/v1/images/latest
@Parcelize
data class Item (
    @Json(name = "id") val id: Int,
    // used to map img_src from the JSON to imgSrcUrl in our class
    @Json(name = "url") val imgSrcUrl: String,
    @Json(name = "source_id") val type: Int

): Parcelable
*/
