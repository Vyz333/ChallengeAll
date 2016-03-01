package kth.id2216.challengeall.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kth.id2216.challengeall.Fragments.HomeFragment.OnListFragmentInteractionListener;
import kth.id2216.challengeall.Fragments.dummy.DummyContent.DummyItem;

import kth.id2216.challengeall.Objects.Challenge;
import kth.id2216.challengeall.R;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    public static final int HEADER_ID = -1;
    private final List<Challenge> mValues;
    private final OnListFragmentInteractionListener mListener;

    public HomeRecyclerViewAdapter(List<Challenge> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM)
            return new ViewHolderI(LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_item_home, parent, false));
        else
            return new ViewHolderH(LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_header_home, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Challenge c = mValues.get(position);
        if(holder instanceof ViewHolderH){//Header View
            ViewHolderH hholder = (ViewHolderH)holder;
            hholder.mIdView.setText(c.title);
        }else {//Item view
            ViewHolderI iholder = (ViewHolderI)holder;
            iholder.mItem = c;
            iholder.mIdView.setText(c.title);
            iholder.mContentView.setText(c.description);
            final Challenge qc = c;
            iholder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(qc);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolderI extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Challenge mItem;

        public ViewHolderI(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.title);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
    public class ViewHolderH extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public Challenge mItem;

        public ViewHolderH(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.title);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //We use challenges with id -1 as list header items
        if(Long.parseLong(mValues.get(position).id)== HEADER_ID)
            return TYPE_HEADER;

        return TYPE_ITEM;
    }
}
