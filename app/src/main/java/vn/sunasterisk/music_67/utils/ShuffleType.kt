package vn.sunasterisk.music_67.utils

import android.support.annotation.IntDef

@IntDef(
		ShuffleType.YES,
		ShuffleType.NO
)
annotation class ShuffleType {
	companion object {
		const val YES = 1
		const val NO = 0
	}
}
