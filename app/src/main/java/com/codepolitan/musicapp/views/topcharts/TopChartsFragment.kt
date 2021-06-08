package com.codepolitan.musicapp.views.topcharts

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codepolitan.musicapp.adapter.TopChartsAdapter
import com.codepolitan.musicapp.databinding.FragmentTopChartsBinding
import com.codepolitan.musicapp.models.Song
import com.codepolitan.musicapp.repository.Repository
import com.codepolitan.musicapp.utils.hide
import com.codepolitan.musicapp.utils.visible
import com.codepolitan.musicapp.views.playsong.PlaySongActivity
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.startActivity

class TopChartsFragment : Fragment() {

    private var _binding: FragmentTopChartsBinding? = null
    private val topChartsBinding get() = _binding
    private lateinit var topChartsAdapter: TopChartsAdapter
    private lateinit var databaseTopCharts: DatabaseReference

    private val eventListenerTopCharts = object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            Log.d("TopChartsFragment", "[onDataChange] ${snapshot.value}")
            val gson = Gson().toJson(snapshot.value)
            val type = object : TypeToken<MutableList<Song>>(){}.type
            val songs = Gson().fromJson<MutableList<Song>>(gson, type)

            if (songs != null)
                topChartsAdapter.setData(songs)
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("TopChartsFragment", "[onCancelled] ${error.message}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopChartsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Init
        topChartsAdapter = TopChartsAdapter()
        databaseTopCharts = FirebaseDatabase.getInstance().getReference("top_charts")

        swipeTopCharts()
        onClick()

        showLoading()
        showTopCharts()
    }

    private fun onClick() {
        topChartsAdapter.onClick { songs, position ->
            context?.startActivity<PlaySongActivity>(
                PlaySongActivity.KEY_SONGS to songs,
                PlaySongActivity.KEY_POSITION to position
            )
        }
    }

    private fun swipeTopCharts() {
        topChartsBinding?.swipeTopCharts?.setOnRefreshListener {
            showTopCharts()
        }
    }

    private fun showLoading() {
        topChartsBinding?.swipeTopCharts?.visible()
    }

    private fun hideLoading() {
        topChartsBinding?.swipeTopCharts?.hide()
    }

    private fun showTopCharts(){
        //GetData
//        val topCharts = Repository.getDataTopChartsFromAssets(context)
//        From Firebase
        databaseTopCharts.addValueEventListener(eventListenerTopCharts)

        //SetupRecyclerView
        topChartsBinding?.rvTopCharts?.adapter = topChartsAdapter
    }
}