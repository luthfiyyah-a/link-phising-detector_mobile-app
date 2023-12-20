package com.fp.golink.data

import androidx.annotation.DrawableRes

data class Post(
    val id: String? = null,
    val judul: String? = null,
    val tulisan: String? = null,
    @DrawableRes val gambar: Int? = null
) {
    // Konstruktor tanpa argumen
    constructor() : this(null, null, null, null) {
        // Anda bisa memberikan nilai default atau tidak melakukan apa-apa di dalam konstruktor ini
    }
}