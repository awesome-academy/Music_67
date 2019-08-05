package vn.sunasterisk.music_67.ui.search

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

class SearchAdapter(
    val context: Context,
    private var tracks: ArrayList<Track>,
    var trackClickListener: TrackClickListener
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_track, viewGroup, false)
        return ViewHolder(context, view, trackClickListener)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(tracks[position])
    }

    class ViewHolder(
        val context: Context,
        val trackView: View,
        val trackClickListener: TrackClickListener
    ) : RecyclerView.ViewHolder(trackView), View.OnClickListener {
        init {
            trackView.setOnClickListener(this)
            trackView.imageMore.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v) {
                trackView.imageMore -> trackClickListener.onMoreClicked(adapterPosition)
                trackView -> trackClickListener.onTrackClicked(adapterPosition)
            }
        }

        fun bindView(track: Track) {
            trackView.textTitle.text = track.title
            trackView.textArtist.text = track.artist
            Glide.with(context)
                .load(track.artworkUrl)
                .error(R.drawable.icon_app)
                .into(trackView.imageTrack)
            trackView.textDuration.text = TimeConvert.convertMilisecondToFormatTime(track.duration!!)
        }
    }

    fun clearList() {
        tracks.clear()
    }
}

interface TrackClickListener {
    fun onTrackClicked(position: Int)
    fun onMoreClicked(position: Int)
}
