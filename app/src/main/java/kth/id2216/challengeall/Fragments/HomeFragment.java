package kth.id2216.challengeall.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kth.id2216.challengeall.Adapters.HomeRecyclerViewAdapter;
import kth.id2216.challengeall.Objects.Challenge;
import kth.id2216.challengeall.Objects.Challenges;
import kth.id2216.challengeall.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_LIST_TYPE = "list-type";
    public static final int HOME_TYPE = 0;
    public static final int ACCEPTED_TYPE = 1;
    public static final int HISTORY_TYPE = 2;
    public static final int COLS_LANDSCAPE = 4;
    public static final int COLS_PORTRAIT = 3;

    private int mListType = 0;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HomeFragment newInstance(int listType) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, listType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mListType = getArguments().getInt(ARG_LIST_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_list, container, false);
        // Set the adapter
        Context context = view.getContext();
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            //if(mListType==HOME_TYPE){
            //    int ncolumns = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE?COLS_LANDSCAPE:COLS_PORTRAIT;
            //    StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(ncolumns, StaggeredGridLayoutManager.VERTICAL);
            //    recyclerView.setLayoutManager(sglm);
            //}else{
                LinearLayoutManager llm = new LinearLayoutManager(context);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
            //}

            HomeRecyclerViewAdapter homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(fillList(mListType), mListener);
            recyclerView.setAdapter(homeRecyclerViewAdapter);
        }
        return view;
    }
    private  List<Challenge> fillList(int type){
        switch (type){
            case HOME_TYPE:
                return getFrontPageChallenges();
            case ACCEPTED_TYPE:
                return getAcceptedChallenges();
            case HISTORY_TYPE:
                return getMyChallenges();
            default: return new ArrayList<Challenge>();
        }

    }
    public  List<Challenge> getFrontPageChallenges() {
        List<Challenge> l = new ArrayList<Challenge>();
        l.add(new Challenge(-1, "Weekly Challenges", "description", null, null, null, null));
        l.add(new Challenge(1233, "title2", "description2", null, null, null, null));
        l.add(new Challenge(123, "title3", "description", null, null, null, null));
        l.add(new Challenge(1233, "title4", "description2", null, null, null, null));
        l.add(new Challenge(123, "title5", "description", null, null, null, null));
        l.add(new Challenge(-1, "Most Popular", "description2", null, null, null, null));
        l.add(new Challenge(123, "title7", "description", null, null, null, null));
        l.add(new Challenge(1233, "title8", "description2", null, null, null, null));
        l.add(new Challenge(123, "title9", "description", null, null, null, null));
        l.add(new Challenge(-1, "Monthly Challenges", "description2", null, null, null, null));
        l.add(new Challenge(123, "title11", "description", null, null, null, null));
        l.add(new Challenge(1233, "title12", "description2", null, null, null, null));
        l.add(new Challenge(123, "title13", "description", null, null, null, null));
        l.add(new Challenge(1233, "title14", "description2", null, null, null, null));
        l.add(new Challenge(123, "title15", "description", null, null, null, null));
        l.add(new Challenge(1233, "title16", "description2", null, null, null, null));
        l.add(new Challenge(123, "title17", "description", null, null, null, null));
        l.add(new Challenge(1233, "title18", "description2", null, null, null, null));
        l.add(new Challenge(123, "title19", "description", null, null, null, null));
        l.add(new Challenge(1233, "title20", "description2", null, null, null, null));
        return l;
    }

    public  List<Challenge> getMyChallenges() {
        List<Challenge> l = new ArrayList<Challenge>();
        l.add(new Challenge(1233, "title2", "description2", null, null, null, null));
        l.add(new Challenge(123, "title3", "description", null, null, null, null));
        l.add(new Challenge(1233, "title4", "description2", null, null, null, null));
        l.add(new Challenge(123, "title7", "description", null, null, null, null));
        l.add(new Challenge(1233, "title8", "description2", null, null, null, null));
        l.add(new Challenge(123, "title9", "description", null, null, null, null));
        l.add(new Challenge(1233, "title14", "description2", null, null, null, null));
        l.add(new Challenge(123, "title15", "description", null, null, null, null));
        l.add(new Challenge(1233, "title16", "description2", null, null, null, null));
        l.add(new Challenge(123, "title17", "description", null, null, null, null));
        l.add(new Challenge(1233, "title18", "description2", null, null, null, null));
        l.add(new Challenge(123, "title19", "description", null, null, null, null));
        l.add(new Challenge(1233, "title20", "description2", null, null, null, null));
        return l;
    }
    public  List<Challenge> getAcceptedChallenges() {
        List<Challenge> l = new ArrayList<Challenge>();
        l.add(new Challenge(123, "title17", "description", null, null, null, null));
        l.add(new Challenge(1233, "title18", "description2", null, null, null, null));
        l.add(new Challenge(123, "title19", "description", null, null, null, null));
        l.add(new Challenge(1233, "title20", "description2", null, null, null, null));
        return l;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    }
}
