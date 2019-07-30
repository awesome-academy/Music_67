package vn.sunasterisk.music_67.ui.bottomsheetfragment

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.utils.TrackAttributes
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.item_track.textArtist
import kotlinx.android.synthetic.main.item_track.textTitle

class MenuFragment : BottomSheetDialogFragment() {
	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_menu, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val titleTrack = arguments!!.getString(TrackAttributes.TITLE)
		val artistTrack = arguments!!.getString(TrackAttributes.ARTIST)
		val image = arguments!!.getString(TrackAttributes.ARTWORK_URL)
		val downloadable = arguments!!.getBoolean(TrackAttributes.DOWNLOADABLE)
		textTitle.text = titleTrack
		textArtist.text = artistTrack
		Glide.with(this)
				.load(image)
				.error(R.drawable.icon_app)
				.into(imageTrackInMenu)
		if(!downloadable){
			textDownload.visibility = View.GONE
			imageDownload.visibility = View.GONE
		}
	}
}
