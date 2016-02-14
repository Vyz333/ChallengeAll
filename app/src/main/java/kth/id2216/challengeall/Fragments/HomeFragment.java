package kth.id2216.challengeall.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HomeFragment newInstance(int columnCount) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_list, container, false);
        // Set the adapter
        mListener.setToolbarTitle(getString(R.string.home_str));
        fillList();
        Context context = view.getContext();
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            HomeRecyclerViewAdapter homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(Challenges.ITEMS, mListener);
            recyclerView.setAdapter(homeRecyclerViewAdapter);
        }
        return view;
    }

    public void fillList() {
        Challenges.ITEMS.add(new Challenge(-1, "Weekly Challenges", "description", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(1233, "title2", "description2", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(123, "title3", "description", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(1233, "title4", "description2", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(123, "title5", "description", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(-1, "Most Popular", "description2", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(123, "title7", "description", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(1233, "title8", "description2", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(123, "title9", "description", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(-1, "Monthly Challenges", "description2", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(123, "title11", "description", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(1233, "title12", "description2", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(123, "title13", "description", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(1233, "title14", "description2", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(123, "title15", "description", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(1233, "title16", "description2", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(123, "title17", "description", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(1233, "title18", "description2", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(123, "title19", "description", null, null, null, null));
        Challenges.ITEMS.add(new Challenge(1233, "title20", "description2", null, null, null, null));
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

        void setToolbarTitle(String s);
    }
}
