package com.michael.fakeorfact.db;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static android.content.ContentValues.TAG;


public class FirebaseDb {

    private List<Map<String, String>> questionList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Get questions based on chosen category
    public void getQuestions(String category) {
        DocumentReference docRef = db.collection("Questions").document("Art");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        questionList = (List<Map<String, String>>) document.get("test");
                        Log.d("Elijah", "Q list == null in getQuestions call  :  " +(questionList == null));

                        Log.v("DatabaseGrab3", "TEST1: " + questionList);
                        Log.v("DatabaseGrab3", "TEST2: " + questionList.get(0));
                        Log.v("DatabaseGrab3", "TEST3: " + questionList.get(1));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public List<Map<String, String>> getQuestionList(){
        Log.d("Elijah", "Q list == null in getQuestionsList call  :  " +(questionList == null));
        return questionList;
    }
}
