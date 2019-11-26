package com.example.androidonlinequizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidonlinequizapp.Common.Common;
import com.example.androidonlinequizapp.Interface.ItemClickListener;
import com.example.androidonlinequizapp.Interface.RankingCallBack;
import com.example.androidonlinequizapp.Model.QuestionScore;
import com.example.androidonlinequizapp.Model.Ranking;
import com.example.androidonlinequizapp.ViewHolder.RankingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RangkingFragment extends Fragment {

    View myFragment;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking,RankingViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference questionScore,rankingTbl;
    int sum = 0;

    public static RangkingFragment newInstance(){
        RangkingFragment rangkingFragment = new RangkingFragment();
        return rangkingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTbl = database.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_rangking, container, false);

        //Init View
        rankingList = (RecyclerView)myFragment.findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                rankingTbl.child(ranking.getUserName())
                        .setValue(ranking);
                //showRanking();
            }
        });

        //Set Adapter
        FirebaseRecyclerOptions<Ranking> options =
                new FirebaseRecyclerOptions.Builder<Ranking>()
                        .setQuery(rankingTbl, Ranking.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RankingViewHolder rankingViewHolder, int position, @NonNull Ranking ranking) {
                rankingViewHolder.txt_name.setText(ranking.getUserName());
                rankingViewHolder.txt_score.setText(String.valueOf(ranking.getScore()));

                rankingViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };
        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);

        return myFragment;
    }


    private void updateScore(final String userName, final RankingCallBack<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data : dataSnapshot.getChildren())
                        {
                            QuestionScore ques = data.getValue(QuestionScore.class);
                            sum+= Integer.parseInt(ques.getScore());

                        }
                        Ranking ranking = new Ranking(userName,sum);
                        callback.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
