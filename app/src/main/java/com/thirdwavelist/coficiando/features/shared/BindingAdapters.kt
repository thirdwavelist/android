package com.thirdwavelist.coficiando.features.shared

import android.animation.ObjectAnimator
import android.databinding.BindingAdapter
import android.net.Uri
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView

@BindingAdapter("image")
internal fun setImage(imageView: ImageView, image: Uri) {
    GlideApp.with(imageView)
        .load(image)
        .into(imageView)
}

@BindingAdapter(value = ["isVisible", "fadeAnimationDuration"], requireAll = false)
internal fun setIsVisibleWithAnimation(view: View, isVisible: Boolean, fadeAnimationDuration: Long?) = when {
    isVisible -> {
        view.visibility = View.VISIBLE
        fadeAnimationDuration?.let {
            ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply { duration = fadeAnimationDuration }.start()
        }
    }
    else -> {
        view.visibility = View.GONE
        fadeAnimationDuration?.let {
            ObjectAnimator.ofFloat(view, "alpha", 1f, 0f).apply { duration = fadeAnimationDuration }.start()
        }
    }
}

@BindingAdapter("htmlText")
internal fun setHtmlText(textView: TextView, htmlText: String) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        @Suppress("DEPRECATION")
        textView.text = Html.fromHtml(htmlText)
    } else {
        textView.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    }}