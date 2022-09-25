package com.bbj.kinono.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bbj.kinono.R
import com.bbj.kinono.domain.models.MovieInfo
import com.bbj.kinono.domain.models.PreviewModel
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class PopularListAdapter(context: Context, private val itemClick: OnListItemClick) :
    RecyclerView.Adapter<PopularListAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    private var isInit = false

    private val list = arrayListOf<PreviewModel>()

    fun add(element: PreviewModel) {
        list.add(element)
        notifyItemChanged(list.lastIndex)
    }

    fun initData(elements: List<PreviewModel>) {
        list.clear()
        list.addAll(elements)
        notifyItemRangeChanged(0,list.size)
    }

    fun addAll(elements: List<PreviewModel>) {
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
        holder.movieName.text = list[position].name
        val rating = list[position].rating
        holder.itemView.resources
        holder.movieRating.text =
            if (!rating.equals("null", true)) rating + "/10" else "0/10"

        val posterUrl = list[position].posterURL
        if (posterUrl == "res") {
            holder.moviePoster.setImageResource(R.color.white_dark)
            holder.movieDuration.text =
                holder.itemView.resources.getString(R.string.duration_sample, list[position].duration)
            holder.movieGenre.text = holder.itemView.resources.getString(R.string.genre_sample, "")
        } else {
            Picasso.get()
                .load(list[position].posterURL)
                .placeholder(R.color.white_dark)
                .error(R.color.white_dark)
                .fit()
                .into(holder.moviePoster)
            holder.movieDuration.text =
                holder.itemView.resources.getString(R.string.duration_sample, list[position].duration)
            holder.movieGenre.text =
                holder.itemView.resources.getString(R.string.genre_sample, list[position].genres)
            holder.itemView.setOnClickListener {
                itemClick.click(list[position].id)
            }
        }
        holder.movieYear.text = holder.itemView.resources.getString(R.string.year_sample, list[position].year)
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