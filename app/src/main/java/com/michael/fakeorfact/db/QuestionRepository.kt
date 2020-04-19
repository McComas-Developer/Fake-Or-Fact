package com.michael.fakeorfact.db

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.michael.fakeorfact.player.PlayerViewAdapter

interface QuestionRepository {
    fun getQuestions(category: String): MutableLiveData<List<Question>>
    fun getPlayers(code: String): MutableLiveData<List<String>>
    fun setAdapter(mAdapt: PlayerViewAdapter, mView: RecyclerView)
    fun createGame(code: String, firstPlayer: String, ID: String, From: Context)
    fun deleteGame(code: String)
    fun addPlayer(code: String, player: String, ID: String)
    fun removePlayer(code: String, ID: String)
}