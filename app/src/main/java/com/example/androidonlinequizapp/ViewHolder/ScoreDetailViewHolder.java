package com.example.androidonlinequizapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidonlinequizapp.R;

public class ScoreDetailViewHolder extends RecyclerView.ViewHolder {
    public TextView txt_name, txt_score;

    public ScoreDetailViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_name = (TextView)itemView.findViewById(R.id.txt_nama);
        txt_score = (TextView)itemView.findViewById(R.id.txt_Score);



    }
}
