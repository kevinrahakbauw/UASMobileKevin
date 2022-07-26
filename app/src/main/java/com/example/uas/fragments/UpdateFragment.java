package com.example.uas.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uas.Preferences;
import com.example.uas.R;
import com.example.uas.adapters.UpdateFragmentAdapter;
import com.example.uas.models.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private List<Data> listData;
    private UpdateFragmentAdapter mAdapter;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Data");

    TextView title;
    public UpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        title = view.findViewById(R.id.tv_title_update);
        title.setTextSize((float) Preferences.getFontSize(getContext()) + 12);
        String color = Preferences.getBackgroundColor(getContext());
        ConstraintLayout cl = view.findViewById(R.id.cl_update);
        if (color.equalsIgnoreCase("White")){
            cl.setBackgroundColor(Color.WHITE);
        } else if (color.equalsIgnoreCase("Yellow")){
            cl.setBackgroundColor(Color.YELLOW);
        } else if (color.equalsIgnoreCase("Green")){
            cl.setBackgroundColor(Color.GREEN);
        } else if (color.equalsIgnoreCase("Blue")){
            cl.setBackgroundColor(Color.BLUE);
        }
        RelativeLayout placeholder = view.findViewById(R.id.rl_update);
        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RelativeLayout holder = (RelativeLayout) inflate.inflate(R.layout.recycler_list, null);
        placeholder.addView(holder);

        mRecyclerView = view.findViewById(R.id.recycleview_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mProgressBar = view.findViewById(R.id.progress_circle);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listData = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Data menu = postSnapshot.getValue(Data.class);
                    listData.add(menu);
                }
                mAdapter = new UpdateFragmentAdapter(getActivity(), listData);
                mRecyclerView.setAdapter(mAdapter);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }
}