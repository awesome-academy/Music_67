package com.example.music_67.utils

import android.support.annotation.StringDef

@StringDef(
		GenreType.ALL_MUSIC,
		GenreType.ALL_AUDIO,
		GenreType.ALTERNATIVE_ROCK,
		GenreType.AMBIENT,
		GenreType.CLASSICAL,
		GenreType.COUNTRY,
		GenreType.KEY_ALL_MUSIC,
		GenreType.KEY_ALL_AUDIO,
		GenreType.KEY_ALTERNATIVE_ROCK,
		GenreType.KEY_AMBIENT,
		GenreType.KEY_CLASSICAL,
		GenreType.KEY_COUNTRY
)
annotation class GenreType {
	companion object{
		const val ALL_MUSIC = "All Music"
		const val ALL_AUDIO = "All Audio"
		const val ALTERNATIVE_ROCK = "Alternative Rock"
		const val AMBIENT = "Ambient"
		const val CLASSICAL = "Classical"
		const val COUNTRY = "Country"
		const val KEY_ALL_MUSIC = "soundcloud:genres:all-music"
		const val KEY_ALL_AUDIO = "soundcloud:genres:all-audio"
		const val KEY_ALTERNATIVE_ROCK = "soundcloud:genres:alternativerock"
		const val KEY_AMBIENT = "soundcloud:genres:ambient"
		const val KEY_CLASSICAL = "soundcloud:genres:classical"
		const val KEY_COUNTRY = "soundcloud:genres:country"
	}
}
