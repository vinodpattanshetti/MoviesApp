package com.example.vinod.moviesapplication.model

import com.google.gson.annotations.SerializedName
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import io.reactivex.annotations.NonNull
import java.io.Serializable

@Entity(tableName = "MoviesList") data class MoviesList(
  @PrimaryKey(autoGenerate = true) @NonNull @SerializedName("id") var id: Int = 0,
  @SerializedName("movies_list") var moviesList: List<Movies>? = null) : Serializable
