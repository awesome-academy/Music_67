package vn.sunasterisk.music_67.ui.detailplaying

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail_playing.*
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.service.ACTION_CREATE
import vn.sunasterisk.music_67.service.PlayingSet
import vn.sunasterisk.music_67.service.PlayingTracksService
import vn.sunasterisk.music_67.service.TrackStateListener
import vn.sunasterisk.music_67.utils.TimeConvert
import vn.sunasterisk.music_67.utils.TrackAttributes

private const val MSG_HANDLER = 1

class DetailPlayingActivity : AppCompatActivity(), View.OnClickListener, TrackStateListener,
		SeekBar.OnSeekBarChangeListener {
	private lateinit var playingTracksService: PlayingTracksService
	lateinit var handler: Handler
	private lateinit var thread: Thread
	private var isBound = true
	private val serviceConnection = object : ServiceConnection {
		override fun onServiceDisconnected(name: ComponentName?) {
			isBound = false
		}

		override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
			val binder = service as PlayingTracksService.LocalBinder
			playingTracksService = binder.getServive()
			getState()
			updateState()
			isBound = true
			playingTracksService.addListener(this@DetailPlayingActivity)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_detail_playing)
		getContentIntent()
		playingTracksService = PlayingTracksService()
		registerListener()
	}

	override fun onStart() {
		super.onStart()
		val playSongIntent = Intent(this, PlayingTracksService::class.java)
		playSongIntent.apply {
			action = ACTION_CREATE
		}
		startService(playSongIntent)
		bindService(playSongIntent, serviceConnection, Context.BIND_AUTO_CREATE)
		pauseToPlay()
		PlayingSet.isStartActivity = true
	}

	override fun onDestroy() {
		super.onDestroy()
		playingTracksService.removeListener(this)
		unbindService(serviceConnection)
	}

	override fun onClick(v: View?) {
		when (v) {
			imageBefore -> onBackPressed()
			imagePlay -> {
				if (playingTracksService.isPlaying()) {
					playingTracksService.pause()
					playToPause()
				} else {
					playingTracksService.start()
					pauseToPlay()
				}
			}
			imageNext -> {
				playingTracksService.next()
				currentTrack()
			}
			imagePrevious -> {
				val previousTrack = playingTracksService.getPreviousTrack()
				playingTracksService.previous()
				updateUI(previousTrack)
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

	private fun registerListener() {
		imagePlay.setOnClickListener(this)
		imageNext.setOnClickListener(this)
		imagePrevious.setOnClickListener(this)
		seekBarPlaying.setOnSeekBarChangeListener(this)
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
		getState()
		updateState()
	}

	private fun getContentIntent() {
		val trackReceived = intent.getParcelableExtra(TrackAttributes.TRACK) as Track
		getUI(trackReceived)
	}

	private fun getUI(track: Track) {
		textTitle.text = track.title
		textArtist.text = track.artist
		textDuration.text = TimeConvert.convertMilisecondToFormatTime(track.duration!!)
		textCurrentTime.text = TimeConvert.convertMilisecondToFormatTime(0)
		Glide.with(applicationContext)
				.load(track.artworkUrl)
				.fallback(R.drawable.icon_app)
				.error(R.drawable.icon_app)
				.into(imageBackground)
		Glide.with(applicationContext)
				.load(track.artworkUrl)
				.fallback(R.drawable.icon_app)
				.error(R.drawable.icon_app)
				.apply(RequestOptions.circleCropTransform())
				.into(imageCenter)
	}

	private fun currentTrack() {
		val currentTrack = playingTracksService.getCurrentTrack()
		updateUI(currentTrack)
	}

	private fun pauseToPlay() {
		imagePlay.setImageResource(R.drawable.ic_pause)
	}

	private fun playToPause() {
		imagePlay.setImageResource(R.drawable.ic_play_arrow)
	}
}
