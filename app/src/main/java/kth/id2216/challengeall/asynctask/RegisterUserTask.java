package kth.id2216.challengeall.asynctask;

import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;
import kth.id2216.challengeall.Objects.User;
import kth.id2216.challengeall.interfaces.OnPutImageListener;
import kth.id2216.challengeall.R;

public class RegisterUserTask extends AsyncTask<Uri, String, String> {
    public static final String TAG = "RegisterUserTask";
    public static final int ASPECT_HEIGHT = -1;
    private ProgressDialog dialog;
    private Context mContext;
    private String mPictureName;
    private ImageLoader mImageLoader;
    private Boolean mShowDialog;
    private User mUser;
    private String mUid;
    private OnPutImageListener mListener;
    private String mPassword;
    private Firebase mFirebaseRef;
    private FirebaseError mFirebaseError;
    private SharedPreferences mSharedPref;

    public RegisterUserTask(User user,String password, Firebase ref,Context context, OnPutImageListener listener, Boolean showDialog){
        mUid = null;
        mContext = context;
        mListener = listener;
        mShowDialog = showDialog;
        mImageLoader = ImageLoader.getInstance();
        mUser=user;
        mPassword = password;
        mFirebaseRef = ref;
        mSharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

    }
    protected void onPreExecute() {
        dialog = new ProgressDialog(mContext);
        dialog.setMessage(mContext.getString(R.string.reg_creating_text));
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
        int picSide = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, r.getDimension(R.dimen.profile_pic_w), d));
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
            mFirebaseRef.createUser(mUser.getEmail(), mPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> stringObjectMap) {
                    //If user creation was successful, store extra data object
                    mUid = stringObjectMap.get("uid").toString();
                    mFirebaseRef.child("users").child(mUid).setValue(mUser);
                    SharedPreferences.Editor editor = mSharedPref.edit();
                    //Write user data to shared preferences
                    Gson gson = new Gson();
                    String json = gson.toJson(mUser);
                    editor.putString("user", json);
                    editor.putString("uid", mUid);
                    editor.putString("username", mUser.getEmail());
                    editor.commit();
                    Log.i(TAG, "User " + mUser.getEmail() + " was registerd successfully!");
                    mFirebaseRef.authWithPassword(mUser.getEmail(), mPassword, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Log.i(TAG, "Login successful: " + mUser.getEmail());
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            mFirebaseError = firebaseError;
                            Log.i(TAG, "Login failed: " + mFirebaseError.getMessage());
                        }
                    });
                    if(mListener != null){
                        mListener.onSuccess(mPictureName);
                    }
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    mFirebaseError = firebaseError;
                    switch (firebaseError.getCode()) {

                        case FirebaseError.EMAIL_TAKEN:
                            dialog.setMessage(mContext.getString(R.string.error_taken_email));
                            break;
                        case FirebaseError.INVALID_EMAIL:
                            dialog.setMessage(mContext.getString(R.string.error_invalid_email));
                            break;
                        default:
                            dialog.setMessage(mFirebaseError.getMessage());
                            break;
                    }
                    Log.e(TAG, "Error registering User " + mUser.getEmail() + " " + firebaseError.getMessage());
                }
            });
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
            mUser.setAvatar(imageFile);
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