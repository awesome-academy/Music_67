package vn.sunasterisk.music_67.data.source

import vn.sunasterisk.music_67.data.model.Track

interface TracksDataSource {
	interface LoadTracksCallback {
		fun onLoadTracksSuccess(tracks: List<Track>)
		fun onLoadTracksFail()
	}

	interface Local {
		fun getDownloadTracks(callback: LoadTracksCallback)
		fun searchDownloadTracks(searchString: String, callback: LoadTracksCallback)
		fun removeDownloadTracks(track: Track, callback: LoadTracksCallback)
	}

	interface Remote {
		fun getRemoteTracks(api: String, callback: LoadTracksCallback)
		fun searchRemoteTracks(api: String, callback: LoadTracksCallback)
	}
}
