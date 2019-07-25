package com.example.music_67.ui.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.music_67.R
import com.example.music_67.data.model.Genre
import kotlinx.android.synthetic.main.item_genre.view.*

class GenreAdapter(val context: Context, private val genres: ArrayList<Genre>)
	: RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
	override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(context)
				.inflate(R.layout.item_genre, viewGroup, false)
		return ViewHolder(context, view)
	}

	override fun getItemCount(): Int {
		return genres.size
	}

	override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
		val item = genres[position]
		viewHolder.bindView(item)
	}

	class ViewHolder(val context: Context, genreView: View) : RecyclerView.ViewHolder(genreView) {
		private val imageGenre = genreView.imageGenre
		private val nameGenre = genreView.textNameGenre
		fun bindView(genre: Genre) {
			Glide.with(context)
					.asBitmap()
					.load(genre.imageId)
					.into(imageGenre)
			nameGenre.text = genre.name
		}
	}
}
