package kth.id2216.challengeall.application;

import android.app.Application;

import com.firebase.client.Firebase;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class LoadData extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        ImageLoaderConfiguration config  =  new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader mImageLoader = ImageLoader.getInstance();
        if(!mImageLoader.isInited())mImageLoader.init(config);
    }

}
