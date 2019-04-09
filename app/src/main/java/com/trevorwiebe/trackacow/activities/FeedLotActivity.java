package com.trevorwiebe.trackacow.activities;

import android.arch.persistence.room.Query;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trevorwiebe.trackacow.R;
import com.trevorwiebe.trackacow.dataLoaders.InsertCallEntity;
import com.trevorwiebe.trackacow.dataLoaders.InsertFeedEntities;
import com.trevorwiebe.trackacow.dataLoaders.QueryCallByLotIdAndDate;
import com.trevorwiebe.trackacow.dataLoaders.QueryFeedByLotIdAndDate;
import com.trevorwiebe.trackacow.dataLoaders.QueryLotByLotId;
import com.trevorwiebe.trackacow.dataLoaders.UpdateCallById;
import com.trevorwiebe.trackacow.db.entities.CallEntity;
import com.trevorwiebe.trackacow.db.entities.FeedEntity;
import com.trevorwiebe.trackacow.db.entities.LotEntity;
import com.trevorwiebe.trackacow.utils.Constants;
import com.trevorwiebe.trackacow.utils.Utility;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Locale;

public class FeedLotActivity extends AppCompatActivity implements
        QueryCallByLotIdAndDate.OnCallByLotIdAndDateLoaded,
        QueryLotByLotId.OnLotByLotIdLoaded,
        QueryFeedByLotIdAndDate.OnFeedByLotIdAndDateLoaded {

    private static final String TAG = "FeedLotActivity";

    private TextInputEditText mCall;
    private LinearLayout mFeedAgainLayout;
    private Button mSave;

    private CallEntity mSelectedCallEntity;
    private int mFeedAgainNumber;
    private long mDate;
    private boolean mShouldAddNewFeedEditText = true;
    private TextWatcher mFeedTextWatcher;
    private NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.getDefault());
    private ArrayList<FeedEntity> mFeedEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_lot);

        Intent intent = getIntent();

        mDate = intent.getLongExtra("date", 0);
        final String lotId = intent.getStringExtra("lotId");

        mCall = findViewById(R.id.feed_lot_call_et);
        mFeedAgainLayout = findViewById(R.id.feed_again_layout);
        mSave = findViewById(R.id.save_feed_lot_btn);

        mFeedTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    mShouldAddNewFeedEditText = shouldAddAnotherEditText();
                    if (mShouldAddNewFeedEditText) {
                        addNewFeedEditText(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCall.length() == 0) {
                    mCall.requestFocus();
                    mCall.setError("Please fill this blank");
                } else {

                    // code for updating the call entity
                    int call = Integer.parseInt(mCall.getText().toString());

                    String callKey;
                    CallEntity callEntity = new CallEntity(call, mDate, lotId, "id");

                    if (mSelectedCallEntity == null) {
                        DatabaseReference baseRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(CallEntity.CALL).push();
                        callKey = baseRef.getKey();
                        callEntity.setId(callKey);
                        new InsertCallEntity(callEntity).execute(FeedLotActivity.this);
                    } else {
                        callKey = mSelectedCallEntity.getId();
                        new UpdateCallById(call, callKey).execute(FeedLotActivity.this);
                    }

                    mCall.setText("");


                    // code for updating the feed entities
                    ArrayList<Integer> feedsIntList = getFeedsFromLayout();
                    ListIterator feedsIterator = feedsIntList.listIterator();
                    while (feedsIterator.hasNext()) {
                        int amountFed = (int) feedsIterator.next();
                        int position = feedsIterator.nextIndex();
                        if (mFeedEntities.size() > position) {
                            FeedEntity feedEntity = mFeedEntities.get(position);
                            feedEntity.setFeed(amountFed);
                            mFeedEntities.remove(position);
                            mFeedEntities.add(position, feedEntity);
                        } else {
                            DatabaseReference feedRef = Constants.BASE_REFERENCE.child(FeedEntity.FEED).push();
                            String feedEntityId = feedRef.getKey();
                            FeedEntity feedEntity = new FeedEntity(amountFed, mDate, feedEntityId, lotId);
                            mFeedEntities.add(feedEntity);
                        }
                    }

                    new InsertFeedEntities(mFeedEntities).execute(FeedLotActivity.this);

                }

            }
        });

        new QueryCallByLotIdAndDate(mDate, lotId, this).execute(this);
        new QueryFeedByLotIdAndDate(mDate, lotId, this).execute(this);
        new QueryLotByLotId(lotId, this).execute(this);
    }

    @Override
    public void onCallByLotIdAndDateLoaded(CallEntity callEntity) {
        mSelectedCallEntity = callEntity;
        if (callEntity == null) {
            mSave.setText("Save and Next Pen");
        } else {
            mSave.setText("Update and Next Pen");
            int call = mSelectedCallEntity.getAmountFed();
            String callStr = Integer.toString(call);
            mCall.setText(callStr);
        }
    }


    @Override
    public void onFeedByLotIdAndDateLoaded(ArrayList<FeedEntity> feedEntities) {
        mFeedEntities = feedEntities;
        ListIterator iterator = feedEntities.listIterator();
        while (iterator.hasNext()) {
            FeedEntity feedEntity = (FeedEntity) iterator.next();
            String feed = numberFormatter.format(feedEntity.getFeed());
            addNewFeedEditText(feed);
        }
        addNewFeedEditText(null);
    }

    @Override
    public void onLotByLotIdLoaded(LotEntity lotEntity) {
        String lotName = lotEntity.getLotName();
        String friendlyDate = Utility.convertMillisToFriendlyDate(mDate);
        setTitle(lotName + ": " + friendlyDate);
    }

    private void addNewFeedEditText(@Nullable String text) {

        mFeedAgainNumber = mFeedAgainNumber + 1;

        final float scale = getResources().getDisplayMetrics().density;
        int pixels24 = (int) (24 * scale + 0.5f);
        int pixels16 = (int) (16 * scale + 0.5f);
        int pixels8 = (int) (8 * scale + 0.5f);

        LinearLayout linearLayout = new LinearLayout(FeedLotActivity.this);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        linearLayout.setId(mFeedAgainNumber);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextInputLayout textInputLayout = new TextInputLayout(FeedLotActivity.this);
        LinearLayout.LayoutParams textInputLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textInputLayout.setLayoutParams(textInputLayoutParams);

        TextInputEditText textInputEditText = new TextInputEditText(FeedLotActivity.this);
        LinearLayout.LayoutParams textInputEditTextParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textInputEditTextParams.setMargins(pixels16, 0, pixels16, pixels8);
        textInputEditText.setLayoutParams(textInputEditTextParams);
        if (text != null) {
            textInputEditText.setText(text);
        }
        textInputEditText.addTextChangedListener(mFeedTextWatcher);
        textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        textInputEditText.setTextSize(16f);
        textInputEditText.setHint("Feed");

        textInputLayout.addView(textInputEditText);


        ImageButton deleteButton = new ImageButton(FeedLotActivity.this);
        LinearLayout.LayoutParams deleteBtnParams = new LinearLayout.LayoutParams(
                pixels24,
                pixels24
        );
        deleteButton.setPadding(pixels8, pixels8, pixels8, pixels8);
        deleteBtnParams.setMargins(0, pixels24, pixels16, pixels8);
        deleteButton.setBackground(getResources().getDrawable(R.drawable.ic_delete_black_24dp));
        deleteButton.setId(mFeedAgainNumber);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewToDelete = view.getId();

                for (int i = 0; i < mFeedAgainLayout.getChildCount(); i++) {
                    View v = mFeedAgainLayout.getChildAt(i);

                    int tagToCompare = v.getId();
                    if (tagToCompare == viewToDelete) {
                        i = i - 1;
                        mFeedAgainNumber = mFeedAgainNumber - 1;
                        mFeedAgainLayout.removeView(v);
                    }
                }

            }
        });
        deleteButton.setLayoutParams(deleteBtnParams);

        linearLayout.addView(textInputLayout);

        if (mFeedAgainNumber >= 2) {
            textInputEditText.setEms(6);
            linearLayout.addView(deleteButton);
        } else {
            textInputEditText.setEms(8);
        }

        mFeedAgainLayout.addView(linearLayout);
    }

    private boolean shouldAddAnotherEditText() {
        for (int i = 0; i < mFeedAgainLayout.getChildCount(); i++) {
            View v = mFeedAgainLayout.getChildAt(i);
            LinearLayout linearLayout = (LinearLayout) v;
            View textLayout = linearLayout.getChildAt(0);
            TextInputLayout textInputLayout = (TextInputLayout) textLayout;
            String text = textInputLayout.getEditText().getText().toString();
            if (text.length() == 0) return false;
        }
        return true;
    }

    private ArrayList<Integer> getFeedsFromLayout() {
        ArrayList<Integer> feedIntList = new ArrayList<>();
        for (int i = 0; i < mFeedAgainLayout.getChildCount(); i++) {
            View v = mFeedAgainLayout.getChildAt(i);
            LinearLayout linearLayout = (LinearLayout) v;
            View textLayout = linearLayout.getChildAt(0);
            TextInputLayout textInputLayout = (TextInputLayout) textLayout;
            String text = textInputLayout.getEditText().getText().toString();
            if (text.length() != 0) {
                feedIntList.add(Integer.parseInt(text));
            }
        }
        return feedIntList;
    }
}
