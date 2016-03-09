package kth.id2216.challengeall.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kth.id2216.challengeall.Adapters.ChallengeLinearListAdapter;
import kth.id2216.challengeall.Adapters.ChallengeListAdapter;
import kth.id2216.challengeall.Objects.MultipartFormField;
import kth.id2216.challengeall.Objects.User;
import kth.id2216.challengeall.R;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final int CHALLENGES_IDX = 0;
    private static final int ACHIEVEMENTS_IDX = 1;
    private static final int HISTORY_IDX = 2;
    @Bind(R.id.profile_pic) public ImageView mImageView;
    @Bind(R.id.profile_desc) public TextView mDescView;
    @Bind(R.id.pager) public ViewPager mViewPager;
    @Bind(R.id.profile_name)  TextView mTitleView;
    private User mUser;
    private ProfileViewsManager mProfileViewsManager;
    private Firebase mFirebaseRef;

    private ProfileSectionsPagerAdapter mSectionsPagerAdapter;


    public static ProfileFragment newInstance(Bundle args) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseRef = new Firebase(getString(R.string.firebase_url));
        Gson gson = new Gson();

        if(savedInstanceState==null)
            mUser = gson.fromJson(getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("user", null), User.class);
        else
            mUser=(User)savedInstanceState.getSerializable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        byte[] decodedString = Base64.decode(mUser.getAvatar(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        mImageView.setImageBitmap(decodedByte);

        mDescView.setText(mUser.getTwitter_bio());
        mTitleView.setText(mUser.getFirstName() + " " + mUser.getLastName());

        setupNavigation(view,inflater);
        return view;

    }

    private void setupNavigation(View view,LayoutInflater inflater){
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mProfileViewsManager = new ProfileViewsManager();
        mSectionsPagerAdapter = new ProfileSectionsPagerAdapter(inflater);

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }
    @OnClick({R.id.accepted_challenges,R.id.history})
    public void pagerClick(View view){
        int idx = Integer.parseInt((String) view.getTag());
        mViewPager.setCurrentItem(idx, true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("user",mUser);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class ProfileViewsManager {
        public List<MultipartFormField> list;

        public ProfileViewsManager() {
            list = new ArrayList<MultipartFormField>();
            list.add(new AcceptedChallengesField());
            list.add(new CreatedChallengesField());

        }


        public class AcceptedChallengesField extends MultipartFormField {
            public int getTitle() {
                return R.string.ns_challenge_name;
            }

            public int getLayout() {
                return R.layout.profile_accepted_challenges;
            }

            public boolean onValidate() {
                return true;
            }

            public void onSetup(ViewGroup layout) {
                Map<String,Object> m = mUser.getChallenges();
                if(m!=null) {
                    final RecyclerView recyclerView = ButterKnife.findById(layout, R.id.accepted_challenges_list);
                    final Firebase f = mFirebaseRef.child("challenges");
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    String uid = mFirebaseRef.getAuth().getUid();
                    final ChallengeLinearListAdapter challengeLinearListAdapter = new ChallengeLinearListAdapter(f, (AppCompatActivity) getActivity(), 1, mUser.getChallenges());
                    recyclerView.setAdapter(challengeLinearListAdapter);

                    mFirebaseRef.child("users/"+uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mUser = new Gson().fromJson(getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("user", null), User.class);
                            ChallengeLinearListAdapter challengeLinearListAdapter = new ChallengeLinearListAdapter(f, (AppCompatActivity) getActivity(), 1, mUser.getChallenges());
                            recyclerView.setAdapter(challengeLinearListAdapter);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        }

        public class CreatedChallengesField extends MultipartFormField {
            public int getTitle() {
                return R.string.ns_challenge_name;
            }

            public int getLayout() {
                return R.layout.profile_accepted_challenges;
            }

            public boolean onValidate() {
                return true;
            }

            public void onSetup(ViewGroup layout) {
                mUser.getEmail();
                Query q = mFirebaseRef.child("challenges").orderByChild("author").equalTo(mUser.getEmail());
                    RecyclerView recyclerView = ButterKnife.findById(layout, R.id.accepted_challenges_list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(new ChallengeListAdapter(q, (AppCompatActivity) getActivity(), 1, false));
            }
        }
    }
    public class ProfileSectionsPagerAdapter extends PagerAdapter {
        LayoutInflater mInflater;
        MultipartFormField mCurrentPage;

        public ProfileSectionsPagerAdapter(LayoutInflater inflater){
            mInflater = inflater;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            mCurrentPage = mProfileViewsManager.list.get(position);
            ViewGroup layout = (ViewGroup) mInflater.inflate(mCurrentPage.getLayout(), collection, false);
            mCurrentPage.onSetup(layout);
            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return ((View) object) == view;
        }

        @Override
        public int getCount() {
            return mProfileViewsManager.list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(mCurrentPage.getTitle());
        }
    }
}
