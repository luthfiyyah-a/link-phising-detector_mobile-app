package com.fp.golink.data

import androidx.annotation.DrawableRes

data class User(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val username: String? = null,
    @DrawableRes val profilePicture: Int? = null
) {
    // Konstruktor tanpa argumen
    constructor() : this(null, null, null, null, null) {
        // Anda bisa memberikan nilai default atau tidak melakukan apa-apa di dalam konstruktor ini
    }
}