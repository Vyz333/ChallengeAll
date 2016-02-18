package kth.id2216.challengeall.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kth.id2216.challengeall.Fragments.dummy.DummyContent.DummyItem;
import kth.id2216.challengeall.Objects.Achievement;
import kth.id2216.challengeall.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class achievementRecyclerViewAdapter extends RecyclerView.Adapter<achievementRecyclerViewAdapter.ViewHolder> {

    private final List<Achievement> mValues;

    public achievementRecyclerViewAdapter(List<Achievement> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievement_item, parent, false);
        int btnSize=view.getLayoutParams().width;
        view.setLayoutParams(new CardView.LayoutParams(btnSize, btnSize));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).title);
        holder.mIcon.setImageResource(mValues.get(position).icon);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final ImageView mIcon;
        public Achievement mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mIcon = (ImageView) view.findViewById(R.id.icon);
        }

    }
}
