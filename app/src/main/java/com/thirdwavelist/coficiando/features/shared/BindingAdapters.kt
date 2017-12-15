package com.thirdwavelist.coficiando.features.shared

import android.databinding.BindingAdapter
import android.net.Uri
import android.view.View
import android.widget.ImageView

@BindingAdapter("image")
internal fun setImage(imageView: ImageView, image: Uri) {
    GlideApp.with(imageView)
        .load(image)
        .into(imageView)
}