package com.sreshtha.newsnest.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.sreshtha.newsnest.BuildConfig


object Constants {

    const val BASE_URL = "https://newsapi.org/"
    const val API_KEY = BuildConfig.API_KEY
    const val PAGE_SIZE = 20
    const val ARTICLE_TAG = "article"
    const val TYPE_TAG = "type"
    const val BREAKING_FRAGMENT = "breaking_fragment"
    const val SAVED_NEWS_FRAGMENT = "saved_news_fragment"
    const val SEARCH_NEWS_FRAGMENT = "search_news_fragment"
    const val NEWS_VIEW_MODEL = "news_view_model"
    const val CHANNEL_ID = "channelID_newsnest"
    const val CHANNEL_NAME = "channelName_newsnest"
    const val NOTIFICATION_ID = 0
    const val NOTIFICATION_ID_TAG = "newsnest_notification_id"
    const val NOTIFICATION_WORK = "newsnest_notification_work"

    fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap? {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (drawable?.let { DrawableCompat.wrap(it) })?.mutate()
        }
        val bitmap =
            drawable?.intrinsicWidth?.let {
                Bitmap.createBitmap(
                    it,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
            }

        val canvas = bitmap?.let { Canvas(it) }

        canvas?.height?.let { drawable?.setBounds(0, 0, canvas.width, it) }
        if (canvas != null) {
            drawable?.draw(canvas)
        }

        return bitmap
    }


}