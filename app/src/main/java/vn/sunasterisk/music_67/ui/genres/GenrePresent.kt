package vn.sunasterisk.music_67.ui.genres

import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.data.source.TracksDataSource
import vn.sunasterisk.music_67.data.source.TracksRepository
import vn.sunasterisk.music_67.utils.GenreType
import vn.sunasterisk.music_67.utils.StringUtils

class GenrePresent(private val repository: TracksRepository, val view: GenresContract.View)
	: GenresContract.Presenter {
	val tracks: List<Track> = ArrayList()
	override fun loadTracks() {
		val keyGenre = generateKeyGenre()
		val api = StringUtils.generateGenresApi(KIND, keyGenre, LIMIT, OFFSET)
		repository.getRemoteTracks(api, object : TracksDataSource.LoadTracksCallback {
			override fun onLoadTracksSuccess(loadingTracks: List<Track>) {
				(tracks as ArrayList).addAll(loadingTracks)
				view.loadTracksSuccess()
			}

			override fun onLoadTracksFail() {

			}
		})
	}

	private fun generateKeyGenre(): String {
		return when (view.getNameGenre()) {
			GenreType.ALL_MUSIC -> GenreType.KEY_ALL_MUSIC
			GenreType.ALL_AUDIO -> GenreType.KEY_ALL_AUDIO
			GenreType.ALTERNATIVE_ROCK -> GenreType.KEY_ALTERNATIVE_ROCK
			GenreType.AMBIENT -> GenreType.KEY_AMBIENT
			GenreType.CLASSICAL -> GenreType.KEY_CLASSICAL
			GenreType.COUNTRY -> GenreType.KEY_COUNTRY
			else -> ""
		}
	}

	companion object {
		private const val LIMIT = 100
		private const val OFFSET = 0
		private const val KIND = "top"
	}
}
