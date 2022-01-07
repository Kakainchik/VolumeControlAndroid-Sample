package kz.kakainchik.sportradarsample.ext

import android.media.AudioManager
import kotlin.math.roundToInt

/**
 * Calculates volume level depending on maximum stream volume value.
 */
fun AudioManager.getVolumeFromPercent(percent: Float, stream: Int): Int =
    (percent  * this.getStreamMaxVolume(stream) / 100).roundToInt()