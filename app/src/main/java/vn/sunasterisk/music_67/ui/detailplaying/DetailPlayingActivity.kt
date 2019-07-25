package vn.sunasterisk.music_67.ui.detailplaying

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.ui.genres.DEFAULT_VALUE
import vn.sunasterisk.music_67.utils.TimeConvert
import vn.sunasterisk.music_67.utils.TrackAttributes
import kotlinx.android.synthetic.main.activity_detail_playing.*

class DetailPlayingActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_detail_playing)
		getContentIntent()
	}

	private fun getContentIntent() {
		val trackTitle = intent.getStringExtra(TrackAttributes.TITLE)
		val trackArtist = intent.getStringExtra(TrackAttributes.ARTIST)
		val durationTrack = intent.getLongExtra(TrackAttributes.DURATION, DEFAULT_VALUE.toLong())
		val imageTrack = intent.getStringExtra(TrackAttributes.ARTWORK_URL)
		textTitle.text = trackTitle
		textArtist.text = trackArtist
		textDuration.text = TimeConvert.convertMilisecondToFormatTime(durationTrack)
		textCurrentTime.text = TimeConvert.convertMilisecondToFormatTime(0)
		Glide.with(this)
				.load(imageTrack)
				.error(R.drawable.icon_app)
				.into(imageBackground)
		Glide.with(this)
				.load(imageTrack)
				.error(R.drawable.icon_app)
				.apply(RequestOptions.circleCropTransform())
				.into(imageCenter)
	}
}
