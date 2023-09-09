package com.example.votingproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class home extends Fragment {

    Intent a;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onResume() {
        TextView conduct,vote,result;
        vote = getActivity().findViewById(R.id.vote);
        conduct=getActivity().findViewById(R.id.conduct);
        result=getActivity().findViewById(R.id.results);
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=new Intent(getContext(),vote.class);
                startActivity(a);

            }
        });
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a= new Intent(getContext(),result.class);
                startActivity(a);
            }
        });
        conduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=new Intent(getContext(),conduct.class);
                startActivity(a);
            }
        });

        super.onResume();
    }
}