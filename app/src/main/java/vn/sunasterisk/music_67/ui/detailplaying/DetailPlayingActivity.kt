package vn.sunasterisk.music_67.ui.detailplaying

import android.animation.ObjectAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail_playing.*
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.service.ACTION_CREATE
import vn.sunasterisk.music_67.service.ACTION_START_FROM_NOTIFICATION
import vn.sunasterisk.music_67.service.PlayingTracksService
import vn.sunasterisk.music_67.service.TrackStateListener
import vn.sunasterisk.music_67.utils.*

class DetailPlayingActivity : AppCompatActivity(), View.OnClickListener, TrackStateListener,
		SeekBar.OnSeekBarChangeListener {
	private lateinit var playingTracksService: PlayingTracksService
	private lateinit var handler: Handler
	private lateinit var thread: Thread
	private lateinit var objectAnimator: ObjectAnimator
	private val serviceConnection = object : ServiceConnection {
		override fun onServiceDisconnected(name: ComponentName?) {
		}

		override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
			val binder = service as PlayingTracksService.LocalBinder
			playingTracksService = binder.getServive()
			updateState()
			getState()
			updateShuffle()
			updateLoop()
			playingTracksService.addListener(this@DetailPlayingActivity)
			if (playingTracksService.isPlaying()) playListener()
			else pauseListener()
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_detail_playing)
		getContentIntent()
		playingTracksService = PlayingTracksService()
		registerListener()
		createRotation()
	}

	override fun onStart() {
		super.onStart()
		val playSongIntent = Intent(this, PlayingTracksService::class.java).apply {
			action = ACTION_CREATE
		}
		if (intent.action != ACTION_START_FROM_NOTIFICATION)
			startService(playSongIntent)
		bindService(playSongIntent, serviceConnection, Context.BIND_AUTO_CREATE)
	}

	override fun onDestroy() {
		super.onDestroy()
		playingTracksService.removeListener(this)
		unbindService(serviceConnection)
	}

	override fun onClick(v: View?) {
		when (v) {
			imageBefore -> onBackPressed()
			imagePlay -> handlePlay()
			imageNext -> handleNext()
			imagePrevious -> handlePrevious()
			imageShuffle -> {
				handleShuffle()
				updateShuffle()
			}
			imageLoop -> {
				handleLoop()
				updateLoop()
			}
		}
	}

	override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

	}

	override fun onStartTrackingTouch(seekBar: SeekBar?) {
		playToPause()
	}

	override fun onStopTrackingTouch(seekBar: SeekBar?) {
		playingTracksService.seek(seekBar!!.progress)
		pauseToPlay()
	}

	override fun completeListener() {
		currentTrack()
	}

	override fun startListener() {
		objectAnimator.start()
		pauseToPlay()
		enableClick()
	}

	override fun pauseListener() {
		if(objectAnimator.isRunning)
			objectAnimator.pause()
		pauseToPlay()
	}

	override fun playListener() {
		if (objectAnimator.isPaused)
			objectAnimator.resume()
		else objectAnimator.start()
		playToPause()
	}

	private fun registerListener() {
		imagePlay.setOnClickListener(this)
		imageNext.setOnClickListener(this)
		imagePrevious.setOnClickListener(this)
		seekBarPlaying.setOnSeekBarChangeListener(this)
		imageShuffle.setOnClickListener(this)
		imageLoop.setOnClickListener(this)
	}

	private fun handleShuffle() {
		when (playingTracksService.getShuffleType()) {
			ShuffleType.YES -> playingTracksService.shuffleOff()
			ShuffleType.NO -> playingTracksService.shuffleOn()
		}
	}

	private fun handleLoop() {
		when (playingTracksService.getLoopType()) {
			LoopType.NO -> playingTracksService.loopAll()
			LoopType.ALL -> playingTracksService.loopOne()
			LoopType.ONE -> playingTracksService.loopNon()
		}
	}

	private fun updateState() {
		handler = Handler(Looper.getMainLooper()) {
			when (it.what) {
				MSG_HANDLER -> {
					seekBarPlaying.progress = it.arg1
					textCurrentTime.text = TimeConvert.convertMilisecondToFormatTime(it.arg1.toLong())
					if (it.arg2 == 1) {
						pauseToPlay()
					} else {
						playToPause()
					}
					return@Handler true
				}
				else -> return@Handler false
			}
		}
	}

	private fun getState() {
		thread = Thread(Runnable {
			val total = playingTracksService.getDuration().toInt()
			seekBarPlaying.max = total
			var cur = playingTracksService.getCurrentPosition()
			while (cur < total) {
				val message = Message()
				message.apply {
					what = MSG_HANDLER
					arg1 = cur
					when (playingTracksService.isPlaying()) {
						true -> arg2 = 1
						false -> arg2 = 0
					}
				}
				handler.sendMessage(message)
				try {
					Thread.sleep(500)
					cur = playingTracksService.getCurrentPosition()
				} catch (e: InterruptedException) {
					e.printStackTrace()
				}
			}
		})
		thread.start()
	}

	private fun updateUI(track: Track) {
		getUI(track)
		pauseToPlay()
		updateState()
		getState()
	}

	private fun getContentIntent() {
		val trackReceived = intent.getParcelableExtra(TrackAttributes.TRACK) as Track
		getUI(trackReceived)
	}

	private fun getUI(track: Track) {
		track.title?.let { textTitle.text = track.title }
		track.artist?.let { textArtist.text = track.artist }
		track.duration?.let {
			textDuration.text = TimeConvert.convertMilisecondToFormatTime(track.duration)
		}
		textCurrentTime.text = TimeConvert.convertMilisecondToFormatTime(0)
		Glide.with(applicationContext)
				.load(track.artworkUrl)
				.fallback(R.drawable.icon_app)
				.error(R.drawable.icon_app)
				.into(imageBackground)
		Glide.with(applicationContext)
				.load(StringUtils.getBigArtwork(track.artworkUrl!!))
				.fallback(R.drawable.icon_app)
				.error(R.drawable.icon_app)
				.apply(RequestOptions.circleCropTransform())
				.into(imageCenter)
		track.downloadable?.let {
			if (!track.downloadable) imageDownload.visibility = View.GONE
		}
		disableClick()
	}

	private fun currentTrack() {
		updateUI(playingTracksService.getCurrentTrack())
	}

	private fun createRotation() {
		objectAnimator = ObjectAnimator.ofFloat(imageCenter, View.ROTATION, START_ANGLE, END_ANGLE)
		objectAnimator.apply {
			duration = DURATION_ROTATION
			repeatCount = Animation.INFINITE
			interpolator = LinearInterpolator()
		}
	}

	private fun updateShuffle() {
		when (playingTracksService.getShuffleType()) {
			ShuffleType.YES -> imageShuffle.setColorFilter(ActivityCompat.getColor(this, R.color.color_primary))
			ShuffleType.NO -> imageShuffle.setColorFilter(ActivityCompat.getColor(this, R.color.color_black))
		}
	}

	private fun updateLoop() {
		when (playingTracksService.getLoopType()) {
			LoopType.NO -> {
				imageLoop.setImageResource(R.drawable.ic_repeat)
				imageLoop.setColorFilter(ActivityCompat.getColor(this, R.color.color_black))
			}
			LoopType.ONE -> {
				imageLoop.setImageResource(R.drawable.ic_repeat_one)
				imageLoop.setColorFilter(ActivityCompat.getColor(this, R.color.color_primary))
			}
			LoopType.ALL -> {
				imageLoop.setImageResource(R.drawable.ic_repeat)
				imageLoop.setColorFilter(ActivityCompat.getColor(this, R.color.color_primary))
			}
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

	private fun pauseToPlay() {
		imagePlay.setImageResource(R.drawable.ic_pause)
	}

	private fun playToPause() {
		imagePlay.setImageResource(R.drawable.ic_play_arrow)
	}

	private fun disableClick() {
		imagePlay.isClickable = false
		imagePrevious.isClickable = false
		imageNext.isClickable = false
	}

	private fun enableClick() {
		imagePlay.isClickable = true
		imagePrevious.isClickable = true
		imageNext.isClickable = true
	}

	companion object {
		private const val MSG_HANDLER = 1
		private const val DURATION_ROTATION = 10000L
		private const val START_ANGLE = 0f
		private const val END_ANGLE = 360f
	}
}
