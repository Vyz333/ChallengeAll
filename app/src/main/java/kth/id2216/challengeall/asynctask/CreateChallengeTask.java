package kth.id2216.challengeall.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import kth.id2216.challengeall.Objects.Challenge;
import kth.id2216.challengeall.Objects.User;
import kth.id2216.challengeall.R;
import kth.id2216.challengeall.interfaces.OnPutImageListener;

public class CreateChallengeTask extends AsyncTask<Uri, String, String> {
    public static final String TAG = "CreateChallengeTask";
    private ProgressDialog dialog;
    private Context mContext;
    private ImageLoader mImageLoader;
    private Boolean mShowDialog;
    private Challenge mChallenge;
    private String mUid;
    private OnPutImageListener mListener;
    private Firebase mFirebaseRef;
    private FirebaseError mFirebaseError;
    private SharedPreferences mSharedPref;

    public CreateChallengeTask(Challenge challenge,Firebase ref, Context context, OnPutImageListener listener, Boolean showDialog){
        mUid = null;
        mContext = context;
        mListener = listener;
        mShowDialog = showDialog;
        mImageLoader = ImageLoader.getInstance();
        mChallenge=challenge;
        mFirebaseRef = ref;
        mSharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

    }
    protected void onPreExecute() {
        dialog = new ProgressDialog(mContext);
        dialog.setMessage(mContext.getString(R.string.ns_creating_text));
        dialog.setCancelable(false);
        if(mShowDialog){
            dialog.show();
        }
    }

    protected String doInBackground(Uri... uris) {
        String url = "";
        if (uris == null || uris.length != 1) {
            return null;
        }
        // The file location of the image selected.
        Uri selectedImage = uris[0];
        if(selectedImage!= null)
            addImages(selectedImage);

        addFirebaseEntry();
        return url;
    }
    protected void onPostExecute(String result) {
        if(mShowDialog){
            dialog.dismiss();
        }
        if(mListener != null){
            mListener.onSuccess(null);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if(mShowDialog){
            dialog.dismiss();
        }
    }
    protected void  addImages(Uri srcUri){
        String url = "";
        Resources r = mContext.getResources();
        DisplayMetrics d =r.getDisplayMetrics();
        int picSide = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r.getDimension(R.dimen.large_thumb_side), d));
        uploadImage(srcUri,picSide,picSide);
    }

    private Bitmap getScaledBitmap(Uri srcUri,int mDstWidth, int mDstHeight){
        Bitmap unscaledBitmap =  mImageLoader.loadImageSync(srcUri.toString());
        Bitmap scaledBitmap;
        ImageSize srcSize = new ImageSize(unscaledBitmap.getWidth(),unscaledBitmap.getHeight());
        ImageSize boundarySize = new ImageSize(mDstWidth,mDstHeight);

        //Use Height -1 for width-dependent images to be used on staggered list
        if(unscaledBitmap.getWidth()<=mDstWidth && unscaledBitmap.getHeight()<=mDstHeight)
            return unscaledBitmap;
        else {
            unscaledBitmap.recycle();
            return  mImageLoader.loadImageSync(srcUri.toString(),getScaledDimension(srcSize,boundarySize));

        }
    }

    private synchronized void addFirebaseEntry(){
        try {
            Firebase ref = mFirebaseRef.push();
            mChallenge.setId(ref.getKey());
            ref.setValue(mChallenge);
            Log.i(TAG,"Challenge created:"+mChallenge.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadImage(Uri srcUri,int mDstWidth, int mDstHeight){
            Bitmap scaledBitmap = getScaledBitmap(srcUri,mDstWidth, mDstHeight);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //scaledBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            scaledBitmap.recycle();
            byte[] bitmapdata = bos.toByteArray();
            String imageFile = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
            mChallenge.setImg(imageFile);
    }
    public static ImageSize getScaledDimension(ImageSize imgSize, ImageSize boundary) {

        int original_width = imgSize.getWidth();
        int original_height = imgSize.getHeight();
        int bound_width = boundary.getWidth();
        int bound_height = boundary.getHeight();
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new ImageSize(new_width, new_height);
    }
}