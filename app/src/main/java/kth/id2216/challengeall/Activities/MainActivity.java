package kth.id2216.challengeall.Activities;

<<<<<<< HEAD
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
=======
>>>>>>> origin/master
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;

import kth.id2216.challengeall.Fragments.HomeFragment;
import kth.id2216.challengeall.Objects.Challenge;
import kth.id2216.challengeall.R;

public class MainActivity extends AppCompatActivity  implements HomeFragment.OnListFragmentInteractionListener {

    public static final int HOME_IDX = 0;
    public static final int SEARCH_IDX = 1;
    public static final int NEW_CHALLENGE_IDX = 2;
    public static final int NOTIFICATIONS_IDX = 4;
    public static final int PROFILE_IDX = 5;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNavigation();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    private void setupNavigation(){
        View navMenu = findViewById(R.id.nav_menu);
        ImageButton homeButton = (ImageButton) navMenu.findViewById(R.id.home);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(HOME_IDX,true);
            }
        });
        ImageButton searchButton = (ImageButton) navMenu.findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(SEARCH_IDX,true);
            }
        });
        ImageButton newChallengeButton = (ImageButton) navMenu.findViewById(R.id.new_challenge);
        newChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(NEW_CHALLENGE_IDX,true);
            }
        });
        ImageButton notificationsButton = (ImageButton) navMenu.findViewById(R.id.notifications);
        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(NOTIFICATIONS_IDX,true);
            }
        });
        ImageButton profileButton = (ImageButton) navMenu.findViewById(R.id.profile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(PROFILE_IDX,true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Challenge c) {

    }

    @Override
    public void setToolbarTitle(String s) {
        getSupportActionBar().setTitle(s);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case HOME_IDX:
                    return HomeFragment.newInstance(1);
                case SEARCH_IDX:
                    //TODO Replace with Search Fragment here
                    return HomeFragment.newInstance(1);
                case NEW_CHALLENGE_IDX:
                    //TODO Replace with Challenge Fragment here
                    return HomeFragment.newInstance(1);
                case NOTIFICATIONS_IDX:
                    //TODO Replace with Notifications Fragment here
                    return HomeFragment.newInstance(1);
                case PROFILE_IDX:
                    //TODO Replace with Profile Fragment here
                    return HomeFragment.newInstance(1);
                default: return HomeFragment.newInstance(1);
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
                case HOME_IDX:
                    return getString(R.string.home_str);
                case SEARCH_IDX:
                    //TODO Add Search Title here
                    return null;
                case NEW_CHALLENGE_IDX:
                    //TODO Add New Challenge Title here
                    return null;
                case NOTIFICATIONS_IDX:
                    //TODO Add New Notifications Title here
                    return null;
                case PROFILE_IDX:
                    //TODO Add Profile Title here
                    return null;
                default: return getString(R.string.home_str);
            }
        }
    }
}
