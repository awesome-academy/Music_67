package com.example.music_67.ui.genres

interface GenresContract {
	interface View {
		fun loadTracksSuccess()
		fun loadTracksFail()
		fun getNameGenre(): String
	}

	interface Presenter {
		fun loadTracks()
	}
}
