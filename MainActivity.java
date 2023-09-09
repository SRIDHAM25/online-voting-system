package com.example.votingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Intent a;
    String email="",password="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            sqlmanager ldb = new sqlmanager(MainActivity.this);
            if ((ldb.checklog()+"").matches("1"))
            {
                email=ldb.getemail();
                password = ldb.getpassword();
                FirebaseAuth auth= FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            a = new Intent(MainActivity.this, tabframe.class);
                            startActivity(a);
                            finish();
                        }
                        else
                        {
                            Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    a=new Intent(MainActivity.this,login.class);
                                    startActivity(a);
                                    finish();
                                }
                            }, 1500);
                        }
                    }
                });
            }
            else
            {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        a=new Intent(MainActivity.this,login.class);
                        startActivity(a);
                        finish();
                    }
                }, 1500);
            }
        }
        catch(Exception e){
            System.out.println("error in main activity "+e);
        }
    }
}