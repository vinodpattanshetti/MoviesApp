package com.example.vinod.moviesapplication.repository

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
import android.util.Log
import com.example.vinod.moviesapplication.model.Movies
import com.example.vinod.moviesapplication.room_database.MoviesDatabase
import org.json.JSONException
import org.json.JSONObject
import rx.Completable
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import java.io.IOException

class MoviesRepository(private val mActivity: Activity) {

  private var mSubscriptions: CompositeSubscription = CompositeSubscription()
  private var mMoviesDatabase: MoviesDatabase? = null
  private val name = "movies_task"

  init {
    mMoviesDatabase =
        Room.databaseBuilder<MoviesDatabase>(mActivity, MoviesDatabase::class.java, name)
          .fallbackToDestructiveMigration().build()
  }

  val listOfMovies: ArrayList<Movies>
    get() = readDataFromJson()

  private fun loadJSONFromAsset(): String {
    val json: String
    try {
      val mFileName = mActivity.assets.open("movies.json")
      val size = mFileName.available()
      val buffer = ByteArray(size)
      mFileName.read(buffer)
      mFileName.close()
      json = String(buffer)
    } catch (ex: IOException) {
      return ""
    }

    return json
  }

  private fun readDataFromJson(): ArrayList<Movies> {
    var moviesList: MutableList<Movies> = mutableListOf()
    try {
      val obj = JSONObject(loadJSONFromAsset())
      val jsonArray = obj.getJSONArray("movies_list")
      moviesList = ArrayList()
      var movies: Movies

      for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        movies = Movies()
        movies.image = jsonObject.optString("image")
        movies.name = jsonObject.optString("name")
        movies.year = jsonObject.optString("year")
        movies.rating = jsonObject.optString("rating")
        movies.genre = jsonObject.optString("genre")
        moviesList.add(movies)
      }
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    return moviesList as ArrayList<Movies>
  }

  fun insertFacilityModel(mMoviesList: List<Movies>) {
    mSubscriptions.add(Completable.fromAction {
      mMoviesDatabase?.getIMoviesDao()?.insertMovie(mMoviesList)
    }.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
      Log.e("Database Add", "Added Data to Database Successfully")
    })
  }

  fun getMoviesList(): LiveData<List<Movies>>? {
    return mMoviesDatabase?.getIMoviesDao()?.fetchAllMovies()
  }

  fun deleteFromDatabase() {
    mSubscriptions.add(Completable.fromAction {
      mMoviesDatabase?.getIMoviesDao()?.deleteAll()
    }.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
      Log.e("Database Delete", "Database Successfully Deleted")
    })
  }


}
