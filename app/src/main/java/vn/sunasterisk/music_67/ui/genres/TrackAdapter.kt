package vn.sunasterisk.music_67.ui.genres

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.ui.bottomsheetfragment.MenuFragment
import vn.sunasterisk.music_67.ui.detailplaying.DetailPlayingActivity
import vn.sunasterisk.music_67.utils.TimeConvert
import vn.sunasterisk.music_67.utils.TrackAttributes
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

	class ViewHolder(val context: Context, val trackView: View) : RecyclerView.ViewHolder(trackView) {
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
			trackView.setOnClickListener {
				context.startActivity(getDetailIntent(context, track))
			}
			trackView.imageMore.setOnClickListener {
				val menuFragment = MenuFragment()
				menuFragment.show((context as AppCompatActivity)
						.supportFragmentManager, MENU_FRAGMENT_TAG)
				menuFragment.arguments = getMenuIBundle(context, track)
			}
		}

		companion object {
			fun getDetailIntent(context: Context, track: Track): Intent {
				val detailIntent = Intent(context, DetailPlayingActivity::class.java)
				detailIntent.apply {
					putExtra(TrackAttributes.TITLE, track.title)
					putExtra(TrackAttributes.ARTIST, track.artist)
					putExtra(TrackAttributes.ARTWORK_URL, track.artworkUrl)
					putExtra(TrackAttributes.DURATION, track.duration)
				}
				return detailIntent
			}

			fun getMenuIBundle(context: Context, track: Track): Bundle {
				val menuBundle = Bundle()
				menuBundle.putString(TrackAttributes.TITLE, track.title)
				menuBundle.putString(TrackAttributes.ARTIST, track.artist)
				menuBundle.putString(TrackAttributes.ARTWORK_URL, track.artworkUrl)
				menuBundle.putBoolean(TrackAttributes.DOWNLOADABLE,track.downloadable!!)
				return menuBundle
			}
		}
	}

	companion object {
		private const val MENU_FRAGMENT_TAG = "vn.sunasterisk.music_67.MENU_FRAGMENT"
	}
}
