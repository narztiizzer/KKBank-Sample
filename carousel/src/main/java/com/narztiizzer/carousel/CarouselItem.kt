package com.narztiizzer.carousel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

internal class CarouselItem: Fragment() {
    private val LAYOUT_KEY = "LAYOUT"
    private val POSITION_KEY = "POSITION"

    private var carouselItemListener: CarouselItemListener? = null
    private var layoutResourceId: Int = 0
    private var index: Int = 0

    companion object {
        fun newInstance(resId: Int, index: Int, carouselItemListener: CarouselItemListener?): CarouselItem {
            return CarouselItem().apply {
                setIndex(index)
                setLayoutResourceId(resId)
                setCarouselItemListener(carouselItemListener)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(LAYOUT_KEY, this.layoutResourceId)
        outState.putInt(POSITION_KEY, this.index)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.initialValue()
        this.restoreState(savedInstanceState)
        return inflater.inflate(this.layoutResourceId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.carouselItemListener?.onCarouselItemCreated(view, index)
    }

    internal fun setIndex(index: Int) { this.index = index }
    internal fun setLayoutResourceId(resId: Int) { this.layoutResourceId = resId }
    internal fun setCarouselItemListener(listener: CarouselItemListener?){ this.carouselItemListener = listener }

    private fun initialValue(){
        if(context is CarouselItemListener) this.carouselItemListener = context as CarouselItemListener
    }

    private fun restoreState(savedInstanceState: Bundle?){
        savedInstanceState?.let{
            this.index = savedInstanceState.getInt(POSITION_KEY, this.index)
            this.layoutResourceId = savedInstanceState.getInt(LAYOUT_KEY, this.layoutResourceId)
        }
    }
}