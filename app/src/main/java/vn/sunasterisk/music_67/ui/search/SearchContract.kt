package vn.sunasterisk.music_67.ui.search

interface SearchContract {
	interface View {
		fun searchSuccess()
		fun searchFail(error: String)
	}

	interface Presenter {
		fun searchTracks(text: String, action: String)
	}
}
