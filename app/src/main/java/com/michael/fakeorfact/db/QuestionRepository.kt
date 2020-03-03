package com.michael.fakeorfact.db

import androidx.lifecycle.MutableLiveData

interface QuestionRepository {
    fun getQuestions(category: String): MutableLiveData<List<Question>>
    fun createGame(code: String, firstPlayer: String)
    fun deleteGame(code: String)
    fun addPlayer(code: String, player: String): String
    fun removePlayer(code: String, player:String)
}