package kth.id2216.challengeall.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import kth.id2216.challengeall.Adapters.ChallengeLinearListAdapter;
import kth.id2216.challengeall.Adapters.NotificationRecyclerViewAdapter;
import kth.id2216.challengeall.Objects.Challenge;
import kth.id2216.challengeall.Objects.ChallengeMessage;
import kth.id2216.challengeall.Objects.Challenges;
import kth.id2216.challengeall.Objects.User;
import kth.id2216.challengeall.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NotificationFragment extends Fragment {
    private Firebase mFirebaseRef;
    private User mUser;
    @Bind(R.id.list)
    public RecyclerView mRecyclerView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotificationFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NotificationFragment newInstance(Bundle args) {
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseRef = new Firebase(getString(R.string.firebase_url));
        Gson gson = new Gson();

        if(savedInstanceState==null)
            mUser = gson.fromJson(getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("user", null), User.class);
        else
            mUser=(User)savedInstanceState.getSerializable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this,view);
        // Set the adapter

        Context context = view.getContext();
        if (view instanceof RecyclerView) {
            String Uid = mFirebaseRef.getAuth().getUid();
            mFirebaseRef.child("messages/"+Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) return;
                    Map<String, ChallengeMessage> k= new HashMap<String, ChallengeMessage>();
                    k = dataSnapshot.getValue(k.getClass());
                    Map<String, Object> param = new HashMap<String, Object>();
                    Iterator entries = k.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry thisEntry = (Map.Entry) entries.next();
                        Object key = thisEntry.getKey();
                        Object value = thisEntry.getValue();
                        LinkedHashMap<String,String> asd = (LinkedHashMap<String,String>)thisEntry.getValue();
                        param.put(asd.get("challengeID"),true);
                    }

                    Map<String,Object> challengesMap ;
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(llm);
                    final Firebase f = mFirebaseRef.child("challenges");
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    String uid = mFirebaseRef.getAuth().getUid();

                    //final ChallengeLinearListAdapter challengeLinearListAdapter = new ChallengeLinearListAdapter(f, (AppCompatActivity) getActivity(), 1, mUser.getChallenges());
                    NotificationRecyclerViewAdapter l = new NotificationRecyclerViewAdapter(f, (AppCompatActivity) getActivity(), 1, param);
                    mRecyclerView.setAdapter(l);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Challenge c);

        void setToolbarTitle(String s);
    }
}
