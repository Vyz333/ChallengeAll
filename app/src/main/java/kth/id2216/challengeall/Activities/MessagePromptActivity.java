package kth.id2216.challengeall.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SpinnerAdapter;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.thomashaertel.widget.MultiSpinner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kth.id2216.challengeall.Objects.ChallengeMessage;
import kth.id2216.challengeall.Objects.User;
import kth.id2216.challengeall.R;


/**
 * A login screen that offers login via email/password.
 */
public class MessagePromptActivity extends AppCompatActivity {
    private Firebase mFirebaseRef;
    protected FirebaseError mFirebaseError;
    private static final String TAG = "MessagePromptActivity";
    private String mKey;
    private List<String> mUsersList;
    private List<String> mUserKeys;
    protected String [ ] mUsersArray;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private MessageTask mAuthTask = null;

    // UI references.
    @Bind(R.id.users_list)
    public MultiSpinner mSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        mFirebaseRef = new Firebase(getString(R.string.firebase_url));
        mKey = getIntent().getStringExtra("key");
        mUserKeys = new ArrayList<>();
        mUsersList = new ArrayList<>();

        spinnerSetup();
    }
    private void spinnerSetup(){
        mFirebaseRef.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                User u = dataSnapshot.getValue(User.class);
                mUsersList.add(u.getFirstName() + " " + u.getLastName());
                mUserKeys.add(dataSnapshot.getKey());
                Log.e(TAG, mUsersList.toString());
                mUsersArray = mUsersList.toArray(new String[mUsersList.size()]);
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) mSpinner.getAdapter();
                adapter.add(u.getFirstName() + " " + u.getLastName());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                mUsersList.remove(dataSnapshot.getValue(User.class));
                mUserKeys.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mUsersArray = mUsersList.toArray(new String[mUsersList.size()]);
        ArrayAdapter<String> adapter= adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(adapter, false, onSelectedListener);
        mSpinner.setAdapter(adapter, false, onSelectedListener);

        // set initial selection
        boolean[] selectedItems = new boolean[adapter.getCount()];
        mSpinner.setSelected(selectedItems);
    }

    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {

        }
    };
    @OnClick(R.id.message_background)
    public void closePrompt(View v){
        onBackPressed();
    }


    @OnClick(R.id.send_message)
    public void startSignupActivity() {
        boolean[] selection = mSpinner.getSelected();
        String uid = mFirebaseRef.getAuth().getUid();
        for(int i=0;i<selection.length;++i){
            if(selection[i])
            {
                String destId = mUserKeys.get(i);
                ChallengeMessage msg = new ChallengeMessage(mKey,uid,new Timestamp(System.currentTimeMillis()),destId);
                mFirebaseRef.child("messages").child(destId).push().setValue(msg);
            }
        }



        onBackPressed();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

}

