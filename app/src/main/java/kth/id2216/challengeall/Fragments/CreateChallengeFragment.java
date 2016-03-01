package kth.id2216.challengeall.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.gson.Gson;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kth.id2216.challengeall.Activities.MainActivity;
import kth.id2216.challengeall.Objects.Challenge;
import kth.id2216.challengeall.Objects.MultipartFormField;
import kth.id2216.challengeall.Objects.User;
import kth.id2216.challengeall.R;
import kth.id2216.challengeall.asynctask.CreateChallengeTask;
import kth.id2216.challengeall.interfaces.OnFragmentInteractionListener;
import kth.id2216.challengeall.interfaces.OnPutImageListener;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateChallengeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateChallengeFragment extends Fragment implements OnPutImageListener{
    public static final String CHALLENGE_KEY = "challenge";
    public static final String PAGE_KEY = "page";
    public static final String URI_KEY = "uri";
    private Firebase mFirebaseRef;
    protected FirebaseError mFirebaseError;
    private static final String TAG = "CreateChallengeFragment";

    private CreateChallengeTask mChallengeTask = null;
    private Challenge mChallenge;
    private Uri mUri;
    private int mCurrentItem;
    private NewChallengeViewsManager mNewChallengeViewsManager;

    // UI references.
    @Bind(R.id.new_challenge_pager)
    public ViewPager mViewPager;
    @Bind(R.id.new_challenge_toolbar)
    public Toolbar mToolbar;
    @Bind(R.id.new_challenge_progress)
    public ProgressBar mProgressBarView;
    @Bind(R.id.new_challenge_progress_str)
    public TextView mProgressBarTextView;
    @Bind(R.id.new_challenge_progress_circle)
    public View mProgressView;
    @Bind(R.id.new_challenge_form)
    public View mChallengeFormView;

    private OnFragmentInteractionListener mListener;


    public CreateChallengeFragment() {
        // Required empty public constructor
    }

    public static CreateChallengeFragment newInstance(Bundle args) {
        CreateChallengeFragment fragment = new CreateChallengeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mChallenge = (Challenge)savedInstanceState.getSerializable(CHALLENGE_KEY);
            mCurrentItem = savedInstanceState.getInt(PAGE_KEY, 0);
            mUri = Uri.parse(savedInstanceState.getString(URI_KEY));
        }
        mChallenge = mChallenge == null ? new Challenge() : mChallenge;
        mFirebaseRef = new Firebase(getString(R.string.firebase_url));
        mNewChallengeViewsManager = new NewChallengeViewsManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_create_challenge, container, false);
        ButterKnife.bind(this, view);
        toolbarSetup();
        EasyImage.configuration(activity)
                .setImagesFolderName("images")
                .saveInAppExternalFilesDir()
                .setCopyExistingPicturesToPublicLocation(true);
        mViewPager.setAdapter(new NewChallengePagerAdapter(inflater));


        updateProgressViews();
        return view;
    }

    private void toolbarSetup(){
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_36dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mViewPager.getCurrentItem();
                if (currentItem == 0) {
                    getActivity().onBackPressed();
                } else {
                    mViewPager.setCurrentItem(Math.max(currentItem - 1, 0), true);
                    updateProgressViews();
                }
            }
        });

    }
    private void updateProgressViews() {
        MultipartFormField r = mNewChallengeViewsManager.list.get(mViewPager.getCurrentItem());
        mToolbar.setTitle(getString(r.getTitle()));

        int vCount = mViewPager.getAdapter().getCount();
        int current = mViewPager.getCurrentItem() + 1;
        mProgressBarTextView.setText(current + "/" + vCount);
        mProgressBarView.setProgress(current * 100 / vCount);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE_KEY, mViewPager.getCurrentItem());
        outState.putSerializable(CHALLENGE_KEY, mChallenge);
        if(mUri!=null)
            outState.putString(URI_KEY, mUri.toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    //Listener to avoid transfering clicks to the views below this fragment.
    @OnClick(R.id.new_challenge_background)
    public void backgroundEmptyClick(){
        //Leave Empty
    }

    @OnClick(R.id.new_challenge_next_button)
    public void nextButton() {
        int vCount = mViewPager.getAdapter().getCount();
        int current = mViewPager.getCurrentItem();
        MultipartFormField r = mNewChallengeViewsManager.list.get(current);
        if (r.onValidate()) {
            if (current + 1 == vCount) {//Send Registration Form
                attemptChallengeCreation();
            }
            mViewPager.setCurrentItem(current + 1, true);
            updateProgressViews();
        }
    }

    public void attemptChallengeCreation(){
        if (mChallengeTask != null) return;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String s = sharedPreferences.getString("user", "");

        if(s!="") {
            User user =new Gson().fromJson(s, User.class);
            mChallenge.setAuthor(user.getEmail());
            mChallenge.setTimestamp(new Timestamp(System.currentTimeMillis() / 1000L));
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String lastTime = df.format(c.getTime());
            mChallengeTask = new CreateChallengeTask(mChallenge,mFirebaseRef.child("challenges"),getActivity(),this,true);
            if(mUri!= null) mChallengeTask.execute(mUri);
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


    public class NewChallengeViewsManager {
        private NewChallengeErrors mNewChallengeErrors;
        public List<MultipartFormField> list;

        public NewChallengeViewsManager() {
            mNewChallengeErrors = new NewChallengeErrors();
            list = new ArrayList<MultipartFormField>();
            list.add(new ChallengeNameField());
            list.add(new ChallengeDescriptionField());
            list.add(new CategoryField());
            list.add(new ImageField());

        }

        public class NewChallengeErrors {
            // String binding
            public final String ERROR_TITLE;
            public final String ERROR_NO_IMG;
            public final String ERROR_DESC;

            NewChallengeErrors() {
                ERROR_TITLE = getString(R.string.ns_challenge_name_error);
                ERROR_NO_IMG = getString(R.string.ns_challenge_no_img_error);
                ERROR_DESC = getString(R.string.ns_challenge_desc_error);
            }
        }

        public class ChallengeNameField extends MultipartFormField {
            public int getTitle() {
                return R.string.ns_challenge_name;
            }

            public int getLayout() {
                return R.layout.new_challenge_name;
            }

            public boolean onValidate() {
                final TextView storyNameView = ButterKnife.findById(mViewPager, R.id.new_challenge_name);
                String s = storyNameView.getText().toString().trim();
                if (TextUtils.isEmpty(s) || s.length() < 5) {
                    storyNameView.setError(mNewChallengeErrors.ERROR_TITLE);
                    return false;
                }
                mChallenge.setTitle(s);
                return true;
            }
            public void onSetup(ViewGroup layout){
                ButterKnife.findById(layout, R.id.new_challenge_name).requestFocus();
            }
        }
        public class ChallengeDescriptionField extends MultipartFormField {
            public int getTitle() {
                return R.string.ns_challenge_description;
            }

            public int getLayout() {
                return R.layout.new_challenge_description;
            }

            public boolean onValidate() {
                final TextView storyNameView = ButterKnife.findById(mViewPager, R.id.new_challenge_description);
                String s = storyNameView.getText().toString().trim();
                if (TextUtils.isEmpty(s) || s.length() < 5) {
                    storyNameView.setError(mNewChallengeErrors.ERROR_DESC);
                    return false;
                }
                mChallenge.setDescription(s);
                return true;
            }
            public void onSetup(ViewGroup layout){
                ButterKnife.findById(layout, R.id.new_challenge_description).requestFocus();
            }
        }

        public class CategoryField extends MultipartFormField {
            private Spinner mSpinner;

            public int getTitle() {
                return R.string.ns_category;
            }

            public int getLayout() {
                return R.layout.new_challenge_category;
            }

            public boolean onValidate() {
                return mChallenge.getCategory() != null;
            }

            public void onSetup(ViewGroup layout) {
                mSpinner = ButterKnife.findById(layout, R.id.new_challenge_category_spinner);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.categories_array, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                mSpinner.setAdapter(adapter);
                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mChallenge.setCategory(mSpinner.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                mSpinner.performClick();


            }

        }
        public class ImageField extends MultipartFormField {

            public int getTitle() {
                return R.string.ns_image;
            }

            public int getLayout() {
                return R.layout.new_challenge_image;
            }

            public void onSetup(ViewGroup layout) {
                ImageButton imageButton = (ImageButton) layout.findViewById(R.id.new_challenge_avatar);
                if(mUri!=null){
                    ButterKnife.findById(layout,R.id.new_challenge_avatar_layout).setVisibility(View.GONE);
                    ImageView resultView = (ImageView)layout.findViewById(R.id.new_challenge_selected_img);
                    resultView.setVisibility(View.VISIBLE);
                    resultView.setImageResource(R.drawable.ic_add_a_photo_black_48dp);
                    resultView.setImageURI(mUri);
                    resultView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EasyImage.openChooserWithDocuments(CreateChallengeFragment.this, mChallenge.getTitle(), 1);
                        }
                    });
                }
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EasyImage.openChooserWithDocuments(CreateChallengeFragment.this, mChallenge.getTitle(), 1);
                    }
                });


            }

            public boolean onValidate() {
                if(mUri ==null){
                    TextInputLayout t = ButterKnife.findById(mViewPager,R.id.new_challenge_avatar_input);
                    if(t!=null)
                        t.setError(mNewChallengeErrors.ERROR_NO_IMG);
                    return false;
                }
                return true;
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                TextInputLayout t = ButterKnife.findById(mViewPager,R.id.new_challenge_avatar_input);
                if(t!=null)
                    t.setError(e.getMessage());
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Handle the image
                onPhotoReturned(imageFile);
            }
        });

    }
    private void onPhotoReturned(File imageFile){
        mUri = Uri.fromFile(imageFile);
        ButterKnife.findById(mViewPager, R.id.new_challenge_avatar_layout).setVisibility(View.GONE);
        ImageView resultView = ButterKnife.findById(mViewPager, R.id.new_challenge_selected_img);
        resultView.setVisibility(View.VISIBLE);
        resultView.setImageResource(R.drawable.ic_add_a_photo_black_48dp);
        resultView.setImageURI(mUri);
        resultView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openChooserWithDocuments(CreateChallengeFragment.this, mChallenge.getTitle(), 1);
            }
        });
    }
    @Override
    public void onSuccess(String url) {
        mListener.onFragmentInteraction(MainActivity.NEW_CHALLENGE_IDX,null);
    }
    private class NewChallengePagerAdapter extends PagerAdapter {
        LayoutInflater mInflater;

        public NewChallengePagerAdapter(LayoutInflater inflater) {
            mInflater = inflater;
        }

        public Object instantiateItem(ViewGroup collection, int position) {
            MultipartFormField r = mNewChallengeViewsManager.list.get(position);
            ViewGroup layout = (ViewGroup) mInflater.inflate(r.getLayout(), collection, false);
            r.onSetup(layout);
            collection.addView(layout);
            updateProgressViews();
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
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }

        @Override
        public int getCount() {
            return mNewChallengeViewsManager.list.size();
        }
    }

}
