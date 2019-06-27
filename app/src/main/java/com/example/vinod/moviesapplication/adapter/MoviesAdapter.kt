package com.example.vinod.moviesapplication.adapter

import java.util.ArrayList

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinod.moviesapplication.R
import com.example.vinod.moviesapplication.model.Movies

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.example.vinod.moviesapplication.utils.orDefaultInt

class MoviesAdapter(
  private val movies: List<Movies>, private val mIActivityCommunicator: IActivityCommunicator
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>(), Filterable {

  private var moviesFiltered: List<Movies>? = null

  init {
    this.moviesFiltered = movies
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
    return MovieViewHolder(view)
  }

  override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
    moviesFiltered?.get(position)?.let { holder.bind(it, position) }
  }

  override fun getItemCount(): Int {
    return moviesFiltered?.size.orDefaultInt()
  }

  override fun getFilter(): Filter {
    return object : Filter() {
      override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
        val charString = charSequence.toString()
        if (charString.isEmpty()) {
          moviesFiltered = movies
        } else {
          val filteredList = ArrayList<Movies>()
          for (row in movies) {

            // name match condition. this might differ depending on your requirement
            // here we are looking for name or phone number match
            if (row.name.toLowerCase().contains(charString.toLowerCase())) {
              filteredList.add(row)
            }
          }

          moviesFiltered = filteredList
        }

        val filterResults = Filter.FilterResults()
        filterResults.values = moviesFiltered
        return filterResults
      }

      override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
        moviesFiltered = filterResults.values as ArrayList<Movies>
        notifyDataSetChanged()
      }
    }
  }

  inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var releaseDate: TextView = itemView.findViewById(R.id.item_movie_release_date)
    private var title: TextView = itemView.findViewById(R.id.item_movie_title)
    private var rating: TextView = itemView.findViewById(R.id.item_movie_rating)
    private var genre: TextView = itemView.findViewById(R.id.item_movie_genre)
    private var mImageView: ImageView = itemView.findViewById(R.id.iv_image_view)

    fun bind(movies: Movies, position: Int) {
      releaseDate.text = movies.year
      title.text = movies.name
      rating.text = movies.rating.toString()
      genre.text = movies.genre
      Glide.with(mImageView.context).load(movies.image)
        .apply(RequestOptions.placeholderOf(R.color.colorPrimary)).into(mImageView)
      itemView.setOnClickListener { mIActivityCommunicator.onItemSelected(movies, position) }
    }
  }

  interface IActivityCommunicator {
    fun onItemSelected(movies: Movies, position: Int)
  }

}
