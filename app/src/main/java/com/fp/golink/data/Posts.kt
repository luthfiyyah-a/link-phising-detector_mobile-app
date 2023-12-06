package com.fp.golink.data

import android.content.res.Resources
import com.fp.golink.R

fun postList(resources: Resources): List<Post> {
    return listOf(
        Post(
            id = "1",
            tulisan = "hai, alhamdulillah aku ga pernah kena kecurian digital hehe",
            gambar = R.drawable.ic_image
        )
    )
}