package vn.sunasterisk.music_67.ui.genres

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_track.view.*
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.service.PlayingSet
import vn.sunasterisk.music_67.utils.TimeConvert
import java.util.*

class TrackAdapter(
		val context: Context,
		private val tracks: ArrayList<Track>,
		var trackClickListener: TrackClickListener
) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {
	private var currentTrack: Track? = null
	private val inflater: LayoutInflater

	init {
		inflater = LayoutInflater.from(context)
	}

	override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
		val view = inflater.inflate(R.layout.item_track, viewGroup, false)
		return ViewHolder(context, view, trackClickListener)
	}

	override fun getItemCount(): Int {
		if (tracks.isNullOrEmpty()) return 0
		else
			return tracks.size
	}

	override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
		viewHolder.bindView(tracks[position], currentTrack)
	}

	class ViewHolder(
			val context: Context,
			val trackView: View,
			val trackClickListener: TrackClickListener
	) : RecyclerView.ViewHolder(trackView) {
		init {
			trackView.setOnClickListener {
				trackClickListener.onTrackClicked(adapterPosition)
			}
			trackView.imageMore.setOnClickListener {
				trackClickListener.onMoreClicked(adapterPosition)
			}
		}

		private val titleTrack = trackView.textTitle
		private val artistTrack = trackView.textArtist
		private val imageTrack = trackView.imageTrack
		private val durationTrack = trackView.textDuration
		fun bindView(track: Track, currentTrack: Track?) {
			titleTrack.text = track.title
			artistTrack.text = track.artist
			Glide.with(context)
					.load(track.artworkUrl)
					.error(R.drawable.icon_app)
					.into(imageTrack)
			durationTrack.text = TimeConvert.convertMilisecondToFormatTime(track.duration!!)
			if (track == currentTrack) {
				itemView.setBackgroundColor(context.resources.getColor(R.color.color_playing_item))
			} else {
				itemView.setBackgroundColor(Color.WHITE)
			}
		}
	}

	fun onMove(oldPosition: Int, newPosition: Int) {
		if (oldPosition < newPosition) {
			for (i in oldPosition until newPosition) {
				Collections.swap(tracks, i, i + 1)
				PlayingSet.playingSet.clear()
				PlayingSet.playingSet.addAll(tracks)
			}
		} else {
			for (i in oldPosition downTo newPosition + 1) {
				Collections.swap(tracks, i, i - 1)
				PlayingSet.playingSet.clear()
				PlayingSet.playingSet.addAll(tracks)
			}
		}
		notifyItemMoved(oldPosition, newPosition)
	}

	fun onSwipe(position: Int) {
		tracks.removeAt(position)
		PlayingSet.playingSet.remove(PlayingSet.playingSet.elementAt(position))
		notifyItemRemoved(position)
	}

	fun setCurrentTrack(track: Track) {
		currentTrack = track
	}
}

interface TrackClickListener {
	fun onTrackClicked(position: Int)
	fun onMoreClicked(position: Int)
}
