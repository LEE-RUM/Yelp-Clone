package com.example.hw5_yelpclone

import com.google.gson.annotations.SerializedName

data class RestaurantData(
    val total : Int,
    val businesses : List<Restaurant>
)

data class Restaurant(
    val name: String,
    val rating: Double,
    val price: String,
    val review_count : Int,
    val distance: Double,
    val image_url: String,
    val categories: List<RestaurantCategory>,
    val location: RestaurantLocation,
) {
    fun convertDistance(): String {
        val milesPerMeter = 0.000621371
        val distanceInMiles = "%.2f".format(distance * milesPerMeter)
        return "$distanceInMiles mi"
    }
}
data class RestaurantCategory(
    val title: String
)

data class RestaurantLocation(
    @SerializedName("address1") val address: String,
)


