package kth.id2216.challengeall.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import com.facebook.appevents.AppEventsLogger;
import com.firebase.client.Firebase;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kth.id2216.challengeall.Fragments.ChallengeFragment;
import kth.id2216.challengeall.Fragments.CreateChallengeFragment;
import kth.id2216.challengeall.Fragments.HomeFragment;

import kth.id2216.challengeall.Fragments.NotificationFragment;

import kth.id2216.challengeall.Fragments.ProfileFragment;
import kth.id2216.challengeall.Fragments.SearchFragment;
import kth.id2216.challengeall.Objects.Challenge;
import kth.id2216.challengeall.R;
import kth.id2216.challengeall.interfaces.OnFragmentInteractionListener;
import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity  implements HomeFragment.OnListFragmentInteractionListener, OnFragmentInteractionListener {
    /* Constants */
    public static final String TAG = "MainActivity";
    public static final int HOME_IDX = 0;
    public static final int SEARCH_IDX = 1;
    public static final int NEW_CHALLENGE_IDX = 2;
    public static final int NOTIFICATIONS_IDX = 3;
    public static final int PROFILE_IDX = 4;
    public static final int CHALLENGE_IDX = 5;
    /*Bundle Keyes*/
    public static final String PAGE_KEY = "page";
    public static final String FRAG_ARGS = "frag_args";
    public static final String FRAG_KEY = "frag";
    /*Fragment tags*/
    public static final String HOME_TAG="home";
    public static final String CHALLENGE_TAG="challenge";
    public static final String NEW_CHALLENGE_TAG="new_challenge";
    public static  final String PROFILE_TAG="profile";
    public static final String NOTIFICATIONS_TAG="notifications";
    /* Activity variables */
    protected Firebase mFirebaseRef;
    protected Menu mMenu;
    private ListView mListView;
    private SectionsFragmentManager mSectionsFragmentManager;
    private FrameLayout mFrameLayout;
    private String currentKey;
    private String currentAuthor;
    private int previousItem;
    private FragmentsEnum mCurrentPage;
    @Bind(R.id.toolbar)
    public Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentPage = savedInstanceState != null && savedInstanceState.containsKey(PAGE_KEY) ? (FragmentsEnum) savedInstanceState.getSerializable(PAGE_KEY) : FragmentsEnum.HOME;
        setContentView(R.layout.activity_main);
        mFirebaseRef = new Firebase(getString(R.string.firebase_url));
        ButterKnife.bind(this);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_36dp);
        setSupportActionBar(mToolbar);
        setupNavigation(savedInstanceState);

    }
    private void setupNavigation(Bundle savedInstanceState) {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsFragmentManager = new SectionsFragmentManager(this.getBaseContext(),getSupportFragmentManager(), R.id.container);
        if (savedInstanceState == null) {
            mSectionsFragmentManager.setPage(mCurrentPage, savedInstanceState);
        } else {
            Fragment f = getSupportFragmentManager().getFragment(savedInstanceState, FRAG_KEY);
            mSectionsFragmentManager.setCurrentFragment(f);
        }
        getSupportActionBar().setTitle(mCurrentPage.getTitle(this));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        updateMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer != null)
                    if (drawer.isDrawerOpen(GravityCompat.START))
                        drawer.closeDrawer(GravityCompat.START);
                    else
                        drawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), 1);
                return true;
            case R.id.action_login:
                launchLogin();
                updateMenu();
                return true;
            case R.id.action_logout:
                mFirebaseRef.unauth();
                SharedPreferences.Editor e = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit();
                e.remove("username");
                e.remove("user");
                e.remove("id");
                e.commit();
                updateMenu();
                Log.i(TAG, "User Logged out.");
                return true;
            case R.id.create_session:
                if (mFirebaseRef.getAuth() != null) {
                    mSectionsFragmentManager.setPage(FragmentsEnum.NEW_CHALLENGE, null);
                    return true;
                } else {
                    launchLogin();
                    updateMenu();
                    return true;
                }
        }
        ;
        return super.onOptionsItemSelected(item);
    }
    void updateMenu() {
        MenuItem loginActionView = mMenu.findItem(R.id.action_login);
        MenuItem logoutActionView = mMenu.findItem(R.id.action_logout);
        if (mFirebaseRef.getAuth() == null) {
            loginActionView.setVisible(true);
            logoutActionView.setVisible(false);
        } else {
            loginActionView.setVisible(false);
            logoutActionView.setVisible(true);
        }
    }
    @OnClick(R.id.login_button)
    protected void launchLogin() {
        startActivityForResult(new Intent(this, LoginActivity.class), 1);
    }

    @OnClick({R.id.home_button, R.id.notifications_button,R.id.new_challenge_button, R.id.profile_button})
    public void onDrawerButtonClick(View v) {
        FragmentsEnum page = FragmentsEnum.valueOf((String) v.getTag());
        if(mFirebaseRef.getAuth()==null || getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE).getString("user",null)==null)
            launchLogin();
        else
            mSectionsFragmentManager.setPage(page, null);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Firebase.setAndroidContext(this);
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onListFragmentInteraction(Challenge c) {

    }

    public void setToolbarTitle(String s) {
        getSupportActionBar().setTitle(s);
    }

    @Override
    public void onFragmentInteraction(int id, Bundle args) {
        switch (id){
            case CHALLENGE_IDX:
                mSectionsFragmentManager.setPage(FragmentsEnum.CHALLENGE,args);
                break;
            case NEW_CHALLENGE_IDX:
                mSectionsFragmentManager.setPage(FragmentsEnum.HOME,args);
                break;
        }
    }


    public enum FragmentsEnum implements Serializable {

        HOME(HOME_TAG, R.string.home_str) {
            @Override
            public Fragment getInstance(Bundle args) {
                return HomeFragment.newInstance(args);
            }
        },
        CHALLENGE(CHALLENGE_TAG, R.string.my_challenges_str) {
            @Override
            public Fragment getInstance(Bundle args) {
                return ChallengeFragment.newInstance(args);
            }
        },

        NEW_CHALLENGE(NEW_CHALLENGE_TAG, R.string.new_challenge_str) {
            @Override
            public Fragment getInstance(Bundle args) {
                return CreateChallengeFragment.newInstance(args);
            }
        },
        PROFILE(PROFILE_TAG, R.string.profile_str) {
            @Override
            public Fragment getInstance(Bundle args) {
                return ProfileFragment.newInstance(args);
            }
        },
        NOTIFICATIONS(NOTIFICATIONS_TAG, R.string.notifications_str) {
            @Override
            public Fragment getInstance(Bundle args) {
                return NotificationFragment.newInstance(args);
            }
        };
        private final String tag;
        private final int titleId;

        private FragmentsEnum(final String tag, final int titleId) {
            this.tag = tag;
            this.titleId = titleId;
        }

        public String getTag(){
            return tag;
        }
        public String getTitle(Context ctx){
            return ctx.getString(titleId);
        }
        public abstract Fragment getInstance(Bundle args);
    }

    private class SectionsFragmentManager {
        Fragment mCurrentFragment;
        FragmentManager mFragmentManager;
        Context mContext;
        int mContainerId;

        public SectionsFragmentManager(Context context,FragmentManager fm, int containerId) {
            mContext = context;
            mFragmentManager = fm;
            mContainerId = containerId;
        }

        public void setPage(FragmentsEnum page, Bundle args) {
            mCurrentPage = page;
            if (mFragmentManager.getBackStackEntryCount() > 6) {
                mFragmentManager.popBackStack(); // remove one (you can also remove more)
            }

            mCurrentFragment = page.getInstance(args);
            getSupportActionBar().setTitle(page.getTitle(mContext));
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.abc_popup_enter, R.anim.abc_fade_out)
                    .add(mContainerId, mCurrentFragment, page.getTag())
                    .addToBackStack(page.getTag())
                    .commit();

        }

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }

        public void setCurrentFragment(Fragment f) {
            mCurrentFragment = f;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
            mSectionsFragmentManager.setCurrentFragment(getSupportFragmentManager().findFragmentById(R.id.container));
            getSupportActionBar().setTitle(mCurrentPage.getTitle(this));
        }
    }
}


