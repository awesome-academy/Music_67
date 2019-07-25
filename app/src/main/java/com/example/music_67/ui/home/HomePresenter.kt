package com.example.music_67.ui.home

import com.example.music_67.R
import com.example.music_67.data.model.Genre

class HomePresenter(private val view: HomeScreenContract.View) : HomeScreenContract.Presenter {
	val genres: List<Genre> = ArrayList()
	override fun loadGenres() {
		addGenres()
		view.loadDownloadedTracksSuccess()
	}

	override fun loadDownloadedTracks() {

	}

	private fun addGenres() {
		(genres as ArrayList).add(Genre(view.getStringResource(R.string.title_all_music),
				R.drawable.all_music_genre))
		genres.add(Genre(view.getStringResource(R.string.title_all_audio),
				R.drawable.all_audio_genre))
		genres.add(Genre(view.getStringResource(R.string.title_alternative_rock),
				R.drawable.alternative_rock_genre))
		genres.add(Genre(view.getStringResource(R.string.title_ambient),
				R.drawable.ambient_genre))
		genres.add(Genre(view.getStringResource(R.string.title_country),
				R.drawable.country_genre))
		genres.add(Genre(view.getStringResource(R.string.title_classical),
				R.drawable.classical_genre))
	}
}
