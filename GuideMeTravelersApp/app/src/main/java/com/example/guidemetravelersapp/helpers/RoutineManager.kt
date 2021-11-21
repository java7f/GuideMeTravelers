package com.example.guidemetravelersapp.helpers

import android.os.Handler
import android.os.Looper

class RoutineManager {
    companion object {
        val mainHandler: Handler = Handler(Looper.getMainLooper())

        fun post(r: Runnable) {
            mainHandler.post(r)
        }

        fun cancelRoutines() {
            mainHandler.removeCallbacksAndMessages(null)
        }
    }
}