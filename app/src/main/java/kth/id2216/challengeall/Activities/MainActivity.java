package kth.id2216.challengeall.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import kth.id2216.challengeall.Fragments.CreateChallengeFragment;
import kth.id2216.challengeall.Fragments.HomeFragment;
import kth.id2216.challengeall.Fragments.ProfileFragment;
import kth.id2216.challengeall.Objects.Challenge;
import kth.id2216.challengeall.R;

public class MainActivity extends AppCompatActivity  implements HomeFragment.OnListFragmentInteractionListener {
    public static final String TAG = "MainActivity";
    public static final int HOME_IDX = 0;
    public static final int SEARCH_IDX = 1;
    public static final int NEW_CHALLENGE_IDX = 2;
    public static final int NOTIFICATIONS_IDX = 3;
    public static final int PROFILE_IDX = 4;
    public static final int MY_CHALLENGES_IDX = 5;
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
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_36dp);
        setSupportActionBar(toolbar);
        setupNavigation();

    }

    private void setupNavigation(){
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(mSectionsPagerAdapter.getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setTitle(mSectionsPagerAdapter.getPageTitle(HOME_IDX));
        View navMenu = findViewById(R.id.nav_menu);
        View homeButton =  findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(HOME_IDX,true);
            }
        });
        View myChallengesButton = findViewById(R.id.favorites_button);
        myChallengesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(SEARCH_IDX,true);
            }
        });
        View newChallengeButton =  findViewById(R.id.new_challenge_button);
        newChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(NEW_CHALLENGE_IDX,true);
            }
        });
        View notificationsButton =  findViewById(R.id.notifications_button);
        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(NOTIFICATIONS_IDX,true);
            }
        });
        View profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(PROFILE_IDX,true);
            }
        });

        Button loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent,-1);
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
        switch (id) {
            case android.R.id.home:
                DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
                if(drawer!=null)
                    if(drawer.isDrawerOpen(GravityCompat.START))
                        drawer.closeDrawer(GravityCompat.START);
                    else
                        drawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListFragmentInteraction(Challenge c) {

    }

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
                    return HomeFragment.newInstance(HomeFragment.HOME_TYPE);
                case SEARCH_IDX:
                    //TODO Replace with Search Fragment here
                    return HomeFragment.newInstance(0);
                case NEW_CHALLENGE_IDX:
                    //TODO Replace with Challenge Fragment here
                    return CreateChallengeFragment.newInstance(1);
                case NOTIFICATIONS_IDX:
                    //TODO Replace with Notifications Fragment here
                    return HomeFragment.newInstance(0);
                case PROFILE_IDX:
                    return ProfileFragment.newInstance(1);
                default: return HomeFragment.newInstance(1);
            }
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
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
                    return getString(R.string.new_challenge_str);
                case NOTIFICATIONS_IDX:
                    //TODO Add New Notifications Title here
                    return null;
                case PROFILE_IDX:
                    return getString(R.string.profile_str);
                default: return getString(R.string.home_str);
            }
        }
    }

}
