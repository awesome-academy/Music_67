package vn.sunasterisk.music_67.ui.genres

import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_genre.*
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.base.BaseActivity
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.data.source.TracksRepository
import vn.sunasterisk.music_67.data.source.local.LocalDataSource
import vn.sunasterisk.music_67.data.source.remote.RemoteDataSource
import vn.sunasterisk.music_67.service.PlayingSet
import vn.sunasterisk.music_67.ui.bottomsheetfragment.MenuFragment
import vn.sunasterisk.music_67.ui.detailplaying.DetailPlayingActivity
import vn.sunasterisk.music_67.utils.TrackAttributes

const val NAME_GENRE = "vn.sunasterisk.music_67.NAME_GENRE"
const val IMAGE_GENRE = "vn.sunasterisk.music_67.IMAGE_GENRE"
const val DEFAULT_VALUE = 1

class GenreActivity : BaseActivity(), GenresContract.View, View.OnClickListener {
	private lateinit var genrePresent: GenrePresent
	private lateinit var trackAdapter: TrackAdapter
	private lateinit var tracksRepository: TracksRepository

	private val trackClickListener = object : TrackClickListener {
		override fun onTrackClicked(position: Int) {
			val track = genrePresent.tracks[position]
			if (PlayingSet.playingSet.isNotEmpty()) {
				PlayingSet.currentPosition = PlayingSet.playingSet.size
			} else {
				PlayingSet.currentPosition++
			}
			if (!(track in PlayingSet.playingSet))
				PlayingSet.playingSet.add(track)
			else PlayingSet.currentPosition = PlayingSet.playingSet.indexOf(track)
			startActivity(getDetailIntent(track))
		}

		override fun onMoreClicked(position: Int) {
			val track = genrePresent.tracks[position]
			val menuFragment = MenuFragment.newInstance(track)
			menuFragment.show(supportFragmentManager, MENU_FRAGMENT_TAG)
		}
	}

	override fun onStart() {
		super.onStart()
		getIntentContent()
		initMiniPlay(R.id.frameMiniPlaying)
		tracksRepository = TracksRepository.getInstance(
				RemoteDataSource.getInstance(),
				LocalDataSource.getInstance()
		)
		genrePresent = GenrePresent(tracksRepository, this)
		trackAdapter = TrackAdapter(
				this,
				genrePresent.tracks as ArrayList<Track>,
				trackClickListener
		)
		recyclerTracks.adapter = trackAdapter
		genrePresent.loadTracks()
	}

	override fun onClick(v: View?) {
		when (v) {
			imageBefore -> onBackPressed()
			buttonPlayAll -> {
				PlayingSet.playingSet.addAll(genrePresent.tracks)
				firstTrackClicked()
			}
		}
	}

	override fun getContentViewId(): Int = R.layout.activity_genre


	override fun showMiniPlaying(isTrackPlaying: Boolean) {
		if (isTrackPlaying) frameMiniPlaying.visibility = View.VISIBLE
		else frameMiniPlaying.visibility = View.GONE
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
					.load(imageGenre)
					.into(imageBackground)
			Glide.with(this)
					.load(imageGenre)
					.into(imageCenter)
		}
	}

	override fun getNameGenre() = intent.getStringExtra(NAME_GENRE)
	private fun getDetailIntent(track: Track): Intent {
		val detailIntent = Intent(this, DetailPlayingActivity::class.java)
		detailIntent.apply {
			putExtra(TrackAttributes.TRACK, track)
		}
		return detailIntent
	}

	private fun firstTrackClicked() {
		val track = genrePresent.tracks[0]
		PlayingSet.currentPosition = 0
		startActivity(getDetailIntent(track))
	}

	companion object {
		private const val MENU_FRAGMENT_TAG = "vn.sunasterisk.music_67.MENU_FRAGMENT"
	}
}
