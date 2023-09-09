package com.example.votingproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class manage extends AppCompatActivity {
    LinearLayout l2;
    String k = "";
    String val ="",temp="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        l2=findViewById(R.id.l2);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        fd.getReference().child("myconducts").child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot snap:task.getResult().getChildren())
                {
                    val =snap.getValue().toString();
                    TextView tv = new TextView(manage.this);
                    if(val.matches("1")){
                        k="Active";
                    }
                    else{
                        k="Inactive";
                    }
                    tv.setText(snap.getKey() + " : "+k);
                    tv.setPadding(10, 10, 10, 10);
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    p.setMargins(10, 10, 10, 10);
                    tv.setLayoutParams(p);
                    tv.setBackground(getDrawable(getResources().getIdentifier("gray", "color", getPackageName())));
                    tv.setTextSize(18);
                    tv.setTextColor(getColor(getResources().getIdentifier("white", "color", getPackageName())));
                    tv.setGravity(Gravity.CENTER);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog ad = new AlertDialog.Builder(manage.this)
                                    .setTitle("Change status ?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            temp=val;
                                            if(temp.matches("1")){
                                                temp="0";
                                            }
                                            else{
                                                temp="1";
                                            }
                                            fd.getReference().child("counter").child(snap.getKey()).setValue(temp);
                                            fd.getReference().child("myconducts").child(auth.getUid()).child(snap.getKey()).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        if(val.matches("1")){
                                                            k="Inactive";
                                                            val="0";
                                                            temp ="0";
                                                        }
                                                        else{
                                                            k= "Active";
                                                            val="1";
                                                            temp="1";
                                                        }
                                                        tv.setText(snap.getKey()+" : "+k);
                                                        Toast.makeText(manage.this, "Mode changed successfully", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });


                                        }
                                    })
                                    .setNegativeButton("No",null)
                                    .create();
                            ad.show();



                        }
                    });
                    l2.addView(tv);
                }
            }
        });
    }
}