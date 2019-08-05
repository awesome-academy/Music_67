package vn.sunasterisk.music_67.ui.nowplaying

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_genre.recyclerTracks
import kotlinx.android.synthetic.main.fragment_now_playing.*
import vn.sunasterisk.music_67.R
import vn.sunasterisk.music_67.data.model.Track
import vn.sunasterisk.music_67.service.PlayingTracksService
import vn.sunasterisk.music_67.service.TrackStateListener
import vn.sunasterisk.music_67.ui.bottomsheetfragment.MenuFragment
import vn.sunasterisk.music_67.ui.detailplaying.DetailPlayingActivity
import vn.sunasterisk.music_67.ui.genres.TrackAdapter
import vn.sunasterisk.music_67.ui.genres.TrackClickListener
import vn.sunasterisk.music_67.ui.home.HomeActivity
import vn.sunasterisk.music_67.ui.search.ACTION_SEARCH_FROM_NOW
import vn.sunasterisk.music_67.ui.search.SearchActivity
import vn.sunasterisk.music_67.utils.StringUtils

class NowPlayingFragment : Fragment(), NowPlayingContract.View, TrackStateListener {
    private lateinit var nowPlayingPresenter: NowPlayingPresenter
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var playingTracksService: PlayingTracksService
    private val trackClickListener = object : TrackClickListener {
        override fun onTrackClicked(position: Int) {
            val track = nowPlayingPresenter.tracks[position]
            playingTracksService.create(track)
            completeListener()
        }

        override fun onMoreClicked(position: Int) {
            val track = nowPlayingPresenter.tracks[position]
            val menuFragment = MenuFragment.newInstance(track)
            menuFragment.show(childFragmentManager, MENU_FRAGMENT_TAG)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_now_playing, container, false)
        playingTracksService = (activity as DetailPlayingActivity).playingTracksService
        setHasOptionsMenu(true)
        return view
    }

    override fun onStart() {
        super.onStart()
        initUI()
        nowPlayingPresenter = NowPlayingPresenter(this)
        trackAdapter = TrackAdapter(
            context!!,
            nowPlayingPresenter.tracks as ArrayList<Track>,
            trackClickListener
        )
        trackAdapter.setCurrentTrack(playingTracksService.getCurrentTrack())
        Glide.with(context!!)
            .load(StringUtils.getBigArtwork(playingTracksService.getCurrentTrack().artworkUrl!!))
            .fallback(R.mipmap.ic_app)
            .error(R.mipmap.ic_app)
            .into(toolbarImage)
        recyclerTracks.adapter = trackAdapter
        nowPlayingPresenter.loadTracks()
        val callback = NowPlayingDragManager(
            context!!, trackAdapter,
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
            ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
        )
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(recyclerTracks)

    }

    override fun loadTracksSuccess() {
        trackAdapter.notifyDataSetChanged()
    }

    override fun loadTracksFail() {

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_home -> {
                val homeIntent = Intent(context, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(homeIntent)
            }
            R.id.menu_search -> startActivity(SearchActivity.getIntent(context!!, ACTION_SEARCH_FROM_NOW))
        }
        return true
    }

    override fun completeListener() {
        Glide.with(context!!)
            .load(StringUtils.getBigArtwork(playingTracksService.getCurrentTrack().artworkUrl!!))
            .fallback(R.mipmap.ic_app)
            .error(R.mipmap.ic_app)
            .into(toolbarImage)
        trackAdapter.setCurrentTrack(playingTracksService.getCurrentTrack())
        trackAdapter.notifyDataSetChanged()
    }

    override fun startListener() {

    }

    override fun pauseListener() {

    }

    override fun playListener() {

    }

    private fun initUI() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                (activity as AppCompatActivity).onBackPressed()
            }
        })
    }

    companion object {
        private const val MENU_FRAGMENT_TAG = "vn.sunasterisk.music_67.MENU_FRAGMENT"
    }
}
