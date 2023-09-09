package com.example.votingproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class conduct extends AppCompatActivity {
    TextView submit ;
    EditText name;
    ArrayList<String> ar = new ArrayList<>();
    ArrayList<String> br= new ArrayList<>();
    ImageView iv,iv2;
     LinearLayout lv,lv2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conduct);
        submit= findViewById(R.id.submit);
        iv = findViewById(R.id.iv);
        lv = findViewById(R.id.lout);
        lv2=findViewById(R.id.lout2);;
        iv2 = findViewById(R.id.iv2);
        name=findViewById(R.id.name);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 EditText ed = new EditText(conduct.this);
                 ed.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog dialog = new AlertDialog.Builder(conduct.this)
                        .setTitle("Add a new voter")
                        .setMessage("Add aadhaar number of voter?")
                        .setView(ed)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(ed.getText().length()==12)
                                {
                                    int f = 0;
                                    String g = ed.getText().toString();
                                    for (int i = 0; i < ar.size(); i++) {
                                        if(ar.get(i).matches(g)){
                                            f=1;
                                        }
                                    }
                                    TextView tv = new TextView(conduct.this);
                                    tv.setText(ed.getText().toString());
                                    tv.setPadding(10,10,10,10);
                                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    p.setMargins(10,5,10,5);
                                    tv.setTextSize(15);
                                    tv.setBackground(getDrawable(getResources().getIdentifier("black","color",getPackageName())));
                                    tv.setLayoutParams(p);
                                    if(f==0) {
                                        lv.addView(tv);
                                        ar.add(g);
                                    }
                                    else
                                        Toast.makeText(conduct.this, "Aadhaar number already added", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(conduct.this, "Invalid aadhaar number", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {

                if(!name.getText().toString().matches("")) {
                    try {
                        FirebaseDatabase fd = FirebaseDatabase.getInstance();
                        fd.getReference().child("elections").child(name.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {

                                        if(ar.size()!=0&&br.size()!=0) {

                                            if (task.getResult().getValue() == null)
                                            {

                                                fd.getReference().child("counter").child(name.getText().toString()).setValue((1));
                                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                                fd.getReference("elections").child(name.getText().toString()).setValue(auth.getUid());
                                                for (int i = 0; i < br.size(); i++) {
                                                    fd.getReference().child("contestant list").child(name.getText().toString()).child(br.get(i)).setValue(("0") + "");
                                                }

                                                for (int i = 0; i < ar.size()-1; i++) {
                                                    fd.getReference().child("voter list").child(name.getText().toString()).child(ar.get(i)).setValue((0) + "");
                                                }
                                                fd.getReference().child("voter list").child(name.getText().toString()).child(ar.get(ar.size()-1)).setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(conduct.this, "Successfully created election", Toast.LENGTH_SHORT).show();
                                                            fd.getReference().child("myconducts").child(auth.getUid()).child(name.getText().toString()).setValue(1);
                                                            finish();
                                                            
                                                        }
                                                        else{
                                                            try{
                                                                throw task.getException();
                                                                
                                                            }
                                                            catch (Exception e){
                                                                Toast.makeText(conduct.this, "Error "+e, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                            else{
                                                Toast.makeText(conduct.this, "Election name must be unique", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else{
                                            Toast.makeText(conduct.this, "Should contain atleast one voter/constant", Toast.LENGTH_SHORT).show();
                                        }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(conduct.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch(Exception e)
                    {
                        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        LinearLayout l = new LinearLayout(conduct.this);
                        l.setOrientation(LinearLayout.VERTICAL);
                        l.setBackground(getDrawable(getResources().getIdentifier("white","color",getPackageName())));
                        l.setLayoutParams(p);
                        TextView tv = new TextView(conduct.this);
                        tv.setLayoutParams(p);
                        tv.setTextSize(18);
                        tv.setText("Error : "+e+"");
                        tv.setTextColor(getColor(getResources().getIdentifier("black","color",getPackageName())));
                        Button b = new Button(conduct.this);
                        b.setText("Ok");
                        b.setLayoutParams(p);
                        b.setTextColor(getColor(getResources().getIdentifier("black","color",getPackageName())));
                        b.setBackground(getDrawable(getResources().getIdentifier("ww","color",getPackageName())));
                        l.addView(tv);
                        l.addView(b);
                        tv.setLayoutParams(p);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(conduct.this);
                                dialog.setView(l);
                        final AlertDialog  ad =  dialog.create();
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ad.dismiss();
                            }
                        });
                       ad.show();

                    }
                }
                else
                {
                    Toast.makeText(conduct.this, "Invalid election name", Toast.LENGTH_SHORT).show();
                }

            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                EditText ed = new EditText(conduct.this);
                ed.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                AlertDialog dialog = new AlertDialog.Builder(conduct.this)
                        .setTitle("Add a new contestant")
                        .setMessage("Add full name number of contestant")
                        .setView(ed)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!ed.getText().toString().matches(""))
                                {
                                    int f = 0;
                                    String g = ed.getText().toString();
                                    for (int i = 0; i < br.size(); i++) {
                                        if(br.get(i).matches(g)){
                                            f=1;
                                        }
                                    }
                                    TextView tv = new TextView(conduct.this);
                                    tv.setText(ed.getText().toString());
                                    tv.setPadding(10,10,10,10);
                                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    p.setMargins(10,5,10,5);
                                    tv.setTextSize(15);
                                    tv.setBackground(getDrawable(getResources().getIdentifier("black","color",getPackageName())));
                                    tv.setLayoutParams(p);
                                    if(f==0) {
                                        lv2.addView(tv);
                                        br.add(g);
                                    }
                                    else
                                        Toast.makeText(conduct.this, "Constant already added", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(conduct.this, "Invalid aadhaar number", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

                TextView tv = new TextView(conduct.this);
                tv.setText(ed.getText().toString());
                tv.setPadding(10,10,10,10);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                p.setMargins(10,5,10,5);
                tv.setTextSize(15);
                tv.setBackground(getDrawable(getResources().getIdentifier("black","color",getPackageName())));
                tv.setLayoutParams(p);
            }
        });
    }

}