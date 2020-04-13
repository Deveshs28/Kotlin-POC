package com.example.kotlinpoc.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Message
import java.util.logging.Handler

class ConnectivityReceiver : BroadcastReceiver {
    private var handler: Handler? = null

    constructor() {}

    constructor(_handler: Handler) {
        handler = _handler
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            if (intent!!.action == "android.net.conn.CONNECTIVITY_CHANGE") {
                initNetwork(context)
                val noConnectivity: Boolean =
                    intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
                val message = Message()
                if (noConnectivity) {
                    message.what = NETWORK_NOT_CONNECTED;
                    IS_NETWORK_CONNECTED = false
                } else {
                    message.what = NETWORK_CONNECTED;
                    IS_NETWORK_CONNECTED = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        var connectivityManager: ConnectivityManager? = null
        val NETWORK_NOT_CONNECTED: Int = 100;
        val NETWORK_CONNECTED: Int = 101;
        var IS_NETWORK_CONNECTED: Boolean = false;

        fun initNetwork(context: Context?) {
            connectivityManager =
                context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        }

        @SuppressLint("MissingPermission")
        fun isOnline(): Boolean {
            val networkInfo: NetworkInfo = connectivityManager!!.activeNetworkInfo
            return networkInfo?.isConnected ?: false
        }

        @SuppressLint("MissingPermission")
        fun isConnected(context: Context?): Boolean {
            val connManager: ConnectivityManager =
                context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo: NetworkInfo? = if (connManager!!.activeNetworkInfo == null) null else connManager!!.activeNetworkInfo
            return netInfo != null && netInfo?.isAvailable && netInfo?.isConnected
        }

        @SuppressLint("MissingPermission")
        fun isWifiConnected(): Boolean {
            var connected: Boolean = false
            if (connectivityManager != null) {
                val mWifi: NetworkInfo =
                    connectivityManager!!.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                if (mWifi.isConnected)
                    connected = true
            }
            return connected
        }
    }
}
