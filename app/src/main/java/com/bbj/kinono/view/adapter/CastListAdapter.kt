package com.bbj.kinono.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bbj.kinono.R
import com.bbj.kinono.domain.models.CastingModel
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class CastListAdapter(context: Context) : RecyclerView.Adapter<CastListAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    private val list = arrayListOf<CastingModel>()

    fun add(element : CastingModel){
        list.add(element)
        notifyItemChanged(list.lastIndex)
    }

    fun addAll(elementS: List<CastingModel>){
        val lastIndex = list.lastIndex
        list.addAll(elementS)
        notifyItemRangeInserted(lastIndex + 1,elementS.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_movie_cast_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.personName.text = list[position].name
        val posterUrl = list[position].posterUrl
        Picasso.get()
            .load(posterUrl)
            .placeholder(R.color.white_dark)
            .error(R.color.white_dark)
            .fit()
            .into(holder.personPoster)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemVIew : View) : RecyclerView.ViewHolder(itemVIew){
        val personPoster = itemVIew.findViewById<ShapeableImageView>(R.id.item_cast_list_image)
        val personName = itemVIew.findViewById<TextView>(R.id.item_cast_list_name)
    }

}