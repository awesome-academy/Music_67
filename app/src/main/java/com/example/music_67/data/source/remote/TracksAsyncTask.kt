package com.example.music_67.data.source.remote

import com.example.music_67.base.BaseAsyncTask
import com.example.music_67.data.model.Track
import com.example.music_67.data.source.TracksDataSource
import com.example.music_67.utils.TrackAttributes
import org.json.JSONObject
import java.io.IOException

class TracksAsyncTask(private val callback: TracksDataSource.LoadTracksCallback) : BaseAsyncTask<Track>() {
	override fun convertJsonToObject(response: String): List<Track> {
		val tracks: List<Track> = ArrayList()
		try {
			val objectRoot = JSONObject(response)
			val collections = objectRoot.getJSONArray(TrackAttributes.COLLECTION)

			for (i in 0 until collections.length()) {
				val trackInfo = collections.getJSONObject(i)
				val trackObject = trackInfo.getJSONObject(trackInfo.getString(TrackAttributes.TRACK))
				val id = trackObject.getInt(TrackAttributes.ID)
				val title = trackObject.getString(TrackAttributes.TITLE)
				val artworkUrl = trackObject.getString(TrackAttributes.ARTWORK_URL)
				val duration = trackObject.getLong(TrackAttributes.DURATION)
				val downloadable = trackObject.getBoolean(TrackAttributes.DOWNLOADABLE)
				val downloadUrl = trackObject.getString(TrackAttributes.DOWNLOAD_URL)
				val streamUrl = trackObject.getString(TrackAttributes.STREAM_URL)
				val username = trackObject.getJSONObject(TrackAttributes.USER).getString(TrackAttributes.USERNAME)
				var artist = ""
				if (!trackObject.isNull(TrackAttributes.PUBLISHER_METADATA)
						&& !trackObject.getJSONObject(TrackAttributes.PUBLISHER_METADATA)
								.isNull(TrackAttributes.ARTIST)) {
					artist = trackObject.getJSONObject(TrackAttributes.PUBLISHER_METADATA)
							.getString(TrackAttributes.ARTIST)
				} else {
					artist = username
				}
				val track = Track(id, title, artworkUrl, duration, downloadable, downloadUrl, streamUrl, artist, username)
				(tracks as ArrayList).add(track)
			}
		} catch (e: IOException) {
			callback.onLoadTracksFail()
		}
		return tracks
	}

	override fun onPostExecute(result: List<Track>?) {
		super.onPostExecute(result)
		if (result == null) callback.onLoadTracksFail()
		else callback.onLoadTracksSuccess(result)
	}
}