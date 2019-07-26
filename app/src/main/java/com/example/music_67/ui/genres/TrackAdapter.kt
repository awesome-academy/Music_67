package com.example.music_67.ui.genres

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.music_67.R
import com.example.music_67.data.model.Track
import com.example.music_67.utils.TimeConvert
import kotlinx.android.synthetic.main.item_track.view.*

class TrackAdapter(val context: Context, private val tracks: ArrayList<Track>)
	: RecyclerView.Adapter<TrackAdapter.ViewHolder>() {
	override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(context)
				.inflate(R.layout.item_track, viewGroup, false)
		return ViewHolder(context, view)
	}

	override fun getItemCount(): Int {
		return tracks.size
	}

	override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
		val item = tracks[position]
		viewHolder.bindView(item)
	}

	class ViewHolder(val context: Context, trackView: View) : RecyclerView.ViewHolder(trackView) {
		private val titleTrack = trackView.textTitle
		private val artistTrack = trackView.textArtist
		private val imageTrack = trackView.imageTrack
		private val durationTrack = trackView.textDuration
		fun bindView(track: Track) {
			titleTrack.text = track.title
			artistTrack.text = track.artist
			Glide.with(context)
					.asBitmap()
					.load(track.artworkUrl)
					.error(R.drawable.icon_app)
					.into(imageTrack)
			durationTrack.text = TimeConvert.convertMilisecondToFormatTime(track.duration!!)
		}
	}
}
