package com.michael.fakeorfact

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class PlayerViewAdapter(private val list: List<String>): RecyclerView.Adapter<PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        Log.d("Michael", "Create View Holder")
        return PlayerViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        Log.d("Michael", "Bind View Holder")
        val player: String = list[position]
        holder.bind(player)
    }

    override fun getItemCount(): Int = list.size

}