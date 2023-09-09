package com.example.votingproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class account extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView delete,logout,reset,email,manage;
        manage = getActivity().findViewById(R.id.manage);
        delete = getActivity().findViewById(R.id.delete);
        logout = getActivity().findViewById(R.id.logout);
        sqlmanager lsq = new sqlmanager(getContext());
        email= getActivity().findViewById(R.id.email);
        reset = getActivity().findViewById(R.id.reset);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Intent a = new Intent(getContext(), MainActivity.class);
        email.setText(auth.getCurrentUser().getEmail().toString());
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b= new Intent(getContext(),manage.class);
                startActivity(b);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete Account");
                builder.setMessage("Are you sure you want to delete?");
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            FirebaseDatabase fd = FirebaseDatabase.getInstance();
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            fd.getReference().child("card").child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    String no=task.getResult().getValue().toString();
                                    fd.getReference().child("card").child(auth.getUid()).removeValue();
                                    fd.getReference().child("card").child(no).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                            lsq.droplogin();
                                            auth.getCurrentUser().delete();
                                            startActivity(a);
                                            getActivity().finish();}
                                            else{
                                                Toast.makeText(getContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Error "+e, Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                        else
                        {}
                    }
                };
                builder.setPositiveButton("Yes", dialogClickListener);
                builder.setNegativeButton("No", dialogClickListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //**************************

        //logout*****************************
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Logout");
                builder.setMessage("Are you sure, you want to logout?");
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            lsq.droplogin();
                            startActivity(a);
                            getActivity().finish();
                        }
                        else
                        {

                        }
                    }
                };
                builder.setPositiveButton("Yes", dialogClickListener);
                builder.setNegativeButton("No", dialogClickListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //**************************************

        //reset**********************************
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    auth.sendPasswordResetEmail(lsq.getemail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(), "Reset link has been sent to your mail", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                try {
                                    throw task.getException();
                                }
                                catch(Exception e){
                                    Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });
                }
                catch(Exception e){
                    Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //******************************************

    }
}