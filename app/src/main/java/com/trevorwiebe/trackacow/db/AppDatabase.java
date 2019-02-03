package com.trevorwiebe.trackacow.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.trevorwiebe.trackacow.db.dao.CowDao;
import com.trevorwiebe.trackacow.db.dao.DrugDao;
import com.trevorwiebe.trackacow.db.dao.DrugsGivenDao;
import com.trevorwiebe.trackacow.db.dao.PenDao;
import com.trevorwiebe.trackacow.db.entities.CowEntity;
import com.trevorwiebe.trackacow.db.entities.DrugEntity;
import com.trevorwiebe.trackacow.db.entities.DrugsGivenEntity;
import com.trevorwiebe.trackacow.db.entities.PenEntity;
import com.trevorwiebe.trackacow.db.holdingDao.HoldingCowDao;
import com.trevorwiebe.trackacow.db.holdingDao.HoldingDrugDao;
import com.trevorwiebe.trackacow.db.holdingDao.HoldingDrugsGivenDao;
import com.trevorwiebe.trackacow.db.holdingDao.HoldingPenDao;
import com.trevorwiebe.trackacow.db.holdingUpdateEntities.HoldingCowEntity;
import com.trevorwiebe.trackacow.db.holdingUpdateEntities.HoldingDrugEntity;
import com.trevorwiebe.trackacow.db.holdingUpdateEntities.HoldingDrugsGivenEntity;
import com.trevorwiebe.trackacow.db.holdingUpdateEntities.HoldingPenEntity;

@Database(entities = {
        PenEntity.class,
        CowEntity.class,
        DrugsGivenEntity.class,
        DrugEntity.class,
        HoldingPenEntity.class,
        HoldingCowEntity.class,
        HoldingDrugsGivenEntity.class,
        HoldingDrugEntity.class
                    }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract PenDao penDao();
    public abstract CowDao cowDao();
    public abstract DrugsGivenDao drugsGivenDao();
    public abstract DrugDao drugDao();

    public abstract HoldingPenDao holdingPenDao();
    public abstract HoldingCowDao holdingCowDao();
    public abstract HoldingDrugsGivenDao holdingDrugsGivenDao();
    public abstract HoldingDrugDao holdingDrugDao();

    public static AppDatabase getAppDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    "track_a_cow_db")
                    .build();
        }
        return INSTANCE;
    }
}
