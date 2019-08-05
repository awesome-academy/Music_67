package vn.sunasterisk.music_67.ui.nowplaying

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import vn.sunasterisk.music_67.ui.genres.TrackAdapter

class NowPlayingDragManager(
		context: Context, val trackAdapter: TrackAdapter, dragDirs: Int, swipeDirs: Int
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
	override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder):
			Boolean {
		trackAdapter.onMove(p1.adapterPosition, p2.adapterPosition)
		return true
	}

	override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
		trackAdapter.onSwipe(p0.adapterPosition)
	}
}
