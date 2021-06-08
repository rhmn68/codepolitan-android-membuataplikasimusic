package com.codepolitan.musicapp.views.topalbums

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codepolitan.musicapp.adapter.TopAlbumsAdapter
import com.codepolitan.musicapp.databinding.FragmentTopAlbumsBinding
import com.codepolitan.musicapp.models.Album
import com.codepolitan.musicapp.repository.Repository
import com.codepolitan.musicapp.utils.hide
import com.codepolitan.musicapp.utils.visible
import com.codepolitan.musicapp.views.detailalbum.DetailAlbumActivity
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.startActivity

class TopAlbumsFragment : Fragment() {

    private var _binding: FragmentTopAlbumsBinding? = null
    private val topAlbumsBinding get() = _binding
    private lateinit var topAlbumsAdapter: TopAlbumsAdapter
    private lateinit var databaseTopAlbums: DatabaseReference

    private val eventListenerTopAlbums = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            Log.d("TopAlbumsFragment", "[onDataChange-snapshot] ${snapshot.value}")
            val gson = Gson().toJson(snapshot.value)
            Log.d("TopAlbumsFragment", "[onDataChange-gson] $gson")
            val type = object : TypeToken<MutableList<Album>>(){}.type
            val albums = Gson().fromJson<MutableList<Album>>(gson, type)

            if (albums != null)
                topAlbumsAdapter.setData(albums)
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("TopAlbumsFragment", "[onCancelled] ${error.message}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopAlbumsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Init
        topAlbumsAdapter = TopAlbumsAdapter()
        databaseTopAlbums = FirebaseDatabase.getInstance().getReference("top_albums")

        swipeTopAlbums()
        onClick()

        showLoading()
        showTopAlbums()
    }

    private fun onClick() {
        topAlbumsAdapter.onClick { album ->
            context?.startActivity<DetailAlbumActivity>(DetailAlbumActivity.KEY_ALBUM to album)
        }
    }

    private fun swipeTopAlbums() {
        topAlbumsBinding?.swipeTopAlbums?.setOnRefreshListener {
            showTopAlbums()
        }
    }

    private fun showLoading() {
        topAlbumsBinding?.swipeTopAlbums?.visible()
    }

    private fun hideLoading() {
        topAlbumsBinding?.swipeTopAlbums?.hide()
    }

    private fun showTopAlbums(){
        //GetData
//        val topAlbums = Repository.getDataTopAlbumsFromAssets(context)
        //Data from Firebase
        databaseTopAlbums.addValueEventListener(eventListenerTopAlbums)
        //SetupRecyclerView
        topAlbumsBinding?.rvTopAlbums?.adapter = topAlbumsAdapter
    }
}