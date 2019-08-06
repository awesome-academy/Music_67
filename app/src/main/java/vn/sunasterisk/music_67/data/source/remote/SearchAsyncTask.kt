package vn.sunasterisk.music_67.data.source.remote

import org.json.JSONArray
import vn.sunasterisk.music_67.base.BaseAsyncTask
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.data.source.TracksDataSource
import vn.sunasterisk.music_67.utils.StringUtils
import vn.sunasterisk.music_67.utils.TrackAttributes
import java.io.IOException

class SearchAsyncTask(private val callback: TracksDataSource.LoadTracksCallback) : BaseAsyncTask<Track>() {
	override fun convertJsonToObject(response: String): List<Track> {
		val tracks: List<Track> = ArrayList()
		try {
			val collections = JSONArray(response)
			for (i in 0 until collections.length()) {
				val trackObject = collections.getJSONObject(i)
				val id = trackObject.getInt(TrackAttributes.ID)
				val title = trackObject.getString(TrackAttributes.TITLE)
				val artworkUrl = trackObject.getString(TrackAttributes.ARTWORK_URL)
				val duration = trackObject.getLong(TrackAttributes.DURATION)
				val downloadable = trackObject.getBoolean(TrackAttributes.DOWNLOADABLE)
				var downloadUrl = ""
				if (!trackObject.isNull(TrackAttributes.DOWNLOAD_URL)) {
					downloadUrl = trackObject.getString(TrackAttributes.DOWNLOAD_URL)
				}
				val streamUrl = StringUtils.generateStreamApi(id)
				val username = trackObject.getJSONObject(TrackAttributes.USER).getString(TrackAttributes.USERNAME)
				val track = Track(id, title, artworkUrl, duration, downloadable, downloadUrl, streamUrl, username, username)
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
