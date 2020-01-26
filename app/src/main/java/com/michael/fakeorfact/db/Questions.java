package com.michael.fakeorfact.db;

import java.util.HashMap;
import java.util.ArrayList;

// Class stores questions and answers
public class Questions {
    private ArrayList<HashMap<String, String>> test;

    Questions() {
    }

    public ArrayList<HashMap<String, String>> getTest() {
        return test;
    }

    public void setTest(ArrayList<HashMap<String, String>> test) {
        this.test = test;
    }
}
