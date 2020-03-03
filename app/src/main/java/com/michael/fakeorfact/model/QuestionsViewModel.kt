package com.michael.fakeorfact.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.michael.fakeorfact.db.Question
import com.michael.fakeorfact.db.QuestionRepository
import com.michael.fakeorfact.db.QuestionsImp

class QuestionsViewModel: ViewModel() {
    var currentQuestionIndex = 0
    private val repository: QuestionRepository = QuestionsImp()
    private var questions = MutableLiveData<List<Question>>()

    fun getQuestions(category: String): LiveData<List<Question>> {
        if(questions.value.isNullOrEmpty()) {
            questions = repository.getQuestions(category)
        }
        return questions
    }
    fun createGame(code: String, firstPlayer: String){
        repository.createGame(code, firstPlayer)
    }
    fun deleteGame(code: String){
        repository.deleteGame(code)
    }
    fun addPlayer(code: String, player: String){
        repository.addPlayer(code, player)
    }
    fun removePlayer(code: String, player: String){
        repository.removePlayer(code, player)
    }
}