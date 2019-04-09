package com.trevorwiebe.trackacow.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.trevorwiebe.trackacow.R;
import com.trevorwiebe.trackacow.dataLoaders.InsertCallEntity;
import com.trevorwiebe.trackacow.dataLoaders.UpdateCallById;
import com.trevorwiebe.trackacow.db.entities.CallEntity;
import com.trevorwiebe.trackacow.db.entities.FeedEntity;
import com.trevorwiebe.trackacow.utils.Utility;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FeedPenRecyclerViewAdapter extends RecyclerView.Adapter<FeedPenRecyclerViewAdapter.FeedPenViewHolder> {

    private static final String TAG = "FeedPenRecyclerViewAdap";

    private ArrayList<Long> mDateList;
    private ArrayList<CallEntity> mCallEntities;
    private ArrayList<FeedEntity> mFeedEntities;

    private NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

    public FeedPenRecyclerViewAdapter() {
    }

    @Override
    public int getItemCount() {
        if (mDateList == null) return 0;
        return mDateList.size();
    }

    @NonNull
    @Override
    public FeedPenViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_feed_pen, viewGroup, false);
        return new FeedPenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedPenViewHolder feedPenViewHolder, int i) {

        long date = mDateList.get(i);

        String friendlyDate = Utility.convertMillisToFriendlyDate(date);
        feedPenViewHolder.mDate.setText(friendlyDate);

        CallEntity callEntity = getCallByDate(date);
        if (callEntity != null) {
            String call = numberFormat.format(callEntity.getAmountFed());
            feedPenViewHolder.mCall.setText(call);
        } else {
            feedPenViewHolder.mCall.setText("0");
        }

        ArrayList<FeedEntity> feedEntities = getFeedEntitiesByDate(date);
        if (feedEntities != null) {
            for (int t = 0; t < feedEntities.size(); t++) {
                FeedEntity feedEntity = feedEntities.get(t);
                String amountFed = numberFormat.format(feedEntity.getFeed());
                feedPenViewHolder.mFed.append(amountFed);
                feedPenViewHolder.mFed.append("\n");
            }
        }

    }

    public void setDateList(ArrayList<Long> dateList) {
        this.mDateList = new ArrayList<>(dateList);
        notifyDataSetChanged();
    }

    public void setCallList(ArrayList<CallEntity> callList) {
        this.mCallEntities = new ArrayList<>(callList);
        notifyDataSetChanged();
    }

    public void setFeedList(ArrayList<FeedEntity> feedList) {
        this.mFeedEntities = new ArrayList<>(feedList);
        notifyDataSetChanged();
    }

    public class FeedPenViewHolder extends RecyclerView.ViewHolder {

        private TextView mDate;
        private TextView mCall;
        private TextView mTimesFed;
        private TextView mFed;
        private TextView mLeftToFeed;
        private TextView mTotalFed;

        public FeedPenViewHolder(View view) {
            super(view);

            mDate = view.findViewById(R.id.feed_pen_date);
            mCall = view.findViewById(R.id.feed_lot_call);
            mTimesFed = view.findViewById(R.id.feed_lot_times_fed);
            mFed = view.findViewById(R.id.feed_lot_fed);
            mLeftToFeed = view.findViewById(R.id.feed_lot_left_to_feed);
            mTotalFed = view.findViewById(R.id.feed_lot_total_fed);

        }
    }

    private CallEntity getCallByDate(long date) {
        if (mCallEntities == null) return null;
        for (int r = 0; r < mCallEntities.size(); r++) {
            CallEntity callEntity = mCallEntities.get(r);
            if (callEntity.getDate() == date) {
                return callEntity;
            }
        }
        return null;
    }

    private ArrayList<FeedEntity> getFeedEntitiesByDate(long date) {
        if (mFeedEntities == null) return null;
        ArrayList<FeedEntity> feedEntities = new ArrayList<>();
        for (int q = 0; q < mFeedEntities.size(); q++) {
            FeedEntity feedEntity = mFeedEntities.get(q);
            Log.d(TAG, "getFeedEntitiesByDate: " + date);
            Log.d(TAG, "getFeedEntitiesByDate: " + feedEntity.getDate());
            if (feedEntity.getDate() == date) {
                feedEntities.add(feedEntity);
            }
        }
        return feedEntities;
    }
}
