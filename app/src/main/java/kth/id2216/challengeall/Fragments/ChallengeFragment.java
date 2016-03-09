package kth.id2216.challengeall.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kth.id2216.challengeall.Activities.LoginActivity;
import kth.id2216.challengeall.Activities.MessagePromptActivity;
import kth.id2216.challengeall.Objects.Challenge;
import kth.id2216.challengeall.Objects.User;
import kth.id2216.challengeall.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChallengeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChallengeFragment extends Fragment {
    private String mKey;
    private String mAuthor;
    private Firebase mFirebaseRef;
    private Challenge mChallenge;
    private User mUser;
    private String mUserId;

    @Bind(R.id.title_text)
    public TextView mTitleView;
    @Bind(R.id.category_text)
    public TextView mCategoryView;
    @Bind(R.id.imageView)
    public ImageView mImageView;
    @Bind(R.id.description_text)
    public TextView mDescriptionView;
    @Bind(R.id.page_buttons)
    public View mPageButtons;


    public ChallengeFragment() {
        // Required empty public constructor
    }

    public static ChallengeFragment newInstance(Bundle args) {
        ChallengeFragment fragment = new ChallengeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseRef = new Firebase(getString(R.string.firebase_url));
        Bundle args = savedInstanceState == null ? getArguments() : savedInstanceState;
        if (args != null) {
            mKey = args.getString("id");
            mAuthor = args.getString("author");
            mChallenge = (Challenge) args.getSerializable("challenge");
            if (args.containsKey("user")) {
                mUser = (User) args.getSerializable("user");
                mUserId = args.getString("user_id");
            } else {
                String json = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("user", null);
                mUser = new Gson().fromJson(json, User.class);
                mUserId = mFirebaseRef.getAuth().getUid();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_challenge, container, false);
        ButterKnife.bind(this, view);
        mTitleView.setText(mChallenge.getTitle());
        mDescriptionView.setText(mChallenge.getDescription());
        mCategoryView.setText(mChallenge.getCategory());

        Bitmap b = mChallenge.getBitmap();
        if (b != null) mImageView.setImageBitmap(b);

        if (mUser.getChallenges().containsKey(mKey)) {
            mPageButtons.setVisibility(View.GONE);
        } else {
            mPageButtons.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("challenge", mChallenge);
        outState.putSerializable("user", mUser);
        outState.putString("id", mKey);
        outState.putString("author", mAuthor);
        outState.putString("user_id", mUserId);
    }

    /*OnClick Callback for Accept Button, does the following:
    1. Add challenge ID to User Object in Firebase
    2. Update Shared Preferences Object.
    3. Returns to the previous fragment (pops fragments backstack)
    4. Displays a lovely toast. :)
    */
    @OnClick(R.id.acceptButton)
    public void acceptButtonAction(View view) {
        //Firebase Entry
        Map<String, Object> m = new HashMap<String, Object>();
        m.put(mKey, true);
        Firebase ref = mFirebaseRef.child("users/" + mUserId + "/challenges");
        ref.updateChildren(m);

        //Shared Preferences Update
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        mUser.getChallenges().put(mKey, true);
        SharedPreferences.Editor editor = activity.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit();
        editor.putString("user", new Gson().toJson(mUser));
        editor.commit();

        //Return to previous fragment.
        activity.getSupportFragmentManager().popBackStack();

        //Toast
        String title = mChallenge.getTitle();
        String toast;
        if (title.toLowerCase().contains("challenge")) {
            toast = title.toUpperCase() + " ACCEPTED!";
        } else {
            toast = title.toUpperCase() + " CHALLENGE ACCEPTED!";
        }

        Toast.makeText(activity, toast, Toast.LENGTH_SHORT);


    }

    @OnClick(R.id.declineButton)
    public void declineButtonAction(View view) {
        //TODO: Actually do something when a user declines a challenge (Or not)
        getActivity().getSupportFragmentManager().popBackStack();
    }
    @OnClick(R.id.shareButton)
    public void shareButtonAction(View view) {
        Intent intent = new Intent(getActivity(), MessagePromptActivity.class);
        intent.putExtra("key", mKey);
        startActivityForResult(intent, 1);
    }
}
