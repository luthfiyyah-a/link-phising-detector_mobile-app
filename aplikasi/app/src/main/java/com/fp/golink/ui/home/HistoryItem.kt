package com.fp.golink.ui.home

import androidx.annotation.DrawableRes

data class HistoryItem (val link: String? = null,
                        val result: String? = null)
{
    constructor() : this(null, null){}
}