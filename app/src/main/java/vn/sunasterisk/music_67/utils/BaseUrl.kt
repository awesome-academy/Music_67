package vn.sunasterisk.music_67.utils

object BaseUrl {
	const val BASE_URL_GENRE =
			"https://api-v2.soundcloud.com/charts?kind=%s&genre=%s&client_id=%s&limit=%d&offset=%d"
	const val BASE_URL_STREAM = "https://api.soundcloud.com/tracks/%d/stream?client_id=%s"
	const val BASE_URL_DOWNLOAD = "https://api.soundcloud.com/tracks/%d/download?client_id=%s"
	const val BASE_URL_SEARCH = "https://api.soundcloud.com/tracks?q=%s&client_id=%s"
}
