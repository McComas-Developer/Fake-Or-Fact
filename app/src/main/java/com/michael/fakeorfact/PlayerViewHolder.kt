package com.michael.fakeorfact

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerViewHolder (inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.view_players, parent, false)) {

    private var mPlayer: TextView? = null

    init {
        mPlayer = itemView.findViewById(R.id.txt_player)
    }

    fun bind(player: String) {
        mPlayer?.text = player
    }

}