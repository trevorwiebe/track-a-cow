package com.trevorwiebe.trackacow.dataLoaders;

import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;
import com.trevorwiebe.trackacow.db.AppDatabase;
import com.trevorwiebe.trackacow.db.entities.DrugEntity;
import com.trevorwiebe.trackacow.db.entities.PenEntity;
import com.trevorwiebe.trackacow.db.holdingUpdateEntities.HoldingDrugEntity;
import com.trevorwiebe.trackacow.db.holdingUpdateEntities.HoldingPenEntity;
import com.trevorwiebe.trackacow.utils.Utility;

import java.util.List;

public class InsertAllLocalChangeToCloud extends AsyncTask<Context, Void, Void> {

    private DatabaseReference baseRef;
    private OnAllLocalDbInsertedToCloud mOnAllLocalDbInsertedToCloud;

    public InsertAllLocalChangeToCloud(DatabaseReference baseRef, OnAllLocalDbInsertedToCloud onAllLocalDbInsertedToCloud){
        this.baseRef = baseRef;
        this.mOnAllLocalDbInsertedToCloud = onAllLocalDbInsertedToCloud;
    }

    public interface OnAllLocalDbInsertedToCloud{
        void onAllLocalDbInsertedToCloud();
    }

    @Override
    protected Void doInBackground(Context... contexts) {

        AppDatabase db = AppDatabase.getAppDatabase(contexts[0]);

        // update drug entities
        List<HoldingDrugEntity> holdingDrugEntities = db.holdingDrugDao().getHoldingDrugList();
        for(int a=0; a<holdingDrugEntities.size(); a++){
            HoldingDrugEntity holdingDrugEntity = holdingDrugEntities.get(a);

            DrugEntity drugEntity = new DrugEntity(holdingDrugEntity.getDefaultAmount(), holdingDrugEntity.getDrugId(), holdingDrugEntity.getDrugName());

            switch (holdingDrugEntity.getWhatHappened()){
                case Utility.INSERT_UPDATE:
                    baseRef.child(DrugEntity.DRUG_OBJECT).child(drugEntity.getDrugId()).setValue(drugEntity);
                    break;
                case Utility.DELETE:
                    baseRef.child(DrugEntity.DRUG_OBJECT).child(drugEntity.getDrugId()).removeValue();
                    break;
                default:
                    break;
            }
        }
        db.holdingDrugDao().deleteHoldingDrugTable();

        //update pen entities
        List<HoldingPenEntity> holdingPenEntities = db.holdingPenDao().getHoldingPenList();
        for(int b=0; b<holdingPenEntities.size(); b++){
            HoldingPenEntity holdingPenEntity = holdingPenEntities.get(b);

            PenEntity penEntity = new PenEntity(holdingPenEntity.getPenId(), holdingPenEntity.getCustomerName(), holdingPenEntity.getIsActive(), holdingPenEntity.getNotes(), holdingPenEntity.getPenName(), holdingPenEntity.getTotalHead());

            switch (holdingPenEntity.getWhatHappened()){
                case Utility.INSERT_UPDATE:
                    baseRef.child(PenEntity.PEN_OBJECT).child(penEntity.getPenId()).setValue(penEntity);
                    break;
                case Utility.DELETE:
                    baseRef.child(PenEntity.PEN_OBJECT).child(penEntity.getPenId()).removeValue();
                    break;
                default:
                        break;
            }
        }
        db.holdingPenDao().deleteHoldingPenTable();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mOnAllLocalDbInsertedToCloud.onAllLocalDbInsertedToCloud();
    }
}
