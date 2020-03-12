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

import com.example.messadmin.Utils.FireBaseMethods;
import com.example.messadmin.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText mUserName,mPassword,mEmail;
    private TextView mPleaseWait,login;
    private ProgressBar mProgressBar;
    private Context mContext;
    private Button RegBtn;

    private String email,userName,password;
    private String append="";

    private FireBaseMethods fireBaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext=RegisterActivity.this;
        fireBaseMethods=new FireBaseMethods(mContext);
        Log.d(TAG, "onCreate: started");

        initWidgets();
        setupFirebaseAuth();
        init();
    }
    private void init()
    {

        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=mEmail.getText().toString();
                userName=mUserName.getText().toString();
                password=mPassword.getText().toString();
                Log.d(TAG, "onClick: navigating for registering");

                if (checkInputs(userName,email,password))
                {

                    mProgressBar.setVisibility(View.GONE);
                    mPleaseWait.setVisibility(View.GONE);

                    //fireBaseMethods.createNewUser(userName,email,password);
                    fireBaseMethods.registerNewEmail(email,password,userName);
                }


            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

    }

    private boolean checkInputs(String userName,String email,String password)
    {
        if (isStringNull(userName)||isStringNull(email)||isStringNull(password))
        {
            Log.d(TAG, "checkInputs: A Field  is null");
            Toast.makeText(mContext, "Fill all the Fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initWidgets()
    {
        Log.d(TAG, "initWidgets: Register Activity Widgets initializing");
        mUserName=(EditText)findViewById(R.id.inputName);
        mEmail=(EditText)findViewById(R.id.inputEmail);
        mPassword=(EditText)findViewById(R.id.inputPassword);
        RegBtn=(Button) findViewById(R.id.SignUpbutton);

        mProgressBar=(ProgressBar) findViewById(R.id.loginRequestLoadingProgressBar);
        mPleaseWait=(TextView)findViewById(R.id.textPleaseWait);
        login=(TextView)findViewById(R.id.linkLogin);

        mContext=RegisterActivity.this;
        mProgressBar.setVisibility(View.GONE);
        mPleaseWait.setVisibility(View.GONE);

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

    private void checkifUsernameExists(final String Username) {
        Log.d(TAG, "checkifUsernameExists: Checking If " + Username + " exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(mContext.getString(R.string.dbname_user))
                .orderByChild(mContext.getString(R.string.field_username))
                .equalTo(Username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()) {
                    if (singleSnapShot.exists()) {
                        Log.d(TAG, "checkifUsernameExists: Match Found " + singleSnapShot.getValue(User.class).getUser_name());

                        append = myRef.push().getKey().substring(3, 10);
                        Log.d(TAG, "onDataChange: username alreaady exists,Appendig Random Strings to Mass");

                    }
                }

                String mUserName="";
                mUserName=mUserName + append;

                //add new user to the database

                fireBaseMethods.addNewUser(email,mUserName, "", "", "");
                Toast.makeText(mContext, "Sign Up Succesfull,adding new user", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onDataChange: Sign up succesfull");
                mAuth.signOut();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        Log.d(TAG, "setupFirebaseAuth: checking firbase authentication");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            checkifUsernameExists(userName);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }


            }
        };

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
