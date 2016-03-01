package kth.id2216.challengeall.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.commons.io.FilenameUtils;

import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import kth.id2216.challengeall.Activities.MainActivity;
import kth.id2216.challengeall.Objects.Challenge;
import kth.id2216.challengeall.R;
import kth.id2216.challengeall.interfaces.OnFragmentInteractionListener;

public class ChallengeListAdapter extends FirebaseListAdapter<Challenge> {
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


    public ChallengeListAdapter(Query ref, AppCompatActivity activity, int listType,boolean searchingFlag) {
        super(ref, Challenge.class, activity, searchingFlag);
        mListType = listType;
        mVItemLayout = listType == STAGGERED? R.layout.challenge_item_home_staggered:R.layout.challenge_item_home;
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
