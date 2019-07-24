package com.example.music_67.ui.nowplaying

interface NowPlayingContract {
	interface View {
		fun loadTracksSuccess()
		fun loadTracksFail()
	}

	interface Presenter {
		fun loadTracks()
	}
}
