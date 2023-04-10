package com.example.noteanalytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.noteanalytics.Adapter.CategoryAdapter;
import com.example.noteanalytics.Adapter.NoteAdapter;
import com.example.noteanalytics.Model.Category;
import com.example.noteanalytics.Model.Note;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class NoteActivity extends AppCompatActivity {

    private RecyclerView rvNote;
    private NoteAdapter noteAdapter;
    private FirebaseFirestore db;
    private ArrayList<Note> noteArrayList;


    Calendar calendar= Calendar.getInstance();
    int hours = calendar.get(Calendar.HOUR);
    int minutes = calendar.get(Calendar.MINUTE);
    int seconds = calendar.get(Calendar.SECOND);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        rvNote = findViewById(R.id.note_recylerview);
        noteAdapter = new NoteAdapter(new ArrayList<Note>());
        rvNote.setLayoutManager(new LinearLayoutManager(this));
        rvNote.setAdapter(noteAdapter);

        db = FirebaseFirestore.getInstance();
        noteArrayList = new ArrayList<>();

        getNoteFromFirestore();
        screenTrack("NoteActivity");
    }

    private void getNoteFromFirestore() {
        db.collection("notes")
                .whereEqualTo("id", getIntent().getStringExtra("categoryId"))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Note note = document.toObject(Note.class);
                            note.setId(document.getId());
                            noteArrayList.add(note);
                        }
                        noteAdapter = new NoteAdapter(noteArrayList);
                        RecyclerView rvNotes = findViewById(R.id.note_recylerview);
                        rvNotes.setAdapter(noteAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("noha", "Failed to load Categories ", e);
                    }
                });
    }

    public void screenTrack(String name){
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle=new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME,name);
        // to Know what the screen he is in
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS,"Analytic");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW,bundle);
    }

    protected void onPause(){

        Calendar calendar= Calendar.getInstance();
        int hours2 = calendar.get(Calendar.HOUR);
        int minutes2 = calendar.get(Calendar.MINUTE);
        int seconds2 = calendar.get(Calendar.SECOND);

        int h =hours2 - hours;
        int m =minutes2 - minutes ;
        int s =seconds2 - seconds ;

        HashMap<String,Object> users =new HashMap<>();
        users.put("hours",h);
        users.put("minutes",m);
        users.put("seconds",s);

        FirebaseFirestore db=FirebaseFirestore.getInstance();

        db.collection("users").add(users)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference){
                        Log.e("n", "add");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("n", "fail");
                    }
                });

        Log.e("hour",String.valueOf(h));
        Log.e("minute",String.valueOf(m));
        Log.e("second",String.valueOf(s));
        super.onPause();


    }


}