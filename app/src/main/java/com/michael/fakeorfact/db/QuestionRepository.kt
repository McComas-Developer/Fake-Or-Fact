package com.michael.fakeorfact.db

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.michael.fakeorfact.PlayerViewAdapter
import com.michael.fakeorfact.PlayerViewHolder

interface QuestionRepository {
    fun getQuestions(category: String): MutableLiveData<List<Question>>
    fun getPlayers(code: String): MutableLiveData<List<String>>
    fun setAdapter(mAdapt: PlayerViewAdapter, mView: RecyclerView)
    fun createGame(code: String, firstPlayer: String)
    fun deleteGame(code: String)
    fun addPlayer(code: String, player: String): String
    fun removePlayer(code: String, player:String)
}