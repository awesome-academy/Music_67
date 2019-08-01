package vn.sunasterisk.music_67.service

import android.media.MediaPlayer
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.utils.StringUtils
import java.io.IOException

class PlayingTracksManager(val playingTracksService: PlayingTracksService) : PlayingTracksInterface {
	private val mediaPlayer = MediaPlayer()
	private lateinit var currentTrack: Track
	private var trackDuration: Long = 0
	override fun create(track: Track) {
		mediaPlayer.reset()
		try {
			mediaPlayer.setDataSource(StringUtils.generateStreamApi(track.id))
			trackDuration = track.duration!!
			PlayingSet.currentPosition = PlayingSet.playingSet.indexOf(track)
		} catch (e: IOException) {
			e.printStackTrace()
		}
		mediaPlayer.setOnPreparedListener(playingTracksService)
		mediaPlayer.setOnErrorListener(playingTracksService)
		mediaPlayer.setOnCompletionListener(playingTracksService)
		mediaPlayer.prepareAsync()
	}

	override fun start() {
		mediaPlayer.start()
	}

	override fun pause() {
		mediaPlayer.pause()
	}

	override fun next() {
		changeTrack(getNextTrack())
	}

	override fun getNextTrack(): Track {
		val positionTrack = PlayingSet.currentPosition
		when (positionTrack) {
			PlayingSet.playingSet.size - 1 -> return PlayingSet.playingSet.elementAt(0)
			else -> return PlayingSet.playingSet.elementAt(positionTrack + 1)
		}
	}

	override fun previous() {
		changeTrack(getPreviousTrack())
	}

	override fun getPreviousTrack(): Track {
		val positionTrack = PlayingSet.currentPosition
		when (positionTrack) {
			0 -> return PlayingSet.playingSet.elementAt(PlayingSet.playingSet.size - 1)
			else -> return PlayingSet.playingSet.elementAt(positionTrack - 1)
		}
	}

	override fun stop() {
		mediaPlayer.stop()
	}

	override fun release() {
		mediaPlayer.release()
	}

	override fun seek(position: Int) {
		mediaPlayer.seekTo(position)
	}

	override fun getDuration() = trackDuration
	override fun getCurrentPosition() = mediaPlayer.currentPosition
	override fun isPlaying(): Boolean = mediaPlayer.isPlaying

	private fun changeTrack(track: Track) {
		currentTrack = track
		create(track)
	}

	fun getCurrentTrack(): Track {
		currentTrack = PlayingSet.playingSet.elementAt(PlayingSet.currentPosition)
		return currentTrack
	}

	companion object {
		private var INSTANCE: PlayingTracksManager? = null
		@JvmStatic
		fun getInstance(
				playingTracksService: PlayingTracksService
		): PlayingTracksManager {
			return INSTANCE ?: PlayingTracksManager(playingTracksService)
					.apply { INSTANCE = this }
		}
	}
}
