package com.example.test.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("first_name") val first_name: String?,
    @SerializedName("photo") val photo: String?,
    @SerializedName("second_name") val second_name: String?,
    @SerializedName("education") val education: Int ?,
    @SerializedName("company") val company: List<Company>?
)
data class Company(
    @SerializedName("name") val name : String?,
    @SerializedName("position") val position : String?
)
enum class Education(var number:Int) {
    high_school(1),
    bachelor(2),
    master(3),
    doctoral(4)
}