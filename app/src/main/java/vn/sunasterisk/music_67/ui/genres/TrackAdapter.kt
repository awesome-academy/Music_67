package vn.sunasterisk.music_67.ui.genres

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_track.view.*
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.utils.TimeConvert

class TrackAdapter(
		val context: Context,
		private val tracks: ArrayList<Track>,
		var trackClickListener: TrackClickListener
) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {
	override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(context)
				.inflate(R.layout.item_track, viewGroup, false)
		return ViewHolder(context, view, trackClickListener)
	}

	override fun getItemCount(): Int {
		if (tracks.isNullOrEmpty()) return 0
		else
			return tracks.size
	}

	override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
		viewHolder.bindView(tracks[position])
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
				trackClickListener.onTrackButtonClicked(adapterPosition)
			}
		}

		private val titleTrack = trackView.textTitle
		private val artistTrack = trackView.textArtist
		private val imageTrack = trackView.imageTrack
		private val durationTrack = trackView.textDuration
		fun bindView(track: Track) {
			titleTrack.text = track.title
			artistTrack.text = track.artist
			Glide.with(context)
					.load(track.artworkUrl)
					.error(R.drawable.icon_app)
					.into(imageTrack)
			durationTrack.text = TimeConvert.convertMilisecondToFormatTime(track.duration!!)
		}
	}
}

interface TrackClickListener {
	fun onTrackClicked(position: Int)
	fun onTrackButtonClicked(position: Int)
}