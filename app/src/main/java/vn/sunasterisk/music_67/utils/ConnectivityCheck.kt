package vn.sunasterisk.music_67.utils

import android.content.Context
import android.net.ConnectivityManager

class ConnectivityCheck {
    companion object {
        fun isConnectedToNetwork(context: Context): Boolean {
            var isConnected = false
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = connectivityManager.activeNetworkInfo
            isConnected = (activeNetwork != null) && (activeNetwork.isConnected())
            return isConnected
        }
    }
}
