package vn.sunasterisk.music_67.ui.nowplaying

import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.service.PlayingSet

class NowPlayingPresenter(private val view: NowPlayingContract.View) : NowPlayingContract.Presenter {
	val tracks: List<Track> = ArrayList()
	override fun loadTracks() {
		(tracks as ArrayList).addAll(PlayingSet.playingSet)
		view.loadTracksSuccess()
	}
}
