package com.snt.phoney.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import dagger.android.AndroidInjection

private const val ACTION_BURN = "com.snt.phoney.ui.album.action.BURN"

/**
 * An [IntentService] subclass for handling asynchronous task requests in a service on a separate handler thread.
 */
class TaskIntentService : IntentService("BurnAlbumIntentService") {

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_BURN -> {
                handleActionBurn()
            }
        }
    }

    /**
     * Handle action burn in the provided background thread with the provided parameters.
     */
    private fun handleActionBurn() {

    }

    companion object {
        /**
         * Starts this service to perform action Burn. If the service is already performing a task this action will be queued.
         * @see IntentService
         */
        @JvmStatic
        fun startActionBurn(context: Context) {
            val intent = Intent(context, TaskIntentService::class.java).apply {
                action = ACTION_BURN
            }
            context.startService(intent)
        }
    }
}
