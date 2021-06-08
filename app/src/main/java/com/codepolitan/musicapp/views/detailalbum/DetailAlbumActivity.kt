package com.codepolitan.musicapp.views.detailalbum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.codepolitan.musicapp.R
import com.codepolitan.musicapp.adapter.SongsAlbumAdapter
import com.codepolitan.musicapp.adapter.TopAlbumsAdapter
import com.codepolitan.musicapp.databinding.ActivityDetailAlbumBinding
import com.codepolitan.musicapp.models.Album
import com.codepolitan.musicapp.models.Song
import com.codepolitan.musicapp.utils.hide
import com.codepolitan.musicapp.utils.visible
import com.codepolitan.musicapp.views.playsong.PlaySongActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class DetailAlbumActivity : AppCompatActivity() {

    companion object{
        const val KEY_ALBUM = "key_album"
    }
    private lateinit var detailAlbumBinding: ActivityDetailAlbumBinding
    private lateinit var songsAlbumsAdapter: SongsAlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailAlbumBinding = ActivityDetailAlbumBinding.inflate(layoutInflater)
        setContentView(detailAlbumBinding.root)

        init()
        showLoading()
        Handler(Looper.getMainLooper()).postDelayed({
            getData()
        }, 2000)
        onClick()
    }

    private fun getData() {
        if (intent != null){
            val album = intent.getParcelableExtra<Album>(KEY_ALBUM)
            if (album != null){
                hideLoading()
                initView(album)
            }else{
                hideLoading()
                toast("Data Null")
            }
        }
    }

    private fun initView(album: Album) {
        detailAlbumBinding.tvNameAlbum.text = album.nameAlbum
        detailAlbumBinding.tvArtistAlbum.text = album.artistAlbum
        detailAlbumBinding.tvReleaseAlbum.text = album.getAlbum()
        Glide.with(this)
            .load(album.imageAlbum)
            .placeholder(android.R.color.darker_gray)
            .into(detailAlbumBinding.ivDetailAlbum)

        showSongsAlbums(album.songs)
    }

    private fun showSongsAlbums(songs: List<Song>?) {
        if (songs != null){
            songsAlbumsAdapter.setData(songs as MutableList<Song>)
            detailAlbumBinding.rvDetailAlbum.adapter = songsAlbumsAdapter
        }else{
            toast("Data Null")
        }
    }

    private fun onClick() {
        detailAlbumBinding.tbDetailAlbum.setNavigationOnClickListener {
            finish()
        }

        songsAlbumsAdapter.onClick { songs, position ->
            startActivity<PlaySongActivity>(
                PlaySongActivity.KEY_SONGS to songs,
                PlaySongActivity.KEY_POSITION to position
            )
        }
    }

    private fun init() {
        //Set Support ActionBar
        setSupportActionBar(detailAlbumBinding.tbDetailAlbum)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        //Init Songs Album Adapter
        songsAlbumsAdapter = SongsAlbumAdapter()
    }

    private fun showLoading(){
        detailAlbumBinding.swipeDetailAlbum.visible()
    }

    private fun hideLoading(){
        detailAlbumBinding.swipeDetailAlbum.hide()
    }
}