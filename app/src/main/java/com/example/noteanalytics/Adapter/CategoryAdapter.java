package com.example.noteanalytics.Adapter;

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
import com.example.noteanalytics.NoteActivity;
import com.example.noteanalytics.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{

        private ArrayList<Category> categoryData;
        private FirebaseFirestore db;
        private FirebaseAnalytics mFirebaseAnalytics;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView categoryName;
            public CardView itemCard;

            public MyViewHolder(View itemView) {
                super(itemView);
                categoryName = itemView.findViewById(R.id.category_title_text);
                itemCard = itemView.findViewById(R.id.category_item);
            }
        }

        public CategoryAdapter(ArrayList<Category> categoryData) {
            this.categoryData = categoryData;
            db = FirebaseFirestore.getInstance();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_card, parent, false);
            return new MyViewHolder(itemView);
        }

    @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final Category category = categoryData.get(position);
            holder.categoryName.setText(category.getCategoryName());

            holder.itemCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle=new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"categoryScreen");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"category");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"cardItem");
                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(holder.categoryName.getContext());
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);


                    Intent intent = new Intent(holder.categoryName.getContext(), NoteActivity.class);
                    intent.putExtra("categoryId", category.getId());
                    Log.d("adapterId", "categoryId: " + category.getId());
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        }

    @Override
        public int getItemCount() {
            return categoryData.size();
        }
    }


