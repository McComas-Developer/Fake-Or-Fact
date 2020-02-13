package com.michael.fakeorfact.db

data class Question(val Answer: Boolean, val Question: String){
    constructor(): this(false, "")
}