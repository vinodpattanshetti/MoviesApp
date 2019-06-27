package com.example.vinod.moviesapplication.room_database

import android.arch.persistence.room.TypeConverter
import com.example.vinod.moviesapplication.model.Movies
import com.example.vinod.moviesapplication.model.MoviesList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {

  @TypeConverter
  fun fromMoviesList(value: MoviesList?): String? {
    val  gson = Gson()
    val type = object : TypeToken<MoviesList?>(){}.type
    return gson.toJson(value, type)
  }

  @TypeConverter
  fun toMoviesList(value: String?): MoviesList? {
    val gson = Gson()
    val type = object : TypeToken<MoviesList?>() {}.type
    return gson.fromJson(value, type)
  }

  @TypeConverter
  fun fromMovies(value: ArrayList<Movies>?): String? {
    val  gson = Gson()
    val type = object : TypeToken<ArrayList<Movies>?>(){}.type
    return gson.toJson(value, type)
  }

  @TypeConverter
  fun toMovies(value: String?): ArrayList<Movies>? {
    val gson = Gson()
    val type = object : TypeToken<ArrayList<Movies>?>() {}.type
    return gson.fromJson(value, type)
  }

}