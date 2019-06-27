package com.example.vinod.moviesapplication.room_database

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.vinod.moviesapplication.model.Movies

@Dao
public interface MoviesDao {
  @Insert fun insertMovie(mMovieList: List<Movies>)

  @Query("SELECT * FROM Movies")
  fun fetchAllMovies(): LiveData<List<Movies>>

  @Query("DELETE from Movies")
  fun deleteAll()
}
