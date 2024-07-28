package com.zalomsky.rickandmorty.utils

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.zalomsky.rickandmorty.R

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.showIf(isVisible: Boolean) {
    this.visibility = if(isVisible) View.VISIBLE else View.GONE
}

fun ImageView.load(url: String, @DrawableRes placeholder: Int = R.drawable.splash) {
    Glide.with(this).load(url).into(this)
}