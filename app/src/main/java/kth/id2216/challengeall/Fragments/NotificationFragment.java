package kth.id2216.challengeall.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kth.id2216.challengeall.Adapters.NotificationRecyclerViewAdapter;
import kth.id2216.challengeall.Objects.Challenge;
import kth.id2216.challengeall.Objects.Challenges;
import kth.id2216.challengeall.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NotificationFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotificationFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NotificationFragment newInstance(int columnCount) {
        NotificationFragment fragment = new NotificationFragment();
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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        // Set the adapter


        Context context = view.getContext();
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            NotificationRecyclerViewAdapter l = new NotificationRecyclerViewAdapter(fillList(), null);
            recyclerView.setAdapter(l);
        }
        return view;
    }

    public List<Challenge> fillList() {
        List<Challenge> ITEMS = new ArrayList<>();
       ITEMS.add(new Challenge(-1, "Friend Requests", "description", null, null, null, null));
       ITEMS.add(new Challenge(1233, "title2", "description2", null, null, null, null));
        ITEMS.add(new Challenge(123, "title3", "description", null, null, null, null));
        ITEMS.add(new Challenge(1233, "title4", "description2", null, null, null, null));
       ITEMS.add(new Challenge(123, "title5", "description", null, null, null, null));
        ITEMS.add(new Challenge(-1, "Challenge requests", "description2", null, null, null, null));
        ITEMS.add(new Challenge(123, "title7", "description", null, null, null, null));
      ITEMS.add(new Challenge(1233, "title8", "description2", null, null, null, null));
        ITEMS.add(new Challenge(123, "title9", "description", null, null, null, null));
        ITEMS.add(new Challenge(-1, "Challenge results", "description2", null, null, null, null));
        ITEMS.add(new Challenge(123, "title11", "description", null, null, null, null));
        ITEMS.add(new Challenge(1233, "title12", "description2", null, null, null, null));
        ITEMS.add(new Challenge(123, "title13", "description", null, null, null, null));
        ITEMS.add(new Challenge(1233, "title14", "description2", null, null, null, null));
        ITEMS.add(new Challenge(123, "title15", "description", null, null, null, null));
        ITEMS.add(new Challenge(1233, "title16", "description2", null, null, null, null));
        ITEMS.add(new Challenge(123, "title17", "description", null, null, null, null));
        ITEMS.add(new Challenge(1233, "title18", "description2", null, null, null, null));
        ITEMS.add(new Challenge(123, "title19", "description", null, null, null, null));
        ITEMS.add(new Challenge(1233, "title20", "description2", null, null, null, null));
        return ITEMS;
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
