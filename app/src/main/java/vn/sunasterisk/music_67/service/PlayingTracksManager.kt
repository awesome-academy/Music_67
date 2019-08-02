package vn.sunasterisk.music_67.service

import android.media.MediaPlayer
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.utils.LoopType
import vn.sunasterisk.music_67.utils.ShuffleType
import vn.sunasterisk.music_67.utils.StringUtils
import java.io.IOException
import kotlin.random.Random

class PlayingTracksManager(private val playingTracksService: PlayingTracksService) : PlayingTracksInterface {
	private val mediaPlayer = MediaPlayer()
	private lateinit var currentTrack: Track
	private var trackDuration: Long = 0
	@ShuffleType
	private var shuffleType = ShuffleType.NO
	@LoopType
	private var loopType = LoopType.NO

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
		when (loopType) {
			LoopType.NO -> {
				when (shuffleType) {
					ShuffleType.YES -> changeTrack(getRandomTrack())
					ShuffleType.NO -> {
						if (PlayingSet.currentPosition == PlayingSet.playingSet.size - 1)
							stop()
						else changeTrack(getNextTrack())
					}
				}
			}
			LoopType.ONE -> {
				create(currentTrack)
			}
			LoopType.ALL -> {
				when (shuffleType) {
					ShuffleType.YES -> changeTrack(getRandomTrack())
					ShuffleType.NO -> changeTrack(getNextTrack())
				}
			}
		}
	}

	override fun getNextTrack(): Track {
		val positionTrack = PlayingSet.currentPosition
		when (positionTrack) {
			PlayingSet.playingSet.size - 1 -> return PlayingSet.playingSet.elementAt(0)
			else -> return PlayingSet.playingSet.elementAt(positionTrack + 1)
		}
	}

	override fun previous() {
		when (loopType) {
			LoopType.NO or LoopType.ALL -> {
				when (shuffleType) {
					ShuffleType.YES -> changeTrack(getRandomTrack())
					ShuffleType.NO -> changeTrack(getPreviousTrack())
				}
			}
			LoopType.ONE -> {
				create(currentTrack)
			}
		}
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

	private fun getRandomTrack(): Track {
		val random = Random.nextInt(0, PlayingSet.playingSet.size - 1)
		return PlayingSet.playingSet.elementAt(random)
	}

	private fun changeTrack(track: Track) {
		currentTrack = track
		create(track)
	}

	fun getCurrentTrack(): Track {
		currentTrack = PlayingSet.playingSet.elementAt(PlayingSet.currentPosition)
		return currentTrack
	}

	@ShuffleType
	fun getShuffleType() = shuffleType

	@LoopType
	fun getLoopType() = loopType

	fun setShuffleType(@ShuffleType shuffleType: Int) {
		this.shuffleType = shuffleType
	}

	fun setLoopType(@LoopType loopType: Int) {
		this.loopType = loopType
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
