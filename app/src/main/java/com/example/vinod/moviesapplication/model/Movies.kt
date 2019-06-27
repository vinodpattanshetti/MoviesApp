package com.example.vinod.moviesapplication.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import io.reactivex.annotations.NonNull
import java.io.Serializable

@Entity(tableName = "Movies") data class Movies(
  @PrimaryKey(autoGenerate = true) @NonNull @SerializedName("id") var id: Int = 0,
  @SerializedName("name") var name: String = "", @SerializedName("image") var image: String? = null,
  @SerializedName("year") var year: String? = null, @SerializedName("rating")
  var rating: String? = null, @SerializedName("genre") var genre: String? = null
) : Serializable
