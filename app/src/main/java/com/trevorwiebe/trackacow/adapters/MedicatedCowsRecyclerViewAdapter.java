package com.trevorwiebe.trackacow.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trevorwiebe.trackacow.R;
import com.trevorwiebe.trackacow.db.entities.CowEntity;
import com.trevorwiebe.trackacow.db.entities.DrugEntity;
import com.trevorwiebe.trackacow.db.entities.DrugsGivenEntity;
import com.trevorwiebe.trackacow.utils.Utility;

import java.util.ArrayList;


public class MedicatedCowsRecyclerViewAdapter extends RecyclerView.Adapter<MedicatedCowsRecyclerViewAdapter.TrackCowViewHolder> {

    private static final String TAG = "MedicatedCowsRecyclerVi";

    private ArrayList<CowEntity> mCowList;
    private ArrayList<DrugEntity> mDrugList;
    private ArrayList<DrugsGivenEntity> mDrugsGivenEntities;
    private Context mContext;

    public MedicatedCowsRecyclerViewAdapter(ArrayList<CowEntity> cowObjects, ArrayList<DrugEntity> drugList, ArrayList<DrugsGivenEntity> drugsGivenObjects, Context context) {
        this.mCowList = cowObjects;
        this.mDrugList = drugList;
        this.mDrugsGivenEntities = drugsGivenObjects;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        if (mCowList == null || mDrugList == null || mDrugsGivenEntities == null) return 0;
        return mCowList.size();
    }

    @NonNull
    @Override
    public TrackCowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_medicated_cows, viewGroup, false);
        return new TrackCowViewHolder(view);
    }

    public void swapData(ArrayList<CowEntity> cowObjectsList, ArrayList<DrugEntity> drugEntities, ArrayList<DrugsGivenEntity> drugsGivenEntities) {
        mCowList = cowObjectsList;
        mDrugList = drugEntities;
        mDrugsGivenEntities = drugsGivenEntities;
        if (mCowList != null && mDrugList != null && mDrugsGivenEntities != null) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final TrackCowViewHolder trackCowViewHolder, final int position) {

        CowEntity cowEntity = mCowList.get(position);
        String tagNumber = Integer.toString(cowEntity.getTagNumber());
        String notes = cowEntity.getNotes();
        String cowId = cowEntity.getCowId();

        Log.d(TAG, "onBindViewHolder: cowId: " + cowId);

        trackCowViewHolder.mTagNumber.setText(tagNumber);
        if (notes == null || notes.length() == 0) {
            trackCowViewHolder.mNotes.setVisibility(View.GONE);
        } else {
            trackCowViewHolder.mNotes.setVisibility(View.VISIBLE);
            trackCowViewHolder.mNotes.setText("Notes: " + notes);
        }

        if (cowEntity.isAlive()) {
            trackCowViewHolder.mTagNumber.setTextColor(mContext.getResources().getColor(android.R.color.black));
            String message = "";
            mDrugsGivenEntities = findDrugsGivenObjectByCowId(cowId, mDrugsGivenEntities);
            for (int q = 0; q < mDrugsGivenEntities.size(); q++) {
                DrugsGivenEntity drugsGivenEntity = mDrugsGivenEntities.get(q);

                String drugId = drugsGivenEntity.getDrugId();
                int amountGiven = drugsGivenEntity.getAmountGiven();
                long date = drugsGivenEntity.getDate();

                DrugEntity drugEntity = findDrugEntities(drugId, mDrugList);
                String drugName = "";
                if (drugEntity != null) {
                    drugName = drugEntity.getDrugName();
                }
                String amountGivenStr = Integer.toString(amountGiven);
                String dateStr = Utility.convertMillisToDate(date);

                message = message + amountGivenStr + "cc of " + drugName + " given on " + dateStr;
                if (mDrugsGivenEntities.size() != q + 1) {
                    message = message + "\n";
                }
                trackCowViewHolder.mDrugsGiven.setText(message);
            }
        } else {
            trackCowViewHolder.mTagNumber.setTextColor(mContext.getResources().getColor(R.color.redText));
            trackCowViewHolder.mDrugsGiven.setText("This cow is dead");
        }
    }

    public class TrackCowViewHolder extends RecyclerView.ViewHolder {

        private TextView mTagNumber;
        private TextView mDrugsGiven;
        private TextView mNotes;

        public TrackCowViewHolder(View view) {
            super(view);

            mTagNumber = view.findViewById(R.id.medicated_cow_tag_number);
            mDrugsGiven = view.findViewById(R.id.medication_given);
            mNotes = view.findViewById(R.id.notes);

        }
    }

    private DrugEntity findDrugEntities(String drugId, ArrayList<DrugEntity> drugList) {
        for (int p = 0; p < drugList.size(); p++) {
            DrugEntity drugEntity = drugList.get(p);
            if (drugEntity.getDrugId().equals(drugId)) {
                return drugEntity;
            }
        }
        return null;
    }

    private ArrayList<DrugsGivenEntity> findDrugsGivenObjectByCowId(String cowId, ArrayList<DrugsGivenEntity> drugsGivenObjects){
        ArrayList<DrugsGivenEntity> drugsGivenToCow = new ArrayList<>();
        for(int r=0; r<drugsGivenObjects.size(); r++){
            DrugsGivenEntity drugsGivenEntity = drugsGivenObjects.get(r);
            Log.d(TAG, "findDrugsGivenObjectByCowId: " + drugsGivenEntity.getCowId() + " " + drugsGivenEntity.getAmountGiven());
            if(drugsGivenEntity.getCowId().equals(cowId)){
                drugsGivenToCow.add(drugsGivenEntity);
            }
        }
        return drugsGivenToCow;
    }
}
