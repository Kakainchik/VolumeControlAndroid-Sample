package kz.kakainchik.sportradarsample.ext

import android.content.Context

fun Context.dpToPx(dp: Int) =
    dp.toFloat() * this.resources.displayMetrics.density