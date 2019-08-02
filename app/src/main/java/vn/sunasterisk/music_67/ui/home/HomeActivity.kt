package vn.sunasterisk.music_67.ui.home

import android.support.v7.widget.DefaultItemAnimator
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.base.BaseActivity
import vn.sunasterisk.music_67.data.model.Genre

class HomeActivity : BaseActivity(), HomeScreenContract.View {
	private lateinit var homePresenter: HomePresenter
	private lateinit var genreAdapter: GenreAdapter
	override fun getContentViewId(): Int = R.layout.activity_home

	override fun onStart() {
		super.onStart()
		initMiniPlay(R.id.frameMiniPlaying)
		homePresenter = HomePresenter(this)
		genreAdapter = GenreAdapter(this, homePresenter.genres as ArrayList<Genre>)
		homePresenter.loadGenres()
		recyclerGenres.apply {
			adapter = genreAdapter
			itemAnimator = DefaultItemAnimator()
		}
	}

	override fun showMiniPlaying(isTrackPlaying: Boolean) {
		when (isTrackPlaying) {
			true -> frameMiniPlaying.visibility = View.VISIBLE
			else -> frameMiniPlaying.visibility = View.GONE
		}
	}

	override fun loadGenresSuccess() {
		genreAdapter.notifyDataSetChanged()
	}

	override fun loadGenresFail() {

	}

	override fun loadDownloadedTracksSuccess() {

	}

	override fun loadDownloadedTracksFail() {

	}

	override fun getStringResource(id: Int): String = this.getString(id)
}
