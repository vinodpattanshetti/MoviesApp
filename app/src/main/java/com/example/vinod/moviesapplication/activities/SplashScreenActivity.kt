package com.example.vinod.moviesapplication.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.vinod.moviesapplication.R
import com.example.vinod.moviesapplication.databinding.ActivityMainBinding
import com.example.vinod.moviesapplication.utils.MyBounceInterpolator

class SplashScreenActivity : AppCompatActivity() {

  private var mAnimUpDown: Animation? = null
  private lateinit var mBinder : ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mBinder = DataBindingUtil.setContentView(this,
      R.layout.activity_main
    )
    initAnimation()
    openHomeScreenImage()
  }

  private fun initAnimation() {
    mAnimUpDown = AnimationUtils.loadAnimation(this,
      R.anim.rotate_animation
    )
    val mInterpolator = MyBounceInterpolator(0.2, 10.0)
    mAnimUpDown?.interpolator = mInterpolator
    mBinder.ivImage.startAnimation(mAnimUpDown)
  }

  private fun openHomeScreenImage() {
    val splashTimeOut = 2000
    Handler().postDelayed({
      val i = Intent(this@SplashScreenActivity, HomeScreenActivity::class.java)
      startActivity(i)
      mAnimUpDown?.cancel()
      finish()
    }, splashTimeOut.toLong())
  }
}
