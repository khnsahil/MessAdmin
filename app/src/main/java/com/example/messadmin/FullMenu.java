package com.example.messadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messadmin.Login.HomeActivity;
import com.example.messadmin.Login.LoginActivity;
import com.example.messadmin.models.MenuItem;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FullMenu extends AppCompatActivity {
    private static final String TAG = "FullMenu";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userId;
    HashMap<String,Integer> objectMap;
    ArrayList<MenuItem> list=new ArrayList<>();

    RecyclerView recyclerView;
    MyManageMenuRvAdapter myMenuRvAdapter;
    Context context=FullMenu.this;
    TextView name;
    EditText item_name,item_price;
    Button save,add;
    RelativeLayout relativeLayout;
    String res_id="-M4jUfnrJZyi2MwKspJk";

    private SharedPreferences mPeferences;
    private SharedPreferences.Editor mEditor;
    String pr_us_id;
    String pr_mess_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_menu);


        setupFirebaseAuth();

        name=findViewById(R.id.tv_mess_name);
        item_name=findViewById(R.id.et_item_name);
        item_price=findViewById(R.id.et_item_price);
        save=findViewById(R.id.bt_save);
        add=findViewById(R.id.bt_add);

        relativeLayout=findViewById(R.id.rel_full_menu);


        mPeferences= PreferenceManager.getDefaultSharedPreferences(context);
        mEditor=mPeferences.edit();
        pr_us_id=mPeferences.getString("user_id","");
        pr_mess_id="-"+mPeferences.getString("res_id","");

        if (pr_mess_id!=null)
            res_id=pr_mess_id;


        Query query = myRef.child("menu").child(res_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    MenuItem menuItem=new MenuItem();
                    for (DataSnapshot snapshot1:snapshot.getChildren())
                    {
                        if (snapshot1.getKey().equals("price")) menuItem.setPrice(Integer.parseInt(snapshot1.getValue().toString()));
                        else  menuItem.setName(snapshot1.getValue().toString());
                        //Log.d(TAG, "onDataChange: inner most "+snapshot1.toString());
                    }

                    Log.d(TAG, "onDataChange: my menu item: "+menuItem.toString());
                    list.add(menuItem);
//
//                    Log.d(TAG, "onDataChange: objmap "+snapshot.getValue().toString());
//                    //MenuItem menuItem=(MenuItem) snapshot.getValue(MenuItem.class);
//                    //objectMap = (HashMap<String, Integer>) snapshot.getValue();
//
//                    if (objectMap!=null) init();
//                    Log.d(TAG, "onDataChange: menu item: "+objectMap.toString());
                }

               // if (!list.isEmpty()) init();
                init();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void init()
    {
        Log.d(TAG, "init: called");
//        for (Map.Entry mapel:objectMap.entrySet())
//        {
//            String key=(String) mapel.getKey();
//            Integer val=Integer.parseInt(mapel.getValue().toString());
//
//            list.add(new MenuItem(key,val));
//
//        }

        recyclerView=findViewById(R.id.rv_menu);
        myMenuRvAdapter=new MyManageMenuRvAdapter(list,context);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myMenuRvAdapter);
        myMenuRvAdapter.notifyDataSetChanged();
        enableSwipeToDeleteAndUndo();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(item_name.getText()) && !TextUtils.isEmpty(item_price.getText()))
                {
                    MenuItem menuItem=new MenuItem(item_name.getText().toString(),Integer.parseInt(item_price.getText().toString()));
                    list.add(menuItem);
                    myMenuRvAdapter.notifyDataSetChanged();
                    item_price.setText("");
                    item_name.setText("");
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child("menu").child(res_id).setValue(list);
                startActivity(new Intent(FullMenu.this, HomeActivity.class));
                finish();


            }
        });

    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final MenuItem item = myMenuRvAdapter.getData().get(position);

                myMenuRvAdapter.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(relativeLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        myMenuRvAdapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
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
            Intent intent=new Intent(FullMenu.this, LoginActivity.class);
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
