package com.example.music_67.ui.genres

import android.view.View
import com.bumptech.glide.Glide
import com.example.music_67.R
import com.example.music_67.base.BaseActivity
import com.example.music_67.data.model.Track
import com.example.music_67.data.source.TracksRepository
import com.example.music_67.data.source.local.LocalDataSource
import com.example.music_67.data.source.remote.RemoteDataSource
import kotlinx.android.synthetic.main.activity_genre.*

const val NAME_GENRE = "com.example.music_67.NAME_GENRE"
const val IMAGE_GENRE = "com.example.music_67.IMAGE_GENRE"
const val DEFAULT_VALUE = 1

class GenreActivity : BaseActivity(), GenresContract.View {
	private lateinit var present: GenrePresent
	private lateinit var trackAdapter: TrackAdapter
	private lateinit var repository: TracksRepository
	override fun onStart() {
		super.onStart()
		getIntentContent()
		repository = TracksRepository.getInstance(RemoteDataSource.getInstance(),
				LocalDataSource.getInstance())
		present = GenrePresent(repository, this)
		trackAdapter = TrackAdapter(this, present.tracks as ArrayList<Track>)
		recyclerTracks.adapter = trackAdapter
		present.loadTracks()
	}

	override fun getContentViewId(): Int = R.layout.activity_genre
	override fun onTrackClicked(view: View) {

	}

	override fun showMiniPlaying(isTrackPlaying: Boolean, viewId: Int) {

	}

	override fun loadTracksSuccess() {
		recyclerTracks.visibility = View.VISIBLE
		progressLoadData.visibility = View.INVISIBLE
		trackAdapter.notifyDataSetChanged()
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

	override fun getNameGenre() = intent.getStringExtra(NAME_GENRE)
}
