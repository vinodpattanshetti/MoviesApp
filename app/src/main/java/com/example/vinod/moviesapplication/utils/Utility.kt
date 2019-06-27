package com.example.vinod.moviesapplication.utils

import android.view.View

fun Any?.isNull(): Boolean = this == null

fun Boolean?.orFalse(): Boolean = this ?: false

fun View.hideView() {
  visibility = View.GONE
}

fun View.showView() {
  visibility = View.VISIBLE
}

fun View.inVisibleView() {
  visibility = View.INVISIBLE
}

fun Int?.orDefaultInt() = this ?: 0

fun Int?.orDefaultValueOneInt() = this ?: 1

fun Long?.orDefaultLong() = this ?:0