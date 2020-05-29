package com.example.messadmin.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messadmin.AddRest;
import com.example.messadmin.AddSurvey;
import com.example.messadmin.FullMenu;
import com.example.messadmin.MainActivity;
import com.example.messadmin.R;
import com.example.messadmin.Utils.FireBaseMethods;
import com.example.messadmin.ViewInterestActivity;
import com.example.messadmin.ViewVotesActivity;
import com.example.messadmin.models.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private Context mContext=HomeActivity.this;
    private FireBaseMethods fireBaseMethods=new FireBaseMethods(mContext);

    TextView mess_name,mess_type,rating,timing,price,dish;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;

    String user_id="";
    Map<String, String> objectMap;
    ArrayList<String> list=new ArrayList<>();
    private SharedPreferences mPeferences;
    private SharedPreferences.Editor mEditor;
    String pr_us_id;
    String pr_mess_id="-M4jUfnrJZyi2MwKspJk";
    Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mess_name=findViewById(R.id.tv_mess_name);
        mess_type=findViewById(R.id.tv_mess_type);
        rating=findViewById(R.id.tv_rating);
        timing=findViewById(R.id.tv_timings);
        price=findViewById(R.id.tv_price);
        dish=findViewById(R.id.tv_serving_dish);
        progressBar=findViewById(R.id.pb_main);
        relativeLayout=findViewById(R.id.rel_main);


        setupFirebaseAuth();
//        mPeferences= PreferenceManager.getDefaultSharedPreferences(mContext);
//        mEditor=mPeferences.edit();
//
//         pr_us_id=mPeferences.getString("user_id","");
//         pr_mess_id=mPeferences.getString("res_id","");

        //Log.d(TAG, "onCreate: user: "+pr_us_id+);

//        if (pr_mess_id.equals("") || !pr_us_id.equals(user_id))
//        {
//            Query query = myRef.child("admin_data").child(user_id);
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    Log.d(TAG, "onDataChange: ds: "+dataSnapshot.toString());
//                    // objectMap = (HashMap<String, String>) dataSnapshot.getValue();
//                    try {
//                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
//                        {
//                            Log.d(TAG, "onDataChange: ps: "+postSnapshot.getValue().toString());
//                            //objectMap = (HashMap<String, String>) postSnapshot.getValue();
//                            list = (ArrayList<String>) postSnapshot.getValue();
//                            // list.add(postSnapshot.getValue().toString());
//
//                        }
//                        list.removeAll(Collections.singletonList(null));
//
//                        if (!list.isEmpty())
//                        {
//                            mEditor.putString("user_id",user_id);
//                            mEditor.putString("res_id",list.get(0));
//                            mEditor.commit();
//
//                            Log.d(TAG, "onDataChange: shared pref res id: "+list.get(0));
//                        }
//
//
//                        Log.d(TAG, "onDataChange: ob: "+list.toString());
//                    }
//                    catch (Exception e)
//                    {
//                        Log.d(TAG, "onDataChange: "+e.getMessage());
//                    }
//                    finally {
//                        pr_us_id=mPeferences.getString("user_id","");
//                        pr_mess_id=mPeferences.getString("res_id","");
//
//                        if (pr_mess_id.equals(""))
//                        {
//                            Log.d(TAG, "onCreate: mess id "+pr_mess_id);
//                            startActivity(new Intent(HomeActivity.this, AddRest.class));
//                        }
//                        else   init();
//                    }
//
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//            Log.d(TAG, "onCreate: list: "+list.toString());
//        }

//
//        pr_us_id=mPeferences.getString("user_id","");
//        pr_mess_id=mPeferences.getString("res_id","");
//
//        if (pr_mess_id.equals(""))
//        {
//            Log.d(TAG, "onCreate: mess id "+pr_mess_id);
//            startActivity(new Intent(HomeActivity.this, AddRest.class));
//        }
//        else   init();



        init();




    }

    private void init() {

        Query query=myRef.child("restaurant").child(pr_mess_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d(TAG, "onDataChange: save mess id: "+pr_mess_id);
                Log.d(TAG, "onDataChange: ds: "+dataSnapshot.toString());
                Restaurant restaurant=dataSnapshot.getValue(Restaurant.class);

                mess_name.setText(restaurant.getRes_name());mess_type.setText(restaurant.getRes_type());
                rating.setText(restaurant.getRating()+"\n Rating");
                price.setText(restaurant.getPrice()+" \n Avg. Price");
               timing.setText(restaurant.getTiming()+"\n Timing");

                progressBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);

//                for (DataSnapshot snapshot:dataSnapshot.getChildren())
//                {
//
//                    if (snapshot.getKey().trim().equals("-"+pr_mess_id))
//                    {
//                       objectMap = (HashMap<String, String >) snapshot.getValue();
//
//                        if (objectMap!=null)
//                        {
//                            mess_name.setText(objectMap.get("res_name"));
//                            mess_type.setText(objectMap.get("res_type"));
//                            rating.setText(objectMap.get("rating")+"\n Rating");
//                            price.setText(objectMap.get("price")+" \n Avg. Price");
//                            timing.setText(objectMap.get("timing")+"\n Timing");
//
//                            progressBar.setVisibility(View.GONE);
//                            relativeLayout.setVisibility(View.VISIBLE);
//                        }
////                        Restaurant my_restaurant=snapshot.getValue(Restaurant.class);
////                       restaurant=my_restaurant;n[0-pvb
//
//                        Log.d(TAG, "onDataChange: found: "+objectMap.toString());
//
//                    }
//                }
                //Log.d(TAG, "onDataChange: rest: "+dataSnapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        Log.d(TAG, "init: saved restaurant: "+objectMap.get("res_name"));


        findViewById(R.id.rel_addSurvey).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: starting Add Survey");
                startActivity(new Intent(HomeActivity.this, AddSurvey.class));
            }
        });



        findViewById(R.id.rel_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: starting Add Survey");
                startActivity(new Intent(HomeActivity.this, ViewVotesActivity.class));
            }
        });

        findViewById(R.id.rel_interest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: starting Add Survey");
                startActivity(new Intent(HomeActivity.this, ViewInterestActivity.class));
            }
        });

        findViewById(R.id.rel_full_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: starting Add Survey");
                startActivity(new Intent(HomeActivity.this, FullMenu.class));
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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void checkCurrentUser(FirebaseUser user)
    {
        if (user==null)
        {
            Intent intent=new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            Log.d(TAG, "checkCurrentUser: no user logged in");
        }

        else {
            user_id=user.getUid();
            Log.d(TAG, "checkCurrentUser: user id:" + user.getUid() + " logged in");
            //startActivity(new Intent(HomeActivity.this, HomeActivity.class));

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        mAuth.addAuthStateListener(mAuthListener);
    }

}
