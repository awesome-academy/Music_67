package vn.sunasterisk.music_67.base

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import vn.sunasterisk.music_67.service.PlayingTracksService
import vn.sunasterisk.music_67.ui.bottomsheetfragment.MiniPlaying

abstract class BaseActivity : AppCompatActivity() {
	private lateinit var playingTracksService: PlayingTracksService

	private val serviceConnection = object : ServiceConnection {
		override fun onServiceDisconnected(name: ComponentName?) {
		}

		override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
			val binder = service as PlayingTracksService.LocalBinder
			playingTracksService = binder.getServive()
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(getContentViewId())
		playingTracksService = PlayingTracksService()
		val playingIntent = Intent(this, PlayingTracksService::class.java)
		bindService(playingIntent, serviceConnection, BIND_AUTO_CREATE)
	}

	override fun onDestroy() {
		super.onDestroy()
		unbindService(serviceConnection)
	}

	@LayoutRes
	abstract fun getContentViewId(): Int

	abstract fun showMiniPlaying(isTrackPlaying: Boolean)

	fun initMiniPlay(viewId: Int) {
		val fragmentTransaction = supportFragmentManager.beginTransaction()
		val miniPlaying = supportFragmentManager.findFragmentByTag(MiniPlaying::class.java.name)
		if (miniPlaying == null) {
			fragmentTransaction.add(viewId, MiniPlaying(), MiniPlaying::class.java.name)
					.commit()
		} else
			fragmentTransaction.replace(viewId, miniPlaying, MiniPlaying::class.java.name)
					.commit()
	}
}
