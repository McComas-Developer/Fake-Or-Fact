package com.michael.fakeorfact.db

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class QuestionsImp(): QuestionRepository {
    private val db = FirebaseFirestore.getInstance()

    override fun getQuestions(category: String): MutableLiveData<List<Question>> {
        var result = MutableLiveData<List<Question>>()
        db.collection(category).get().addOnSuccessListener {
            result.postValue(it.documents.map {doc ->  doc.toObject(Question::class.java)!! })
            Log.d("Questions: ", "repository got: " + result.toString())
        }
        return result
    }
}