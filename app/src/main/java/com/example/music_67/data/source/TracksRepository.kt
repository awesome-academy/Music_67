package com.example.music_67.data.source

import com.example.music_67.data.model.Track

class TracksRepository(
		val remote: TracksDataSource.Remote,
		val local: TracksDataSource.Local
) : TracksDataSource.Local, TracksDataSource.Remote {

	override fun getDownloadTracks(callback: TracksDataSource.LoadTracksCallback) {

	}

	override fun searchDownloadTracks(searchString: String, callback: TracksDataSource.LoadTracksCallback) {

	}

	override fun removeDownloadTracks(track: Track, callback: TracksDataSource.LoadTracksCallback) {

	}

	override fun getRemoteTracks(api: String, callback: TracksDataSource.LoadTracksCallback) {
		remote.getRemoteTracks(api, callback)
	}

	override fun searchRemoteTracks(api: String, callback: TracksDataSource.LoadTracksCallback) {
		remote.searchRemoteTracks(api, callback)
	}

	companion object {
		private var INSTANCE: TracksRepository? = null
		@JvmStatic
		fun getInstance(
				remote: TracksDataSource.Remote,
				local: TracksDataSource.Local
		): TracksRepository {
			return INSTANCE ?: TracksRepository(remote, local)
					.apply { INSTANCE = this }
		}

		@JvmStatic
		fun destroyInstance() {
			INSTANCE = null
		}
	}
}
