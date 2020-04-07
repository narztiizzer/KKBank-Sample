package com.narztiizzer.sample.kkbank

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.narztiizzer.carousel.CarouselItemChangeListener
import com.narztiizzer.carousel.CarouselItemListener
import com.narztiizzer.sample.kkbank.utils.LoadingDialogFragment
import com.narztiizzer.sample.kkbank.viewmodel.VMHome
import kotlinx.android.synthetic.main.home_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity(), CarouselItemListener, CarouselItemChangeListener {
    private val icons = listOf(R.drawable.bee, R.drawable.cat, R.drawable.dog, R.drawable.fish, R.drawable.iguana, R.drawable.lion)
    private val viewModel: VMHome by viewModel()
    private val loadingDialogFragment: LoadingDialogFragment by lazy { LoadingDialogFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        this.initialValue()
        this.initialAction()
        this.initialObserver()
    }

    override fun onCarouselItemCreated(view: View, position: Int) {
        viewModel.loadCarousel.value?.get(position)?.imageURL?.let {
            Glide
                .with(this)
                .load(it)
                .into(view.findViewById(com.narztiizzer.carousel.R.id.image))
        }
    }

    override fun onCarouselItemChanged(position: Int) {
        indicator.animatePageSelected(position)
    }

    private fun initialValue(){
        viewModel.getCarouselItems()
    }

    private fun initialAction(){
        logout.setOnClickListener { viewModel.logout() }
    }

    private fun initialObserver() {
        viewModel.loadingDialog.observe(this, Observer { isShow ->
            if(isShow) loadingDialogFragment.show(supportFragmentManager, "LOADING")
            else loadingDialogFragment.dismissAllowingStateLoss()
        })

        viewModel.logoutSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) finish()
        })

        viewModel.errorMessage.observe(this, Observer {
            Snackbar.make(container, it, Snackbar.LENGTH_SHORT).apply {
                setAction("OK"){ this.dismiss() }
            }.show()
        })

        viewModel.loadCarousel.observe(this, Observer { data ->
            indicator.createIndicators(data.size,0)
            carousel.apply {
                this.fragmentManager = supportFragmentManager
                this.carouselItemChangeListener = this@HomeActivity

                setDataSize(data.size)
                setCarouselItemLayout(R.layout.carousel_item)
                setCarouselItemListener(this@HomeActivity)
                apply()
            }
        })
    }
}
