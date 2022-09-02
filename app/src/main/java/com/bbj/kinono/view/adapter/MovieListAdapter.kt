package com.bbj.kinono.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bbj.kinono.R
import com.bbj.kinono.data.models.Film
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class MovieListAdapter(context: Context, private val itemClick: OnListItemClick) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    private val res = context.resources

    private var isInit = false

    private val filmPlaceholder = Film(
        listOf(), 0, "", listOf(), "", "", "res", "", "", 0, 0, ""
    )

    private val list = arrayListOf<Film>().apply {
        repeat(5) {
            add(filmPlaceholder)
        }
    }

    fun add(element: Film) {
        list.add(element)
        notifyItemChanged(list.lastIndex)
    }

    fun initData(elements: ArrayList<Film>) {
        list.clear()
        list.addAll(elements)
        notifyDataSetChanged()
    }

    fun addAll(elements: ArrayList<Film>) {
        if (!isInit) {
            list.clear()
            isInit = true
        }
        val lastIndex = list.lastIndex
        list.addAll(elements)
        notifyItemRangeInserted(lastIndex + 1, elements.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_home_popular_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movieName.text = list[position].nameRu
        val rating = list[position].rating
        holder.movieRating.text =
            if (!rating.equals("null", true)) rating + "/10" else "0/10"

        val posterUrl = list[position].posterUrl
        if (posterUrl == "res") {
            holder.moviePoster.setImageResource(R.color.white_dark)
            holder.movieDuration.text =
                res.getString(R.string.duration_sample, list[position].filmLength)
            holder.movieGenre.text = res.getString(R.string.genre_sample, "")
        } else {
            Picasso.get()
                .load(list[position].posterUrl)
                .placeholder(R.color.white_dark)
                .error(R.color.white_dark)
                .fit()
                .into(holder.moviePoster)
            holder.movieDuration.text =
                res.getString(R.string.duration_sample, list[position].getDuration())
            holder.movieGenre.text =
                res.getString(R.string.genre_sample, list[position].getGenres())
            holder.itemView.setOnClickListener {
                itemClick.click(list[position].filmId)
            }
        }
        holder.movieYear.text = res.getString(R.string.year_sample, list[position].year)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moviePoster = itemView.findViewById<ShapeableImageView>(R.id.popular_poster)
        val movieName = itemView.findViewById<TextView>(R.id.popular_name)
        val movieRating = itemView.findViewById<TextView>(R.id.popular_rating)
        val movieYear = itemView.findViewById<TextView>(R.id.popular_year)
        val movieDuration = itemView.findViewById<TextView>(R.id.popular_duration)
        val movieGenre = itemView.findViewById<TextView>(R.id.popular_genre)
    }

}