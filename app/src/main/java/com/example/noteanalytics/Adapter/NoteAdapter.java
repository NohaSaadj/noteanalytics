package com.example.noteanalytics.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteanalytics.Model.Category;
import com.example.noteanalytics.Model.Note;
import com.example.noteanalytics.NoteDetailsActivity;
import com.example.noteanalytics.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder>{

    private ArrayList<Note> NoteData;
    private FirebaseFirestore db;
    public Context context;


    public NoteAdapter(ArrayList<Note> NoteData) {
        this.NoteData = NoteData;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView noteTitle;
        public TextView contentTitle;
        public CardView itemCard;

        public MyViewHolder(View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title_txt);
            contentTitle = itemView.findViewById(R.id.note_content_txt);
            itemCard = itemView.findViewById(R.id.note_item);
        }
    }

    @NonNull
    @Override
    public NoteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card, parent, false);
        context = parent.getContext();
        db = FirebaseFirestore.getInstance();
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.MyViewHolder holder, int position) {


            final Note note = NoteData.get(position);
            holder.noteTitle.setText(note.getTitle());
            holder.contentTitle.setText(note.getContent());

            holder.itemCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(holder.noteTitle.getContext());
                    Bundle bundle=new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"NoteScreen");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Note");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"cardItem");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);

                    Intent intent = new Intent(context, NoteDetailsActivity.class);
                    intent.putExtra("noteId", note.getId());
                    Log.d("noteAdapterId", "noteId: " + note.getId());
                    context.startActivity(intent);
                }
            });
        }

    @Override
    public int getItemCount() {
        return NoteData.size();
    }

}
