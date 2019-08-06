package vn.sunasterisk.music_67.ui.search

import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.data.source.TracksDataSource
import vn.sunasterisk.music_67.data.source.TracksRepository
import vn.sunasterisk.music_67.service.PlayingSet
import vn.sunasterisk.music_67.utils.StringUtils

class SearchPresenter(
    private val repository: TracksRepository,
    private val view: SearchContract.View
) : SearchContract.Presenter {
    val tracks: List<Track> = ArrayList()


    override fun searchTracks(text: String, action: String) {
        when (action) {
            ACTION_SEARCH_FROM_HOME -> handleSearchHome(text)
            ACTION_SEARCH_FROM_NOW -> handleSearchNow(text)
        }
    }

    private fun handleSearchHome(text: String) {
        val api = StringUtils.generateSearchApi(text)
        repository.searchRemoteTracks(api, object : TracksDataSource.LoadTracksCallback {
            override fun onLoadTracksSuccess(loadingTracks: List<Track>) {
                (tracks as ArrayList).addAll(loadingTracks)
                view.searchSuccess()
            }

            override fun onLoadTracksFail() {
                if (tracks.isEmpty()) view.searchFail(ERROR_NOT_FOUND)
            }
        })
    }

    private fun handleSearchNow(text: String) {
        for (i in 0 until PlayingSet.playingSet.size) {
            if (PlayingSet.playingSet.elementAt(i).title!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                (tracks as ArrayList).add(PlayingSet.playingSet.elementAt(i))
            }
        }
        if (tracks.isEmpty()) view.searchFail(ERROR_NOT_FOUND)
        view.searchSuccess()
    }

    companion object {
        const val ERROR_NOT_FOUND = "vn.sunasterisk.music_67.NO_TRACK_FOUND"
    }
}
