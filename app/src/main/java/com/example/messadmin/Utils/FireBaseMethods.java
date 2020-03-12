package com.example.messadmin.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.messadmin.R;

import com.example.messadmin.models.Restaurant;
import com.example.messadmin.models.User;
import com.example.messadmin.models.UserAccountSetting;
import com.example.messadmin.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class FireBaseMethods {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference myRef;

    private Context mContext;

    private EditText eMail,mUserName,mPassword;
    private String userID;

    public FireBaseMethods(Context context)
    {
        mAuth= FirebaseAuth.getInstance();
        mFireBaseDatabase=FirebaseDatabase.getInstance();
        myRef=mFireBaseDatabase.getReference();
        mContext=context;
       // mAuth.addAuthStateListener(mAuthListener);

        if (mAuth.getCurrentUser()!=null)
        {
           userID=mAuth.getCurrentUser().getUid();
        }



    }


//    public boolean checkIfUserNameExists(String username, DataSnapshot dataSnapshot)
//    {
//        Log.d(TAG, "checkIfUserNameExists: navigating to check if username already exists");
//        User user=new User();
//
//
//        if (userID!=null)
//        {
//            for (DataSnapshot ds : dataSnapshot.child(userID).getChildren()) {
//                Log.d(TAG, "checkIfUserNameExists: datasnapshot: " + ds);
//                user.setUser_name(ds.getValue(User.class).getUser_name());
//
//                if (StringManipulation.expandUsername(user.getUser_name()).equals(username)) {
//                    Log.d(TAG, "checkIfUserNameExists: Found A MATCH" + user.getUser_name());
//                    return true;
//
//                }
//
//
//            }
//            return false;
//        }
//        Log.d(TAG, "checkIfUserNameExists: user id not found");
//        return false;
//
//    }

    //updating username in user's and usersetting's nodes
    public void updateUserName(String Username)
    {
        Log.d(TAG, "updateUserName: Updating UserName to "+ Username);

        myRef.child(mContext.getString(R.string.dbname_user))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(Username);

        myRef.child(mContext.getString(R.string.dbname_user_account_setting))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(Username);


    }
    //updating email in user's node
    public void updateEmail(String email)
    {
        Log.d(TAG, "updateUserName: Updating UserName to "+ email);

        myRef.child(mContext.getString(R.string.dbname_user))
                .child(userID)
                .child(mContext.getString(R.string.field_email))
                .setValue(email);



    }
    //updating phone num in user's node
    public void updatePhoneNum(Long phoneNum)
    {
        Log.d(TAG, "updateUserName: Updating UserName to "+ phoneNum);

        myRef.child(mContext.getString(R.string.dbname_user))
                .child(userID)
                .child(mContext.getString(R.string.field_phone_number))
                .setValue(phoneNum);



    }
    //updating userid
    public void updateDisplayName(String DisplayName)
    {
        Log.d(TAG, "updateUserName: Updating UserId to "+ DisplayName);

        myRef.child(mContext.getString(R.string.dbname_user_account_setting))
                .child(userID)
                .child(mContext.getString(R.string.field_display_name))
                .setValue(DisplayName);


    }
    //updating website
    public void updateWebsite(String website)
    {
        Log.d(TAG, "updateUserName: Updating UserId to "+ website);

        myRef.child(mContext.getString(R.string.dbname_user_account_setting))
                .child(userID)
                .child(mContext.getString(R.string.field_website))
                .setValue(website);


    }
    //updating descriptio
    public void updateDescription(String Description)
    {
        Log.d(TAG, "updateUserName: Updating UserId to "+ Description);

        myRef.child(mContext.getString(R.string.dbname_user_account_setting))
                .child(userID)
                .child(mContext.getString(R.string.field_description))
                .setValue(Description);


    }

//    public void createNewUser(final String userName,final String email,String password)
//    {
//        mAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                // Sign in success, update UI with the signed-in user's information
//                Log.d(TAG, "createUserWithEmail:success");
//
//
//                FirebaseUser user = mAuth.getCurrentUser();
//                userID=mAuth.getCurrentUser().getUid();
//                updateUI(user);
//                Toast.makeText(mContext, "Registeration Successful", Toast.LENGTH_SHORT).show();
//                 }
//
//                 else {
//                // If sign in fails, display a message to the user.
//                Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                Toast.makeText(mContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show();
//                updateUI(null);
//            }
//
//            // ...
//        }
//    });
//
//    }

    public void registerNewEmail(final String email, String password, final String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else if(task.isSuccessful()){

                            sendverificationEmail();
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }

                    }
                });
    }


    public void sendverificationEmail()
    {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(mContext, "Verification Email Send", Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                            Toast.makeText(mContext, "Could not send verification Emaol", Toast.LENGTH_SHORT).show();
                        }
                }
            });
        }


    }
    private void updateUI(Object o) {
    };

    public void addNewUser(String email, String user_name, String description, String website, String profile_photo)
    {

        User user=new User(email,1, userID,user_name,"admin");

        myRef.child(mContext.getString(R.string.dbname_user)).child(userID).setValue(user);

        UserAccountSetting userAccountSetting=new UserAccountSetting(
                description,
                user_name,
                0,
                0,
                0,
                profile_photo,
                user_name,
                website);
        myRef.child(mContext.getString(R.string.dbname_user_account_setting)).child(userID).setValue(userAccountSetting);
        Log.d(TAG, "addNewUser: user & user account setting data added");

    }


    public UserSettings getuserAccountSetting(DataSnapshot dataSnapshot)
    {
        Log.d(TAG, "userAccountSetting: retriving user account setting fro firebase");

        UserAccountSetting userAccountSetting=new UserAccountSetting();
        User user=new User();

        for (DataSnapshot ds: dataSnapshot.getChildren())
        {

            try {


                if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_setting))) {
                    Log.d(TAG, "getuserAccountSetting: datasnapshot " + ds);

                    userAccountSetting.setDisplay_name(ds.child(userID).getValue(UserAccountSetting.class).getDisplay_name());
                    userAccountSetting.setDescription(ds.child(userID).getValue(UserAccountSetting.class).getDescription());
                    userAccountSetting.setWebsite(ds.child(userID).getValue(UserAccountSetting.class).getWebsite());
                    userAccountSetting.setFollowers(ds.child(userID).getValue(UserAccountSetting.class).getFollowers());
                    userAccountSetting.setFollowing(ds.child(userID).getValue(UserAccountSetting.class).getFollowing());
                    userAccountSetting.setPosts(ds.child(userID).getValue(UserAccountSetting.class).getPosts());
                    userAccountSetting.setUser_name(ds.child(userID).getValue(UserAccountSetting.class).getUser_name());
                    userAccountSetting.setProfile_photo(ds.child(userID).getValue(UserAccountSetting.class).getProfile_photo());


                    Log.d(TAG, "getuserAccountSetting: " + userAccountSetting.toString());
                }

                if (ds.getKey().equals(mContext.getString(R.string.dbname_user))) {
                    Log.d(TAG, "getuserAccountSetting: datasnapshot " + ds);

                    user.setUser_name(ds.child(userID).getValue(User.class).getUser_name());
                    user.setEmail(ds.child(userID).getValue(User.class).getEmail());
                    user.setPhone_number(ds.child(userID).getValue(User.class).getPhone_number());

                    Log.d(TAG, "getuserAccountSetting: " + user.toString());

                }
            }

            catch (NullPointerException e)
            {
                Log.d(TAG, "getuserAccountSetting: "+e.getMessage());
            }



        }

        return new UserSettings(user,userAccountSetting);

    }

    public void addNewRestaurant(String name, String type, String time, String rating, String price)
    {

        Restaurant restaurant=new Restaurant(name,type,time,price,rating,"https://www.chatelaine.com/wp-content/uploads/2019/01/canada-new-food-guide-2019.jpeg");
        String mResId = myRef.push().getKey();

        myRef.child(mContext.getString(R.string.dbname_restaurant)).child(mResId).setValue(restaurant);

        Log.d(TAG, "addNewRestaurant: Adding new restaurant");
    }


















}
