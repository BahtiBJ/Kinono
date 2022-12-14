package com.bbj.kinono.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bbj.kinono.R
import com.bbj.kinono.domain.models.FactsModel

class FactListAdapter(context : Context) : RecyclerView.Adapter<FactListAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    private val list = arrayListOf<FactsModel>()

    fun add(element : FactsModel){
        list.add(element)
        notifyItemChanged(list.lastIndex)
    }

    fun addAll(elements: ArrayList<FactsModel>){
        val result : MutableList<FactsModel> = if (elements.size > 10)
            elements.subList(0,10)
        else elements
        list.addAll(result)
        notifyItemRangeInserted(0,result.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_movie_fact_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.factText.text = list[position].factText
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemVIew : View) : RecyclerView.ViewHolder(itemVIew){
        val factText = itemVIew.findViewById<TextView>(R.id.item_fact_text)
    }

}