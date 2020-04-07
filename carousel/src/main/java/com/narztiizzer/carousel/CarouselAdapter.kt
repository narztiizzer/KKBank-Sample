package com.narztiizzer.carousel

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

internal class CarouselAdapter(fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT), ViewPager.PageTransformer {
    private var carouselItemListener: CarouselItemListener? = null
    private var itemLayout = R.layout.carousel_item_weapper
    private var isInfinite = true
    private var pagerSize = 0

    companion object {
        internal const val LOOPS = 1000
    }

    override fun getItem(position: Int): Fragment {
        val itemIndex = if(isInfinite) position % pagerSize else position
        return CarouselItem.newInstance(itemLayout, itemIndex, carouselItemListener)
    }

    override fun getCount(): Int = if(isInfinite) pagerSize * LOOPS else pagerSize

    override fun transformPage(page: View, position: Float) {
        page.apply {
            when {
                position < -1 -> { // [-Infinity,-1)
                    page.scaleX = CarouselConstant.BIG_SCALE * abs(position)
                    page.scaleY = CarouselConstant.BIG_SCALE  * abs(position)
                }
                position <= 1 -> { // [-1,1]
                    page.scaleX = 1.0F - .5F * abs(position)
                    page.scaleY = 1.0F - .5F * abs(position)
                }
                else -> { // (1,+Infinity]
                    page.scaleX = CarouselConstant.BIG_SCALE  * abs(position)
                    page.scaleY = CarouselConstant.BIG_SCALE  * abs(position)
                }
            }
        }
    }

    internal fun setInfinite(isInfinite: Boolean) { this.isInfinite = isInfinite }
    internal fun isInfinite() = this.isInfinite
    internal fun setCarouselItemLayout(layoutResId: Int){ this.itemLayout = layoutResId }
    internal fun setPagerSize(size: Int){ this.pagerSize = size }
    internal fun getPagerSize() = this.pagerSize
    internal fun setCarouselItemListener(listener: CarouselItemListener) { this.carouselItemListener = listener }
}