package vn.sunasterisk.music_67.service

import vn.sunasterisk.music_67.data.model.Track

object PlayingSet {
	val playingSet = mutableSetOf<Track>()
	var currentPosition: Int = -1
}
