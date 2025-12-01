package uk.ac.tees.mad.dailywords.ui.domain.util

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class AudioPlayer(private val context: Context) {

    private var player: MediaPlayer? = null

    fun play(file: File) {
        MediaPlayer.create(context, file.toUri())?.apply {
            player = this
            start()

            setOnCompletionListener {
                stop()
            }
        }
    }

    fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}