package com.michael.fakeorfact.db

import androidx.lifecycle.MutableLiveData

interface QuestionRepository {
    fun getQuestions(category: String): MutableLiveData<List<Question>>
}