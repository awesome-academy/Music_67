package vn.sunasterisk.music_67.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.NotificationTarget
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.ui.detailplaying.DetailPlayingActivity
import vn.sunasterisk.music_67.utils.LoopType
import vn.sunasterisk.music_67.utils.ShuffleType
import vn.sunasterisk.music_67.utils.TrackAttributes

const val ACTION_CREATE = "vn.sunasterisk.music_67.CREATE"
const val ACTION_PLAY_PAUSE = "vn.sunasterisk.music_67.PLAY_PAUSE"
const val ACTION_NEXT = "vn.sunasterisk.music_67.NEXT"
const val ACTION_PREVIOUS = "vn.sunasterisk.music_67.PREVIOUS"
const val ACTION_START_FROM_NOTIFICATION = "vn.sunasterisk.music_67.START_FROM_NOTIFICATION"

class PlayingTracksService : Service(), PlayingTracksInterface, MediaPlayer.OnPreparedListener,
		MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
	private lateinit var localBinder: LocalBinder
	private lateinit var playingTracksManager: PlayingTracksManager
	private val listeners = ArrayList<TrackStateListener>()
	private var shuffleType = ShuffleType.NO
	private var loopType = LoopType.NO
	private lateinit var remoteViews: RemoteViews
	private lateinit var expandedRemoteViews: RemoteViews
	private lateinit var notificationIntent: Intent
	private lateinit var pendingIntent: PendingIntent
	private lateinit var notification: Notification

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
				createNotification(getCurrentTrack())
			}
			ACTION_PLAY_PAUSE -> {
				when (isPlaying()) {
					true -> {
						pause()
						playToPause()
					}
					else -> {
						start()
						pauseToPlay()
					}
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
		pauseToPlay()
	}

	override fun pause() {
		playingTracksManager.pause()
		stopForeground(false)
		playToPause()
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
		pauseToPlay()
	}

	override fun getNextTrack() = playingTracksManager.getNextTrack()
	override fun getPreviousTrack() = playingTracksManager.getPreviousTrack()
	override fun getDuration() = playingTracksManager.getDuration()
	override fun getCurrentPosition() = playingTracksManager.getCurrentPosition()
	override fun isPlaying(): Boolean = playingTracksManager.isPlaying()
	override fun onPrepared(mp: MediaPlayer?) {
		start()
		updateNotificationUI(getCurrentTrack())
		pauseToPlay()
		sendToListeners()
	}

	override fun onCompletion(mp: MediaPlayer?) {
		next()
	}

	override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
		return true
	}

	private fun createNotificationChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val importance = NotificationManager.IMPORTANCE_DEFAULT
			val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
			val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			notificationManager.createNotificationChannel(channel)
		}
	}

	private fun createNotification(track: Track) {
		createNotificationChannel()
		remoteViews = RemoteViews(packageName, R.layout.layout_notification)
		expandedRemoteViews = RemoteViews(packageName, R.layout.layout_large_notification)
		notificationIntent = Intent(this, DetailPlayingActivity::class.java)
				.apply {
					putExtra(TrackAttributes.TRACK, track)
					action = ACTION_START_FROM_NOTIFICATION
					flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
				}
		pendingIntent = PendingIntent.getActivity(
				this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
		)
		val pausePlayIntent = Intent(this, PlayingTracksService::class.java).apply {
			action = ACTION_PLAY_PAUSE
		}
		val pendingPausePlay = PendingIntent.getService(this, 0, pausePlayIntent, 0)
		val nextIntent = Intent(this, PlayingTracksService::class.java).apply {
			action = ACTION_NEXT
		}
		val pendingNext = PendingIntent.getService(this, 0, nextIntent, 0)
		val prevIntent = Intent(this, PlayingTracksService::class.java).apply {
			action = ACTION_PREVIOUS
		}
		val pendingPrev = PendingIntent.getService(this, 0, prevIntent, 0)
		remoteViews.setOnClickPendingIntent(R.id.imagePlay, pendingPausePlay)
		expandedRemoteViews.setOnClickPendingIntent(R.id.imagePlay, pendingPausePlay)
		expandedRemoteViews.setOnClickPendingIntent(R.id.imageNext, pendingNext)
		expandedRemoteViews.setOnClickPendingIntent(R.id.imagePrevious, pendingPrev)
		notification = NotificationCompat.Builder(this, CHANNEL_ID)
				.setSmallIcon(R.drawable.ic_headphone)
				.setStyle(NotificationCompat.DecoratedCustomViewStyle())
				.setContentIntent(pendingIntent)
				.setCustomContentView(remoteViews)
				.setCustomBigContentView(expandedRemoteViews)
				.build()
	}

	private fun updateNotificationUI(track: Track) {
		remoteViews.setTextViewText(R.id.textNameTrack, track.title)
		remoteViews.setImageViewResource(R.id.imageTrackInNotification, R.drawable.all_music_genre)
		expandedRemoteViews.setTextViewText(R.id.textNameTrack, track.title)
		expandedRemoteViews.setTextViewText(R.id.textArtist, track.artist)
		expandedRemoteViews.setImageViewResource(R.id.imageTrackInNotification, R.drawable.all_music_genre)
		val notificationTarget = NotificationTarget(this,
				R.id.imageTrackInNotification,
				remoteViews,
				notification,
				NOTIFICATION_ID)
		Glide.with(this)
				.asBitmap()
				.load(track.artworkUrl)
				.into(notificationTarget)
		val notificationTargetExpanded = NotificationTarget(this,
				R.id.imageTrackInNotification,
				expandedRemoteViews,
				notification,
				NOTIFICATION_ID)
		Glide.with(this)
				.asBitmap()
				.load(track.artworkUrl)
				.into(notificationTargetExpanded)

		startForeground(NOTIFICATION_ID, notification)
	}

	private fun pauseToPlay() {
		remoteViews.setImageViewResource(R.id.imagePlay, R.drawable.ic_pause)
		expandedRemoteViews.setImageViewResource(R.id.imagePlay, R.drawable.ic_pause)
		NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
	}

	private fun playToPause() {
		remoteViews.setImageViewResource(R.id.imagePlay, R.drawable.ic_play_arrow)
		expandedRemoteViews.setImageViewResource(R.id.imagePlay, R.drawable.ic_play_arrow)
		NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
	}

	fun shuffleOn() {
		shuffleType = ShuffleType.YES
		playingTracksManager.setShuffleType(shuffleType)
	}

	fun shuffleOff() {
		shuffleType = ShuffleType.NO
		playingTracksManager.setShuffleType(shuffleType)
	}

	fun loopNon() {
		loopType = LoopType.NO
		playingTracksManager.setLoopType(loopType)
	}

	fun loopOne() {
		loopType = LoopType.ONE
		playingTracksManager.setLoopType(loopType)
	}

	fun loopAll() {
		loopType = LoopType.ALL
		playingTracksManager.setLoopType(loopType)
	}

	fun getShuffleType() = playingTracksManager.getShuffleType()
	fun getLoopType() = playingTracksManager.getLoopType()

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
			listeners[i].startListener()
		}
	}

	inner class LocalBinder : Binder() {
		fun getServive(): PlayingTracksService = this@PlayingTracksService
	}

	companion object {
		private const val CHANNEL_ID = "vn.sunasterisk.music_67.CHANNEL_ID"
		private const val CHANNEL_NAME = "vn.sunasterisk.music_67.CHANNEL_NAME"
		private const val NOTIFICATION_ID = 211
	}
}
