package uk.ac.tees.mad.dailywords.ui.domain.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TextToSpeechProvider(context: Context, onReady: (() -> Unit)? = null) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var pendingText: String? = null

    private val initializationListeners = mutableListOf<() -> Unit>()

    init {
        Log.i("TTS", "Initializing TextToSpeech.")
        onReady?.let { initializationListeners.add(it) }
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            Log.i("TTS", "Initialization SUCCESS.")
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language (US) is not supported!")
            } else {
                Log.i("TTS", "Language set to US English.")
            }
            initializationListeners.forEach { it.invoke() }
            initializationListeners.clear()

            pendingText?.let {
                Log.i("TTS", "Speaking pending text: $it")
                speak(it)
                pendingText = null
            }
        } else {
            Log.e("TTS", "Initialization FAILED! Status: $status")
        }
    }

    fun speak(text: String) {
        Log.d("TTS", "speak() called with text: '$text'. isInitialized: $isInitialized")
        if (isInitialized) {
            val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
            if (result == TextToSpeech.ERROR) {
                Log.e("TTS", "tts.speak() failed for '$text'")
            } else {
                Log.i("TTS", "tts.speak() queued text '$text'")
            }
        } else {
            pendingText = text
            Log.w("TTS", "TTS not initialized, pending text: '$text'")
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
        Log.i("TTS", "TTS has been shut down.")
    }
}