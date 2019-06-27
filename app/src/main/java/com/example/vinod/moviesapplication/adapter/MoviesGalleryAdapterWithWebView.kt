package com.example.vinod.moviesapplication.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.vinod.moviesapplication.R
import com.example.vinod.moviesapplication.model.Movies
import com.example.vinod.moviesapplication.utils.MyWebView
import com.example.vinod.moviesapplication.utils.orDefaultInt

class MoviesGalleryAdapterWithWebView(
  private val moviesList: List<Movies>?, private val iActivityCommunicator: IActivityCommunicator
) : RecyclerView.Adapter<MoviesGalleryAdapterWithWebView.MyViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_my_web, parent, false)
    return MyViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    iActivityCommunicator.onClick(position)
    holder.myWebView.load(moviesList?.get(position)?.image.orEmpty())
  }

  override fun getItemCount(): Int {
    return moviesList?.size.orDefaultInt()
  }

  inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var myWebView: MyWebView = itemView.findViewById(R.id.myWebView)
  }

  interface IActivityCommunicator {
    fun onClick(position: Int)
  }
}
