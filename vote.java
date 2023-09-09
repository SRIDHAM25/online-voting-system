package com.example.votingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;

public class vote extends AppCompatActivity {
    LinearLayout l ;
    ArrayList<String> ar = new ArrayList();
    TextView tv;
    EditText ed;
    String ele="";
    String aa="";
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        tv=findViewById(R.id.tv);
        l = findViewById(R.id.l2);
        ed= findViewById(R.id.ed);
        b= findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        FirebaseAuth auth=FirebaseAuth.getInstance();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ed.getText().toString().matches(""))
                {
                    try {

                        FirebaseDatabase f = FirebaseDatabase.getInstance();
                        f.getReference("contestant list").child(ed.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                for (DataSnapshot snap : task.getResult().getChildren()) {
                                    ar.add(snap.getKey());
                                }
                            }
                        });
                        FirebaseDatabase fd = FirebaseDatabase.getInstance();
                        fd.getReference().child("counter").child(ed.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().getValue() == (null)) {
                                        Toast.makeText(vote.this, "Does not exists", Toast.LENGTH_SHORT).show();
                                    } else if (task.getResult().getValue().toString().matches("0")) {
                                        Toast.makeText(vote.this, "Voting is inactive now", Toast.LENGTH_SHORT).show();
                                    } else if (task.getResult().getValue().toString().matches(1 + "")) {
                                        fd.getReference("card").child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    fd.getReference("voter list").child(ed.getText().toString()).child(task.getResult().getValue() + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                        @SuppressLint("NewApi")
                                                        @Override
                                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                if (task.getResult().getValue() == null) {
                                                                    Toast.makeText(vote.this, "You are not allowed to vote here", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                } else if (task.getResult().getValue().toString().matches("1")) {
                                                                    Toast.makeText(vote.this, "You have already utilized your vote", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                } else if (task.getResult().getValue().toString().matches("0")) {
                                                                    ele = ed.getText().toString();
                                                                    aa = task.getResult().getKey();
                                                                    ed.setVisibility(View.GONE);
                                                                    tv.setVisibility(View.GONE);
                                                                    Toast.makeText(vote.this, "Loading please wait..", Toast.LENGTH_SHORT).show();

                                                                    for (int i = 0; i < ar.size(); i++) {
                                                                        TextView t = new TextView(vote.this);
                                                                        t.setText(ar.get(i));
                                                                        t.setTextSize(18);
                                                                        t.setPadding(10, 10, 10, 10);
                                                                        t.setGravity(Gravity.CENTER);
                                                                        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                                        p.setMargins(10, 10, 10, 10);
                                                                        t.setLayoutParams(p);
                                                                        t.setBackground(getDrawable(getResources().getIdentifier("gray", "color", getPackageName())));
                                                                        t.setTextColor(getColor(getResources().getIdentifier("white", "color", getPackageName())));
                                                                        t.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View view) {
                                                                                AlertDialog ad = new AlertDialog.Builder(vote.this)
                                                                                        .setTitle("Vote")
                                                                                        .setMessage("You want to vote for " + t.getText())
                                                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                                                Toast.makeText(vote.this, "Successfully voted for " + t.getText(), Toast.LENGTH_SHORT).show();
                                                                                                fd.getReference().child("contestant list").child(ele).child(t.getText().toString()).setValue(ServerValue.increment(1));
                                                                                                fd.getReference().child("voter list").child(ele).child(aa).setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        finish();
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        })
                                                                                        .setNegativeButton("No", null)
                                                                                        .create();
                                                                                ad.show();

                                                                            }
                                                                        });
                                                                        l.addView(t);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                    catch(Exception e){
                        Toast.makeText(vote.this, "An error occurred "+e, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            }
        });

    }
}