package com.bbj.kinono.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bbj.kinono.R
import com.bbj.kinono.domain.models.FoundedMovie
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class SearchResultAdapter(context : Context, private val itemClick : OnListItemClick) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    private val res = context.resources

    private val list = arrayListOf<FoundedMovie>()

    fun add(element : FoundedMovie){
        list.add(element)
        notifyItemChanged(list.lastIndex)
    }

    fun clearList(){
        val size = list.size
        list.clear()
        notifyItemRangeRemoved(0,size)
    }

    fun addAll(elements: List<FoundedMovie>){
        val lastIndex = list.lastIndex
        list.addAll(elements)
        notifyItemRangeInserted(if (lastIndex == 0) 0 else (lastIndex + 1),elements.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_search_result,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movieName.text = list[position].name
        holder.movieRating.text = list[position].rating + "/10"
        holder.movieGenre.text = res.getString(R.string.genre_sample,list[position].genres)
        holder.movieYear.text = res.getString(R.string.year_sample,list[position].year)
        holder.movieCountry.text = res.getString(R.string.country_sample,list[position].countries)

        val posterUrl = list[position].posterURL
        if (posterUrl == "res") {
            holder.moviePoster.setImageResource(R.color.white_dark)
        } else {
            Picasso.get()
                .load(list[position].posterURL)
                .placeholder(R.color.white_dark)
                .error(R.color.white_dark)
                .fit()
                .into(holder.moviePoster)
            holder.itemView.setOnClickListener {
                itemClick.click(list[position].id)
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val moviePoster = itemView.findViewById<ShapeableImageView>(R.id.item_search_poster)
        val movieName = itemView.findViewById<TextView>(R.id.item_search_name)
        val movieRating = itemView.findViewById<TextView>(R.id.item_search_rating)
        val movieYear = itemView.findViewById<TextView>(R.id.item_search_year)
        val movieCountry = itemView.findViewById<TextView>(R.id.item_search_country)
        val movieGenre = itemView.findViewById<TextView>(R.id.item_search_genre)
    }

}