package com.narztiizzer.carousel

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager

class CarouselView: ViewPager {
    var fragmentManager: FragmentManager? = null
    var carouselItemChangeListener: CarouselItemChangeListener? = null

    private val carouselAdapter: CarouselAdapter by lazy { CarouselAdapter(this.fragmentManager!!) }

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = 0
        var tempHeightMeasureSpec = heightMeasureSpec
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > height) height = h
        }

        if (height != 0) {
            tempHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }

        super.onMeasure(widthMeasureSpec, tempHeightMeasureSpec)
        super.onMeasure(widthMeasureSpec, tempHeightMeasureSpec)
    }

    fun setCarouselItemLayout(resId: Int){ this.carouselAdapter.setCarouselItemLayout(resId) }

    fun setDataSize(size: Int){ this.carouselAdapter.setPagerSize(size) }

    fun setCarouselItemListener(listener: CarouselItemListener) { this.carouselAdapter.setCarouselItemListener(listener) }

    fun setInfinite(isInfinite: Boolean){ this.carouselAdapter.setInfinite(isInfinite) }

    fun apply(){
        this.adapter = this.carouselAdapter
        this.setPageTransformer(false, this.carouselAdapter)
        this.currentItem = this.calculateStartItem()
        this.offscreenPageLimit = 3
        this.pageMargin = -this.context.resources.getInteger(R.integer.carousel_page_margin)

        addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) { }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) { }

            override fun onPageSelected(position: Int) {
                this@CarouselView.carouselItemChangeListener?.onCarouselItemChanged(this@CarouselView.calculateSelectedItem(position))
            }

        })
    }

    private fun calculateStartItem(): Int = if(this.carouselAdapter.isInfinite()) (this.carouselAdapter.getPagerSize() * CarouselAdapter.LOOPS) / 2 else 0

    private fun calculateSelectedItem(position: Int): Int = if(this.carouselAdapter.isInfinite()) position % this.carouselAdapter.getPagerSize() else position
}