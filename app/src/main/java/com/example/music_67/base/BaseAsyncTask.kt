package com.example.music_67.base

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

abstract class BaseAsyncTask<T> : AsyncTask<String, Void, List<out T>>() {

    abstract fun convertJsonToObject(response: String): List<out T>

    override fun doInBackground(vararg params: String?): List<out T> {
        var response = ""
        val url = URL(params[0])
        val httpConnection = url.openConnection() as HttpURLConnection
        httpConnection.requestMethod = REQUEST
        val inputStream = httpConnection.inputStream
        response = convertToString(inputStream)
        return convertJsonToObject(response)
    }

    private fun convertToString(inputStream: InputStream): String {
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line = bufferReader.readLine()
        while (line != null) {
            stringBuilder.append(line).append("\n")
            line = bufferReader.readLine()
        }
        inputStream.close()
        return stringBuilder.toString()
    }

    companion object {
        const val REQUEST = "GET"
    }
}
