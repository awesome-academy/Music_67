package com.example.music_67.data.source.local

import com.example.music_67.data.model.Track
import com.example.music_67.data.source.TracksDataSource

class LocalDataSource : TracksDataSource.Local {
	override fun getDownloadTracks(callback: TracksDataSource.LoadTracksCallback) {

	}

	override fun searchDownloadTracks(searchString: String, callback: TracksDataSource.LoadTracksCallback) {

	}

	override fun removeDownloadTracks(track: Track, callback: TracksDataSource.LoadTracksCallback) {

	}

	companion object {
		private var INSTANCE: LocalDataSource? = null
		@JvmStatic
		fun getInstance(): LocalDataSource {
			if (INSTANCE == null) {
				synchronized(LocalDataSource::javaClass) {
					INSTANCE = LocalDataSource()
				}
			}
			return INSTANCE!!
		}

		fun clearInstance() {
			INSTANCE = null
		}
	}
}
