package vn.sunasterisk.music_67.ui.bottomsheetfragment

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.item_track.textArtist
import kotlinx.android.synthetic.main.item_track.textTitle
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.service.PlayingSet

class MenuFragment : BottomSheetDialogFragment(), View.OnClickListener {
	private lateinit var trackReceived: Track
	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_menu, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		trackReceived = arguments!!.getParcelable(ARGUMENT_TRACK)
		if (!trackReceived.title.isNullOrEmpty()) textTitle.text = trackReceived.title
		if (!trackReceived.artist.isNullOrEmpty()) textArtist.text = trackReceived.artist
		Glide.with(this)
				.load(trackReceived.artworkUrl)
				.error(R.drawable.icon_app)
				.into(imageTrackInMenu)
		if (!trackReceived.downloadable!!) {
			textDownload.visibility = View.GONE
			imageDownload.visibility = View.GONE
		}
		registerListener()
	}

	override fun onClick(v: View?) {
		when (v) {
			imageAdd -> {
				PlayingSet.playingSet.add(trackReceived)
				Toast.makeText(context,
						trackReceived.title + getString(R.string.msg_add_success),
						Toast.LENGTH_SHORT)
						.show()
			}
		}
	}

	private fun registerListener() {
		imageAdd.setOnClickListener(this)
		imageDownload.setOnClickListener(this)
	}

	companion object {
		private const val ARGUMENT_TRACK = "vn.sunasterisk.music_67.ARGUMENT_TRACK"
		fun newInstance(track: Track): MenuFragment {
			val menuFragment = MenuFragment()
			val bundle = Bundle()
			bundle.putParcelable(ARGUMENT_TRACK, track)
			menuFragment.arguments = bundle
			return menuFragment
		}
	}
}
