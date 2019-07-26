package com.example.music_67.utils

import android.support.annotation.StringDef

@StringDef(
		TrackAttributes.COLLECTION,
		TrackAttributes.TRACK,
		TrackAttributes.USER,
		TrackAttributes.URI,
		TrackAttributes.PUBLISHER_METADATA,
		TrackAttributes.ID,
		TrackAttributes.TITLE,
		TrackAttributes.ARTWORK_URL,
		TrackAttributes.DURATION,
		TrackAttributes.DOWNLOADABLE,
		TrackAttributes.DOWNLOAD_URL,
		TrackAttributes.STREAM_URL,
		TrackAttributes.ARTIST,
		TrackAttributes.USERNAME
)
annotation class TrackAttributes {
	companion object {
		const val COLLECTION = "collection"
		const val TRACK = "track"
		const val USER = "user"
		const val URI = "uri"
		const val PUBLISHER_METADATA = "publisher_metadata"
		const val ID = "id"
		const val TITLE = "title"
		const val ARTWORK_URL = "artwork_url"
		const val DURATION = "duration"
		const val DOWNLOADABLE = "downloadable"
		const val DOWNLOAD_URL = "download_url"
		const val STREAM_URL = "stream_url"
		const val ARTIST = "artist"
		const val USERNAME = "username"
	}
}
