package com.example.vinod.moviesapplication.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.vinod.moviesapplication.R
import com.example.vinod.moviesapplication.adapter.MoviesGalleryAdapterWithWebView
import com.example.vinod.moviesapplication.databinding.ActivityMoviesDetail1Binding
import com.example.vinod.moviesapplication.model.Movies
import com.example.vinod.moviesapplication.utils.Constants.ITEM_POSITION_KEY
import com.example.vinod.moviesapplication.utils.Constants.MOVIES_LIST_KEY
import com.example.vinod.moviesapplication.utils.Constants.MOVIES_LIST_SIZE
import com.example.vinod.moviesapplication.utils.orDefaultInt

class MoviesDetailActivityWithWebView : AppCompatActivity(), MoviesGalleryAdapterWithWebView.IActivityCommunicator {
  private lateinit var mBinder: ActivityMoviesDetail1Binding
  private var mAdapter: MoviesGalleryAdapterWithWebView? = null
  private var mMoviesList: List<Movies>? = null
  private var currPosition: Int? = null
  private var mLayoutManager: LinearLayoutManager? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mBinder = DataBindingUtil.setContentView(
      this, R.layout.activity_movies_detail1
    )

    setToolbar()
    mMoviesList = intent.getSerializableExtra(MOVIES_LIST_KEY) as ArrayList<Movies>?
    currPosition = intent?.getIntExtra(ITEM_POSITION_KEY, 0)
    mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
    bindData()
  }

  private fun setToolbar() {
    mBinder.tbToolbar.run {
      setNavigationOnClickListener {
        onBackPressed()
      }
      title = getString(R.string.txt_movies_list_with_web_view)
      setTitleTextColor(
        ContextCompat.getColor(
          this@MoviesDetailActivityWithWebView, R.color.color_white
        )
      )
    }
  }

  private fun bindData() {
    mAdapter = MoviesGalleryAdapterWithWebView(
      mMoviesList,
      this
    )
    mBinder.rvMovieGallery.layoutManager =
        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    mBinder.rvMovieGallery.adapter = mAdapter
    mBinder.rvMovieGallery.scrollToPosition(currPosition.orDefaultInt())
    mBinder.tvNext.setOnClickListener {
      if (15 > currPosition.orDefaultInt()) {
        currPosition = currPosition?.plus(1)
        mBinder.rvMovieGallery.scrollToPosition(currPosition.orDefaultInt())
        mAdapter?.notifyDataSetChanged()
      }
      updateImageIndicatorNumber(currPosition.orDefaultInt())
    }
    mBinder.tvPrevious.setOnClickListener {
      if (currPosition.orDefaultInt() > 0) {
        currPosition = currPosition?.minus(1)
        mBinder.rvMovieGallery.scrollToPosition(currPosition.orDefaultInt())
        mAdapter?.notifyDataSetChanged()
      }
      updateImageIndicatorNumber(currPosition.orDefaultInt())
    }
  }

  private fun updateImageIndicatorNumber(currPosition: Int) {
    mBinder.tvImageNumber.text = "${currPosition + 1} / $MOVIES_LIST_SIZE"
  }

  override fun onClick(position: Int) {
    this.currPosition = position
    updateImageIndicatorNumber(currPosition.orDefaultInt())
  }

  override fun onBackPressed() {
    super.onBackPressed()
  }
}
