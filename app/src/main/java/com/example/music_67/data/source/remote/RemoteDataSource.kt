package com.example.music_67.data.source.remote

import com.example.music_67.data.source.TracksDataSource

class RemoteDataSource: TracksDataSource.Remote {

	override fun getRemoteTracks(api: String, callback: TracksDataSource.LoadTracksCallback) {
		TracksAsyncTask(callback).execute(api)
	}

	override fun searchRemoteTracks(api: String, callback: TracksDataSource.LoadTracksCallback) {
		TracksAsyncTask(callback).execute(api)
	}
}
