package vn.sunasterisk.music_67.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import vn.sunasterisk.music_67.data.model.Track

const val ACTION_CREATE = "vn.sunasterisk.music_67.CREATE"
const val ACTION_PLAY_PAUSE = "vn.sunasterisk.music_67.PLAY_PAUSE"
const val ACTION_NEXT = "vn.sunasterisk.music_67.NEXT"
const val ACTION_PREVIOUS = "vn.sunasterisk.music_67.PREVIOUS"

class PlayingTracksService : Service(), PlayingTracksInterface, MediaPlayer.OnPreparedListener,
		MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
	private lateinit var localBinder: LocalBinder
	private lateinit var playingTracksManager: PlayingTracksManager
	private val listeners = ArrayList<TrackStateListener>()

	inner class LocalBinder : Binder() {
		fun getServive(): PlayingTracksService = this@PlayingTracksService
	}

	override fun onBind(intent: Intent): IBinder {
		return localBinder
	}

	override fun onCreate() {
		super.onCreate()
		localBinder = LocalBinder()
		playingTracksManager = PlayingTracksManager.getInstance(this)
	}

	override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
		when (intent.action) {
			ACTION_CREATE -> {
				if (playingTracksManager.isPlaying()) {
					playingTracksManager.stop()
				}
				create(playingTracksManager.getCurrentTrack())
			}
			ACTION_PLAY_PAUSE -> {
				when (isPlaying()) {
					true -> pause()
					else -> start()
				}
			}
			ACTION_NEXT -> next()
			ACTION_PREVIOUS -> previous()
		}
		return START_STICKY
	}

	override fun onUnbind(intent: Intent?): Boolean {
		return true
	}

	override fun onDestroy() {
		playingTracksManager.release()
		super.onDestroy()
	}

	override fun create(track: Track) {
		playingTracksManager.create(track)
	}

	override fun start() {
		playingTracksManager.start()
	}

	override fun pause() {
		playingTracksManager.pause()
	}

	override fun next() {
		playingTracksManager.next()
	}

	override fun previous() {
		playingTracksManager.previous()
	}

	override fun stop() {
		playingTracksManager.stop()
	}

	override fun release() {
		playingTracksManager.release()
	}

	override fun seek(position: Int) {
		playingTracksManager.seek(position)

	}

	override fun getNextTrack() = playingTracksManager.getNextTrack()
	override fun getPreviousTrack() = playingTracksManager.getPreviousTrack()
	override fun getDuration() = playingTracksManager.getDuration()
	override fun getCurrentPosition() = playingTracksManager.getCurrentPosition()
	override fun isPlaying(): Boolean = playingTracksManager.isPlaying()
	override fun onPrepared(mp: MediaPlayer?) {
		start()
		if (!PlayingSet.isStartActivity)
			sendToListeners()
		PlayingSet.isStartActivity = false
	}

	override fun onCompletion(mp: MediaPlayer?) {
		if (PlayingSet.playingSet.size <= 1) {
			stopSelf()
		} else {
			next()
		}
	}

	override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
		return true
	}

	fun getCurrentTrack() = playingTracksManager.getCurrentTrack()

	fun addListener(listener: TrackStateListener) {
		listeners.add(listener)
	}

	fun removeListener(listener: TrackStateListener) {
		listeners.remove(listener)
	}

	fun sendToListeners() {
		for (i in 0 until listeners.size) {
			listeners[i].completeListener()
		}
	}
}