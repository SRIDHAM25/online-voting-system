package com.example.votingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class result extends AppCompatActivity {

    TextView tv;
    EditText ed;
    LinearLayout ll;
    String ele="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ed=findViewById(R.id.ed);
        ll=findViewById(R.id.ll);
        tv=findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                ele= ed.getText().toString();
                if(ele.matches("")){
                    Toast.makeText(result.this, "Invalid election name", Toast.LENGTH_SHORT).show();
                }
                else{
                    try{
                        FirebaseDatabase fd = FirebaseDatabase.getInstance();
                        fd.getReference().child("contestant list").child(ele).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    ll.removeAllViews();
                                for(DataSnapshot snap : task.getResult().getChildren()) {
                                    TextView tv = new TextView(result.this);
                                    tv.setText(snap.getKey() + " : " + snap.getValue());
                                    tv.setPadding(10, 10, 10, 10);
                                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    p.setMargins(10, 10, 10, 10);
                                    tv.setLayoutParams(p);
                                    tv.setBackground(getDrawable(getResources().getIdentifier("gray", "color", getPackageName())));
                                    tv.setTextSize(18);
                                    tv.setTextColor(getColor(getResources().getIdentifier("white", "color", getPackageName())));
                                    tv.setGravity(Gravity.CENTER);
                                    ll.addView(tv);

                                }
                                }
                                else
                                {
                                    Toast.makeText(result.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    catch(Exception e){


                        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(result.this);
                        dialog.setTitle("Error ");
                        dialog.setMessage(""+e);
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        final AlertDialog ad =  dialog.create();

                        ad.show();
                    }
                }
            }
        });
    }
}