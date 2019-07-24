package com.example.music_67.ui.search

interface SearchContract {
	interface View {
		fun searchSuccess()
		fun searchFail()
	}

	interface Presenter {
		fun searchTracks()
	}
}
