package com.michael.fakeorfact.db

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore


class QuestionsImp : QuestionRepository {
    private val db = FirebaseFirestore.getInstance()

    // Grab Questions based on Category
    override fun getQuestions(category: String): MutableLiveData<List<Question>> {
        val result = MutableLiveData<List<Question>>()
        db.collection(category).get().addOnSuccessListener {
            result.postValue(it.documents.map {doc ->  doc.toObject(Question::class.java)!! })
            Log.d("Questions: ", "repository got: $result")
        }
        return result
    }
}