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

    private List<String> questions = new ArrayList<>();
    private List<String> answers = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Get questions based on chosen category
    public void getQuestions(String category) {
        DocumentReference docRef = db.collection("Questions").document("Science");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> doc = document.getData();
                        for (Map.Entry<String, Object> docs: doc.entrySet()) {
                            questions.add(document.getString("q"));
                            answers.add(document.getString("ans"));

                            Log.v("DatabaseGrab1", "Question: " + questions);
                            Log.v("DatabaseGrab2", "Answer: " + answers);
                        }
                        Log.v("DatabaseGrab3", "Question: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
