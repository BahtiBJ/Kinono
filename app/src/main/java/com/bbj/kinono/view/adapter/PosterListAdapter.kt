package com.bbj.kinono.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bbj.kinono.R
import com.bbj.kinono.domain.models.PosterInfo
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class PosterListAdapter(context: Context, private val itemClick: OnListItemClick) :
    RecyclerView.Adapter<PosterListAdapter.ViewHolder>() {

    private val defaultPoster = "0"

    private val inflater = LayoutInflater.from(context)

    private val itemPlaceholder = PosterInfo(0,defaultPoster)

    private val list = arrayListOf(itemPlaceholder,itemPlaceholder,itemPlaceholder)

    fun add(element: PosterInfo) {
        list.add(element)
        notifyItemChanged(list.lastIndex)
    }

    fun addAll(elementS: List<PosterInfo>) {
        list.clear()
        list.addAll(elementS)
        notifyItemRangeChanged(0,list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_home_premiere_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val posterUrl = list[position].posterURL
        if (posterUrl == defaultPoster) {
            holder.moviePoster.setImageResource(R.color.white_dark)
        } else {
            Picasso.get()
                .load(posterUrl)
                .placeholder(R.color.white_dark)
                .error(R.color.white_dark)
                .fit()
                .into(holder.moviePoster)
            holder.itemView.setOnClickListener {
                itemClick.click(list[position].filmId)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemVIew: View) : RecyclerView.ViewHolder(itemVIew) {
        val moviePoster = itemVIew.findViewById<ShapeableImageView>(R.id.item_premiere_poster)
    }

}