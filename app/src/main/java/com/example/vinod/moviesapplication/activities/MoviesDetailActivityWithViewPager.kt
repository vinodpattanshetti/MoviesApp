package com.example.vinod.moviesapplication.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.example.vinod.moviesapplication.R
import com.example.vinod.moviesapplication.adapter.MoviesGalleryAdapterWithViewPager
import com.example.vinod.moviesapplication.databinding.ActivityMoviesDetailBinding
import com.example.vinod.moviesapplication.model.Movies
import com.example.vinod.moviesapplication.utils.Constants.ITEM_POSITION_KEY
import com.example.vinod.moviesapplication.utils.Constants.MOVIES_LIST_KEY
import com.example.vinod.moviesapplication.utils.orDefaultInt

class MoviesDetailActivityWithViewPager : AppCompatActivity(), ViewPager.OnPageChangeListener {
  private lateinit var mBinder: ActivityMoviesDetailBinding
  private var mAdapter: MoviesGalleryAdapterWithViewPager? = null
  private var mMoviesList: List<Movies>? = null
  private var currPosition: Int? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mBinder = DataBindingUtil.setContentView(
      this, R.layout.activity_movies_detail
    )
    setToolbar()
    mMoviesList = intent.getSerializableExtra(MOVIES_LIST_KEY) as ArrayList<Movies>?
    currPosition = intent?.getIntExtra(ITEM_POSITION_KEY, 0)
    bindData()
  }

  private fun setToolbar() {
    mBinder.tbToolbar.run {
      setNavigationOnClickListener {
        onBackPressed()
      }
      title = getString(R.string.txt_movies_list_with_view_pager)
      setTitleTextColor(
        ContextCompat.getColor(
          this@MoviesDetailActivityWithViewPager, R.color.color_white
        )
      )
    }
  }

  private fun bindData() {
    mAdapter = mMoviesList?.let { MoviesGalleryAdapterWithViewPager(it) }
    mBinder.viewPager.adapter = mAdapter
    mBinder.viewPager.currentItem = currPosition.orDefaultInt()
    mBinder.viewPager.addOnPageChangeListener(this@MoviesDetailActivityWithViewPager)
    updateImageIndicatorNumber(mBinder.viewPager.currentItem)
    mBinder.tvPrevious.setOnClickListener {
      mBinder.viewPager.currentItem = mBinder.viewPager.currentItem.dec().orDefaultInt()
      updateImageIndicatorNumber(mBinder.viewPager.currentItem)
    }
    mBinder.tvNext.setOnClickListener {
      mBinder.viewPager.currentItem = mBinder.viewPager.currentItem.inc().orDefaultInt()
      updateImageIndicatorNumber(mBinder.viewPager.currentItem)
    }
  }

  private fun updateImageIndicatorNumber(currPosition: Int) {
    mBinder.tvImageNumber.text = "${currPosition + 1} / ${mAdapter?.count}"
  }

  override fun onPageSelected(position: Int) {
    updateImageIndicatorNumber(position)
  }

  override fun onPageScrollStateChanged(p0: Int) {
    //Implementation isn't required
  }

  override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    //Implementation isn't required
  }

  override fun onBackPressed() {
    super.onBackPressed()
  }
}