package com.example.music_67.ui.genres

interface GenresContract {
	interface View {
		fun loadTracksSuccess()
		fun loadTracksFail()
	}

	interface Presenter {
		fun loadTracks()
	}
}
