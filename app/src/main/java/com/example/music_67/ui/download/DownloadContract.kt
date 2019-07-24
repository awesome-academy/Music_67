package com.example.music_67.ui.download

interface DownloadContract {
	interface View {
		fun loadTracksSuccess()
		fun loadTracksFail()
	}

	interface Presenter {
		fun loadTracks()
	}
}
