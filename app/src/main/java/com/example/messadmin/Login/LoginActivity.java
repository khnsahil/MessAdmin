package com.example.messadmin.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mMail,mPassword;
    private TextView mPleaseWait;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgressBar=(ProgressBar)findViewById(R.id.loginRequestLoadingProgressBar);
        mMail=(EditText)findViewById(R.id.inputEmail);
        mPassword=(EditText) findViewById(R.id.inputPassword);
        mPleaseWait=(TextView) findViewById(R.id.textPleaseWait);
        mContext=LoginActivity.this;

        Log.d(TAG, "onCreate: started");

        mPleaseWait.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);

        setupFirebaseAuth();
        init();
    }

    private boolean isStringNull(String string)
    {
        Log.d(TAG, "isStringNull: checking if string is null");
        if (string.equals(""))
        {
            return true;
        }
        else return false;


    }

    private void setupFirebaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();

        Log.d(TAG, "setupFirebaseAuth: checking firbase authentication");

        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user= firebaseAuth.getCurrentUser();



                if (user!=null)
                {
                    Log.d(TAG, "onAuthStateChanged: signed in"+user.getUid());
                }
                else
                {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }


            }
        };


    }

    private void init()
    {
        Button loginBtn=(Button)findViewById(R.id.loginbutton);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to login");

                String email=mMail.getText().toString();
                String password=mPassword.getText().toString();

                if (isStringNull(email) || isStringNull(password))
                {
                    Log.d(TAG, "onClick: email or password null");
                    Toast.makeText(mContext, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mPleaseWait.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    Log.d(TAG, "onComplete: Sing in with email complete"+task.isSuccessful());
                                    FirebaseUser user=mAuth.getCurrentUser();

                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        try {

                                            if (user.isEmailVerified())
                                            {
                                                Log.d(TAG, "onComplete: Email is verified");
                                                Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                Log.d(TAG, "onComplete: Emaail not verified");
                                                Toast.makeText(mContext, "Eamil not verified \n Check your MailbBox", Toast.LENGTH_SHORT).show();
                                                mProgressBar.setVisibility(View.GONE);
                                                mPleaseWait.setVisibility(View.GONE);
                                                mAuth.signOut();
                                            }
                                        }
                                        catch (NullPointerException e)
                                        {
                                            Log.d(TAG, "onComplete: Null Pointer Exception"+e.getMessage());

                                        }



                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(mContext, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);

                                        mProgressBar.setVisibility(View.GONE);
                                        mPleaseWait.setVisibility(View.GONE);
                                    }

                                    // ...
                                }
                            });
                }

            }
        });

        TextView signUpLink=(TextView) findViewById(R.id.linkSignUp);
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to register activity");
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }


    private void updateUI(FirebaseUser currentUser) {
    }
}
