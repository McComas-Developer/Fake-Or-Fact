package com.michael.fakeorfact.db

data class Question(val Answer: Boolean, val Explain: String, val Question: String){
    constructor(): this(false, "", "")
}