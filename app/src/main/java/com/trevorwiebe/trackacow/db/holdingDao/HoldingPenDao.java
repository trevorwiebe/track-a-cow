package com.trevorwiebe.trackacow.db.holdingDao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.trevorwiebe.trackacow.db.holdingUpdateEntities.HoldingPenEntity;

import java.util.List;

@Dao
public interface HoldingPenDao {

    @Insert
    void insertHoldingPen(HoldingPenEntity holdingPenEntity);

    @Insert
    void insertHoldingPenList(List<HoldingPenEntity> holdingPenEntities);

    @Query("SELECT * FROM HoldingPen WHERE penId = :id")
    HoldingPenEntity getHoldingPenById(String id);

    @Query("SELECT * FROM HoldingPen")
    List<HoldingPenEntity> getHoldingPenList();

    @Query("DELETE FROM HoldingPen")
    void deleteHoldingPenTable();

    @Update
    void updateHoldingPen(HoldingPenEntity holdingPenEntity);

    @Delete
    void deleteHoldingPen(HoldingPenEntity holdingPenEntity);

}
