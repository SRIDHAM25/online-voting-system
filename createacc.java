package com.example.votingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class createacc extends AppCompatActivity {

    Button b1,b2;
    EditText ed1,ed2,ed3,ed4;
    TextView tv;
    Intent a;
    String pw2="",email="",pw="",aadhaar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacc);
        b1=findViewById(R.id.signup);
        b2=findViewById(R.id.blogin);
        tv=findViewById(R.id.error);
        ed1=findViewById(R.id.email);
        ed2=findViewById(R.id.password);
        ed4=findViewById(R.id.aadhaar);
        ed3=findViewById(R.id.rp);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=new Intent(createacc.this,login.class);
                startActivity(a);
                finish();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email =ed1.getText().toString();
                aadhaar=ed4.getText().toString();
                pw=ed2.getText().toString();

                pw2=ed3.getText().toString();
                if(aadhaar.length()!=12){
                    Toast.makeText(createacc.this, "Invalid aadhaar number", Toast.LENGTH_SHORT).show();
                }
                else if(!pw2.matches("")&&!email.matches("")&&!pw.matches("")&&!aadhaar.matches(""))
                {
                    if(ed2.getText().toString().matches(pw))
                    {

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("card");

                        try{
                            db.child(aadhaar).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                          if(task.getResult().getValue()==null){
                                              try
                                              {
                                                  auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                      @Override
                                                      public void onComplete(@NonNull Task<AuthResult> task) {
                                                          if (task.isSuccessful())
                                                          {
                                                              db.child(aadhaar).setValue(auth.getUid());
                                                              db.child(auth.getUid()).setValue(aadhaar);
                                                              Toast.makeText(createacc.this, "Logging in", Toast.LENGTH_SHORT).show();
                                                              sqlmanager ldb = new sqlmanager(createacc.this);
                                                              ldb.droplogin();
                                                              ldb.login(email,pw);
                                                              a=new Intent(createacc.this,tabframe.class);
                                                              startActivity(a);
                                                              finish();
                                                          }
                                                          else
                                                          {
                                                              try
                                                              {
                                                                  throw task.getException();
                                                              }
                                                              catch (FirebaseAuthWeakPasswordException e) {
                                                                  tv.setText("Password too weak");

                                                              }
                                                              catch (FirebaseAuthInvalidCredentialsException e) {
                                                                  tv.setText("Invalid Credentials");
                                                              }
                                                              catch (FirebaseAuthUserCollisionException e) {
                                                                  tv.setText("Email is already in use");
                                                              }
                                                              catch (FirebaseAuthException e) {
                                                                  tv.setText("" + e);
                                                              }
                                                              catch (Exception e) {
                                                                  tv.setText("" + e);
                                                              }
                                                          }
                                                      }
                                                  });
                                              }
                                              catch (Exception e) {
                                                  System.out.println("error at createac " + e);
                                                  tv.setText("" + e);}
                                          }
                                          else{
                                              Toast.makeText(createacc.this, "Aadhaar number taken", Toast.LENGTH_SHORT).show();
                                          }
                                    }
                                    else{
                                        try{
                                        throw task.getException();}
                                        catch(Exception e){
                                            Toast.makeText(createacc.this, "ee "+e, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                        catch (Exception e){
                            Toast.makeText(createacc.this, "error "+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        tv.setText("Password didn't match!");
                    }
                }
                else
                {
                    tv.setText("Invalid user details!");
                }
            }
        });

    }
}
