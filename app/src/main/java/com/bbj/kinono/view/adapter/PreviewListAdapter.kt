package com.bbj.kinono.view.adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bbj.kinono.R
import com.bbj.kinono.data.models.Item
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class PreviewListAdapter(context: Context, private val itemClick: OnListItemClick) :
    RecyclerView.Adapter<PreviewListAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    private val resources = context.resources

    private val itemPlaceholder = Item(listOf(),0
        , listOf(), 0
        , "", ""
        , "res", ""
        , "", 0
    )

    private val list = arrayListOf<Item>().apply {
        repeat(5){
            add(itemPlaceholder)
        }
    }

    fun add(element: Item) {
        list.add(element)
        notifyItemChanged(list.lastIndex)
    }

    fun addAll(elementS: ArrayList<Item>) {
        list.clear()
        list.addAll(elementS)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_home_premiere_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val posterUrl = list[position].posterUrl
        if (posterUrl == "res") {
            holder.moviePoster.setImageResource(R.color.white_dark)
        } else {
            Picasso.get()
                .load(posterUrl)
                .placeholder(R.color.white_dark)
                .error(R.color.white_dark)
                .fit()
                .into(holder.moviePoster)
            holder.itemView.setOnClickListener {
                itemClick.click(list[position].kinopoiskId)
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