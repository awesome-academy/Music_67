package vn.sunasterisk.music_67.ui.search

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_search.*
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.base.BaseActivity
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.data.source.TracksRepository
import vn.sunasterisk.music_67.data.source.local.LocalDataSource
import vn.sunasterisk.music_67.data.source.remote.RemoteDataSource
import vn.sunasterisk.music_67.service.PlayingSet
import vn.sunasterisk.music_67.ui.bottomsheetfragment.MenuFragment
import vn.sunasterisk.music_67.ui.detailplaying.DetailPlayingActivity
import vn.sunasterisk.music_67.utils.ConnectivityCheck
import vn.sunasterisk.music_67.utils.TrackAttributes

const val ACTION_SEARCH_FROM_HOME = "vn.sunasterisk.music_67.ACTION_SEARCH_FROM_HOME"
const val ACTION_SEARCH_FROM_NOW = "vn.sunasterisk.music_67.ACTION_SEARCH_FROM_NOW"

class SearchActivity : BaseActivity(),
    SearchContract.View,
    SearchView.OnQueryTextListener,
    View.OnClickListener {
    private lateinit var searchPresenter: SearchPresenter
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var tracksRepository: TracksRepository
    private val trackClickListener = object : TrackClickListener {
        override fun onTrackClicked(position: Int) {
            val track = searchPresenter.tracks[position]
            if (PlayingSet.playingSet.isNotEmpty()) {
                PlayingSet.currentPosition = PlayingSet.playingSet.size
            } else {
                PlayingSet.currentPosition++
            }
            if (!(track in PlayingSet.playingSet))
                PlayingSet.playingSet.add(track)
            else PlayingSet.currentPosition = PlayingSet.playingSet.indexOf(track)
            startActivity(getDetailIntent(track))
        }

        override fun onMoreClicked(position: Int) {
            val track = searchPresenter.tracks[position]
            val menuFragment = MenuFragment.newInstance(track)
            menuFragment.show(supportFragmentManager, MENU_FRAGMENT_TAG)
        }
    }

    override fun onStart() {
        super.onStart()
        tracksRepository = TracksRepository.getInstance(
            RemoteDataSource.getInstance(),
            LocalDataSource.getInstance()
        )
        searchPresenter = SearchPresenter(tracksRepository, this)
        searchAdapter = SearchAdapter(
            this,
            searchPresenter.tracks as ArrayList<Track>,
            trackClickListener
        )
        recyclerTracks.adapter = searchAdapter
        searchView.apply {
            isActivated = true
            onActionViewExpanded()
            clearFocus()
            setOnQueryTextListener(this@SearchActivity)
        }
        imageBefore.setOnClickListener(this)
    }

    override fun getContentViewId() = R.layout.activity_search

    override fun showMiniPlaying(isTrackPlaying: Boolean) {

    }

    override fun searchSuccess() {
        searchAdapter.notifyDataSetChanged()
    }

    override fun searchFail(error: String) {
        Toast.makeText(this, getString(R.string.msg_empty_list), Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!ConnectivityCheck.isConnectedToNetwork(this))
            Toast.makeText(this, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show()
        else {
            if (newText.isNullOrEmpty())
                Toast.makeText(this, getString(R.string.msg_no_input), Toast.LENGTH_SHORT).show()
            else {
                searchAdapter.clearList()
                Handler().postDelayed({
                    searchPresenter.searchTracks(newText, intent.action)
                }, DELAY_MILLIS)
            }
        }
        return false
    }

    override fun onClick(v: View?) {
        when (v) {
            imageBefore -> onBackPressed()
        }
    }

    private fun getDetailIntent(track: Track): Intent {
        val detailIntent = Intent(this, DetailPlayingActivity::class.java).apply {
            putExtra(TrackAttributes.TRACK, track)
        }
        return detailIntent
    }

    companion object {
        private const val MENU_FRAGMENT_TAG = "vn.sunasterisk.music_67.MENU_FRAGMENT"
        private const val DELAY_MILLIS = 500L
        fun getIntent(context: Context, action: String?) = Intent(context, SearchActivity::class.java).apply {
            setAction(action)
        }
    }
}
