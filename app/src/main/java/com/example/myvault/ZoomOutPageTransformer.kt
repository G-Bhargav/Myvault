package com.example.myvault

import android.view.View
import androidx.viewpager2.widget.ViewPager2


class ZoomOutPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> {
                    view.setScrollX((pageWidth * 0.75 * -1).toInt())
                }
                position <= 1 -> {
                    if (position < 0) {
                        view.setScrollX((pageWidth * 0.75 * position).toInt())
                    } else {
                        view.setScrollX((pageWidth * 0.75 * position).toInt())
                    }

                }
                else -> {
                    view.setScrollX((pageWidth * 0.75 ).toInt())
                }
            }

            view.pivotX = (if (position < 0) 0 else view.width).toFloat()
            view.scaleX = if (position < 0) 1f + position else 1f - position
        }
    }
}