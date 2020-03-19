package com.michael.fakeorfact.db

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.michael.fakeorfact.player.PlayerViewAdapter


class QuestionsImp(): QuestionRepository {
    private val db = FirebaseFirestore.getInstance()    // DB instance
    private var mID: String? = null                     // Keeps track of player ID in DB
    private var mCount = 0                              // Track player count
    private var mAdapter: PlayerViewAdapter? = null     // Adapter for RecyclerView
    private var mAdaptView: RecyclerView? = null        // RecyclerView to update
    // Grab Questions based on Category
    override fun getQuestions(category: String): MutableLiveData<List<Question>> {
        var result = MutableLiveData<List<Question>>()
        db.collection(category).get().addOnSuccessListener {
            result.postValue(it.documents.map {doc ->  doc.toObject(Question::class.java)!! })
            Log.d("Questions: ", "repository got: ${result.toString()}")
        }
        return result
    }
    // Grab current game players
    override fun getPlayers(code: String): MutableLiveData<List<String>> {
        val players = MutableLiveData<List<String>>()
        val list: MutableList<String> = ArrayList()
        // Set up listener
        mCount = 0
        val docRef = db.collection("Games").document(code)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("CreateGame","Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d("CreateGame", "Current data: ${snapshot.data}")
                val map = snapshot.data
                // Reset variables
                list.clear()
                players.postValue(null)
                // If data exists, pull players
                if (map != null) {
                    for ((_, value) in map) {
                        list.add(value.toString())
                        mCount++
                    }
                    players.postValue(list)
                }
                // Update RecyclerView Adapter
                mAdapter = PlayerViewAdapter(list)
                mAdapter!!.notifyDataSetChanged()
            }
            else {
                Log.d("CreateGame", "Current data: null")
            }
        }
        return players
    }
    // Create a game w/ Access code and add its first player
    override fun createGame(code: String, firstPlayer: String, ID: String) {
        val game: MutableMap<String, Any> = HashMap()
        mID = ID
        Log.d("CreateGame", "Got ID = $mID")
        game[mID!!] = firstPlayer
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
    override fun addPlayer(code: String, player: String, ID: String) {
        // TODO: Need to determine if game exists, if so increase com.michael.fakeorfact.player count and use count for
        //  com.michael.fakeorfact.player DB name
        mID = ID
        Log.d("AddPlayer", "Got ID2 = $mID")
        Log.d("AddPlayer", "Got code = $code")
        val data: MutableMap<String, Any> = HashMap()
        data[mID!!] = player

        db.collection("Games").document(code)[data] = SetOptions.merge()
    }
    // Remove player from Game
    override fun removePlayer(code: String, ID: String) {
        val docRef = db.collection("Games").document(code)
        // Remove player from game based on stored ID
        Log.d("RemovePlayer", "Got code = $code")
        val updates = hashMapOf<String, Any>(ID to FieldValue.delete())
        // If more than 1 player, remove player
        if(mCount > 1) {
            docRef.update(updates).addOnCompleteListener { }
            Log.d("RemovePlayer", "Remove com.michael.fakeorfact.player with ID = $ID")
            mCount--
        }
        // If only one player, delete game
        else if(mCount == 1){
            deleteGame(code)
        }
    }
    override fun setAdapter(mAdapt: PlayerViewAdapter, mView: RecyclerView){
        mAdapter = mAdapt
        mAdaptView = mView
        mAdaptView!!.adapter = mAdapter
    }
}