package kth.id2216.challengeall.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;
import java.util.Map;

import kth.id2216.challengeall.Activities.MainActivity;
import kth.id2216.challengeall.Fragments.HomeFragment.OnListFragmentInteractionListener;
import kth.id2216.challengeall.Fragments.dummy.DummyContent.DummyItem;
import kth.id2216.challengeall.Objects.Challenge;
import kth.id2216.challengeall.R;
import kth.id2216.challengeall.interfaces.OnFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class NotificationRecyclerViewAdapter extends FirebaseListFilteredAdapter<Challenge> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    public static final int LIST = 1;
    public static final int STAGGERED = 0;
    private static final String TAG = "ChallengeListAdapter";
    private static final String HEADER_ID = "header";
    public static MainActivity.FragmentsEnum page = MainActivity.FragmentsEnum.HOME;
    private int mVItemLayout;
    private int mListType;
    private Context mCtx;
    private AppCompatActivity mActivity;
    private ImageLoader mImageLoader;
    private OnFragmentInteractionListener mOnFragmentInteractionListener;


    public NotificationRecyclerViewAdapter(final Firebase ref, final AppCompatActivity activity, int listType, Map<String,Object> filter) {
        super(ref, Challenge.class, activity, filter);
        mListType = listType;
        mVItemLayout = R.layout.challenge_item_notifications;
        mCtx = activity.getApplicationContext();

        mActivity = activity;
        mOnFragmentInteractionListener =(OnFragmentInteractionListener) mActivity;
        ImageLoaderConfiguration config  =  new ImageLoaderConfiguration.Builder(mCtx).build();
        mImageLoader = ImageLoader.getInstance();
        if(!mImageLoader.isInited())mImageLoader.init(config);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //if(viewType == TYPE_ITEM)
        return new ViewHolderI(LayoutInflater.from(parent.getContext()).inflate(mVItemLayout, parent, false));
        //else
        //    return new ViewHolderH(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_header, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Challenge s = getItem(position);
        final String key = getItemKey(position);

        final ViewHolderI iholder = (ViewHolderI)holder;
        iholder.mItem = s;
        iholder.mTitleView.setText(s.getTitle());
        iholder.mDescriptionView.setText(s.getDescription());
        iholder.mAuthorCat.setText("By "+s.getAuthor()+" on "+s.getCategory());


        byte[] decodedString = Base64.decode(s.getImg(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        iholder.mIconView.setImageBitmap(decodedByte);
        iholder.mSelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("id", key);
                args.putString("author", iholder.mItem.getAuthor());
                args.putSerializable("challenge",iholder.mItem);
                mOnFragmentInteractionListener.onFragmentInteraction(MainActivity.CHALLENGE_IDX, args);
            }
        });

    }




    public class ViewHolderI extends RecyclerView.ViewHolder {
        public final View mView;
        public final View mSelView;
        public final TextView mTitleView;

        public final TextView mDescriptionView;
        public final TextView mAuthorCat;

        public final ImageView mIconView;
        public Challenge mItem;

        public ViewHolderI(View view) {
            super(view);
            mView = view;
            mSelView = view.findViewById(R.id.card_view);
            mTitleView = (TextView)view.findViewById(R.id.title);
            mDescriptionView = (TextView)view.findViewById(R.id.description);
            mAuthorCat = (TextView)view.findViewById(R.id.author_category);
            mIconView = (ImageView) view.findViewById(R.id.thumb);
        }
    }

}
