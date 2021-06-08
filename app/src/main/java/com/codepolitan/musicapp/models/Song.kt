package com.codepolitan.musicapp.models

import android.os.Parcelable
import com.google.firebase.database.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(

	@field:SerializedName("album_name_song")
	@get:PropertyName("album_name_song")
	@set:PropertyName("album_name_song")
	var albumNameSong: String? = null,

	@field:SerializedName("name_song")
	@get:PropertyName("name_song")
	@set:PropertyName("name_song")
	var nameSong: String? = null,

	@field:SerializedName("year_song")
	@get:PropertyName("year_song")
	@set:PropertyName("year_song")
	var yearSong: Int? = null,

	@field:SerializedName("artist_song")
	@get:PropertyName("artist_song")
	@set:PropertyName("artist_song")
	var artistSong: String? = null,

	@field:SerializedName("uri_song")
	@get:PropertyName("uri_song")
	@set:PropertyName("uri_song")
	var uriSong: String? = null,

	@field:SerializedName("image_song")
	@get:PropertyName("image_song")
	@set:PropertyName("image_song")
	var imageSong: String? = null,

	@field:SerializedName("key_song")
	@get:PropertyName("key_song")
	@set:PropertyName("key_song")
	var keySong: String? = null
) : Parcelable