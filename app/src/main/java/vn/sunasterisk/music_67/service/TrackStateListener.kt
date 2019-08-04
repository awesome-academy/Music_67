package vn.sunasterisk.music_67.service

interface TrackStateListener {
	fun completeListener()
	fun startListener()
	fun pauseListener()
	fun playListener()
}
