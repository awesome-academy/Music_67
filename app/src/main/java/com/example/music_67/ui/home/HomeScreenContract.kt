package com.example.music_67.ui.home

interface HomeScreenContract {
	interface View {
		fun loadGenresSuccess()
		fun loadGenresFail()
		fun loadDownloadedTracksSuccess()
		fun loadDownloadedTracksFail()
	}

	interface Presenter {
		fun loadGenres()
		fun loadDownloadedTracks()
	}
}
