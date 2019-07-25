package com.example.music_67.utils

import com.example.music_67.BuildConfig

object StringUtils {
	fun generateGenresApi(kind: String, genres: String, limit: Int, offset: Int) =
			String.format(BaseUrl.BASE_URL_GENRE, kind, genres, BuildConfig.CLIENT_ID, limit, offset)

	fun generateStreamApi(trackId: Int) =
			String.format(BaseUrl.BASE_URL_STREAM, trackId, BuildConfig.CLIENT_ID)

	fun generateSearchApi(searchString: String) =
			String.format(BaseUrl.BASE_URL_SEARCH, searchString, BuildConfig.CLIENT_ID)

	fun generateDownloadApi(trackId: Int) =
			String.format(BaseUrl.BASE_URL_DOWNLOAD, trackId, BuildConfig.CLIENT_ID)
}
