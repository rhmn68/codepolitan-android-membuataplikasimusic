package com.codepolitan.musicapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepolitan.musicapp.databinding.ItemAlbumBinding
import com.codepolitan.musicapp.models.Album

class TopAlbumsAdapter: RecyclerView.Adapter<TopAlbumsAdapter.ViewHolder>() {

    private var albums = mutableListOf<Album>()
    private var listener: ((Album) -> Unit)? = null

    class ViewHolder(private val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindItem(album: Album, listener: ((Album) -> Unit)?) {
            Glide.with(itemView).load(album.imageAlbum).placeholder(android.R.color.darker_gray).into(binding.ivAlbum)
            binding.tvNameAlbum.text = album.nameAlbum
            binding.tvArtistAlbum.text = album.artistAlbum

            itemView.setOnClickListener {
                if (listener != null){
                    listener(album)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(albums[position], listener)
    }

    override fun getItemCount(): Int = albums.size

    fun onClick(listener: ((Album) -> Unit)){
        this.listener = listener
    }

    fun setData(albums: MutableList<Album>){
        this.albums = albums
        notifyDataSetChanged()
    }
}