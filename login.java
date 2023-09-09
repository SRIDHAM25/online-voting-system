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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class login extends AppCompatActivity {

    Button b1,b2;
    EditText ed1,ed2;
    TextView tv,tv2;
    Intent a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        b1=findViewById(R.id.login);
        b2=findViewById(R.id.create);
        ed1=findViewById(R.id.email);
        ed2=findViewById(R.id.password);
        tv=findViewById(R.id.error);
        tv2=findViewById(R.id.tv);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        sqlmanager ldb = new sqlmanager(login.this);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth b = FirebaseAuth.getInstance();
                try
                {

                    b.sendPasswordResetEmail(ed1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override

                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                tv.setText("Reset email has been sent");
                            }
                            else{
                                tv.setText(task.getException().toString());
                            }

                        }
                    });
                }
                catch(Exception e){
                    tv.setText(e.toString());
                }
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed1.getText().toString().equals("") && !ed2.getText().toString().matches("")) {

                    auth.signInWithEmailAndPassword(ed1.getText().toString(), ed2.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                a = new Intent(login.this, tabframe.class);
                                ldb.droplogin();
                                ldb.login(ed1.getText().toString(),ed2.getText().toString());
                                startActivity(a);
                                finish();
                            }
                            try
                            {
                                if(task.getException()!=null)
                                    throw task.getException();
                            }
                            catch (FirebaseAuthInvalidUserException e){
                                tv.setText("Invalid user account");
                            }
                            catch(FirebaseAuthInvalidCredentialsException e)
                            {
                                tv.setText("Invalid email address or password");
                            }
                            catch(Exception e)
                            {
                                tv.setText(""+e);
                                System.out.println("Error at login"+e);
                            }


                        }
                    });
                }
                else
                {
                    tv.setText("Email or password can't be null");
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=new Intent(login.this,createacc.class);
                startActivity(a);
                finish();
            }
        });
    }
}
