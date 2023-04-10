package com.example.noteanalytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.noteanalytics.Adapter.CategoryAdapter;
import com.example.noteanalytics.Model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView rvCategory;
    private CategoryAdapter categoryAdapter;
    private FirebaseFirestore db;
    private ArrayList<Category> categoryArrayList;

    Calendar calendar= Calendar.getInstance();
    int hours = calendar.get(Calendar.HOUR);
    int minutes = calendar.get(Calendar.MINUTE);
    int seconds = calendar.get(Calendar.SECOND);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


                rvCategory = findViewById(R.id.category_recylerview);


                db = FirebaseFirestore.getInstance();
                categoryArrayList = new ArrayList<>();

                getCategoryDataFromFirestore();
                screenTrack("CategoryActivity");
            }

            private void getCategoryDataFromFirestore(){
                db.collection("Categories")
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Category category = document.toObject(Category.class);
                                category.setCategoryName(document.getString("title"));
                                categoryArrayList.add(category);
                            }

                            categoryAdapter = new CategoryAdapter(categoryArrayList);
                            rvCategory.setLayoutManager(new LinearLayoutManager(this));
                            rvCategory.setAdapter(categoryAdapter);
                        })
                        .addOnFailureListener(e -> Log.e("n", "Failed to load Categories"));
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
