package kth.id2216.challengeall.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SubSectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static ProfileFragment newInstance(int columnCount) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        //args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setupNavigation(view);
        return view;

    }

    private void setupNavigation(View view){
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SubSectionsPagerAdapter(getActivity().getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        View profMenu = view.findViewById(R.id.prof_menu);
        ImageButton acceptedButton = (ImageButton) profMenu.findViewById(R.id.accepted_challenges);
        acceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(CHALLENGES_IDX,true);
            }
        });
        ImageButton achievementsButton = (ImageButton) profMenu.findViewById(R.id.achievements);
        achievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(ACHIEVEMENTS_IDX,true);
            }
        });
        ImageButton historyButton = (ImageButton) profMenu.findViewById(R.id.history);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(HISTORY_IDX,true);
            }
        });

    }
    @Override
    public void onDetach() {
        super.onDetach();
    }


    public class SubSectionsPagerAdapter extends FragmentPagerAdapter {

        public SubSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            getPageTitle(position);
            switch (position) {
                case CHALLENGES_IDX:
                    return HomeFragment.newInstance(HomeFragment.ACCEPTED_TYPE);
                case ACHIEVEMENTS_IDX:
                    return AchievementsFragment.newInstance(3);
                case HISTORY_IDX:
                    return HomeFragment.newInstance(HISTORY_IDX);
                default: return CreateChallengeFragment.newInstance(1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case CHALLENGES_IDX:
                    return getString(R.string.home_str);
                case ACHIEVEMENTS_IDX:
                    //TODO Add Search Title here
                    return null;
                case HISTORY_IDX:
                    return getString(R.string.new_challenge_str);
                default: return getString(R.string.home_str);
            }
        }
    }
}
