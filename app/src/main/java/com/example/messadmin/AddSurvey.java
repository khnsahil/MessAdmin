package com.example.messadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messadmin.Login.HomeActivity;
import com.example.messadmin.Login.LoginActivity;
import com.example.messadmin.models.Survey;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class AddSurvey extends AppCompatActivity {

    private static final String TAG = "AddSurvey";

    EditText op1,op2,op3,op4;
    Button add_survey;
    TextView tv_date;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    String user_id="";
    String res_id="";
    ArrayList<String> list=new ArrayList<>();
    String tomorrowAsString;

    private SharedPreferences mPeferences;
    private SharedPreferences.Editor mEditor;
    String pr_us_id;
    String pr_mess_id;
    private Context mContext=AddSurvey.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_survey);

        op1=findViewById(R.id.et_op1);
        op2=findViewById(R.id.et_op2);
        op3=findViewById(R.id.et_op3);
        op4=findViewById(R.id.et_op4);

        tv_date=findViewById(R.id.tv_date);
        add_survey=findViewById(R.id.bt_add_survey);

        setupFirebaseAuth();

        mPeferences= PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor=mPeferences.edit();

        pr_us_id=mPeferences.getString("user_id","");
        pr_mess_id="-"+mPeferences.getString("res_id","");
      //  pr_mess_id="-M-xlkn_sAwzP2Zv2bGR";

        if (pr_mess_id.equals("") || !pr_us_id.equals(user_id))
        {
            Query query = myRef.child("admin_data").child(user_id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.d(TAG, "onDataChange: ds: "+dataSnapshot.toString());
                    // objectMap = (HashMap<String, String>) dataSnapshot.getValue();
                    try {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            Log.d(TAG, "onDataChange: ps: "+postSnapshot.getValue().toString());
                            //objectMap = (HashMap<String, String>) postSnapshot.getValue();
                            list = (ArrayList<String>) postSnapshot.getValue();
                            // list.add(postSnapshot.getValue().toString());

                        }
                        list.removeAll(Collections.singletonList(null));

                        if (!list.isEmpty())
                        {
                            mEditor.putString("user_id",user_id);
                            mEditor.putString("res_id",list.get(0));
                            mEditor.commit();

                            Log.d(TAG, "onDataChange: shared pref res id: "+list.get(0));
                        }


                        Log.d(TAG, "onDataChange: ob: "+list.toString());
                    }
                    catch (Exception e)
                    {
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                    finally {
                        pr_us_id=mPeferences.getString("user_id","");
                        pr_mess_id=mPeferences.getString("res_id","");

                        if (pr_mess_id.equals(""))
                        {
                            Log.d(TAG, "onCreate: mess id "+pr_mess_id);
                            startActivity(new Intent(AddSurvey.this, AddRest.class));
                            finish();
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Log.d(TAG, "onCreate: list: "+list.toString());
        }

        if (pr_mess_id.equals(""))
        {startActivity(new Intent(AddSurvey.this, AddRest.class)); finish();}
        else init();








    }

    private void init() {

        Log.d(TAG, "init: running");
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        final String tomorrowAsString = dateFormat.format(tomorrow);
        tv_date.setText(tomorrowAsString.toString());

        add_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!op1.getText().equals(null) && !op2.getText().equals(null) && !op3.getText().equals(null) && !op4.getText().equals(null))
                {
                    HashMap<String,String> map=new HashMap<>();

                    map.put("op1",op1.getText().toString());
                    map.put("op2",op2.getText().toString());
                    map.put("op3",op3.getText().toString());
                    map.put("op4",op4.getText().toString());



                    Survey survey=new Survey(tomorrowAsString,map);

                    String surveyID=myRef.push().getKey();

                    //String res_id="-M-xlv43EDQCoV1m73bo";


                    myRef.child("survey").child(pr_mess_id).child(tomorrowAsString).setValue(survey);

                    Toast.makeText(AddSurvey.this, "Survey Successfully Added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddSurvey.this, HomeActivity.class));
                    finish();


                }
                else Toast.makeText(AddSurvey.this, "Please Fill All The Options", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //-------------------------------firebase-----stuff-------------------------------------------------------------------------

    private void setupFirebaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();


        Log.d(TAG, "setupFirebaseAuth: checking firbase authentication");

        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user= firebaseAuth.getCurrentUser();

                checkCurrentUser(user);

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

    private void checkCurrentUser(FirebaseUser user)
    {
        if (user==null)
        {
            Intent intent=new Intent(AddSurvey.this, LoginActivity.class);
            startActivity(intent);
            finish();
            Log.d(TAG, "checkCurrentUser: no user logged in");
        }

        else{
            user_id=user.getUid();
            Log.d(TAG, "checkCurrentUser: user id:"+user.getUid()+" logged in");}
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        mAuth.addAuthStateListener(mAuthListener);
    }


    private void updateUI(FirebaseUser currentUser) {
    }



}
