package com.codepolitan.musicapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codepolitan.musicapp.databinding.ItemSongBinding
import com.codepolitan.musicapp.models.Song

class TopChartsAdapter: RecyclerView.Adapter<TopChartsAdapter.ViewHolder>() {

    private var songs = mutableListOf<Song>()
    private var listener: ((MutableList<Song>, Int) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindItem(song: Song, listener: ((MutableList<Song>, Int) -> Unit)?) {
            binding.tvIndexSong.text = (adapterPosition + 1).toString()
            binding.tvArtistSong.text = song.artistSong
            binding.tvTitleSong.text = song.nameSong

            itemView.setOnClickListener {
                if (listener != null){
                    listener(songs, adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(songs[position], listener)
    }

    override fun getItemCount(): Int = songs.size

    fun onClick(listener: ((MutableList<Song>, Int) -> Unit)){
        this.listener = listener
    }

    fun setData(songs: MutableList<Song>){
        this.songs = songs
        notifyDataSetChanged()
    }
}