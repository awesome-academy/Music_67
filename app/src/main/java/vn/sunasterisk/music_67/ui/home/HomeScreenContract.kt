package vn.sunasterisk.music_67.ui.home

interface HomeScreenContract {
	interface View {
		fun loadGenresSuccess()
		fun loadGenresFail()
		fun loadDownloadedTracksSuccess()
		fun loadDownloadedTracksFail()
		fun getStringResource(id: Int): String
	}

	interface Presenter {
		fun loadGenres()
		fun loadDownloadedTracks()
	}
}
