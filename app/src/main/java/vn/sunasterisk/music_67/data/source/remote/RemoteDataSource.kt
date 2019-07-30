package vn.sunasterisk.music_67.data.source.remote

import vn.sunasterisk.music_67.data.source.TracksDataSource

class RemoteDataSource : TracksDataSource.Remote {

	override fun getRemoteTracks(api: String, callback: TracksDataSource.LoadTracksCallback) {
		TracksAsyncTask(callback).execute(api)
	}

	override fun searchRemoteTracks(api: String, callback: TracksDataSource.LoadTracksCallback) {
		TracksAsyncTask(callback).execute(api)
	}

	companion object {
		private var INSTANCE: RemoteDataSource? = null
		@JvmStatic
		fun getInstance(): RemoteDataSource {
			if (INSTANCE == null) {
				synchronized(RemoteDataSource::javaClass) {
					INSTANCE = RemoteDataSource()
				}
			}
			return INSTANCE!!
		}

		fun clearInstance() {
			INSTANCE = null
		}
	}
}
