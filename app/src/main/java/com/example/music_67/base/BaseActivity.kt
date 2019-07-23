package com.example.music_67.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View

abstract class BaseActivity : AppCompatActivity() {
	@LayoutRes
	abstract fun getContentViewId(): Int

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(getContentViewId())
	}

	abstract fun onTrackClicked(view: View)

	abstract fun showMiniPlaying(isTrackPlaying: Boolean, viewId: Int)
}
