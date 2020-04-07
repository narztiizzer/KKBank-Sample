package com.narztiizzer.carousel

import android.view.View

interface CarouselItemListener {
    fun onCarouselItemCreated(view: View, position: Int)
}