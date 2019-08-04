package vn.sunasterisk.music_67.ui.bottomsheetfragment

import android.animation.ObjectAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_mini_playing.*
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.base.BaseActivity
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.service.ACTION_START_FROM_NOTIFICATION
import vn.sunasterisk.music_67.service.PlayingTracksService
import vn.sunasterisk.music_67.service.TrackStateListener
import vn.sunasterisk.music_67.ui.detailplaying.DetailPlayingActivity
import vn.sunasterisk.music_67.utils.TrackAttributes

class MiniPlaying : Fragment(), View.OnClickListener, TrackStateListener {
	private lateinit var playingTracksService: PlayingTracksService
	private lateinit var objectAnimator: ObjectAnimator
	private val serviceConnection = object : ServiceConnection {
		override fun onServiceDisconnected(name: ComponentName?) {
		}

		override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
			val binder = service as PlayingTracksService.LocalBinder
			playingTracksService = binder.getServive()
			playingTracksService.addListener(this@MiniPlaying)
			if (playingTracksService.isPlaying()) playListener()
			else pauseListener()
		}
	}

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_mini_playing, container, false)
		playingTracksService = PlayingTracksService()
		val playSongIntent = Intent(context, PlayingTracksService::class.java)
		context!!.bindService(playSongIntent, serviceConnection, Context.BIND_AUTO_CREATE)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		registerListener()
		createRotation()
	}

	override fun onDestroy() {
		super.onDestroy()
		playingTracksService.removeListener(this)
		context!!.unbindService(serviceConnection)
	}

	override fun completeListener() {
		currentTrack()
	}

	override fun playListener() {
		if (objectAnimator.isPaused)
			objectAnimator.resume()
		else objectAnimator.start()
		pauseToPlay()
	}

	override fun startListener() {
		getUI(playingTracksService.getCurrentTrack())
		(activity as BaseActivity).showMiniPlaying(true)
		if (playingTracksService.isPlaying()) {
			pauseToPlay()
			objectAnimator.start()
		} else playToPause()
	}

	override fun pauseListener() {
		if(objectAnimator.isRunning)
			objectAnimator.pause()
		playToPause()
	}

	override fun onClick(v: View?) {
		when (v) {
			imagePlay -> handlePlay()
			imageNext -> handleNext()
			imagePrevious -> handlePrevious()
			else -> handleOtherClicked()
		}
	}

	private fun currentTrack() {
		getUI(playingTracksService.getCurrentTrack())
	}

	private fun getUI(track: Track) {
		track.title?.let { textTitle.text = track.title }
		track.artist?.let { textArtist.text = track.artist }
		Glide.with(this)
				.load(track.artworkUrl)
				.fallback(R.drawable.icon_app)
				.error(R.drawable.icon_app)
				.apply(RequestOptions.circleCropTransform())
				.into(imageTrack)
	}

	private fun registerListener() {
		imagePlay.setOnClickListener(this)
		imagePrevious.setOnClickListener(this)
		imageNext.setOnClickListener(this)
		textTitle.setOnClickListener(this)
		textArtist.setOnClickListener(this)
		imageTrack.setOnClickListener(this)
	}

	private fun pauseToPlay() {
		imagePlay.setImageResource(R.drawable.ic_pause)
	}

	private fun playToPause() {
		imagePlay.setImageResource(R.drawable.ic_play_arrow)
	}

	private fun createRotation() {
		objectAnimator = ObjectAnimator.ofFloat(imageTrack, View.ROTATION, START_ANGLE, END_ANGLE)
		objectAnimator.apply {
			duration = DURATION_ROTATION
			repeatCount = Animation.INFINITE
			interpolator = LinearInterpolator()
		}
	}

	private fun handlePlay() {
		if (::playingTracksService.isInitialized) {
			if (playingTracksService.isPlaying())
				playingTracksService.pause()
			else playingTracksService.start()
		}
	}

	private fun handleNext() {
		playingTracksService.next()
		currentTrack()
		if (objectAnimator.isRunning)
			objectAnimator.end()
	}

	private fun handlePrevious() {
		playingTracksService.previous()
		currentTrack()
		if (objectAnimator.isRunning)
			objectAnimator.end()
	}

	private fun handleOtherClicked() {
		val detailIntent = Intent(context, DetailPlayingActivity::class.java).apply {
			putExtra(TrackAttributes.TRACK, playingTracksService.getCurrentTrack())
			action = ACTION_START_FROM_NOTIFICATION
			flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
		}
		startActivity(detailIntent)
	}

	companion object {
		private const val DURATION_ROTATION = 10000L
		private const val START_ANGLE = 0f
		private const val END_ANGLE = 360f
	}
}
