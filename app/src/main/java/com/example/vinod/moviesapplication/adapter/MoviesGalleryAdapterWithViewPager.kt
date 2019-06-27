package com.example.vinod.moviesapplication.adapter

import android.databinding.DataBindingUtil
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinod.moviesapplication.R
import com.example.vinod.moviesapplication.databinding.MoviesGallaryItemBinding
import com.example.vinod.moviesapplication.model.Movies

class MoviesGalleryAdapterWithViewPager(private val mMoviesList: List<Movies>) : PagerAdapter() {

  private lateinit var mBinder: MoviesGallaryItemBinding

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    mBinder = DataBindingUtil.inflate(
      LayoutInflater.from(container.context), R.layout.movies_gallary_item, container, false
    )
    mBinder.ivImageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
    Glide.with(mBinder.ivImageView.context).load(mMoviesList[position].image)
      .apply(RequestOptions.placeholderOf(R.color.colorPrimary)).into(mBinder.ivImageView)
    container.addView(mBinder.root)
    return mBinder.root
  }

  override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

  override fun getCount(): Int = mMoviesList.size

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    val view = `object` as View
    container.removeView(view)
  }


}