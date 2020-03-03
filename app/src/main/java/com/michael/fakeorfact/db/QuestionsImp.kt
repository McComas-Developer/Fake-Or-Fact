package com.michael.fakeorfact.db

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class QuestionsImp(): QuestionRepository {
    private val db = FirebaseFirestore.getInstance()
    // Grab Questions based on Category
    override fun getQuestions(category: String): MutableLiveData<List<Question>> {
        var result = MutableLiveData<List<Question>>()
        db.collection(category).get().addOnSuccessListener {
            result.postValue(it.documents.map {doc ->  doc.toObject(Question::class.java)!! })
            Log.d("Questions: ", "repository got: " + result.toString())
        }
        return result
    }
    // Create a game w/ Access code and add its first player
    override fun createGame(code: String, firstPlayer: String) {
        val game: MutableMap<String, Any> = HashMap()
        game["player"] = firstPlayer
        game["playerCount"] = 1
        db.collection("Games").document(code)
                .set(game)
                .addOnSuccessListener { Log.d("Create Game", "Game successfully created!") }
                .addOnFailureListener { e -> Log.d("Create Game", "Error creating game", e) }
    }
    // Delete Game based on Access Code
    override fun deleteGame(code: String) {
        db.collection("Games").document(code)
                .delete()
                .addOnSuccessListener { Log.d("Delete Game", "Game successfully deleted!") }
                .addOnFailureListener { e -> Log.w("Delete Game", "Error deleting Game", e) }
    }
    // Add Player to an Existing Game
    override fun addPlayer(code: String, player: String): String {
        // TODO: Need to determine if game exists, if so increase player count and use count for
        //  player DB name
        val data: MutableMap<String, Any> = HashMap()
        data["player"] = player

        db.collection("Games").document(code)[data] = SetOptions.merge()
        return player
    }
    // Remove player from Game
    override fun removePlayer(code: String, player: String) {
        //TODO: Need way to fetch player DB Name, not Screen Name
        val docRef = db.collection("Games").document(code)

        // Remove the 'capital' field from the document
        val updates = hashMapOf<String, Any>(
                "player" to FieldValue.delete()
        )
        docRef.update(updates).addOnCompleteListener { }
    }
}