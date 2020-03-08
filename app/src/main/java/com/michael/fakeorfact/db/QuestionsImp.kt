package com.michael.fakeorfact.db

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.michael.fakeorfact.PlayerViewAdapter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class QuestionsImp(): QuestionRepository {
    private val db = FirebaseFirestore.getInstance()
    private var mAdapter: PlayerViewAdapter? = null
    private var mAdaptView: RecyclerView? = null
    // Grab Questions based on Category
    override fun getQuestions(category: String): MutableLiveData<List<Question>> {
        var result = MutableLiveData<List<Question>>()
        db.collection(category).get().addOnSuccessListener {
            result.postValue(it.documents.map {doc ->  doc.toObject(Question::class.java)!! })
            Log.d("Questions: ", "repository got: " + result.toString())
        }
        return result
    }
    // Grab current game players
    override fun getPlayers(code: String): MutableLiveData<List<String>> {
        val players = MutableLiveData<List<String>>()
        val list: MutableList<String> = ArrayList()

        // Set up listener
        val docRef = db.collection("Games").document(code)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("CreateGame","Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d("CreateGame", "Current data: ${snapshot.data}")
                val map = snapshot.data
                list.clear()
                players.postValue(null)
                if (map != null) {
                    for ((_, value) in map) {
                        list.add(value.toString())
                    }
                    players.postValue(list)
                }
                mAdapter = PlayerViewAdapter(list)
                mAdapter!!.notifyDataSetChanged()

            } else {
                Log.d("CreateGame", "Current data: null")
            }
        }
        return players
    }
    // Create a game w/ Access code and add its first player
    override fun createGame(code: String, firstPlayer: String) {
        val game: MutableMap<String, Any> = HashMap()
        game["player"] = firstPlayer
        db.collection("Games").document(code)
                .set(game)
                .addOnSuccessListener { Log.d("CreateGame", "Game successfully created!") }
                .addOnFailureListener { e -> Log.d("CreateGame", "Error creating game", e) }
    }
    // Delete Game based on Access Code
    override fun deleteGame(code: String) {
        Log.d("DeleteGame", "In function!")
        db.collection("Games").document(code)
                .delete()
                .addOnSuccessListener { Log.d("DeleteGame", "Game successfully deleted!") }
                .addOnFailureListener { e -> Log.w("DeleteGame", "Error deleting Game", e) }
    }
    // Add Player to an Existing Game
    override fun addPlayer(code: String, player: String): String {
        // TODO: Need to determine if game exists, if so increase player count and use count for
        //  player DB name
        val id = UUID.randomUUID().toString().substring(0, 15)
        val data: MutableMap<String, Any> = HashMap()
        data[id] = player

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

    override fun setAdapter(mAdapt: PlayerViewAdapter, mView: RecyclerView){
        mAdapter = mAdapt
        mAdaptView = mView
        mAdaptView!!.adapter = mAdapter
    }
}