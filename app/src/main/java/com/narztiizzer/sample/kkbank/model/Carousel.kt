package com.narztiizzer.sample.kkbank.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Carousel(
    val id: Int,
    val imageURL: String
): Parcelable