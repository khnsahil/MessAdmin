package com.example.messadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.example.messadmin.Login.LoginActivity;
import com.example.messadmin.models.MenuItem;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ViewVotesActivity extends AppCompatActivity {
    private static final String TAG = "ViewVotesActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userId;
    HashMap<String,Integer> objectMap=new HashMap<>();
    ArrayList<MenuItem> list=new ArrayList<>();

    RecyclerView recyclerView;
    MyMenuRvAdapter myMenuRvAdapter;
    Context context=ViewVotesActivity.this;
    TextView name;
    String res_id;

    private SharedPreferences mPeferences;
    private SharedPreferences.Editor mEditor;
    String pr_us_id;
    String pr_mess_id;

    ArrayList<String> intrest_list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_votes);

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        String tomorrowAsString = dateFormat.format(tomorrow);

        mPeferences= PreferenceManager.getDefaultSharedPreferences(context);
        mEditor=mPeferences.edit();
        pr_us_id=mPeferences.getString("user_id","");
        pr_mess_id="-"+mPeferences.getString("res_id","");

        setupFirebaseAuth();

        Query query = myRef.child("survey_res").child(pr_mess_id).child(tomorrowAsString);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Log.d(TAG, "onDataChange: snap: "+snapshot.getValue().toString());
                    intrest_list.add(snapshot.getValue().toString().trim());
                }
                if (!intrest_list.isEmpty()) init();
                   // for (DataSnapshot ds:snapshot.getValue())
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {

        for (String item:intrest_list)
        {
            if (objectMap.containsKey(item)) objectMap.put(item,objectMap.get(item)+1);
            else objectMap.put(item,1);

        }

        for (Map.Entry mapel:objectMap.entrySet())
        {
            String key=(String) mapel.getKey();
            Integer val=Integer.parseInt(mapel.getValue().toString());

            list.add(new MenuItem(key,val));

        }

        recyclerView=findViewById(R.id.rv_menu);
        myMenuRvAdapter=new MyMenuRvAdapter(list,context);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myMenuRvAdapter);
        myMenuRvAdapter.notifyDataSetChanged();
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
                    userId=user.getUid();
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
            Intent intent=new Intent(ViewVotesActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            Log.d(TAG, "checkCurrentUser: no user logged in");
        }

        else
            Log.d(TAG, "checkCurrentUser: user id:"+user.getUid()+" logged in");
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
