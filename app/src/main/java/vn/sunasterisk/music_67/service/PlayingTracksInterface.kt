package vn.sunasterisk.music_67.service

import vn.sunasterisk.music_67.data.model.Track

interface PlayingTracksInterface {
    fun create(track: Track)
    fun start()
    fun pause()
    fun next()
    fun getNextTrack(): Track
    fun getPreviousTrack(): Track
    fun previous()
    fun stop()
    fun release()
    fun seek(position: Int)
    fun getDuration(): Long
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
}
