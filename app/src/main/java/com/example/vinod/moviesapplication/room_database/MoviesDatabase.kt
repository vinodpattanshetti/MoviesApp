package com.example.vinod.moviesapplication.room_database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.vinod.moviesapplication.model.Movies

@Database(entities = [Movies::class], version = 1, exportSchema = false)

@TypeConverters(DataConverter::class)
abstract class MoviesDatabase : RoomDatabase() {
  abstract fun getIMoviesDao(): MoviesDao
}