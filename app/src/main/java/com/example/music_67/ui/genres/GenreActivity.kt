package com.example.music_67.ui.genres

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.example.music_67.R
import com.example.music_67.base.BaseActivity
import kotlinx.android.synthetic.main.activity_genre.*

const val NAME_GENRE = "com.example.music_67.NAME_GENRE"
const val IMAGE_GENRE = "com.example.music_67.IMAGE_GENRE"
const val DEFAULT_VALUE = 1

class GenreActivity : BaseActivity(), GenresContract.View {
	override fun onStart() {
		super.onStart()
		getIntentContent()
	}

	override fun getContentViewId(): Int = R.layout.activity_genre
	override fun onTrackClicked(view: View) {

	}

	override fun showMiniPlaying(isTrackPlaying: Boolean, viewId: Int) {

	}

	override fun loadTracksSuccess() {

	}

	override fun loadTracksFail() {

	}

	private fun getIntentContent() {
		if (intent.hasExtra(NAME_GENRE) && intent.hasExtra(IMAGE_GENRE)) {
			val nameGenre = intent.getStringExtra(NAME_GENRE)
			val imageGenre = intent.getIntExtra(IMAGE_GENRE, DEFAULT_VALUE)
			textNameGenre.text = nameGenre
			Glide.with(this)
					.asBitmap()
					.load(imageGenre)
					.into(imageBackground)
			Glide.with(this)
					.asBitmap()
					.load(imageGenre)
					.into(imageCenter)
		}
	}
}
