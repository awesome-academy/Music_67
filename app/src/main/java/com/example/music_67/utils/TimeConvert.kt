package com.example.music_67.utils

import java.util.concurrent.TimeUnit

class TimeConvert {
    companion object {
        private const val pattern = "%02d:%02d"
        @JvmStatic
        fun convertMilisecondToFormatTime(milisecond: Long): String {
            return String.format(
                pattern,
                TimeUnit.MILLISECONDS.toMinutes(milisecond) -
                        TimeUnit.HOURS.toMinutes
                            (TimeUnit.MILLISECONDS.toHours(milisecond)),
                TimeUnit.MILLISECONDS.toSeconds(milisecond) -
                        TimeUnit.MINUTES.toSeconds
                            (TimeUnit.MILLISECONDS.toMinutes(milisecond))
            )
        }
    }
}
