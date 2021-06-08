package com.codepolitan.musicapp.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(

	@field:SerializedName("name_album")
	@get:PropertyName("name_album")
	@set:PropertyName("name_album")
	var nameAlbum: String? = null,

	@field:SerializedName("songs")
	@get:PropertyName("songs")
	@set:PropertyName("songs")
	var songs: List<Song>? = null,

	@field:SerializedName("key_album")
	@get:PropertyName("key_album")
	@set:PropertyName("key_album")
	var keyAlbum: String? = null,

	@field:SerializedName("year_album")
	@get:PropertyName("year_album")
	@set:PropertyName("year_album")
	var yearAlbum: Int? = null,

	@field:SerializedName("image_album")
	@get:PropertyName("image_album")
	@set:PropertyName("image_album")
	var imageAlbum: String? = null,

	@field:SerializedName("artist_album")
	@get:PropertyName("artist_album")
	@set:PropertyName("artist_album")
	var artistAlbum: String? = null

) : Parcelable{
	@Exclude
	fun getAlbum(): String{
		return "Album - $yearAlbum"
	}
}